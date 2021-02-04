package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateSourceFileC {

    public static void createSourceCodeFile(String nameFile, String sourceCode){
        nameFile = nameFile.replace(".toy.txt",".c");
        try {
            File myObj = new File(nameFile);
            myObj.createNewFile();
            writeFile(nameFile,sourceCode);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void writeFile(String nameFile,String sourceCode){
        try {
            FileWriter myWriter = new FileWriter(nameFile);
            myWriter.write(sourceCode);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
