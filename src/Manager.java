package src;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import com.google.gson.Gson;

public interface Manager {

    /**
     * @param file Destination file name
     * @param circuit Circuit to write to a file
     * @return If circuit has successfully been written
     */
    public default boolean writeCircuitToFile(String file, Circuit circuit) {
        file = "Saved Circuits\\" + file + ".json";
        File newFile = new File(file);
        try {
            newFile.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Gson gson = new Gson();
        Object[] components = new Object[5];
        
        components[0] = circuit.toString();
        components[1] = circuit.getInputs();
        int[] layerSize = new int[circuit.getLayers().length];
        for(int i = 0; i < circuit.getLayers().length; i++) {
            layerSize[i] = circuit.getLayers()[i].length;
        }
        components[2] = layerSize;
        components[3] = circuit.getThresholds();
        components[4] = circuit.getConnectionStrengths();
        
        
        String json = gson.toJson(components);
        try {
            FileWriter writer = new FileWriter(newFile);
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
            FileReader reader = new FileReader("Saved Circuits\\" + file + ".json");
            String json = "";

            int character;
            while((character = reader.read()) != -1) { // For each character in the file, add it to the string
                json += (char)character;
            }
            reader.close();

            Object[] components = gson.fromJson(json, Object[].class);
            String id = (String)components[0];
            int inputs = Math.round(Math.round((double)components[1]));
            Object[] layerSizeNotCast = ((ArrayList<Double>)components[2]).toArray();
            int[] layerSize = new int[layerSizeNotCast.length];
            for(int i = 0; i < layerSize.length; i++) {
                layerSize[i] = Math.round(Math.round((double)layerSizeNotCast[i]));
            }

            ArrayList<ArrayList<Double>> thresholdsArrayList = (ArrayList<ArrayList<Double>>)components[3];
            double[][] thresholds = convert2D(thresholdsArrayList);
            ArrayList<ArrayList<Double>> connectionStrengthArrayList = (ArrayList<ArrayList<Double>>)components[4];
            double[][] connectionStrength = convert2D(connectionStrengthArrayList);

            
            
            return new Circuit(inputs, layerSize, thresholds, connectionStrength, id);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private double[] convert1D(ArrayList<Double> arrayList) {
        double[] array = new double[arrayList.size()];
        Object[] objectArray = arrayList.toArray();
        for(int i = 0; i < array.length; i++) {
            array[i] = (double)objectArray[i];
        }
        return array;
    }

    private double[][] convert2D(ArrayList<ArrayList<Double>> arrayList) {
        double[][] array = new double[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<Double> rowArrayList = arrayList.get(i);
            array[i] = convert1D(rowArrayList);
        }
        return array; 
    }

    public default Task startCircuitTask(Circuit circuit, double[] inputs, String threadName) {
        Task task = new Task(circuit, inputs);
        Thread thread = new Thread(task, threadName);
        thread.start();
        return task;
    }
}
