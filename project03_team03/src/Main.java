import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        int coreNum=1;
        int algo=0;
        int burstQuantum=0;
        boolean success = false;

        for (int i = 0; i < args.length; i+=2){
            if (args[i].equals("-S")){
                algo = Integer.parseInt(args[i+1]);
                if (algo==2){
                    burstQuantum = Integer.parseInt(args[i+2]);
                    i++;
                }
            }
            else if (args[i].equals("-C")){
                coreNum = Integer.parseInt(args[i+1]);
            }
            else{
                System.out.println("Unexpected argument.");
                break;
            }
        }
        if (algo > 0 && algo < 4 && coreNum >= 1 && coreNum <= 4 && burstQuantum >=0) success = true;
        if (success) {
            System.out.println("Algo: " + algo);
            System.out.println("Cores: "+ coreNum);
            if (burstQuantum > 0){
                System.out.println("Burst Quantum: "+burstQuantum);
            }
            generation(algo,coreNum,burstQuantum); // calls method to start generating.
        }
        else {
            System.out.println("One or more parameters were out of expected ranges.");
        }

    }
    public static void generation (int algo, int coreNum, int burstQuantum){
        Random random = new Random();
        int numTasks = random.nextInt(25)+1;
        
        ArrayList<TaskThread> readyQueue = new ArrayList<>();
        ArrayList<Thread> tasks = new ArrayList<>();
        ArrayList<Thread> dispatchers = new ArrayList<>();
        ArrayList<Thread> cpus = new ArrayList<>();


        ArrayList<Semaphore> threadSem = new ArrayList<>();
        ArrayList<Semaphore> threadSemFin = new ArrayList<>();
        ArrayList<Semaphore> dispatchSem = new ArrayList<>();
        ArrayList<Semaphore> cpuSem = new ArrayList<>();

        for (int i = 0; i <coreNum; i++){
            Semaphore dSem = new Semaphore(1);
            Semaphore cSem = new Semaphore(0);
            dispatchSem.add(dSem);
            cpuSem.add(cSem);
        }
        for (int i = 0; i < numTasks; i++){
            Semaphore tSem = new Semaphore(0);
            Semaphore tSemf = new Semaphore(0);
            threadSem.add(tSem);
            threadSemFin.add(tSemf);
        }
        for (int i = 0; i < numTasks; i++){
            int taskBurst = random.nextInt(50)+1;
            TaskThread t0 = new TaskThread(threadSem,threadSemFin,taskBurst,0,0,i);
            readyQueue.add(t0);
            Thread t2 = new Thread(t0);
            tasks.add(t2);
        }
        for (TaskThread t : readyQueue) System.out.println(t.Tosting());

        for (int i = 0; i < coreNum; i++){

            CPU c1 = new CPU(burstQuantum,algo,threadSem,threadSemFin,dispatchSem,i,readyQueue,cpuSem);
            Thread c2 = new Thread(c1);
            cpus.add(c2);
            Dispatcher d1 = new Dispatcher(i,algo,c1,readyQueue,dispatchSem,cpuSem);
            Thread d2 = new Thread(d1);
            dispatchers.add(d2);
        }
        for (Thread t : tasks) t.start();
        for (Thread t : cpus) t.start();
        for (Thread t : dispatchers) t.start();

        for (Thread t : dispatchers) try {t.join(); } catch (Exception e) { System.out.println(e.getMessage());}
        for (Thread t : cpus) try {t.join(); } catch (Exception e) { System.out.println(e.getMessage());}
        System.out.println("Program Finished");
    }
}