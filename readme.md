**Задача**  
Протестировать работу потокобезопасной структуры в памяти  

**Структура**  
```text
ConcurrentMap<String /*partition_key*/, List<Entity>> entities;
    |
    +---CopyOnWriteArrayList_1    )---- grouping by partitions
    |       |
    |       +---Entity_1    )---- each entity has ReentrantLock
    |       +---Entity_2
    |       +--- ...
    |
    +---CopyOnWriteArrayList_2
    |       |
    |       +---Entity_1
    |       +---Entity_2
    |       +--- ...
    |
    +--- ...
```

**Операции**  
Над структурой производится 4 вида действий:  
1. Обновление всех сущностей по таймеру (ScheduledReupdater);  
2. Обновление случайной сущности (RandomUpdater);  
3. Удаление/добавление партиции (InsertRemovePartition);  
4. Удаление/добавление сущности в партицию (InsertRemoveEntity);  

Все 4 действия выполняются в отдельных потоках, а внутри потоков еще и в пуле потоков.

**Результаты работы**  
```text
SR - ===== '1' Start ScheduledReupdater thread : Thread-4
EntityInMemoryRepository - Thread-7 Start putting new partition 'A'
EntityInMemoryRepository - Thread-7 Stop putting new partition 'A'
EntityInMemoryRepository - Thread-7 Start putting new partition 'B'
EntityInMemoryRepository - Thread-7 Stop putting new partition 'B'
EntityInMemoryRepository - Thread-7 Start putting new partition 'C'
EntityInMemoryRepository - Thread-7 Stop putting new partition 'C'
EntityInMemoryRepository - Thread-7 Start putting new partition 'D'
EntityInMemoryRepository - Thread-7 Stop putting new partition 'D'
EntityInMemoryRepository - Thread-7 Start putting new partition 'E'
EntityInMemoryRepository - Thread-7 Stop putting new partition 'E'
SR - pool-3-thread-1 Start process with entity Entity{id='A0', payload='payload', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-1 Start process with entity Entity{id='D4', payload='payload', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-2 Start process with entity Entity{id='E0', payload='payload', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-3 Start process with entity Entity{id='D3', payload='payload', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
RU - pool-4-thread-1 Stop process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='A0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
SR - pool-3-thread-1 Start process with entity Entity{id='A1', payload='payload', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
RU - pool-4-thread-4 Start process with entity Entity{id='D2', payload='payload', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-2 Stop process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-5 Start process with entity Entity{id='B0', payload='payload', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-3 Stop process with entity Entity{id='D3', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='A1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
SR - pool-3-thread-1 Start process with entity Entity{id='A2', payload='payload', subEntities=[SubEntity(id=A21), SubEntity(id=A22)]}
RU - pool-4-thread-4 Stop process with entity Entity{id='D2', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-7 Start process with entity Entity{id='E1', payload='payload', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
RU - pool-4-thread-5 Stop process with entity Entity{id='B0', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-6  Waited for java.util.concurrent.locks.ReentrantLock@73daa88d[Locked by thread pool-4-thread-5], total time = 300
RU - pool-4-thread-6 Start process with entity Entity{id='B0', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-8 Start process with entity Entity{id='D3', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='A2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A21), SubEntity(id=A22)]}
SR - pool-3-thread-1 Start process with entity Entity{id='A3', payload='payload', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
RU - pool-4-thread-9 Start process with entity Entity{id='E3', payload='payload', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-7 Stop process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
RU - pool-4-thread-6 Stop process with entity Entity{id='B0', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-10 Start process with entity Entity{id='C1', payload='payload', subEntities=[SubEntity(id=C11), SubEntity(id=C12)]}
RU - pool-4-thread-8 Stop process with entity Entity{id='D3', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'A' - '7'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'A' - '7'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'A' - '7'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'A' - '8'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'A' - '8'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'A' - '8'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'A' - '9'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'A' - '9'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'A' - '9'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'B' - '7'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'B' - '7'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'B' - '7'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'B' - '8'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'B' - '8'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'B' - '8'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'B' - '9'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'B' - '9'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'B' - '9'
EntityInMemoryRepository - pool-1-thread-1 Start remove entity 'A' - '4'
EntityInMemoryRepository - pool-1-thread-1 Remove entity successfully 'A' - '4'
EntityInMemoryRepository - pool-1-thread-1 Stop remove entity 'A' - '4'
SR - pool-3-thread-1 Stop process with entity Entity{id='A3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
SR - pool-3-thread-1 Start process with entity Entity{id='A4', payload='payload', subEntities=[SubEntity(id=A41), SubEntity(id=A42)]}
RU - pool-4-thread-11 Start process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
RU - pool-4-thread-9 Stop process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-12 Start process with entity Entity{id='A9', payload='payload', subEntities=[SubEntity(id=A91), SubEntity(id=A92)]}
RU - pool-4-thread-10 Stop process with entity Entity{id='C1', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=C11), SubEntity(id=C12)]}
RU - pool-4-thread-13 Start process with entity Entity{id='A0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='A4', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A41), SubEntity(id=A42)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B0', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-11 Stop process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
RU - pool-4-thread-12 Stop process with entity Entity{id='A9', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=A91), SubEntity(id=A92)]}
RU - pool-4-thread-14  Waited for java.util.concurrent.locks.ReentrantLock@50e4014f[Locked by thread pool-4-thread-12], total time = 92
RU - pool-4-thread-14 Start process with entity Entity{id='A9', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=A91), SubEntity(id=A92)]}
RU - pool-4-thread-15 Start process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-13 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B1', payload='payload', subEntities=[SubEntity(id=B11), SubEntity(id=B12)]}
RU - pool-4-thread-16 Start process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-14 Stop process with entity Entity{id='A9', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=A91), SubEntity(id=A92)]}
RU - pool-4-thread-1 Start process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-15 Stop process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-2 Start process with entity Entity{id='C4', payload='payload', subEntities=[SubEntity(id=C41), SubEntity(id=C42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B11), SubEntity(id=B12)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B2', payload='payload', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-16 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-3 Start process with entity Entity{id='E4', payload='payload', subEntities=[SubEntity(id=E41), SubEntity(id=E42)]}
RU - pool-4-thread-1 Stop process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-4 Start process with entity Entity{id='C3', payload='payload', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
RU - pool-4-thread-2 Stop process with entity Entity{id='C4', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=C41), SubEntity(id=C42)]}
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'C' - '7'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'C' - '7'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'C' - '7'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'C' - '8'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'C' - '8'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'C' - '8'
EntityInMemoryRepository - pool-1-thread-1 Start putting new entity 'C' - '9'
EntityInMemoryRepository - pool-1-thread-1 Add entity successfully 'C' - '9'
EntityInMemoryRepository - pool-1-thread-1 Stop putting new entity 'C' - '9'
SR - pool-3-thread-1 Stop process with entity Entity{id='B2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B3', payload='payload', subEntities=[SubEntity(id=B31), SubEntity(id=B32)]}
RU - pool-4-thread-5 Start process with entity Entity{id='A8', payload='payload', subEntities=[SubEntity(id=A81), SubEntity(id=A82)]}
RU - pool-4-thread-3 Stop process with entity Entity{id='E4', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=E41), SubEntity(id=E42)]}
RU - pool-4-thread-7 Start process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-4 Stop process with entity Entity{id='C3', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
RU - pool-4-thread-6 Start process with entity Entity{id='B1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B11), SubEntity(id=B12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B31), SubEntity(id=B32)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B4', payload='payload', subEntities=[SubEntity(id=B41), SubEntity(id=B42)]}
RU - pool-4-thread-5 Stop process with entity Entity{id='A8', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=A81), SubEntity(id=A82)]}
RU - pool-4-thread-8 Start process with entity Entity{id='E2', payload='payload', subEntities=[SubEntity(id=E21), SubEntity(id=E22)]}
RU - pool-4-thread-7 Stop process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-9 Start process with entity Entity{id='B8', payload='payload', subEntities=[SubEntity(id=B81), SubEntity(id=B82)]}
RU - pool-4-thread-6 Stop process with entity Entity{id='B1', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=B11), SubEntity(id=B12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B4', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B41), SubEntity(id=B42)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B7', payload='payload', subEntities=[SubEntity(id=B71), SubEntity(id=B72)]}
RU - pool-4-thread-10 Start process with entity Entity{id='C3', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
RU - pool-4-thread-8 Stop process with entity Entity{id='E2', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=E21), SubEntity(id=E22)]}
RU - pool-4-thread-11 Start process with entity Entity{id='A1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
RU - pool-4-thread-9 Stop process with entity Entity{id='B8', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=B81), SubEntity(id=B82)]}
RU - pool-4-thread-12 Start process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B7', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B71), SubEntity(id=B72)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B8', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=B81), SubEntity(id=B82)]}
RU - pool-4-thread-10 Stop process with entity Entity{id='C3', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
RU - pool-4-thread-13 Start process with entity Entity{id='C7', payload='payload', subEntities=[SubEntity(id=C71), SubEntity(id=C72)]}
RU - pool-4-thread-11 Stop process with entity Entity{id='A1', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
RU - pool-4-thread-14 Start process with entity Entity{id='C3', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
EntityInMemoryRepository - pool-2-thread-1 Start removing new partition 'C'
EntityInMemoryRepository - pool-2-thread-1 Stop removing new partition 'C'
RU - pool-4-thread-12 Stop process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B8', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B81), SubEntity(id=B82)]}
SR - pool-3-thread-1 Start process with entity Entity{id='B9', payload='payload', subEntities=[SubEntity(id=B91), SubEntity(id=B92)]}
RU - pool-4-thread-15 Start process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-13 Stop process with entity Entity{id='C7', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=C71), SubEntity(id=C72)]}
RU - pool-4-thread-16 Start process with entity Entity{id='A1', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
RU - pool-4-thread-14 Stop process with entity Entity{id='C3', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
RU - pool-4-thread-1 Start process with entity Entity{id='D0', payload='payload', subEntities=[SubEntity(id=D01), SubEntity(id=D02)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='B9', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B91), SubEntity(id=B92)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C0', payload='payload', subEntities=[SubEntity(id=C01), SubEntity(id=C02)]}
RU - pool-4-thread-15 Stop process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-2 Start process with entity Entity{id='B7', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B71), SubEntity(id=B72)]}
RU - pool-4-thread-16 Stop process with entity Entity{id='A1', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
RU - pool-4-thread-3 Start process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-1 Stop process with entity Entity{id='D0', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=D01), SubEntity(id=D02)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C01), SubEntity(id=C02)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C1', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=C11), SubEntity(id=C12)]}
RU - pool-4-thread-4 Start process with entity Entity{id='B9', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B91), SubEntity(id=B92)]}
RU - pool-4-thread-2 Stop process with entity Entity{id='B7', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=B71), SubEntity(id=B72)]}
RU - pool-4-thread-5 Start process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-3 Stop process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-7 Start process with entity Entity{id='B2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C11), SubEntity(id=C12)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C2', payload='payload', subEntities=[SubEntity(id=C21), SubEntity(id=C22)]}
RU - pool-4-thread-4 Stop process with entity Entity{id='B9', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=B91), SubEntity(id=B92)]}
RU - pool-4-thread-6 Start process with entity Entity{id='E2', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=E21), SubEntity(id=E22)]}
RU - pool-4-thread-5 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
EntityInMemoryRepository - pool-2-thread-1 Start putting new partition 'Z'
EntityInMemoryRepository - pool-2-thread-1 Stop putting new partition 'Z'
RU - pool-4-thread-7 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-8  Waited for java.util.concurrent.locks.ReentrantLock@3d7177b[Locked by thread pool-4-thread-7], total time = 93
RU - pool-4-thread-8 Start process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C21), SubEntity(id=C22)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C3', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
RU - pool-4-thread-9 Start process with entity Entity{id='Z0', payload='payload', subEntities=[SubEntity(id=Z01), SubEntity(id=Z02)]}
RU - pool-4-thread-6 Stop process with entity Entity{id='E2', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=E21), SubEntity(id=E22)]}
RU - pool-4-thread-10 Start process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-8 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-11 Start process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C31), SubEntity(id=C32)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C4', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=C41), SubEntity(id=C42)]}
RU - pool-4-thread-9 Stop process with entity Entity{id='Z0', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=Z01), SubEntity(id=Z02)]}
RU - pool-4-thread-12 Start process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-10 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-13 Start process with entity Entity{id='Z0', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=Z01), SubEntity(id=Z02)]}
RU - pool-4-thread-11 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C4', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C41), SubEntity(id=C42)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C7', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=C71), SubEntity(id=C72)]}
RU - pool-4-thread-14 Start process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-12 Stop process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-15 Start process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-13 Stop process with entity Entity{id='Z0', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=Z01), SubEntity(id=Z02)]}
RU - pool-4-thread-16 Start process with entity Entity{id='D1', payload='payload', subEntities=[SubEntity(id=D11), SubEntity(id=D12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C7', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C71), SubEntity(id=C72)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C8', payload='payload', subEntities=[SubEntity(id=C81), SubEntity(id=C82)]}
RU - pool-4-thread-14 Stop process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-1 Start process with entity Entity{id='Z0', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=Z01), SubEntity(id=Z02)]}
RU - pool-4-thread-15 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-2 Start process with entity Entity{id='A3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
EntityInMemoryRepository - pool-2-thread-1 Start putting new partition 'Y'
EntityInMemoryRepository - pool-2-thread-1 Stop putting new partition 'Y'
RU - pool-4-thread-16 Stop process with entity Entity{id='D1', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=D11), SubEntity(id=D12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C8', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C81), SubEntity(id=C82)]}
SR - pool-3-thread-1 Start process with entity Entity{id='C9', payload='payload', subEntities=[SubEntity(id=C91), SubEntity(id=C92)]}
RU - pool-4-thread-3 Start process with entity Entity{id='Y4', payload='payload', subEntities=[SubEntity(id=Y41), SubEntity(id=Y42)]}
RU - pool-4-thread-1 Stop process with entity Entity{id='Z0', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=Z01), SubEntity(id=Z02)]}
RU - pool-4-thread-2 Stop process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
RU - pool-4-thread-4  Waited for java.util.concurrent.locks.ReentrantLock@6fbc6e84[Locked by thread pool-4-thread-2], total time = 110
RU - pool-4-thread-4 Start process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
RU - pool-4-thread-5 Start process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='C9', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=C91), SubEntity(id=C92)]}
SR - pool-3-thread-1 Start process with entity Entity{id='D0', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=D01), SubEntity(id=D02)]}
RU - pool-4-thread-3 Stop process with entity Entity{id='Y4', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=Y41), SubEntity(id=Y42)]}
RU - pool-4-thread-7 Start process with entity Entity{id='B0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-4 Stop process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
RU - pool-4-thread-6 Start process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-5 Stop process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='D0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D01), SubEntity(id=D02)]}
SR - pool-3-thread-1 Start process with entity Entity{id='D1', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=D11), SubEntity(id=D12)]}
RU - pool-4-thread-8 Start process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-7 Stop process with entity Entity{id='B0', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=B01), SubEntity(id=B02)]}
RU - pool-4-thread-9 Start process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
RU - pool-4-thread-6 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-10 Start process with entity Entity{id='Z4', payload='payload', subEntities=[SubEntity(id=Z41), SubEntity(id=Z42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='D1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D11), SubEntity(id=D12)]}
SR - pool-3-thread-1 Start process with entity Entity{id='D2', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-8 Stop process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-11 Start process with entity Entity{id='A7', payload='payload', subEntities=[SubEntity(id=A71), SubEntity(id=A72)]}
RU - pool-4-thread-9 Stop process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
EntityInMemoryRepository - pool-2-thread-1 Start putting new partition 'X'
EntityInMemoryRepository - pool-2-thread-1 Stop putting new partition 'X'
RU - pool-4-thread-12 Start process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-10 Stop process with entity Entity{id='Z4', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=Z41), SubEntity(id=Z42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='D2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
SR - pool-3-thread-1 Start process with entity Entity{id='D3', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
RU - pool-4-thread-11 Stop process with entity Entity{id='A7', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=A71), SubEntity(id=A72)]}
RU - pool-4-thread-14 Start process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-12 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-15 Start process with entity Entity{id='A1', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='D3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
SR - pool-3-thread-1 Start process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-13  Waited for java.util.concurrent.locks.ReentrantLock@11167cea[Locked by thread pool-3-thread-1], total time = 471
RU - pool-4-thread-13 Start process with entity Entity{id='D3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
RU - pool-4-thread-16 Start process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-14 Stop process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-1 Start process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-12'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-15 Stop process with entity Entity{id='A1', payload='' - RU modif - pool-4-thread-15'', subEntities=[SubEntity(id=A11), SubEntity(id=A12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='D4', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
SR - pool-3-thread-1 Start process with entity Entity{id='E0', payload='' - RU modif - pool-4-thread-14'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
RU - pool-4-thread-13 Stop process with entity Entity{id='D3', payload='' - RU modif - pool-4-thread-13'', subEntities=[SubEntity(id=D31), SubEntity(id=D32)]}
RU - pool-4-thread-2 Start process with entity Entity{id='X1', payload='payload', subEntities=[SubEntity(id=X11), SubEntity(id=X12)]}
RU - pool-4-thread-16 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-3 Start process with entity Entity{id='D2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-1 Stop process with entity Entity{id='B2', payload='' - RU modif - pool-4-thread-1'', subEntities=[SubEntity(id=B21), SubEntity(id=B22)]}
RU - pool-4-thread-4 Start process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='E0', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=E01), SubEntity(id=E02)]}
SR - pool-3-thread-1 Start process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
RU - pool-4-thread-2 Stop process with entity Entity{id='X1', payload='' - RU modif - pool-4-thread-2'', subEntities=[SubEntity(id=X11), SubEntity(id=X12)]}
RU - pool-4-thread-5 Start process with entity Entity{id='Y3', payload='payload', subEntities=[SubEntity(id=Y31), SubEntity(id=Y32)]}
RU - pool-4-thread-3 Stop process with entity Entity{id='D2', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-7 Start process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-16'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-4 Stop process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='E1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
SR - pool-3-thread-1 Start process with entity Entity{id='E2', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=E21), SubEntity(id=E22)]}
RU - pool-4-thread-6 Start process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-4'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
RU - pool-4-thread-5 Stop process with entity Entity{id='Y3', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=Y31), SubEntity(id=Y32)]}
RU - pool-4-thread-7 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-8  Waited for java.util.concurrent.locks.ReentrantLock@20468ec7[Locked by thread pool-4-thread-7], total time = 110
RU - pool-4-thread-8 Start process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-7'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-9 Start process with entity Entity{id='E1', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='E2', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=E21), SubEntity(id=E22)]}
SR - pool-3-thread-1 Start process with entity Entity{id='E3', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
RU - pool-4-thread-6 Stop process with entity Entity{id='A3', payload='' - RU modif - pool-4-thread-6'', subEntities=[SubEntity(id=A31), SubEntity(id=A32)]}
RU - pool-4-thread-10 Start process with entity Entity{id='D4', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-11 Start process with entity Entity{id='D2', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-8 Stop process with entity Entity{id='A0', payload='' - RU modif - pool-4-thread-8'', subEntities=[SubEntity(id=A01), SubEntity(id=A02)]}
RU - pool-4-thread-9 Stop process with entity Entity{id='E1', payload='' - RU modif - pool-4-thread-9'', subEntities=[SubEntity(id=E11), SubEntity(id=E12)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='E3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=E31), SubEntity(id=E32)]}
SR - pool-3-thread-1 Start process with entity Entity{id='E4', payload='' - RU modif - pool-4-thread-3'', subEntities=[SubEntity(id=E41), SubEntity(id=E42)]}
RU - pool-4-thread-12 Start process with entity Entity{id='Y3', payload='' - RU modif - pool-4-thread-5'', subEntities=[SubEntity(id=Y31), SubEntity(id=Y32)]}
RU - pool-4-thread-10 Stop process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
RU - pool-4-thread-14 Start process with entity Entity{id='B3', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=B31), SubEntity(id=B32)]}
RU - pool-4-thread-11 Stop process with entity Entity{id='D2', payload='' - RU modif - pool-4-thread-11'', subEntities=[SubEntity(id=D21), SubEntity(id=D22)]}
RU - pool-4-thread-15 Start process with entity Entity{id='D4', payload='' - RU modif - pool-4-thread-10'', subEntities=[SubEntity(id=D41), SubEntity(id=D42)]}
SR - pool-3-thread-1 Stop process with entity Entity{id='E4', payload='' - SR modif - pool-3-thread-1'', subEntities=[SubEntity(id=E41), SubEntity(id=E42)]}
SR - ===== '1' Finished ScheduledReupdater thread : Thread-4, total time: 15788
```  