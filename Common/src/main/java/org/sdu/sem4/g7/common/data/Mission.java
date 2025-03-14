package org.sdu.sem4.g7.common.data;

/*
 * Metadata for a mission
 */
public class Mission {
    
    /*
     * Mission id
     */
    private int id;

    /*
     * Name of the mission
     */
    private String name;

    /*
     * Description descriping the mission
     */
    private String description;

    /*
     * Scale of difficulty of the mission
     */
    private int difficulty;


	public Mission(int id, String name, String description, int difficulty) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.difficulty = difficulty;
	}

	public Mission() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return "Mission [id=" + id + ", name=" + name + ", description=" + description + ", difficulty=" + difficulty
				+ "]";
	}
}
