package glim.antony.example.concurrency.repositories;

import lombok.Data;
import glim.antony.example.concurrency.model.Entity;
import glim.antony.example.concurrency.model.SubEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static glim.antony.example.concurrency.logic.common.Common.buildThreadIdentifier;
import static glim.antony.example.concurrency.utils.Constants.ANSI_CYAN;
import static glim.antony.example.concurrency.utils.Constants.ANSI_GREEN;

/*
 *
 * @author antony.glim
 * Created at 23.06.2020
 */
@Data
public class EntityInMemoryRepository {

    private static ConcurrentMap<String /*partition_key*/, List<Entity>> entities;

    private static final String COLOR = ANSI_CYAN;
    private static final String CLASS_NAME = "EntityInMemoryRepository";

    static {
        entities = new ConcurrentHashMap();
        for (char c : upperCaseAlphabet()) {
            if (c == 'F') break;
            putNewPartition(c + "");
        }
    }

    public static void putNewPartition(String partitionName) {
        System.out.printf(buildThreadIdentifier(CLASS_NAME, COLOR) + "Start putting new partition '%s'\n", partitionName);
        entities.put(partitionName, new CopyOnWriteArrayList<>(Arrays.asList(
                new Entity(partitionName + "0", new ArrayList<>(Arrays.asList(new SubEntity(partitionName + "01"), new SubEntity(partitionName + "02")))),
                new Entity(partitionName + "1", new ArrayList<>(Arrays.asList(new SubEntity(partitionName + "11"), new SubEntity(partitionName + "12")))),
                new Entity(partitionName + "2", new ArrayList<>(Arrays.asList(new SubEntity(partitionName + "21"), new SubEntity(partitionName + "22")))),
                new Entity(partitionName + "3", new ArrayList<>(Arrays.asList(new SubEntity(partitionName + "31"), new SubEntity(partitionName + "32")))),
                new Entity(partitionName + "4", new ArrayList<>(Arrays.asList(new SubEntity(partitionName + "41"), new SubEntity(partitionName + "42"))))
        )));
        System.out.printf(buildThreadIdentifier(CLASS_NAME, COLOR) + "Stop putting new partition '%s'\n", partitionName);
    }

    public static void removePartition(String partitionName) {
        System.out.printf(buildThreadIdentifier(CLASS_NAME, COLOR) + "Start removing new partition '%s'\n", partitionName);
        entities.remove(partitionName);
        System.out.printf(buildThreadIdentifier(CLASS_NAME, COLOR) + "Stop removing new partition '%s'\n", partitionName);
    }

    public static void putEntity(String partitionName, int entityId) {
        System.out.printf(buildThreadIdentifier(CLASS_NAME, ANSI_GREEN) + "Start putting new entity '%s' - '%d'\n", partitionName, entityId);
        List<Entity> entityList = entities.get(partitionName);
        if (entityList != null) {
            entityList.add(new Entity(partitionName + entityId,
                    new ArrayList<>(Arrays.asList(
                            new SubEntity(partitionName + entityId + "1"),
                            new SubEntity(partitionName + entityId + "2")
                    ))
            ));
            System.out.printf(buildThreadIdentifier(CLASS_NAME, ANSI_GREEN) + "Add entity successfully '%s' - '%d'\n", partitionName, entityId);
        }
        System.out.printf(buildThreadIdentifier(CLASS_NAME, ANSI_GREEN) + "Stop putting new entity '%s' - '%d'\n", partitionName, entityId);
    }

    public static void removeEntity(String partitionName, int entityId) {
        System.out.printf(buildThreadIdentifier(CLASS_NAME, ANSI_GREEN) + "Start remove entity '%s' - '%d'\n", partitionName, entityId);
        Entity entity = findOneById(partitionName + entityId);
        for (Map.Entry<String /*partition_key*/, List<Entity>> entry : entities.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(partitionName)){
                entry.getValue().remove(entity);
                System.out.printf(buildThreadIdentifier(CLASS_NAME, ANSI_GREEN) + "Remove entity successfully '%s' - '%d'\n", partitionName, entityId);
            }
        }
        System.out.printf(buildThreadIdentifier(CLASS_NAME, ANSI_GREEN) + "Stop remove entity '%s' - '%d'\n", partitionName, entityId);
    }

    public static char[] upperCaseAlphabet() {
        return new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    }

    public static ConcurrentMap<String, List<Entity>> getEntities() {
        return entities;
    }

    public static Entity findOneById(String entityId) {
        for (Map.Entry<String /*partition_key*/, List<Entity>> entry : entities.entrySet()) {
            for (Entity entity : entry.getValue()) {
                if (entityId.equalsIgnoreCase(entity.getId()))
                    return entity;
            }
        }
        return null;
    }
}
