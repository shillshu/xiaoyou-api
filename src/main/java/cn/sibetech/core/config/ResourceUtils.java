package cn.sibetech.core.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hexl
 * @date 2019/11/23
 */
@Component
public class ResourceUtils {

    private static boolean exFileActive;

    private static String exFileStaticPath;

    public static String getClasspath() {
        try {
            String path = org.springframework.util.ResourceUtils.getURL("classpath:").getPath();
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStaticPath() {
        try {
            String path;
            if (!exFileActive) {
                path = org.springframework.util.ResourceUtils.getURL("classpath:").getPath();
            }else {
                path = exFileStaticPath;
            }
            return path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public static List<JSONObject> getPermJson() {
        try {
            InputStream is = null;
            if (!exFileActive) {
                ClassPathResource resource = new ClassPathResource("static/json/perm.json");
                is = resource.getInputStream();
            }else {
                is = new FileInputStream(new File(exFileStaticPath+"json/perm.json"));
            }
            String fileString = readFile(is);
            List<JSONObject> list = new ArrayList<>();
            try {
                list = JSONArray.parseArray(fileString, JSONObject.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<JSONObject> getMenuJson() {
        try {
            InputStream is = null;
            if (!exFileActive) {
                ClassPathResource resource = new ClassPathResource("static/json/menu.json");
                is = resource.getInputStream();
            }else {
                is = new FileInputStream(new File(exFileStaticPath+"json/menu.json"));
            }
            String fileString = readFile(is);
            List<JSONObject> list = new ArrayList<>();
            try {
                list = JSONArray.parseArray(fileString, JSONObject.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<JSONObject> getAppJson() {
        try {
            InputStream is = null;
            if (!exFileActive) {
                ClassPathResource resource = new ClassPathResource("static/json/app.json");
                is = resource.getInputStream();
            }else {
                is = new FileInputStream(new File(exFileStaticPath+"json/app.json"));
            }
            String fileString = readFile(is);
            List<JSONObject> list = null;
            try {
                list = JSONArray.parseArray(fileString, JSONObject.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Value("${sys.static-active}")
    public void setExFileActive(boolean exFileActive) {
        ResourceUtils.exFileActive = exFileActive;
    }

    @Value("${sys.static-path}")
    public void setExFileStaticPath(String exFileStaticPath) {
        ResourceUtils.exFileStaticPath = exFileStaticPath;
    }
}
