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
 * Pull - Data
 *  
 * @author admin
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
        out.println("<br>" + e.toHTMLString());
        
        baseRequest.setHandled(true);
    }
}


