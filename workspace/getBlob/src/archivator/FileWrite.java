package archivator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author javadb.com
 */
public class FileWrite {
    
    /**
     * Prints some data to a file using a BufferedWriter
     */
    public void writeToFile(String filename, Byte b) {
        
        BufferedWriter bufferedWriter = null;
        
        try {
            
            //Construct the BufferedWriter object
            bufferedWriter = new BufferedWriter(new FileWriter(filename));
            
            //Start writing to the output stream
            //bufferedWriter.write("Writing line one to file");
            //bufferedWriter.newLine();
            //bufferedWriter.write("Writing line two to file");
            bufferedWriter.write(b);
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedWriter
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
   
}