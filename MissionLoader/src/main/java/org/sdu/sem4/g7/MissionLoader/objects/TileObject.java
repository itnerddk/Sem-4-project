package org.sdu.sem4.g7.MissionLoader.objects;

/*
 * A metadata object for a tile
 */
public class TileObject {
    
    /*
     * Tile id
     */
    private int id;

    /*
     * Tile Image
     */
    private String image;

    /*
     * Standard z index
     */
    private int z;

    /*
     * Standard collision property
     */
    private boolean collision;

    /*
     * Standard immoveable property
     */
    private boolean immoveable;

    /*
     * Standard health
     */
    private int health;

    public TileObject() {}

	public TileObject(int id, String image, int z, boolean collision, boolean immoveable, int health) {
		this.id = id;
		this.image = image;
		this.z = z;
		this.collision = collision;
		this.immoveable = immoveable;
		this.health = health;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public boolean isImmoveable() {
		return immoveable;
	}

	public void setImmoveable(boolean immoveable) {
		this.immoveable = immoveable;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public String toString() {
		return "TileObject [id=" + id + ", image=" + image + ", z=" + z + ", collision=" + collision + ", immoveable="
				+ immoveable + ", health=" + health + "]";
	}
    
}
