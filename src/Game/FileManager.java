package Game;


import java.io.*;
import java.util.ArrayList;

public class FileManager {
    FileReader fileReader;
    BufferedReader bufferReader;
    FileWriter fileWriter;
    BufferedWriter bufferWriter;
    final String FilePath = "src/resources/fileText.txt";

    final int NumberOfLines = 3;


    public ArrayList<String> ReadFileInArray() {
        ArrayList<String> TextRead = new ArrayList<String>();

        try {
            fileReader = new FileReader(FilePath);
            bufferReader = new BufferedReader(fileReader);
            String line = bufferReader.readLine();

            while(line != null){
                TextRead.add(line + "\n");
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

    public String ReadFileInString() {
        String TextRead = "";
        try {
            fileReader = new FileReader(FilePath);
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

    public void WriteFile(String newLine){
        try {
            fileWriter = new FileWriter(FilePath,true);
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
