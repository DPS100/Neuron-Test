package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public interface Manager {

    /**
     * @param file Destination file
     * @param circuit Circuit to write to a file
     * @return If circuit has successfully been written
     */
    public default boolean writeCircuitToFile(File file, Circuit circuit) {
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
     * @param file Destination file
     * @param circuit Null circuit object that will be overwritten
     * @return If circuit has been successfully read
     */
    public default Circuit readCircuitFromFile(File file) {
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

}
