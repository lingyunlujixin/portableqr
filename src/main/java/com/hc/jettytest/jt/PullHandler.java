package com.hc.jettytest.jt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.hc.jettytest.jt.bean.QfEntry;
import com.hc.jettytest.jt.h2.H2Util;

/**
 * <br> Pull( retrieve ) - Data Handler </br>
 * 
 * <br> 扫码时就调用此类的handle函数返回结果  </br>
 *  
 * @author Lujx
 *
 */
public class PullHandler extends AbstractHandler
{
	
	private static final QfEntry EMPTY = new QfEntry();

    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        
        response.setStatus(HttpServletResponse.SC_OK);

        // req[0] = id, req[1] = MD5
        String req[] = H2Util.extract(request.getParameter("id"));
        
        PrintWriter out = response.getWriter();

        QfEntry e = null;
        
        // 用id查询
        List<QfEntry> res = H2Util.select(Long.valueOf(req[0]));
        
        if(res.size() > 0 ) {

        	// 用MD5验证
        	e = req[1].equals(H2Util.MD5(res.get(0).getSerialNum())) ? res.get(0) : EMPTY;
        		
        } else {
        	
        	e = EMPTY;
        }
        
        System.out.println("x: " + e.toHTMLString());
        
        // 加入颜色照片的支持 Add 2016/09/08
        String imgHtml = "";
        if(e.getRemark01() != null && !"".equals(e.getRemark01().trim()) ) {
        	imgHtml = String.format("<img src=\"/res/color/%1$s.%2$s\" alt=\"%1$s\" /><br>", e.getRemark01(), "png");	
        }
        
        String style =    "<style type=text/css>"
        				+ "table.gridtable {"
        				+ "font-family: verdana,arial,sans-serif;"
        				+ "font-size:11px;"
        				+ "color:#333333;"
        				+ "border-width: 1px;"
        				+ "border-color: #666666;"
        				+ "border-collapse: collapse;"
        				+ "}"
	//        				+ "table.gridtable th {"
	//        				+ "border-width: 1px;"
	//        				+ "padding: 8px;"
	//        				+ "border-style: solid;"
	//        				+ "border-color: #666666;"
	//        				+ "background-color: #dedede;"
	//        				+ "}"
        				+ "table.gridtable td {"
			        	+ "border-width: 1px;"
			        	+ "padding: 8px;"
			        	+ "border-style: ridge;"
			        	+ "border-color: #666666;"
			        	+ "background-color: #ffffff;"
			        	+ "}"
        			   + "</style>";
        // bgcolor=\"%1$s\"
        String output  = "<body>" + style + imgHtml + e.toHTMLString() + "</body>";
        
        
        out.println(String.format(output, H2Util.get("bgcolor", "#EE82EE")));
        
        
        baseRequest.setHandled(true);
    }
}


