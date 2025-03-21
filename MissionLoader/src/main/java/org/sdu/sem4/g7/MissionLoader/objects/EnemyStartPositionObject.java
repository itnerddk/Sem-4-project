package org.sdu.sem4.g7.MissionLoader.objects;

/*
 * Data object containing enemy a enenmy, and its startlocation
 */
public class EnemyStartPositionObject {

	/*
     * Entity type
     */
    private String entityType;

    /*
     * Start position
     */
    private int x;
    private int y;

    /*
     * Start health
     */
    private int health;

	public EnemyStartPositionObject() {}

    public EnemyStartPositionObject(String entityType, int x, int y, int health) {
		this.entityType = entityType;
		this.x = x;
		this.y = y;
		this.health = health;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public String toString() {
		return "EnemyStartPositionObject [entityType=" + entityType + ", x=" + x + ", y=" + y + ", health=" + health
				+ "]";
	}
    
}
