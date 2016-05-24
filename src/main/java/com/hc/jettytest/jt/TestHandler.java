package com.hc.jettytest.jt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.hc.jettytest.jt.bean.QfEntry;
import com.hc.jettytest.jt.h2.H2Util;

/**
 * test - Data
 *  
 * @author admin
 *
 */
public class TestHandler extends AbstractHandler
{
	private final static Logger logger = Log.getLogger(TestHandler.class);
	
    public void handle( String target,
                        Request baseRequest,
                        HttpServletRequest request,
                        HttpServletResponse response ) throws IOException,
                                                      ServletException
    {
        response.setContentType("text/html; charset=utf-8");
        
        response.setStatus(HttpServletResponse.SC_OK);
        
        String req1 = request.getParameter("manu");
        
        String req2 = request.getParameter("today");
        
        // is date
        String sval = H2Util.formatDate(req1);
        String today = H2Util.formatDate(req2);
        
        List<QfEntry> lst = null;
        
        // is long
        // Long lval = null;
        Long cnt = null;
        
        String print = null;
        
        if(sval != null) {
        	
        	lst = H2Util.selectByManuDate(sval);
        	
        	int i = 1;
        	print = "<br> ManuDate : " + sval + "<br>";
        	for (QfEntry e : lst) {
        		print = print + "<br> => ( " + i++ + " ) " + e.getId() + " : " + e.getSerialNum() + "<br>";
        	}
        	
        } else if(today != null) {
        	
        	lst = H2Util.selectByStamp(today);
        	
        	int i = 1;
        	print = "<br> stamp : " + today + "<br>";
        	for (QfEntry e : lst) {
        		print = print + "<br> => ( " + i++ + " ) " + e.getId() + " : " + e.getSerialNum() + "<br>";
        	}
        
        } else {
        	

        	// lval = Long.parseLong(req);
        	cnt = H2Util.count();
        	print = "<br> Current count : " + cnt + "<br>";
        	
        	logger.info(print);
        	
        }

        PrintWriter out = response.getWriter();
        
        // System.out.println("x: " + e.toHTMLString());
        out.println(print);
        
        baseRequest.setHandled(true);
    }
    
}


