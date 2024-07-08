import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class MessageProtocol {

    private final List<Subscriber> subscribers;
    private final AtomicInteger activeSubscribers;
    private final int minSubscribers;
    private final int maxSubscribers;

    public MessageProtocol(int minSubscribers, int maxSubscribers) {
        if (minSubscribers > maxSubscribers || minSubscribers < 1) {
            throw new IllegalArgumentException("Invalid subscriber range");
        }
        this.subscribers = Collections.synchronizedList(new ArrayList<>());
        this.activeSubscribers = new AtomicInteger(0);
        this.minSubscribers = minSubscribers;
        this.maxSubscribers = maxSubscribers;
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
        if(subscriber.isActive()){
            activeSubscribers.incrementAndGet();
        }

    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
        activeSubscribers.decrementAndGet();
    }

    public void broadcast(String message) {
        int currentActive = activeSubscribers.get();

            // Send message to all active subscribers
            for (Subscriber subscriber : subscribers) {
//                if (subscriber.isActive()) {


                try {
                    subscriber.receive(message);
                    System.out.println("Active message read by subscriber");
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                }
            }

    }

    public void sendAtLeastN(String message, int n) {
        if (n < 1 || n > maxSubscribers) {
            throw new IllegalArgumentException("Invalid n value");
        }
        int currentActive = activeSubscribers.get();
//        System.out.println(currentActive);
        if (currentActive >= minSubscribers ) {
            // Send message to all active subscribers
            for (Subscriber subscriber : subscribers) {
                if (subscriber.isActive()) {
                    subscriber.receive(message);
                    try {
                        System.out.println("Active message read by subscriber");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        else{
            System.out.println("Message Avoided due to low active");
        }
    }

    public void sendNeverMoreThanN(String message, int n) {
        int currentActive = activeSubscribers.get();
        if ( currentActive <= maxSubscribers) {
            // Send message to all active subscribers
            for (Subscriber subscriber : subscribers) {
                if (subscriber.isActive()) {
                    subscriber.receive(message);
                    try {
                        System.out.println("Active message read by subscriber");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
        else{
            System.out.println("Message Avoided due to high active");
        }
    }

    public interface Subscriber {
        boolean isActive();
        void receive(String message);
    }
}


class MySubscriber implements MessageProtocol.Subscriber {

    private final String name;
    private boolean active;

    public MySubscriber(String name) {
        this.name = name;
        this.active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void receive(String message) {
        System.out.println("Subscriber " + name + " received message: " + message);
    }

    public void setActive(boolean active) {
        this.active = active;

    }
}

class MessageProducer implements Runnable {

    private final MessageProtocol protocol;
    private final String message;

    public MessageProducer(MessageProtocol protocol, String message) {
        this.protocol = protocol;
        this.message = message;
    }

    @Override
    public void run() {

        try {
            System.out.println("\n             BroadCast Message !\n");
            protocol.broadcast(message);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("\n             Atleast N !\n");
            protocol.sendAtLeastN(message,2);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("\n             Never More than N !\n");
            protocol.sendNeverMoreThanN(message,2);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

public class MessageSubscribe {

    public static void main(String[] args) throws InterruptedException {
        MessageProtocol protocol = new MessageProtocol(2, 4);

        MySubscriber subscriber1 = new MySubscriber("Alice");
        MySubscriber subscriber2 = new MySubscriber("Bob");
        MySubscriber subscriber3 = new MySubscriber("Charlie");
        MySubscriber subscriber4 = new MySubscriber("Ajay");
        MySubscriber subscriber5 = new MySubscriber("Vijay");
        MySubscriber subscriber6 = new MySubscriber("Jay");

//        subscriber2.setActive(false);
        subscriber3.setActive(false);
        subscriber4.setActive(false);
        subscriber5.setActive(false);
//        subscriber6.setActive(false);



        protocol.subscribe(subscriber1);
        protocol.subscribe(subscriber2);
        protocol.subscribe(subscriber3);
        protocol.subscribe(subscriber4);
        protocol.subscribe(subscriber5);
//        protocol.subscribe(subscriber6);

        protocol.unsubscribe(subscriber5);




        Thread producerThread = new Thread(new MessageProducer(protocol, "Annonymous message"));
        producerThread.start();


    }
}

