package org.sdu.sem4.g7.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.enums.EntityType;

public abstract class WorldData {

    private final Map<String, Entity> entityMap;
    private Map<String, List<Class<? extends Entity>>> entityTypes;

    public WorldData() {
        this.entityMap = new ConcurrentHashMap<>();
        this.entityTypes = new ConcurrentHashMap<>();
    }

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
        for (Entity e : entity.getChildren()) {
            removeEntity(e);
        }
    }

    public Collection<Entity> getEntities() {
        return entityMap.values();
    }

    @SuppressWarnings("unchecked")
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

    /**
     * Returns if the player has lost the game
     * 
     * @return true if player has lost
     */
	public boolean isGameLost() {
		for (Entity e : this.getEntities()) {
			if (e.getEntityType().equals(EntityType.PLAYER)) {
				return false;
			}
		}
		return true;
	}

    /**
     * returns of the player has won the game
     * 
     * @return true if player has won
     */
	public boolean isGameWon() {
		for (Entity e : this.getEntities()) {
			if (e.getEntityType().equals(EntityType.ENEMY)) {
				return false;
			}
		}
		return true;
	}
}
