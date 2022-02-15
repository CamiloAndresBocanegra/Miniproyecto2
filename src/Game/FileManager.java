package Game;


import java.io.*;
import java.util.ArrayList;

public class FileManager {
    BufferedReader bufferReader;
    InputStream inputStream;
    FileWriter fileWriter;
    BufferedWriter bufferWriter;
    String resourcesPath = "/resources/";

    public ArrayList<String> readFileInArray(String fileName) {
        ArrayList<String> TextRead = new ArrayList<String>();

        try {
            inputStream = FileManager.class.getResourceAsStream(resourcesPath+fileName);
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
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
            inputStream = FileManager.class.getResourceAsStream(resourcesPath+fileName);
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
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

    public void writeEntireFile(String filename, ArrayList<String> textLines)
    {
        try {
            File userstxt = new File("src/resources/users.txt");
            userstxt.createNewFile();
            fileWriter = new FileWriter(userstxt,false);
            bufferWriter = new BufferedWriter(fileWriter);
            for(int line = 0; line < textLines.size(); line++)
            {
                bufferWriter.write(textLines.get(line));
                bufferWriter.newLine();
            }
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
