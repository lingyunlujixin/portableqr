package com.hc.jettytest.jt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.alibaba.fastjson.JSON;
import com.hc.jettytest.jt.bean.QfEntry;
import com.hc.jettytest.jt.h2.H2Util;

/**
 * <br> Push ( Inserts ) - Data </br>
 * 
 * <br> 通过curl客户端向jetty批量插入数据（json形式），并生成对应你的qr图形及url </br>
 *  
 * @author Lujx
 *
 */
public class PushHandler extends AbstractHandler
{

	private final static Logger logger = Log.getLogger(PushHandler.class);
	
    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
		request.setCharacterEncoding("GBK");
		
		Map<String, String[]> map = request.getParameterMap();
		
		String json, crlf;
		
		// 如果从浏览器端push
		if(map.size() > 0) {
			
			StringBuffer json0 = new StringBuffer("{");
			

			for(String k : map.keySet()) {
				json0.append(String.format("'%1$s':'%2$s'", k, map.get(k)[0]));
				json0.append(",");
			}
			
			json0.deleteCharAt(json0.length() - 1).append("}");
			
			json = json0.toString();
			
			crlf = "<br>";
			
		// 通过curl进行push
		} else {
	        
	        json = getJsonFromRequest(request);
	        
	        crlf = "\n";
		}
		
        
        PrintWriter out = response.getWriter();

        
        logger.info(json);
        
        List<QfEntry> es = new ArrayList<QfEntry>(1);
        
        if(json.trim().charAt(0) == '[') {
        	es.addAll(JSON.parseArray(json, QfEntry.class));
        } else {
        	es.add(JSON.parseObject(json, QfEntry.class));
        }
        
        // 返回值: List<len#id#MD5#SerialNum>
        List<String> showVal = H2Util.inserts(es);

        for (String s : showVal) {
        	
        	String[] s1 = s.split("#");
        	
        	// 生成该条数据对应的url
        	String url = H2Util.makeRequestURL(request, s1);
        	
        	// 对url进行编码，生成访问二维码，返回这个二维码图片对应的url地址给客户端，便于客户端直接访问 二维码
        	String qrurl = H2Util.encodeURL(request, url, H2Util.yyyymmdd(), s1);
        	
        	out.println(String.format("%1$s(1) URL : %2$s%1$s(2) SER : %3$s%1$s(3) QRU : %4$s", crlf, url, s1[3], qrurl));
        }

        baseRequest.setHandled(true);
    }
    
	private String getJsonFromRequest(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		BufferedReader reader;
		try {

			// request.setCharacterEncoding("GBK");
			
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			if ("".equals(json.toString().trim())) {
				if ("GET".equals(request.getMethod())) {
					String str = new String(request.getQueryString().getBytes(
							"iso-8859-1"), "utf-8").replaceAll("%22", "\"")
							.replaceAll("%7B", "{").replaceAll("%7D", "}")
							.replaceAll("%20", " ").replaceAll("%5B", "[")
							.replaceAll("%5D", "]").replaceAll("%3A", ":")
							.replaceAll("%2C", ",");
					System.out.println(str);
					java.net.URLDecoder.decode(str, "UTF-8");
					System.out.println(str);
					return str;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}
	
	public void download(HttpServletResponse response ) {
		
		ServletOutputStream sos = null; 
		
		BufferedInputStream fin = null;
		
//      PrintWriter writer = response.getWriter();
	      try {
	    	  sos = response.getOutputStream(); 
	    	  fin = new BufferedInputStream(new FileInputStream("E:\\Work\\201604\\459561f2db1a82b8a06b802b19b8c2c6.jpg"));
      
	    	  byte[] content = new byte[1024];  
      
	    	  int length;  
      
			  while ((length = fin.read(content, 0, content.length)) != -1) {  
				   sos.write(content, 0, length);  
			  }
			  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  finally {
      try {
		fin.close();
	      sos.flush();  
	      sos.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  

	}
	}
}

