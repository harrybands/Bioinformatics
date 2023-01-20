package cs321;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filename: Utils.java
 * Utils and Helper methods for Unit Tests
 * @author Joshua Corrales
 */
public class Utils
{
    public static List<String> readFromFlie(String file) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader((file)));
        List<String> lines = new ArrayList<>();

        String line;

        while((line = reader.readLine()) != null)
        {
            lines.add(line);
        }

        reader.close();
        return lines;
    }
}
