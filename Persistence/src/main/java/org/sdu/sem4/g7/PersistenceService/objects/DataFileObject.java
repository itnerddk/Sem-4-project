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

    private Map<String, List<String>> stringMap;

    private Map<String, List<Integer>> intMap;

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

    public Map<String, List<String>> getStringMap() {
        return stringMap;
    }

    public void setStringMap(Map<String, List<String>> stringMap) {
        this.stringMap = stringMap;
    }

    public Map<String, List<Integer>> getIntMap() {
        return intMap;
    }

    public void setIntMap(Map<String, List<Integer>> intMap) {
        this.intMap = intMap;
    }

    @Override
    public String toString() {
        return "DataFileObject [created=" + created + ", updated=" + updated + ", revision=" + revision + ", stringMap="
                + stringMap + ", intMap=" + intMap + "]";
    }

}
