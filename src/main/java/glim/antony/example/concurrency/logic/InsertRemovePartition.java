package glim.antony.example.concurrency.logic;

import glim.antony.example.concurrency.repositories.EntityInMemoryRepository;
import glim.antony.example.concurrency.utils.Constants;

import java.util.*;
import java.util.concurrent.*;

/*
 *
 * @author antony.glim
 * Created at 25.06.2020
 */
public class InsertRemovePartition implements Runnable {

    private static final String COLOR = Constants.ANSI_GREEN;
    private static final String CLASS_NAME = "IRP";

    private static final SplittableRandom RANDOM = new SplittableRandom();

    @Override
    public void run() {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Collection<Future<?>> futures = new LinkedList<>();

        futures.add(executor.submit(() -> {
            try {
                Thread.sleep(6000);
                EntityInMemoryRepository.removePartition("C");
                Thread.sleep(2000);

                EntityInMemoryRepository.putNewPartition("Z");
                Thread.sleep(2000);
                EntityInMemoryRepository.putNewPartition("Y");
                Thread.sleep(2000);
                EntityInMemoryRepository.putNewPartition("X");
                Thread.sleep(15000);

                EntityInMemoryRepository.removePartition("Z");
                EntityInMemoryRepository.removePartition("Y");
                EntityInMemoryRepository.removePartition("X");
                Thread.sleep(10000);

                EntityInMemoryRepository.putNewPartition("C");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.printf(COLOR + CLASS_NAME + " -  Exception in synchronized thread (future.get())\n\t\tError: '%s', cause: '%s', message: '%s'\n" + Constants.ANSI_RESET,
                        e.getClass(), e.getCause(), e.getMessage());
            }
        }
    }

}
