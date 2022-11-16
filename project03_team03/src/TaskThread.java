import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TaskThread implements Runnable {
    int id;
    int totalBurstTime;
    int currentBurstTime;
    int burstGoalTime;
    ArrayList<Semaphore> taskThreads;



    //Constructor is being generated from MAIN
    public TaskThread(ArrayList<Semaphore> taskThreads, int totalBurstTime, int currentBurstTime, int burstGoalTime, int id){
        this.taskThreads = taskThreads;
        this.totalBurstTime = totalBurstTime;
        this.currentBurstTime = currentBurstTime;
        this.burstGoalTime = burstGoalTime;
        this.id = id;
    }



    /** getters for other classes to access our objects **/
    //dispatcher uses this
    public int getTotalBurstTime() {
        return totalBurstTime;
    }

    //CPU uses these
    public int getCurrentBurstTime() {
        return currentBurstTime;
    }

    public void setBurstGoalTime(int burstGoalTime){
        this.burstGoalTime = burstGoalTime;
    }

    //CPU uses this
    public int getBurstGoalTime() {
        return burstGoalTime;
    }

    public int getId(){ return id;}

    @Override
    public void run() {
        //keeps track of the looping/time for each thread
        taskThreads.get(id).acquireUninterruptibly();

        while(currentBurstTime < totalBurstTime){
            taskThreads.get(id).acquireUninterruptibly();

            for(int i = 0; i < burstGoalTime; i++){
                System.out.println("burst (i): " + i);
            }

            taskThreads.get(id).release();
        }
    }
}
