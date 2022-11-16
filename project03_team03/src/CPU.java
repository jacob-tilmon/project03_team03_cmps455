import java.util.ArrayList;
import java.util.concurrent.Semaphore;


public class CPU implements Runnable{
    int timeQuantum;
    int algo;
    TaskThread currentTask;
    int id;

    ArrayList<Semaphore> taskThreadSems;
    ArrayList<Semaphore> dispatchThreadSems;
    ArrayList<TaskThread> readyQueue;



   //this will get called from MAIN
    public CPU(int timeQuantum, int algo, ArrayList<Semaphore> taskThreadSems, ArrayList<Semaphore> dispatchThreadSems, int id,
               ArrayList<TaskThread> readyQueue) {
        this.timeQuantum = timeQuantum;
        this.algo = algo;
        this.taskThreadSems = taskThreadSems;
        this.dispatchThreadSems = dispatchThreadSems;
        this.id = id;
        this.readyQueue = readyQueue;
    }

    public void setCurrentTaskID(TaskThread currentTask){
        this.currentTask = currentTask;
    }




    @Override
    public void run() {
        taskThreadSems.get(currentTask.getId()).acquireUninterruptibly(); //makes sure CPU runs: release thread semaphores

        /**update burst time if RR (in this scenario RR will be assigned to int 3)**/
        if(algo == 3){
            //finding burst time per time quantum cycle
            readyQueue.get(currentTask.getId()).setBurstGoalTime(readyQueue.get(currentTask.getId()).getCurrentBurstTime()+ timeQuantum);
        }
        else{
            //when current burst time = total burst time
            readyQueue.get(currentTask.getId()).setBurstGoalTime(readyQueue.get(currentTask.getId()).getTotalBurstTime());
        }

        taskThreadSems.get(currentTask.getId()).release(); //makes sure CPU doesn't start another task while running
        taskThreadSems.get(currentTask.getId()).acquireUninterruptibly();


        dispatchThreadSems.get(id).release();
    }
}
