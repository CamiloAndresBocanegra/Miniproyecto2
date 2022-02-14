package Game;


import java.io.*;
import java.util.ArrayList;

public class FileManager {
    FileReader fileReader;
    BufferedReader bufferReader;
    FileWriter fileWriter;
    BufferedWriter bufferWriter;
    final String RESOURCES_PATH = "src/resources/";

    public ArrayList<String> readFileInArray(String fileName) {
        ArrayList<String> TextRead = new ArrayList<String>();

        try {
            fileReader = new FileReader(RESOURCES_PATH + fileName);
            bufferReader = new BufferedReader(fileReader);
            String line = bufferReader.readLine();

            while(line != null){
                TextRead.add(line);
                line = bufferReader.readLine();
            } // ERROR CODE{ -------------------------------------------------------
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// }END ERROR CODE _________________________________________________________
        return TextRead;
    }

    public String readFileInString(String fileName) {
        String TextRead = "";
        try {
            fileReader = new FileReader(RESOURCES_PATH + fileName);
            bufferReader = new BufferedReader(fileReader);
            String line = bufferReader.readLine();
            while(line != null){
                TextRead += line;
                TextRead += "\n";
                line = bufferReader.readLine();
            } // ERROR CODE{ -------------------------------------------------------
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// }END ERROR CODE _________________________________________________________
        return TextRead;
    }

    public void writeLine(String filename, String newLine){
        try {
            fileWriter = new FileWriter(RESOURCES_PATH + filename,true);
            bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(newLine);
            bufferWriter.newLine();
            // ERROR CODE{ ------------------------------------------------------------
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                bufferWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }// }END ERROR CODE ------------------------------------------------------------
    }

}
