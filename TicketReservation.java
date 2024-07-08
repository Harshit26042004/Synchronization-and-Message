import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketReservation implements Runnable{
    static int tickets = 100;
    String s;

    TicketReservation(String s){
        this.s = s;
    }
    @Override
    public void run() {
        System.out.println("New Customer : "+Thread.currentThread().getName());
        criticalProcess();
    }
    static synchronized void criticalProcess(){
        register(2);
        cancel(1);
    }
    public static void register(int n_ticket){
        System.out.println("\nBuying new Ticket : "+Thread.currentThread().getName());
        try{
            Thread.sleep(3000);
            if(tickets-n_ticket<0){
                System.out.println("Required amount of tickets not available");
                return;
            }
            tickets -= n_ticket;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println(tickets+" tickets available");
        }
    }
    public static void cancel(int n_ticket){
        System.out.println("\nCancelling the ticket bought : "+Thread.currentThread().getName());
        try{
            Thread.sleep(3000);
            tickets += n_ticket;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            System.out.println("Tickets cancelled and bill refunded");
            System.out.println(tickets+" tickets available");
        }
    }
}

class Test{
    public static void main(String[] args) {
        TicketReservation thread = new TicketReservation(" ");
        int n_customers = 5;
        int n_collectors = 5;
        allocateCounter(n_customers,n_collectors,thread);

    }
    static void allocateCounter(int n_customers,int n_collectors,Runnable run){
        ExecutorService executor = Executors.newFixedThreadPool(n_collectors);
        for(int i=0;i<n_customers;i++){
            executor.execute(run);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
//            System.out.println("Something went wrong!!!");
//            break;
        }

        System.out.println("\nFinished all threads");
}


}
