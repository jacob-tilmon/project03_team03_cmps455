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
        System.out.println("generation reached");
    }
}