import java.util.ArrayList;
import java.util.Date;
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
                try {
                    algo = Integer.parseInt(args[i + 1]);
                    if (algo == 2) {
                        burstQuantum = Integer.parseInt(args[i + 2]);
                        i++;
                    }
                } catch (Exception e) {
                    System.out.println("One or more parameters fell outside of expected values.");
                }
            }
            else if (args[i].equals("-C")){
                try {
                    coreNum = Integer.parseInt(args[i + 1]);
                } catch (Exception e) {
                    System.out.println("Core parameter fell outside of expected values. Defaulting to 1 core...");
                }
            }
            else{
                System.out.println("Unexpected argument.");
                break;
            }
        }
        if (algo > 0 && algo < 5 && coreNum >= 1 && coreNum <= 4 && burstQuantum >=0) success = true;
        if (success) {
            if (algo == 4) {
                System.out.println("PSJF is not available in our simulation.");
            } else {
                switch (algo) {
                    case 1:
                        System.out.println("Algo: FCFS");
                        break;
                    case 2:
                        System.out.println("Algo: RR");
                        break;
                    case 3:
                        System.out.println("Algo: NSJF");
                        break;
                    case 4:
                        System.out.println("Algo: PSJF");
                        break;
                    default:
                        System.out.println("This shouldn't happen.");
                }
                System.out.println("Cores: " + coreNum);
                if (burstQuantum > 0) {
                    System.out.println("Burst Quantum: " + burstQuantum);
                }
                //Date start = new Date();
                generation(algo, coreNum, burstQuantum); // calls method to start generating.
                //Date end = new Date();
                //System.out.println("Time: " + (end.getTime() - start.getTime()));
            }
        }
        else {
            System.out.println("One or more parameters were out of expected ranges.");
        }

    }
    public static void generation (int algo, int coreNum, int burstQuantum){
        Random random = new Random();
        int numTasks = random.nextInt(25)+1;
        //int numTasks = 5;

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
            System.out.println("Main : Created Task "+i);
        }

        /*
        TaskThread t0 = new TaskThread(threadSem,threadSemFin,18,0,0,0);
        TaskThread t1 = new TaskThread(threadSem,threadSemFin,7,0,0,1);
        TaskThread t2 = new TaskThread(threadSem,threadSemFin,25,0,0,2);
        TaskThread t3 = new TaskThread(threadSem,threadSemFin,42,0,0,3);
        TaskThread t4 = new TaskThread(threadSem,threadSemFin,21,0,0,4);
        readyQueue.add(t0); readyQueue.add(t1);readyQueue.add(t2);readyQueue.add(t3);readyQueue.add(t4);
        Thread t00 = new Thread(t0);
        Thread t01 = new Thread(t1);
        Thread t02 = new Thread(t2);
        Thread t03 = new Thread(t3);
        Thread t04 = new Thread(t4);
        tasks.add(t00);
        tasks.add(t01);
        tasks.add(t02);
        tasks.add(t03);
        tasks.add(t04);
         */
        System.out.println("----------READY QUEUE---------");
        for (TaskThread t : readyQueue) System.out.println(t.Tosting());
        System.out.println("------------------------------");

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
        for (int i = 0; i < dispatchers.size(); i++) {
            dispatchers.get(i).start();
            System.out.println("Forking Dispatcher "+i);
        }

        for (Thread t : dispatchers) try {t.join(); } catch (Exception e) { System.out.println(e.getMessage());}
        for (Thread t : cpus) try {t.join(); } catch (Exception e) { System.out.println(e.getMessage());}
        System.out.println("Program Finished");
    }
}