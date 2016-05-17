package com.hc.jettytest.jt;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Qrer {
	
	public static void main(String[] args) {
		String httpUrl = "http://apis.baidu.com/3023/qr/qrcode/";
		// String httpArg = "size=8&qr=http%3A%2F%2Fwww.3023.com";
		String httpArg = "size=8&qr=http%3A%2F%2F121.42.59.173:8080%2Fpull%2F?id=3490411869287C8F9AA783C635B60772FC9F";
		
		String jsonResult = request(httpUrl, httpArg);
		System.out.println(jsonResult);
	}

	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "040aa0c52abf3966fab09236ab9eaa61");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}
