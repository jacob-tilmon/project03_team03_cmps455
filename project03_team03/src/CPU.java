import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class CPU implements Runnable{
    int timeQuantum;
    int algo;
    TaskThread currentTask;
    int id;

    ArrayList<Semaphore> taskThreadSems;
    ArrayList<Semaphore> dispatchThreadSems;
    ArrayList<TaskThread> readyQueue;
    ArrayList<Semaphore> cpuSems;
    ArrayList<Semaphore> taskThreadSemsFin;
    boolean stop = false;


   //this will get called from MAIN
    public CPU(int timeQuantum, int algo, ArrayList<Semaphore> taskThreadSems, ArrayList<Semaphore> taskThreadSemsFin,ArrayList<Semaphore> dispatchThreadSems, int id,
               ArrayList<TaskThread> readyQueue, ArrayList<Semaphore> cpuSems) {
        this.timeQuantum = timeQuantum;
        this.algo = algo;
        this.taskThreadSems = taskThreadSems;
        this.dispatchThreadSems = dispatchThreadSems;
        this.id = id;
        this.readyQueue = readyQueue;
        this.cpuSems = cpuSems;
        this.taskThreadSemsFin = taskThreadSemsFin;

    }
    public void setStop(boolean stop) { this.stop = stop;}
    public void setCurrentTaskID(TaskThread currentTask){
        this.currentTask = currentTask;
    }
    @Override
    public void run() {
        while (!stop) {
            //taskThreadSems.get(currentTask.getId()).acquireUninterruptibly(); //makes sure CPU runs: release thread semaphores
            try {
                if (cpuSems.get(id).tryAcquire(1, TimeUnit.SECONDS)) {

                    if (currentTask != null) {
                        /**update burst time if RR (in this scenario RR will be assigned to int 3)**/
                        if (algo == 2) {
                            //finding burst time per time quantum cycle
                            currentTask.setBurstGoalTime(currentTask.getCurrentBurstTime() + timeQuantum);
                        } else {
                            //when current burst time = total burst time
                            currentTask.setBurstGoalTime(currentTask.getTotalBurstTime());
                        }
                        System.out.println("CPU " + id + " is running: \n" + currentTask.Tosting());
                        taskThreadSems.get(currentTask.getId()).release(); //makes sure CPU doesn't start another task while running
                        taskThreadSemsFin.get(currentTask.getId()).acquire();
                        currentTask = null;

                        dispatchThreadSems.get(id).release();
                    } else break;
                }
            }
            catch (Exception e) { System.out.println(e.getMessage());}
        }
        //System.out.println("CPU " +id+ " has left loop.");
    }
}
