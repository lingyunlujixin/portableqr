package com.hc.jettytest.jt;


//
//========================================================================
//Copyright (c) 1995-2016 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//  The Eclipse Public License is available at
//  http://www.eclipse.org/legal/epl-v10.html
//
//  The Apache License v2.0 is available at
//  http://www.opensource.org/licenses/apache2.0.php
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================
//

import org.eclipse.jetty.server.Server;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.StdErrLog;

import com.hc.jettytest.jt.h2.H2Util;

public class EmbeddedServerApp
{
	
	private final static Logger logger = Log.getLogger(EmbeddedServerApp.class);
	
	static {
		
		System.getProperties().setProperty("jetty.logs", H2Util.get("jetty.logs.defautpath"));
		
		String defaultPath = (String) System.getProperties().get("jetty.logs");
		
		PrintStream stream = null;
		
		try {
			
			stream = new PrintStream(defaultPath + "/jetty.log");
			
		} catch (FileNotFoundException e) {

			e.printStackTrace(System.err);
		}
		
		// 默认日志时
		if(logger instanceof StdErrLog) {
			((StdErrLog) logger).setStdErrStream(stream);
		}
	}
	
public static void main( String[] args ) throws Exception
{
	
    Server server = new Server( Integer.valueOf(H2Util.get("jetty.port")) );

    // Add a single handler on context "/hello"
    ContextHandler contextPull = new ContextHandler("/push");
    // contextPull.setContextPath(  );
    contextPull.setHandler(new PushHandler());
    
    ContextHandler contextPush = new ContextHandler("/pull");
    contextPush.setHandler(new PullHandler());
    
    ContextHandler contextTest = new ContextHandler("/test");
    contextTest.setHandler(new TestHandler());
    
    ContextHandler contextRes = new ContextHandler("/res");
    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    // resource_handler.setWelcomeFiles(new String[]{ "l.png" });
    resource_handler.setResourceBase(H2Util.get("qr.encode.base"));
    contextRes.setHandler(resource_handler);
    
    // Can be accessed using http://localhost:8080/hello

    ContextHandlerCollection contexts = new ContextHandlerCollection();
    contexts.setHandlers(new Handler[] { contextPush, contextPull, contextTest, contextRes });

    server.setHandler(contexts);

    // Start the server
    logger.info("Start the server....");
    
    server.start();
    server.join();
}
}
