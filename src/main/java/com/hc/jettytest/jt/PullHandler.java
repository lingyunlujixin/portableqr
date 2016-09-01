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
        out.println("<img src=\"/res/20160901/2337C4A5D2B0E89EF330C77F881AE9C3740.png\" alt=\"上海鲜花港 - 郁金香\" /><br>" + e.toHTMLString());
        
        baseRequest.setHandled(true);
    }
}


