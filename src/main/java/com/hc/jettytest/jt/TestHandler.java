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
 * <br> test - Data </br>
 * 
 * <br> 反馈数据库总条数、某日新增条数等额外信息便于客户端参考  </br>
 *  
 * @author Lujx
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
        
        logger.info(target);
        
        String req1 = request.getParameter("manu");
        
        String req2 = request.getParameter("today");

        // is date
        String sval = H2Util.formatDate(req1);
        String today = H2Util.formatDate(req2);
        
        // 另一个数据取法，替代today
        if (target != null && target.length() > 1) {
            
        	String  testTar = H2Util.formatDate(target.substring(1));
        	
        	today = testTar == null ? sval : testTar;
        }

        
        List<QfEntry> lst = null;
        
        // is long
        // Long lval = null;
        Long cnt = null;
        
        String print = null;
        
        if(sval != null) {
        	
        	lst = H2Util.selectByManuDate(sval);
        	
        	print = "<br> ManuDate : " + sval + "<br>";
        	
        } else if(today != null) {
        	
        	lst = H2Util.selectByStamp(today);
        	
        	print = "<br> stamp : " + today + "<br>";
        
        } else {
        	

        	// lval = Long.parseLong(req);
        	cnt = H2Util.count();
        	print =   "<br> Current count : " + cnt + "<br>"
    			+ "<br> 帮助 （1） 输入 [ ~/test/?manu=20160527 ] 查看制造日期为20160527的数据<br>"
        		+ "<br> 帮助 （2） 输入 [ ~/test/?today=20160527 ] 或 [ ~/test/20160527 ]查看输入日期为20160527的数据<br>";
        	
        	logger.info(print);
        	
        }
        
    	int i = 1;
    	for (QfEntry e : lst) {
    		
    		print = print + "<br> => ( " + i++ + " ) #" + e.getId() + "#" + e.getSerialNum() + "#" + H2Util.makeRequestId(e.getId(), e.getSerialNum()) + "<br>";
    	}

        PrintWriter out = response.getWriter();
        
        // System.out.println("x: " + e.toHTMLString());
        out.println(print);
        
        baseRequest.setHandled(true);
    }
    
}


