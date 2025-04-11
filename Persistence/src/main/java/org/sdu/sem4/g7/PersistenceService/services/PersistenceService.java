package org.sdu.sem4.g7.PersistenceService.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sdu.sem4.g7.PersistenceService.objects.DataFileObject;
import org.sdu.sem4.g7.common.services.IPersistenceService;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PersistenceService implements IPersistenceService {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static File persistenceFolder = new File("./data/playerdata/");
    private static File persistenceFile = new File(persistenceFolder, "save.json");

    /**
     * 
     * Reads the savefile
     * 
     * @return DataFileObject with all the savefile data
     * @throws StreamReadException
     * @throws DatabindException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private DataFileObject readFileObject() throws StreamReadException, DatabindException, FileNotFoundException, IOException {
        return objectMapper.readValue(new FileReader(persistenceFile), DataFileObject.class);
    }


    /**
     * 
     * writes the savefile
     * 
     * @param object A complete DataFileObject
     * @throws IOException
     */
    private void writeFileObject(DataFileObject object) throws IOException {
        
        // update updated time and revision number
        object.setUpdated(new Date());
        object.setRevision(object.getRevision() + 1);

        // Write the file
        Writer writer = new FileWriter(persistenceFile);
        writer.write(objectMapper.writeValueAsString(object));
        writer.close();
    }

    public PersistenceService() {

        // Create persistence datadir if not exists
        if (!persistenceFolder.isDirectory()) {
            persistenceFolder.mkdirs();
        }

        // Create a barebone persistence file, if it does not exists
        if (!persistenceFile.isFile()) {
            DataFileObject dfo = new DataFileObject();
            dfo.setCreated(new Date());
            dfo.setRevision(0);
            dfo.setStringMap(new HashMap<>());
            dfo.setIntMap(new HashMap<>());
            try {
                writeFileObject(dfo);
            } catch (IOException e) {
                System.err.println("Could not create the playerdata file!");
                e.printStackTrace();
            }
        }

    }

    @Override
    public List<String> getStringList(String key) {
        try {
            return readFileObject().getStringMap().get(key);
        } catch (IOException e) {
            System.err.println("Could not read playerdata file! key given: " + key);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveStringList(String key, List<String> value) {
        try {
            // Load playerdata
            DataFileObject dfo = readFileObject();
            Map<String, List<String>> data = dfo.getStringMap();

            // update data
            data.put(key, value);

            // Save file
            writeFileObject(dfo);

        } catch (IOException e) {
            System.err.println("Could not save stringlist to playerdata with key: " + key);
            e.printStackTrace();
        }
    }

    @Override
    public boolean stringListExists(String key) {
        try {
            // Load playerdata
            DataFileObject dfo = readFileObject();
            Map<String, List<String>> data = dfo.getStringMap();

            // update data
            return data.containsKey(key);
        } catch (IOException e) {
            System.err.println("Could not load playerdata");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Integer> getIntList(String key) {
        try {
            return readFileObject().getIntMap().get(key);
        } catch (IOException e) {
            System.err.println("Could not read playerdata file! key given: " + key);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveIntList(String key, List<Integer> value) {
        try {
            // Load playerdata
            DataFileObject dfo = readFileObject();
            Map<String, List<Integer>> data = dfo.getIntMap();

            // update data
            data.put(key, value);

            // Save file
            writeFileObject(dfo);

        } catch (IOException e) {
            System.err.println("Could not save intlist to playerdata with key: " + key);
            e.printStackTrace();
        }
    }

    @Override
    public boolean intListExists(String key) {
        try {
            // Load playerdata
            DataFileObject dfo = readFileObject();
            Map<String, List<Integer>> data = dfo.getIntMap();

            // update data
            return data.containsKey(key);
        } catch (IOException e) {
            System.err.println("Could not load playerdata");
            e.printStackTrace();
        }
        return false;
    }
    
}
