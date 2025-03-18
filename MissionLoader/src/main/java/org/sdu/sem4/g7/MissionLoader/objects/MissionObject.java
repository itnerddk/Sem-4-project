package org.sdu.sem4.g7.MissionLoader.objects;

import java.util.List;

import org.sdu.sem4.g7.common.data.Mission;

/*
 * Extended version of Mission from the Common module.
 * 
 * While the Mission object from Common, only contains metadata, this object aims to contains everything.
 * 
 * NOTE: This is the object serialized to a file.
 */
public class MissionObject extends Mission {
    
    /*
     * For reference, the Mission (From common), contains the following
     * - (int) id
     * - (string) name
     * - (string) description
     * - (int) difficulty
     */

    /*
     * Start position of the player
     */
    private PlayerStartPositionObject player;

    /*
     * Start position for enemies + type of enemies
     */
    private List<EnemyStartPositionObject> enemies;
    
    /*
     * Map in a 2d list, with the integers referencing the tile id.
     */
    private List<List<Integer>> map;

    public MissionObject() {
        super();
    }

	public MissionObject(PlayerStartPositionObject player, List<EnemyStartPositionObject> enemies,
			List<List<Integer>> map) {
		this.player = player;
		this.enemies = enemies;
		this.map = map;
	}

    public MissionObject(int id, String name, String description, int difficulty, PlayerStartPositionObject player,
			List<EnemyStartPositionObject> enemies, List<List<Integer>> map) {
		super(id, name, description, difficulty);
		this.player = player;
		this.enemies = enemies;
		this.map = map;
	}

	public PlayerStartPositionObject getPlayer() {
		return player;
	}

	public void setPlayer(PlayerStartPositionObject player) {
		this.player = player;
	}

	public List<EnemyStartPositionObject> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<EnemyStartPositionObject> enemies) {
		this.enemies = enemies;
	}

	public List<List<Integer>> getMap() {
		return map;
	}

	public void setMap(List<List<Integer>> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return "MissionObject [player=" + player + ", enemies=" + enemies + ", map=" + map + "]";
	}
}
