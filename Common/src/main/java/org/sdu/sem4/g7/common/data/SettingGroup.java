package org.sdu.sem4.g7.common.data;
import java.util.ArrayList;
import java.util.List;

public class SettingGroup {
    private String name;
    private String description;
    private List<Setting> settings = new ArrayList<>();

    /**
     * Constructor for the SettingGroup class.
     * @param name The name of the setting group
     * @param description The description of the setting group
     */
    public SettingGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor for the SettingGroup class.
     * @param name The name of the setting group
     * @param description The description of the setting group
     * @param settings The initial list of settings in the group
     */
    public SettingGroup(String name, String description, List<Setting> settings) {
        this.name = name;
        this.description = description;
        this.settings = settings;
    }

    /**
     * Get the name of the setting group.
     * @return The name of the setting group
     */
    public String getName() {
        return name;
    }
    /**
     * Get the description of the setting group.
     * @return The description of the setting group
     */
    public String getDescription() {
        return description;
    }
    /**
     * Get the list of settings in the group.
     * @return The list of settings in the group
     */
    public List<Setting> getSettings() {
        return settings;
    }
    /**
     * Add a setting to the group.
     * @param setting The setting to add
     */
    public void addSetting(Setting setting) {
        settings.add(setting);
    }
}