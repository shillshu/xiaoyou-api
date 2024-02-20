package cn.sibetech.core.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * 进行http请求，获取第三方返回的结果
 * 
 * */
public class HttpConnectionUtil {
	public static String getHttpContent(String url, Map<String, Object> paramsMap) {
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			StringBuffer params = new StringBuffer();
			if (paramsMap != null) {
				for (String key : paramsMap.keySet()) {
					params.append(key).append("=").append(paramsMap.get(key))
							.append("&");
				}
			}
			byte[] bytes = params.toString().getBytes();
			con.getOutputStream().write(bytes);
			is = con.getInputStream();
			return readInputStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		return null;
	}
	public static String readInputStream(InputStream inStream) throws Exception{
		 BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
		 StringBuffer buffer = new StringBuffer();
		 String line = "";
		 while ((line = in.readLine()) != null){
			 buffer.append(line);
		 }
		 return buffer.toString();
	}

	public static String post(String url, String body) {
		String json = "";
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setConnectTimeout(20 * 60 * 1000);
			DataOutputStream writer = new DataOutputStream(con.getOutputStream());
			writer.write(body.getBytes("utf-8"));
			writer.flush();
			writer.close();
			is = con.getInputStream();
			json = readInputStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		return json;
	}
	public static String postAuth(String url,String token, String body) {
		String json = "";
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestProperty("Authorization", token);
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setConnectTimeout(20 * 60 * 1000);
			DataOutputStream writer = new DataOutputStream(con.getOutputStream());
			writer.write(body.getBytes("utf-8"));
			writer.flush();
			writer.close();
			is = con.getInputStream();
			json = readInputStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		return json;
	}

	public static String getAuth(String url,String token) {
		String json = "";
		HttpURLConnection con = null;
		InputStream is = null;
		try {
			URL httpUrl = new URL(url);
			con = (HttpURLConnection) httpUrl.openConnection();
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestProperty("Authorization", token);
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setConnectTimeout(20 * 60 * 1000);
			is = con.getInputStream();
			json = readInputStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (con != null) {
				con.disconnect();
			}
		}
		return json;
	}
}
