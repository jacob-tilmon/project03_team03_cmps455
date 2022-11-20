import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class TaskThread implements Runnable {
    int id;
    int totalBurstTime;
    int currentBurstTime;
    int burstGoalTime;
    ArrayList<Semaphore> taskThreads;
    ArrayList<Semaphore> taskThreadsFin;



    //Constructor is being generated from MAIN
    public TaskThread(ArrayList<Semaphore> taskThreads, ArrayList<Semaphore> taskThreadsFin,int totalBurstTime, int currentBurstTime, int burstGoalTime, int id){
        this.taskThreads = taskThreads;
        this.totalBurstTime = totalBurstTime;
        this.currentBurstTime = currentBurstTime;
        this.burstGoalTime = burstGoalTime;
        this.id = id;
        this.taskThreadsFin = taskThreadsFin;
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
        if (burstGoalTime < totalBurstTime) this.burstGoalTime = burstGoalTime;
        else this.burstGoalTime = this.totalBurstTime;
    }

    //CPU uses this
    public int getBurstGoalTime() {
        return burstGoalTime;
    }

    public int getId(){ return id;}

    public String Tosting(){
        return "ID:"+ id + "\tMax Burst:"+ totalBurstTime +  "\tCurrent Burst:" + currentBurstTime + "\tBurst Goal: "+burstGoalTime;
    }

    @Override
    public void run() {
        //keeps track of the looping/time for each thread
        //taskThreads.get(id).acquireUninterruptibly();

        while(currentBurstTime < totalBurstTime){
            taskThreads.get(id).acquireUninterruptibly();

            for(int i = currentBurstTime; i < burstGoalTime && i < totalBurstTime; i++){
                currentBurstTime++;
                System.out.println("Thread "+id+ " Burst: " + currentBurstTime);
            }
            //if (currentBurstTime == totalBurstTime)
                taskThreadsFin.get(id).release();
        }
        //System.out.println("Thread "+id+ " has left loop.");
    }
}
