package com.boyaa.application.testbundle.handler;

import android.app.Instrumentation;
import android.view.KeyEvent;

import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.TestBundle;


/**
 * This handler is used to press back.
 * 
 */
/**
 * @author JonLiang
 *
 */
public class PressBack extends com.boyaa.application.testbundle.CommandHandler {

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
	Instrumentation instru = TestBundle.getTheInstrumentation();
	instru.sendCharacterSync(KeyEvent.KEYCODE_BACK); //如果不行尝试模拟硬件操作的sendKeyDownUpSync。还有sendStringSync(String text)用于发送字符串
    // Press back returns false even when back was successfully pressed.
    // Always return true.
    return getSuccessResult(true);
  }
}
