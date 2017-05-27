package io;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Roshan on 2/10/2017.
 */
public class FileHandler {
    public String read(String path) throws IOException {
        /*
        Read from file

        path : location of the file

        return : String file content
         */

        // Open file to read
        File file = new File(path);
        FileReader reader = new FileReader(file);

        // Create buffer
        char[] f = new char[(int) file.length()];

        // Perform file read operation
        reader.read(f);
        reader.close();

        return new String(f);
    }

    public void write(String data, String path) throws IOException {
        /*
        Write String to a file

        data : String data
        path : location where to be written

        return : null
         */

        // Create file to write
        File file = new File(path);
        file.createNewFile();

        // Perform file write operation
        FileWriter writer = new FileWriter(file);
        writer.write(data);
        writer.flush();
        writer.close();
    }

    public void write(ArrayList<String> data, String path) throws IOException {
        /*
        Write string array list to file

        data : ArrayList of string data
        path : location where to be written

        return : null
         */

        // Create file to write
        File file = new File(path);
        file.createNewFile();

        // Perform file write operation
        FileWriter writer = new FileWriter(file);
        for(String line: data){
            writer.write(line + "\n");
        }
        writer.flush();
        writer.close();
    }


}
