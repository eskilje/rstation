//  Readers/Writers with concurrent read or exclusive write
//
// Usage:
//         javac rw.real.java
//         java Main rounds

import java.util.LinkedList;
import java.util.Random;

class Car extends Thread {
    int carType;
    Station station;
    public Car(int carType, Station station) {
        this.carType = carType;
        this.station = station;
    }
    public void run() {
        station.repair(carType);
    }
}

class Station {
    int slots;
    int[] slotsType = new int[3];
    public Station(int slots,int A, int B, int C) {
        this.slots = slots;
        this.slotsType[0] = A;
        this.slotsType[1] = B;
        this.slotsType[2] = C;

    }
    private synchronized void tryRepair(int Type) {
        while(true) {
            if(slotsType[Type]== 0)
                try {
                    System.out.print("\n" + Type + " typeslots empty");
                    wait();
                } catch(InterruptedException e){}
            else {slotsType[Type]--; break;}
        }
        while(true) {
            if (slots == 0)
                try {
                    System.out.print("\n" + Type + " slots empty");
                    wait();
                } catch (InterruptedException e) {}
            else {slots--; break;}
        }
    }
    private synchronized void endRepair(int Type) {
        slots++;
        slotsType[Type]++;
        notify();
    }
    public void repair(int Type) {
        tryRepair(Type);
        System.out.print("\n" + Type + " just repaired");
        endRepair(Type);
    }
}

class repairstation {
    static int nmrCars = 200;
    static Station station = new Station(5, 1, 8, 8); // V, A, B, C
    public static void main(String[] arg) {
        Random rand = new Random();
        for (int i = 0; i < nmrCars; i++) {
            new Car(rand.nextInt(3), station).start();
        }
    }
}
