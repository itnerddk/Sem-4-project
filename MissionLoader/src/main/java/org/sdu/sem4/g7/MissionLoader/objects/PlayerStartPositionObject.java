package org.sdu.sem4.g7.MissionLoader.objects;

/**
 * Data object containing the players start position
 * 
 * Q&A: Why a seperate object? A: Well, it makes the json (or other type of human readable format) file, in my opinion more readable.
 */
public class PlayerStartPositionObject {
    
    /**
     * Start position
     */
    private int x;
    private int y;

    public PlayerStartPositionObject() {}

	public PlayerStartPositionObject(int x, int y) {
		this.x = x;
		this.y = y;
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

	@Override
	public String toString() {
		return "PlayerStartPositionObject [x=" + x + ", y=" + y + "]";
	}
}
