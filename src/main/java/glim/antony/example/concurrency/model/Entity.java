package glim.antony.example.concurrency.model;

import lombok.Data;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *
 * @author antony.glim
 * Created at 23.06.2020
 */
@Data
public class Entity {
    private Lock locker = new ReentrantLock();
    private String id;
    private String payload = "payload";
    private List<SubEntity> subEntities;

    public Entity(String id, List<SubEntity> subEntities) {
        this.id = id;
        this.subEntities = subEntities;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id='" + id + '\'' +
                ", payload='" + payload + '\'' +
                ", subEntities=" + subEntities +
                '}';
    }
}
