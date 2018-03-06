package com.boyaa.application.testbundle;

import com.boyaa.application.testbundle.Logger;
import com.boyaa.application.testbundle.SocketServer;

import android.app.Instrumentation;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.boyaa.application.testbundle.exceptions.SocketServerException;

/**
 * @author JonLiang
 *
 */
public class TestBundle extends InstrumentationTestCase{
	
	private static Context targetContext = null;
	
	public static Instrumentation instru = null;

	public TestBundle() {		
		super();
	}
	
	public static Context getTargetContext() {
		return targetContext;
	}
	
	public static Instrumentation getTheInstrumentation() {
		return instru;
	}
	
//	public static LocalLib getLocalLib(){
//		return locallib;
//	}
	/*
	 * 每一次Test运行前都会调用setUp用于初始化一些测试配置
	 * @see com.boyaa.rainbow.CatTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
    
	/*
	 * 每一次Test运行完成后都会调用tearDown用于关闭进程等需要运行完成后的操作
	 * @see com.boyaa.rainbow.CatTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {		
		super.tearDown();
	}

	public void test_RunServer(){
		instru = getInstrumentation();
		targetContext = instru.getTargetContext();
		
		SocketServer server = null;
	    try {
	    	server = new SocketServer(4724);
	    	server.listenForever();
	    } catch (final SocketServerException e) {
	    	Logger.error(e.getError());
	    } catch (final Exception e) {
	    	Logger.error(e.getMessage());
	    }
	    finally {
	    	System.exit(1);
	    }
	}
	
}