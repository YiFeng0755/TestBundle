package com.boyaa.application.testbundle.handler;

import java.util.Hashtable;

import android.app.Instrumentation;
import android.view.KeyEvent;

import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.TestBundle;


/**
 * This handler is used to press keycode.
 * 
 */
/**
 * @author JonLiang
 *
 */
public class PressKeyCode extends com.boyaa.application.testbundle.CommandHandler {
	
	public Integer keyCode;

  /*
   * @param command The {@link AndroidCommand} used for this handler.
   * 
   * @return {@link AndroidCommandResult}
   * 
   * @throws JSONException
   * 
   * @see io.appium.android.bootstrap.CommandHandler#execute(io.appium.android.
   * bootstrap.AndroidCommand)
   */
  @Override
  public LuaCommandResult execute(final LuaCommand command) {
	  try {
	      final Hashtable<String, Object> params = command.params();
	      Object kc = params.get("keycode");
	      if (kc instanceof Integer) {
	        keyCode = (Integer) kc;
	      } else if (kc instanceof String) {
	        keyCode = Integer.parseInt((String) kc);
	      } else {
	        throw new IllegalArgumentException("Keycode of type " + kc.getClass() + "not supported.");
	      }
	
	    } catch (final Exception e) {
	      return getErrorResult(e.getMessage());
	    }
	  Instrumentation instru = TestBundle.getTheInstrumentation();
	  instru.sendCharacterSync(keyCode); //如果不行尝试模拟硬件操作的sendKeyDownUpSync。还有sendStringSync(String text)用于发送字符串
      // Press back returns false even when back was successfully pressed.
      // Always return true.
      return getSuccessResult(true);
  }
}
