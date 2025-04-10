package org.sdu.sem4.g7.common.data;

import java.util.function.BiConsumer;

/**
 * This class represents a setting in the game.
 * It can only be used to modify GameData.
 */
public class Setting {

    private String name;
    private String description;

    /**
     * The valueType of the setting.
     * This is setup so it can be one of 3 things:
     * 1. A boolean (true / false)
     * 2. An integer (-∞ - ∞)
     * 3. A float (0.0 - 1.0)
     */
    private Class<?> valueTypeClass;

    private Object value;

    /**
     * The function to call when the setting is applied.
     */
    private BiConsumer<GameData, Object> invoke;

    /**
     * Constructor for the Setting class.
     * @param name
     * @param description
     * @param valueTypeClass
     * @param value Value here is treated as an object
     * @param invoke
     */
    private Setting(String name, String description, Class<?> valueTypeClass, Object value, BiConsumer<GameData, Object> invoke) {
        this.name = name;
        this.description = description;
        this.valueTypeClass = valueTypeClass;
        this.value = value;
        this.invoke = invoke;
    }

    /**
     * Constructor for the Setting class.
     * @param name The name of the setting
     * @param description The description of the setting
     * @param value The value of the setting (true / false)
     * @param invoke The function to call when the setting is applied
     */
    public Setting(String name, String description, Boolean value, BiConsumer<GameData, Object> invoke) {
        this(name, description, Boolean.class, (Object) value, invoke);
    }
    /**
     * Constructor for the Setting class.
     * @param name The name of the setting
     * @param description The description of the setting
     * @param value The value of the setting (-∞ - ∞)
     * @param invoke The function to call when the setting is applied
     */
    public Setting(String name, String description, Integer value, BiConsumer<GameData, Object> invoke) {
        this(name, description, Integer.class, (Object) value, invoke);
    }
    /**
     * Constructor for the Setting class.
     * @param name The name of the setting
     * @param description The description of the setting
     * @param value The value of the setting (0.0 - 1.0)
     * @param invoke The function to call when the setting is applied
     */
    public Setting(String name, String description, Float value, BiConsumer<GameData, Object> invoke) {
        this(name, description, Float.class, (Object) value, invoke);
    }


    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Class<?> getValueTypeClass() {
        return valueTypeClass;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }

    public void apply(GameData gameData) {
        if (invoke != null) {
            invoke.accept(gameData, this.value);
        }
    }
}
