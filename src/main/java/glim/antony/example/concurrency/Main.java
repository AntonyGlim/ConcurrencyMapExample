package glim.antony.example.concurrency;

import glim.antony.example.concurrency.logic.RandomUpdater;
import glim.antony.example.concurrency.logic.InsertRemoveEntity;
import glim.antony.example.concurrency.logic.InsertRemovePartition;
import glim.antony.example.concurrency.logic.ScheduledReupdater;

/*
 *
 * @author antony.glim
 * Created at 23.06.2020
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            while (true) {
                try {
                    Thread scheduledRecounter = new Thread(new ScheduledReupdater());
                    scheduledRecounter.start();
                    scheduledRecounter.join();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
//            while (true) {
                try {
                    Thread insertRemovePartition = new Thread(new InsertRemovePartition());
                    insertRemovePartition.start();
                    insertRemovePartition.join();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }).start();

        new Thread(() -> {
//            while (true) {
                try {
                    Thread insertRemoveEntity = new Thread(new InsertRemoveEntity());
                    insertRemoveEntity.start();
                    insertRemoveEntity.join();
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }).start();

        new Thread(() -> {
            new Thread(new RandomUpdater()).start();
        }).start();

    }
}