package com.boyaa.application.testbundle;

import android.util.Log;

/**
 * Log to standard out so that the Appium framework can pick it up.
 *
 */
/**
 * @author JonLiang
 *
 */
public class Logger {

  private static String tag = "BOYAA-TESTBUNDLE";

  public static void debug(final String msg) {
    Log.i(tag, msg);
  }

  public static void error(final String msg) {
	  Log.e(tag, msg);
  }

  public static void info(final String msg) {
	  Log.i(tag, msg);
  }
  
  public static void verbose(final String msg) {
	    Log.v(tag, msg);
	  }
}
