package com.example.authorization.Services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;

@Service
public class FileReader {
    public static String usingBufferedReader(String filePath) throws FileNotFoundException
    {
        StringBuilder contentBuilder = new StringBuilder();
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(targetStream)))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(contentBuilder.toString());
        return new String(contentBuilder.toString().getBytes(Charset.forName("UTF-8")));
    }
}
