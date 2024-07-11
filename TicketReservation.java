import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class TicketReservation implements Runnable{
    static AtomicInteger tickets;
    static ReentrantLock lock = new ReentrantLock();
    String s;

    TicketReservation(String s,AtomicInteger shared_memory){
        this.s = s;
        this.tickets = shared_memory;
    }
    @Override
    public void run() {
        System.out.println("New Customer : "+Thread.currentThread().getName());
        criticalProcess();
    }
    static void criticalProcess(){

        lock.lock();
        register(2);
        cancel(1);
        lock.unlock();

    }
    public static void register(int n_ticket){
        System.out.println("\nBuying new Ticket : "+Thread.currentThread().getName());
        try{
            lock.lock();
            Thread.sleep(3000);
            AtomicInteger t = new AtomicInteger(tickets.intValue());
            if(t.addAndGet(-n_ticket)<0){
                System.out.println("Required amount of tickets not available");
                return;
            }
            tickets.addAndGet(-n_ticket);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            lock.unlock();
            System.out.println(tickets+" tickets available");
        }
    }
    public static void cancel(int n_ticket){
        System.out.println("\nCancelling the ticket bought : "+Thread.currentThread().getName());
        try{
            lock.lock();
            Thread.sleep(3000);
            tickets.addAndGet(n_ticket);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Tickets cancelled and bill refunded");
            lock.unlock();
            System.out.println(tickets+" tickets available");
        }
    }
}

class Test{
    public static void main(String[] args) {
        AtomicInteger shared_memory = new AtomicInteger(100);
        int n_customers = 7;
        int n_collectors = 5;

        TicketReservation thread = new TicketReservation(" ",shared_memory);
        allocateCounter(n_customers,n_collectors,thread);

    }
    static void allocateCounter(int n_customers,int n_collectors,Runnable run){
        ExecutorService executor = Executors.newFixedThreadPool(n_collectors);
        for(int i=0;i<n_customers;i++){
            executor.execute(run);
        }
        executor.shutdown();

        while(!executor.isTerminated()){}

        System.out.println("\nFinished all threads");
}


}
