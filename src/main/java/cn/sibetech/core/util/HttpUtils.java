package cn.sibetech.core.util;

import com.alibaba.fastjson2.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    private static int TIMEOUT = 10000;

    public static String convert(String input) {
        if (input == null) {
            throw new IllegalArgumentException("输入参数是null");
        }
        StringBuffer sb = new StringBuffer(input.length());
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if ((c >= 0) && (c <= 255)) {
                sb.append(c);
            }
            else {
                try {
                    byte[] bytes = Character.toString(c).getBytes("utf-8");

                    for (int j = 0; j < bytes.length; ++j) {
                        int tmp = bytes[j];
                        if (tmp < 0) {
                            tmp += 256;
                        }

                        sb.append("%").append(Integer.toHexString(tmp).toUpperCase());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        return httpsRequest(requestUrl, requestMethod, outputStr, "UTF-8");
    }
    public static String httpsPost(String requestUrl, Map<String,Object> paramsMap) {
        StringBuffer params=new StringBuffer();
        if(paramsMap!=null){
            for(String key:paramsMap.keySet()){
                params.append(key).append("=").append(paramsMap.get(key))
                        .append("&");
            }
        }
        return httpsRequest(requestUrl, "POST", params.toString());
    }
    /**
     * 发送https请求
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr, String encoding) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            if (!"https".equals(url.getProtocol().toLowerCase())) {
                throw new RuntimeException("不是https请求");
            }

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);

            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes(encoding));
                outputStream.close();
            }

            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject httpsRequestForJson(String requestUrl, String requestMethod, String outputStr) {
        return httpsRequestForJson(requestUrl, requestMethod, outputStr, "UTF-8");
    }
    public static JSONObject httpsRequestForJson(String requestUrl, String requestMethod, String outputStr, String encoding) {
        String result = httpsRequest(requestUrl, requestMethod, outputStr, encoding);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static String httpPost(String url, Map<String,Object> paramsMap, String encoding){
        String json = "";
        try {
            HttpURLConnection con=null;
            URL httpUrl=new URL(url);
            con=(HttpURLConnection)httpUrl.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Charset", encoding);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(TIMEOUT);
            StringBuffer params=new StringBuffer();
            if(paramsMap!=null){
                for(String key:paramsMap.keySet()){
                    params.append(key).append("=").append(paramsMap.get(key))
                            .append("&");
                }
            }
            byte[] bytes=params.toString().getBytes();
            con.getOutputStream().write(bytes);
            InputStream is=con.getInputStream();
            json=new String(readInputStream(is, encoding));
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public static String httpPostEncode(String url, Map<String,Object> paramsMap, String encoding){
        String json = "";
        try {
            HttpURLConnection con=null;
            URL httpUrl=new URL(url);
            con=(HttpURLConnection)httpUrl.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Charset", encoding);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(TIMEOUT);
            StringBuffer params=new StringBuffer();
            if(paramsMap!=null){
                for(String key:paramsMap.keySet()){
                    params.append(key).append("=").append(Encodes.urlEncode((String) paramsMap.get(key)) )
                            .append("&");
                }
            }
            byte[] bytes=params.toString().getBytes();
            con.getOutputStream().write(bytes);
            InputStream is=con.getInputStream();
            json=new String(readInputStream(is, encoding));
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public static String httpGet(String url, String encoding){
        String json = "";
        try {
            HttpURLConnection con=null;
            URL httpUrl=new URL(url);
            con=(HttpURLConnection)httpUrl.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setConnectTimeout(TIMEOUT);
            InputStream is=con.getInputStream();
            json=new String(readInputStream(is, encoding));
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
    public static String httpBodyPost(String url, String body){
        return httpBodyPost(url, body, "UTF-8");
    }
    public static String httpBodyPost(String url, String body, String encoding){
        String json = "";
        try {
            HttpURLConnection con=null;
            URL httpUrl=new URL(url);
            con=(HttpURLConnection)httpUrl.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            DataOutputStream writer = new DataOutputStream(con.getOutputStream());
            writer.write(body.getBytes("utf-8"));
            writer.flush();
            writer.close();
            InputStream is=con.getInputStream();
            json=new String(readInputStream(is, encoding));
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    private static String readInputStream(InputStream inStream, String encoding) throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, encoding));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }
}
