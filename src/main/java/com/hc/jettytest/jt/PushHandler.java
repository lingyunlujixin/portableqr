package com.hc.jettytest.jt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.alibaba.fastjson.JSON;
import com.hc.jettytest.jt.bean.QfEntry;
import com.hc.jettytest.jt.h2.H2Util;

/**
 * <br> Push ( Inserts ) - Data </br>
 * 
 * <br> 通过curl客户端向jetty批量插入数据（json形式），并生成对应你的qr图形及url </br>
 *  
 * @author LUJIXIN
 *
 */
public class PushHandler extends AbstractHandler
{

	// private final static Logger logger = Log.getLogger(PushHandler.class);
	
    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
		request.setCharacterEncoding("GBK");
		
		Map<String, String> data = H2Util.handleRequest(request);
        
        PrintWriter out = response.getWriter();
        
        List<QfEntry> es = new ArrayList<QfEntry>(1);
        
        if(data.get("json").trim().charAt(0) == '[') {
        	es.addAll(JSON.parseArray(data.get("json"), QfEntry.class));
        } else {
        	es.add(JSON.parseObject(data.get("json"), QfEntry.class));
        }
        
        // 返回值: List<len#id#MD5#SerialNum>
        List<String> showVal = H2Util.inserts(es);

        for (String s : showVal) {
        	
        	String[] s1 = s.split("#");
        	
        	// 生成该条数据对应的url
        	String url = H2Util.makeRequestURL(request, s1);
        	
        	// 对url进行编码，生成访问二维码，返回这个二维码图片对应的url地址给客户端，便于客户端直接访问 二维码
        	String qrurl = H2Util.encodeURL(request, url, H2Util.yyyymmdd(), s1);
        	
        	// 如果是浏览器端展示，直接输出图片
        	String imgHtml =    "curl" == data.get("source") 
        			          ? "" 
        			          : String.format("<br><img src=\"%1$s\"/>", qrurl);	

        	out.printf("%1$s(1) URL : %2$s%1$s(2) SER : %3$s%1$s(3) QRU : %4$s%5$s%1$s"
        			, data.get("crlf")  // 换行符
        			, url               // 数据URL
        			, s1[3]             // 商品序列号
					, qrurl             // 生成的QR-CODE的URL
					, imgHtml           // 对应的图片是哪个
			);         
        }

        baseRequest.setHandled(true);
    }
	
//	public void download(HttpServletResponse response ) {
//		
//		ServletOutputStream sos = null; 
//		
//		BufferedInputStream fin = null;
//		
////      PrintWriter writer = response.getWriter();
//	      try {
//	    	  sos = response.getOutputStream(); 
//	    	  fin = new BufferedInputStream(new FileInputStream("E:\\Work\\201604\\459561f2db1a82b8a06b802b19b8c2c6.jpg"));
//      
//	    	  byte[] content = new byte[1024];  
//      
//	    	  int length;  
//      
//			  while ((length = fin.read(content, 0, content.length)) != -1) {  
//				   sos.write(content, 0, length);  
//			  }
//			  
//	} catch (IOException e) {
//		//  Auto-generated catch block
//		e.printStackTrace();
//	}  finally {
//      try {
//		fin.close();
//	      sos.flush();  
//	      sos.close();
//	} catch (IOException e) {
//		//  Auto-generated catch block
//		e.printStackTrace();
//	}  
//
//	}
//	}
    
}

