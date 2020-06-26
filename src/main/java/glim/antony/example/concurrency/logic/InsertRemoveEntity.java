package glim.antony.example.concurrency.logic;

import glim.antony.example.concurrency.repositories.EntityInMemoryRepository;
import glim.antony.example.concurrency.utils.Constants;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/*
 *
 * @author antony.glim
 * Created at 25.06.2020
 */
public class InsertRemoveEntity implements Runnable {

    private static final String COLOR = Constants.ANSI_GREEN;
    private static final String CLASS_NAME = "IRE";

    @Override
    public void run() {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Collection<Future<?>> futures = new LinkedList<>();

        futures.add(executor.submit(() -> {
            try {
                Thread.sleep(2_000);
                EntityInMemoryRepository.putEntity("A", 7);
                EntityInMemoryRepository.putEntity("A", 8);
                EntityInMemoryRepository.putEntity("A", 9);
                EntityInMemoryRepository.putEntity("B", 7);
                EntityInMemoryRepository.putEntity("B", 8);
                EntityInMemoryRepository.putEntity("B", 9);

                EntityInMemoryRepository.removeEntity("A", 4);

                Thread.sleep(2_000);
                EntityInMemoryRepository.putEntity("C", 7);
                EntityInMemoryRepository.putEntity("C", 8);
                EntityInMemoryRepository.putEntity("C", 9);

                Thread.sleep(60_000);

                EntityInMemoryRepository.putEntity("A", 4);

                EntityInMemoryRepository.removeEntity("A", 7);
                EntityInMemoryRepository.removeEntity("A", 8);
                EntityInMemoryRepository.removeEntity("A", 9);
                EntityInMemoryRepository.removeEntity("B", 7);
                EntityInMemoryRepository.removeEntity("B", 8);
                EntityInMemoryRepository.removeEntity("B", 9);
                EntityInMemoryRepository.removeEntity("C", 7);
                EntityInMemoryRepository.removeEntity("C", 8);
                EntityInMemoryRepository.removeEntity("C", 9);

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
