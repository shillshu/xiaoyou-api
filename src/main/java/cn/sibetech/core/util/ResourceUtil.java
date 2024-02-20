package cn.sibetech.core.util;

import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author liwj
 * @date 2022/2/14 16:38
 */
public class ResourceUtil {
    public static String getFile(String filename) {
        InputStream is = null;
        try {
            ClassPathResource resource = new ClassPathResource(filename);
            is = resource.getInputStream();
            return readFile(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    private static String readFile(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(is, "utf-8")) {
            int ch = 0;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
