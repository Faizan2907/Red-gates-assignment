/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.redgateassignment1;

/**
 *
 * @author faizan
 */
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.json.JSONArray;

import org.json.JSONObject;
import org.json.JSONException;

public class RedGateAssignment1 {

    
    
    public static void main(String[] args) {
        

        String folderPath = "./data";
        String resultFile = "./result/result.csv";
        String errorFile = "error.txt";
        
        File resultFileCheck = new File(resultFile);
        if (!resultFileCheck.exists()) {
            try {
                resultFileCheck.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths
                .filter(Files::isRegularFile)
                .filter(path-> path.getFileName().toString().endsWith(".json"))
                .forEach(path -> {
                    String fileContent = null;
                    try {
                        
                        fileContent = new String(Files.readAllBytes(path));
//                        JSONObject json = new JSONObject(fileContent);
                        
                        JSONArray jsonArray = new JSONArray(fileContent);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
//                            System.out.println(obj.getInt("id"));
                            int id = obj.getInt("id");
                            String kurzbezeichnung = obj.getString("kurzbezeichnung");
                            String strassenbezeichnung = obj.getString("strassenbezeichnung");
                            // write to result file
                            FileWriter csvWriter = new FileWriter(resultFile, true);
                            csvWriter.append(Integer.toString(id));
                            csvWriter.append(",");
                            csvWriter.append(kurzbezeichnung);
                            csvWriter.append(",");
                            csvWriter.append(strassenbezeichnung);
                            csvWriter.append("\n");
                            csvWriter.flush();
                            csvWriter.close();
                        }          
                        
                    } catch (IOException e) {
                        // write to error file
                        try{
                            FileWriter errorWriter = new FileWriter(errorFile, true);
                            errorWriter.append(path.toString());
                            errorWriter.append(":");
                            errorWriter.append(e.getMessage());
                            errorWriter.append("\n");
                            errorWriter.flush();
                            errorWriter.close();
                        }catch(Exception ex){}
                                      
                    } catch (JSONException e) {
                        // write to error file
                        try{
                            FileWriter errorWriter = new FileWriter(errorFile, true);
                            errorWriter.append(path.toString());
                            errorWriter.append(":");
                            errorWriter.append(e.getMessage());
                            errorWriter.append("\n");
                            errorWriter.flush();
                            errorWriter.close();
                        }catch(Exception ex){}
                        
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
