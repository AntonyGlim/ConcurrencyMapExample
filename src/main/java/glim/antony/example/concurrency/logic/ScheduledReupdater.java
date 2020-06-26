package glim.antony.example.concurrency.logic;

import glim.antony.example.concurrency.logic.common.Common;
import glim.antony.example.concurrency.model.Entity;
import glim.antony.example.concurrency.repositories.EntityInMemoryRepository;
import glim.antony.example.concurrency.utils.Constants;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 *
 * @author antony.glim
 * Created at 23.06.2020
 */
public class ScheduledReupdater implements Runnable {

    private static final String COLOR = Constants.ANSI_BLUE;
    private static final String CLASS_NAME = "SR";
    private static final AtomicInteger COUNTER = new AtomicInteger();

    public void run() {
        long timeStart = System.currentTimeMillis();
        System.out.printf(COLOR + CLASS_NAME + " - ===== '%d' Start ScheduledReupdater thread : %s\n" + Constants.ANSI_RESET,
                COUNTER.incrementAndGet(), Thread.currentThread().getName());

        ConcurrentMap<String, List<Entity>> entities = EntityInMemoryRepository.getEntities();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Collection<Future<?>> futures = new LinkedList<>();

        for (Map.Entry<String, List<Entity>> stringListEntry : entities.entrySet()) {
            futures.add(executor.submit(() -> {
                    for (Entity entity : stringListEntry.getValue()) {
                        Common.doWork(CLASS_NAME, COLOR, entity);
                    }
            }));
        }
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.printf(COLOR + CLASS_NAME + " -  Exception in synchronized thread (future.get())\n\t\tError: '%s', cause: '%s', message: '%s'\n" + Constants.ANSI_RESET,
                        e.getClass(), e.getCause(), e.getMessage());
            }
        }
        System.out.printf(COLOR + CLASS_NAME + " - ===== '%d' Finished ScheduledReupdater thread : %s, total time: %d\n" + Constants.ANSI_RESET,
                COUNTER.get(), Thread.currentThread().getName(), System.currentTimeMillis() - timeStart);
    }

}
