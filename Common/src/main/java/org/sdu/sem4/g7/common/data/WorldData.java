package org.sdu.sem4.g7.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class WorldData {

    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();
    private Map<String, List<Class<? extends Entity>>> entityTypes = new ConcurrentHashMap<>();

    public void start() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
        entity.getSprite().viewOrderProperty().set(entity.getzIndex());

        return entity.getID();
    }

    public void addEntity(List<Entity> entities) {
        for (Entity entity : entities) {
            this.addEntity(entity);
        }
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    public <E extends Entity> List<E> getEntities(Class<E> entityType) {
        List<E> r = new ArrayList<>();
        for (Entity e : getEntities()) {
            if (entityType.equals(e.getClass())) {
                r.add((E) e);
            }
        }
        return r;
    }

    public Entity getEntity(String ID) {
        return entityMap.get(ID);
    }

    public Map<String, List<Class<? extends Entity>>> getEntityTypes() {
        return entityTypes;
    }

    @SafeVarargs // Up for debate
    public final void addEntityType(String key, Class<? extends Entity>... entities) {
        if (entityTypes.containsKey(key)) {
            for (Class<? extends Entity> entity : entities) {
                entityTypes.get(key).add(entity);
            }
            return;
        }
        List<Class<? extends Entity>> entityList = new ArrayList<>();
        for (Class<? extends Entity> entity : entities) {
            entityList.add(entity);
        }
        entityTypes.put(key, entityList);
    }

    public abstract void load();
}
