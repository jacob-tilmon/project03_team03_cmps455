import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Dispatcher implements Runnable {
    int id;
    //Semaphore dispatcher = new Semaphore(1);
    static Semaphore semReadyQueue = new Semaphore(1);
    ArrayList<Semaphore> semCPUs;
    ArrayList<Semaphore> semDispatchers;
    CPU myCPU;//= new CPU();
    int algorithm;
    ArrayList<TaskThread> readyQueue;


    public Dispatcher(int id, int algorithm, CPU myCPU, ArrayList<TaskThread> readyQueue, ArrayList<Semaphore> semDispatchers, ArrayList<Semaphore> semCPU) {
        this.id = id;
        this.myCPU = myCPU;
        this.algorithm = algorithm;
        this.readyQueue = readyQueue;
        this.semDispatchers = semDispatchers;
        this.semCPUs = semCPU;
    }

    public TaskThread FCFS() {
        TaskThread temp = readyQueue.get(0);
        readyQueue.remove(0);
        return temp;
    }

    public TaskThread SJF() {
        TaskThread thread = readyQueue.get(0);
        int smallest = readyQueue.get(0).getTotalBurstTime();
        int index = 0;
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.get(i).getTotalBurstTime() < smallest) {
                thread = readyQueue.get(i);
                smallest = readyQueue.get(i).getTotalBurstTime();
                index = i;
            }
        }
        readyQueue.remove(index);
        return thread;
    }


    //need help with this
    public TaskThread PSJF() {
        // idea is to instantiate first thread as the one with the shortest time. Then go over ready queue to find the actual thread with the shortest time;
        TaskThread thread = readyQueue.get(0);
        //int smallest = readyQueue.get(0).getTime();
        int index = 0;
        for (int i = 0; i < readyQueue.size(); i++) {

            //  if (readyQueue.get(i).getTime() < smallest){
            //     thread = readyQueue.get(i);
            //      smallest = readyQueue.get(i).getTime();
            //      index =i;
            // }

        }
        semReadyQueue.release();
        return thread;
    }


    public TaskThread RR() {
        TaskThread thread = readyQueue.get(0);

        //if thread.getTime()-timeQuantum <= 0{
        //  readyQueue.remove(0);
        //}
        //else{
        //  readyQueue.add(thread); // question: do I (dispatcher) add it to end of RQ if its burst> time quantum
        // readyQueue.remove(0);
        //}
        return thread;

    }


    public void removeThreadFromRQ() {
        for (int i = 0; i < readyQueue.size(); i++) {
            //if (readyQueue.get(i).currentBurst()== readyQueue.get(i).totalBurst()){
            // readyQueue.remove(i);
            // }

            // for RR
            // if (algorithm == 3){
            // if ( (readyQueue.get(i).totalBurst() >  readyQueue.get(i).currentBurst()) && readyQueue.get(i).currentBurst!=0  ){
            //      Taskthread temp = readyQueue.get(i);
            //      readyQueue.remove(i);
            //      readyQueue.add(temp);
            //
            // }
            //}
        }
    }

    @Override
    public void run() {
        try {
            //

            // acquire ready queue and loop until it has at threads
            //semReadyQueue.acquire();

            while (readyQueue.size() > 0) {
                semDispatchers.get(id).acquire();
                semReadyQueue.acquire();  //should this be here or before while loop?

                if (algorithm == 1) {
                    TaskThread thread = FCFS();
                    // int threadID = FCFS();
                    myCPU.setCurrentTaskID(thread);
                } else if (algorithm == 3) {
                    TaskThread thread = SJF();
                    //int threadID = SJF();
                    myCPU.setCurrentTaskID(thread);
                } else if (algorithm == 4) {
                    TaskThread thread = PSJF();
                    myCPU.setCurrentTaskID(thread);

                } else if (algorithm == 2) {
                    TaskThread thread = RR();
                    myCPU.setCurrentTaskID(thread);
                }

                removeThreadFromRQ(); //method to remove thread from RQ if task thread's current burst = total burst.


                semCPUs.get(id).release();
                semReadyQueue.release();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Dispatcher "+id);
    }
}