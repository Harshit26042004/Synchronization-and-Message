import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class MessageProtocol {

    List<MySubscriber> subscribers;
    AtomicInteger activeSubscribers;
    int minSubscribers;
    int maxSubscribers;

    public MessageProtocol(int minSubscribers, int maxSubscribers) {
        this.subscribers = new CopyOnWriteArrayList<>(new ArrayList<>());
        this.activeSubscribers = new AtomicInteger(0);
        this.minSubscribers = minSubscribers;
        this.maxSubscribers = maxSubscribers;
    }

    public void subscribe(MySubscriber subscriber) {
        subscribers.add(subscriber);
        if (subscriber.isActive()){
            activeSubscribers.incrementAndGet();
        }
    }

    public void unsubscribe(MySubscriber subscriber) {
        subscribers.remove(subscriber);
        if (subscriber.isActive()){
            activeSubscribers.decrementAndGet();
        }
    }

    public void broadcast(String message) {
        prepareToReceive();
        for (MySubscriber subscriber : subscribers) {
            try {
                subscriber.receive(message);
                subscriber.acknowledge();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        commitOrDiscard("Broadcast");
    }

    public void sendAtLeastN(String message, int n) {
        prepareToReceive();
        int count = 0;
        for (MySubscriber subscriber : subscribers) {
            if (subscriber.isActive()) {
                try {
                    subscriber.receive(message);
                    subscriber.acknowledge();
                    count++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (count >= n) {
                break;
            }
        }
        commitOrDiscard("Atleast");
    }

    public void sendNeverMoreThanN(String message, int n) {
        prepareToReceive();
        int count = 0;
        for (MySubscriber subscriber : subscribers) {
            if (subscriber.isActive()) {
                try {
                    subscriber.receive(message);
                    subscriber.acknowledge();
                    count++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (count >= n) {
                break;
            }
        }
        commitOrDiscard("Atmost");
    }

    private void prepareToReceive() {
        // Reset acknowledgements for all subscribers
        for (MySubscriber subscriber : subscribers) {
            subscriber.setAcknowledge(false);
        }
    }

    private void commitOrDiscard(String pattern) {
        int totalAcked = 0;
        for (MySubscriber subscriber : subscribers) {
            if (subscriber.isAcknowledge()) {
                totalAcked++;
            }
        }
        if(pattern.equals("Broadcast")) {
            if (totalAcked == subscribers.size()) {
                System.out.println("Message Committed");
            } else {
                System.out.println("Message Discarded");
            }
        }
        else if(pattern.equals("Atleast")){
            if (totalAcked >= minSubscribers) {
                System.out.println("Message Committed");
            } else {
                System.out.println("Message Discarded");
            }

        }
        else if(pattern.equals("Atmost")){
            if (totalAcked <= maxSubscribers) {
                System.out.println("Message Committed!!");
            } else {
                System.out.println("Message Discarded!!");
            }
        }
    }
}

class MySubscriber {

    String name;
    boolean active;
    boolean acknowledge;

    public MySubscriber(String name) {
        this.name = name;
        this.active = true;
        this.acknowledge = false;
    }

    public boolean isActive() {
        return active;
    }

    public void receive(String message) throws InterruptedException {
        System.out.println("Subscriber " + name + " received message: " + message);
        // Simulate processing time
        Thread.sleep(800);
    }

    public void acknowledge() {
        acknowledge = true;
    }

    public boolean isAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(boolean acknowledge) {
        this.acknowledge = acknowledge;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

class MessageProducer implements Runnable {

    MessageProtocol protocol;
    String message;

    public MessageProducer(MessageProtocol protocol, String message) {
        this.protocol = protocol;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            System.out.println("\n                  BroadCast Message !\n");
            protocol.broadcast(message);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("\n                  Atleast N !\n");
            protocol.sendAtLeastN(message, 2);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("\n                  Never More than N !\n");
            protocol.sendNeverMoreThanN(message, 2);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class MessageSubscribe {

    public static void main(String[] args) throws InterruptedException {
        int min_subscriber = 2;
        int max_subscriber = 4;
        MessageProtocol protocol = new MessageProtocol(min_subscriber, max_subscriber);

        MySubscriber subscriber1 = new MySubscriber("Ram");
        MySubscriber subscriber2 = new MySubscriber("Babu");
        MySubscriber subscriber3 = new MySubscriber("Shyam");
        MySubscriber subscriber4 = new MySubscriber("Ajay");
        MySubscriber subscriber5 = new MySubscriber("Vijay");
        MySubscriber subscriber6 = new MySubscriber("Jay");

        subscriber2.setActive(false);
        subscriber3.setActive(false);
        subscriber4.setActive(false);
        subscriber6.setActive(false);

        protocol.subscribe(subscriber1);
        protocol.subscribe(subscriber2);
        protocol.subscribe(subscriber3);
        protocol.subscribe(subscriber4);
        protocol.subscribe(subscriber5);
        protocol.subscribe(subscriber6);

        protocol.unsubscribe(subscriber5);

        Thread producerThread = new Thread(new MessageProducer(protocol, "Anonymous message"));
        producerThread.start();
    }
}
