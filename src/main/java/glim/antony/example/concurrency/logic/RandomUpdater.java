package glim.antony.example.concurrency.logic;

import glim.antony.example.concurrency.logic.common.Common;
import glim.antony.example.concurrency.model.Entity;
import glim.antony.example.concurrency.repositories.EntityInMemoryRepository;
import glim.antony.example.concurrency.utils.Constants;

import java.util.*;
import java.util.concurrent.*;

/*
 *
 * @author antony.glim
 * Created at 23.06.2020
 */
public class RandomUpdater implements Runnable {

    private static final SplittableRandom RANDOM = new SplittableRandom();
    private static final String COLOR = Constants.ANSI_PURPLE;
    private static final String CLASS_NAME = "RU";

    private List<Entity> entitiesWithoutPartitions;

    private List<Entity> turnMapToListWithoutPartitions() {
        List<Entity> entitiesWithoutPartitions = new CopyOnWriteArrayList<>();
        for (Map.Entry<String, List<Entity>> partition : EntityInMemoryRepository.getEntities().entrySet()) {
            entitiesWithoutPartitions.addAll(partition.getValue());
        }
        return entitiesWithoutPartitions;
    }

    public void run() {
        entitiesWithoutPartitions = turnMapToListWithoutPartitions();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(16);

        while (true) {
            executor.submit(() -> {
                Entity entity = entitiesWithoutPartitions.get(RANDOM.nextInt(0, entitiesWithoutPartitions.size()));
                Common.doWork(CLASS_NAME, COLOR, entity);
            });
            entitiesWithoutPartitions = turnMapToListWithoutPartitions(); //reread entities
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
