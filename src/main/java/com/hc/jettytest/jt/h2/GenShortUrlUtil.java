package com.hc.jettytest.jt.h2;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 * 生成短网址并返回
 * @author: Jerri 
 * @date: 2014年3月22日下午9:58:54
 */
public class GenShortUrlUtil {

	public static void helpHw(){
		
		String result;
		
		// nodejs 伪代码
		String domain = "m.duolerong.com";
		String xid = "3ef5f7f6e0fb4277bbb666b1367b352e";
		// exKey为系统分配给您当前域名的密钥，请妥善保管
		String exKey = "fa07d4a4d72d422c8479f295e9043d57";
		long timestamp = System.currentTimeMillis() / 1000;
		
		String base64str = Base64.getEncoder().encodeToString((domain + xid + exKey + timestamp).getBytes());
		String secret = H2Util.MD5(base64str).toLowerCase();
		
		Map<String, String> args = new HashMap<String, String>();
		
		args.put("tag", "");
		args.put("xid", xid);
		args.put("domain", domain);
		args.put("timestamp", String.valueOf(timestamp));
		args.put("secret", secret);
				
		StringBuffer httpUrl = new StringBuffer("http://bdplus.baidu.com/portrait?");
		
		for(String k : args.keySet()) {
			httpUrl.append(k + "=" + args.get(k) + "&");
		}
		httpUrl.deleteCharAt(httpUrl.length() - 1);
		
		out.println(httpUrl);
		
	      URL url;
		try {
			url = new URL(httpUrl.toString());

	        
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
	        connection.setRequestMethod("GET");
	        
	        connection.connect();
			InputStream is = connection.getInputStream();


			result = ConvertStream2Json(is);
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			result = null;
		}
		
		out.println(result);
		
	}
	

	private static String ConvertStream2Json(InputStream inputStream) {
		String jsonStr = "";

		// ByteArrayOutputStream�൱���ڴ������
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		// ��������ת�Ƶ��ڴ��������
		try {
			while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, len);
			}
			// ���ڴ���ת��Ϊ�ַ���
			jsonStr = new String(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonStr;
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
	    httpUrl = httpUrl ; // + "?" + httpArg;

	    System.out.println(httpUrl);
	    try {
	    	
	        URL url = new URL(httpUrl);
	        
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);  
            connection.setDoInput(true);  
            connection.setUseCaches(false);  
	        connection.setRequestMethod("POST");
	        // 填入apikey到HTTP header
	        // connection.setRequestProperty("apikey",  "您自己的apikey");
	        
	        connection.getOutputStream().write(httpArg.getBytes());  
	        
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
	
	/**
	 * 测试生成端连接
	 * @param args
	 * @author: Jerri 
	 * @date: 2014年3月22日下午5:34:05
	 */
	public static void main(String []args){

		// String httpUrl = "http://dwz.cn/create.php";
		// String httpArg = "url=http://121.42.59.173:8080/res/20160527/3497B2F130ADF5F914E5788E1CB0EA7C76E6.png";
		// String jsonResult = request(httpUrl, httpArg);
		// System.out.println(jsonResult);
		
		// helpHw();
		String a = "a_b_c";
		
		String[] as = a.split("_");
		
		for (String t : as ){
			System.out.println(t);
		}
		
		System.out.printf("%1$s.%2$s\n", "A", "b");
		
		System.out.printf(".%1$3s.", "A");
	}
	
	// http://dwz.cn/create.php
	// {"status":-1,"err_msg":"暂时不支持ip域名，请重新输入","longurl":"http://121.42.59.173:8080/res/20160527/3497B2F130ADF5F914E5788E1CB0EA7C76E6.png"}


}