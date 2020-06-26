package glim.antony.example.concurrency.logic.common;

import glim.antony.example.concurrency.model.Entity;
import glim.antony.example.concurrency.utils.Constants;

import java.util.concurrent.locks.Lock;

/*
 *
 * @author antony.glim
 * Created at 25.06.2020
 */
public class Common {


    public static void doWork(String className, String color, Entity entity) {
        Lock locker = entity.getLocker();
        try {
            long startTime = System.currentTimeMillis();
            String lockedBy = locker.toString();
            locker.lock();
            long totalTime = System.currentTimeMillis() - startTime;
            if (totalTime > 10) {
                System.out.printf(buildThreadIdentifier(className, Constants.ANSI_RED) + " Waited for %s, total time = %d\n" + Constants.ANSI_RESET, lockedBy, totalTime);
            }
            System.out.printf(buildThreadIdentifier(className, color) + "Start process with entity %s\n" + Constants.ANSI_RESET, entity);

            Thread.sleep(500);

            entity.setPayload("'" + " - " + className + " modif - " + Thread.currentThread().getName() + "'");
            System.out.printf(buildThreadIdentifier(className, color) + "Stop process with entity %s\n" + Constants.ANSI_RESET, entity);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            locker.unlock();
        }
    }

    public static String buildThreadIdentifier(String className, String color) {
        return color + className + " - " + Thread.currentThread().getName() + " ";
    }
}
