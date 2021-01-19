package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public interface Manager {

    /**
     * @param file Destination file name
     * @param circuit Circuit to write to a file
     * @return If circuit has successfully been written
     */
    public default boolean writeCircuitToFile(String file, Circuit circuit) {
        Gson gson = new Gson();
        String json = gson.toJson(circuit);
        
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 
     * @param file Destination file name
     * @param circuit Null circuit object that will be overwritten
     * @return If circuit has been successfully read
     */
    public default Circuit readCircuitFromFile(String file) {
        Gson gson = new Gson();
        try {
            FileReader reader = new FileReader(file);
            String json = "";

            int character;
            while((character = reader.read()) != -1) { // For each character in the file, add it to the string
                json += (char)character;
            }
            reader.close();

            return gson.fromJson(json, Circuit.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public default Task startCircuitTask(Circuit circuit, double[] inputs, String threadName) {
        Task task = new Task(circuit, inputs);
        Thread thread = new Thread(task, threadName);
        thread.start();
        return task;
    }
}
