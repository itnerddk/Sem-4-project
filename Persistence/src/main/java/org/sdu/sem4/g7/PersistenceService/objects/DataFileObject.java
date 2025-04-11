package org.sdu.sem4.g7.PersistenceService.objects;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Object that will be serialized to the save file
 */
public class DataFileObject {

    private Date created;

    private Date updated;

    private int revision;

    private Map<String, String> stringMap;

    private Map<String, Integer> intMap;

    private Map<String, List<String>> stringListMap;

    private Map<String, List<Integer>> intListMap;

    public DataFileObject() {}

    public DataFileObject(Date created, Date updated, int revision, Map<String, String> stringMap,
            Map<String, Integer> intMap, Map<String, List<String>> stringListMap,
            Map<String, List<Integer>> intListMap) {
        this.created = created;
        this.updated = updated;
        this.revision = revision;
        this.stringMap = stringMap;
        this.intMap = intMap;
        this.stringListMap = stringListMap;
        this.intListMap = intListMap;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public Map<String, String> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, String> stringMap) {
        this.stringMap = stringMap;
    }

    public Map<String, Integer> getIntMap() {
        return intMap;
    }

    public void setIntMap(Map<String, Integer> intMap) {
        this.intMap = intMap;
    }

    public Map<String, List<String>> getStringListMap() {
        return stringListMap;
    }

    public void setStringListMap(Map<String, List<String>> stringListMap) {
        this.stringListMap = stringListMap;
    }

    public Map<String, List<Integer>> getIntListMap() {
        return intListMap;
    }

    public void setIntListMap(Map<String, List<Integer>> intListMap) {
        this.intListMap = intListMap;
    }

    @Override
    public String toString() {
        return "DataFileObject [created=" + created + ", updated=" + updated + ", revision=" + revision + ", stringMap="
                + stringMap + ", intMap=" + intMap + ", stringListMap=" + stringListMap + ", intListMap=" + intListMap
                + "]";
    }

}
