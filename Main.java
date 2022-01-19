package com.company ;
import java.util.*;

class Current{
    int current_number;
    Current(int current_number){
        this.current_number=current_number;
    }
}
class Previous{
    int previous[];
    Previous(int [] prev){
        this.previous=prev;
    }
}
class Max{
    int max_steps;
    Max(int pos ,int steps){
        this.max_steps =steps;
    }
}
class Collatz{
    public static int number;

    public static  void main(String[]args){
        Scanner scan = new Scanner(System.in);

        System.out.print("Please Enter a number :");
        number = scan.nextInt();

        Current current = new Current(1);
        Previous previous = new Previous(new int[number]);
        Max max = new Max(0,0);
        CollatzCalculator calculator = new CollatzCalculator(current,previous,max);

        int num_threads = args.length >0?Integer.parseInt(args[0]):4;
        Thread threads[] = new Thread[num_threads];
        System.out.println("Starting "+num_threads+" threads(s)");

        for (int i = 0; i < num_threads; i++) {
            threads[i]= new Thread(calculator);
            threads[i].start();
        }
        try {
            for (int i=0;i<num_threads;i++) {
                threads[i].join();
            }
        }catch (InterruptedException ie){}
        System.out.println("number of steps :"+max.max_steps);


    }

}

class CollatzCalculator implements Runnable{
    Current current ;Previous previous;Max max ;long temp ;int start , number_steps;boolean keep_going;

    public CollatzCalculator(Current current,Previous previous,Max max){
        this.current =current;
        this.previous = previous;
        this.max = max;
    }

    @Override
    public synchronized void run() {
        while(true){
            synchronized (current){
                keep_going = current.current_number<=Collatz.number;
                temp = current.current_number;
                start = current.current_number;
                current.current_number++;
            }
            if(!keep_going){
                break;
            }
            number_steps =0;
            while (temp!=1){
                if(temp<start){
                    synchronized (previous){
                        number_steps +=previous.previous[(int)temp];
                    }
                    break;
                }
                temp = temp%2 ==0 ? temp/2:3*temp + 1;
                number_steps++;
            }

            if(start<Collatz.number){
                synchronized (previous){
                    previous.previous[start]= number_steps;
                }
            }
            synchronized (max){
                if (number_steps >max.max_steps){
                    max.max_steps = number_steps;
                }
            }
        }
    }
}

