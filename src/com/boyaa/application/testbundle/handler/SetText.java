package com.boyaa.application.testbundle.handler;

import java.util.Hashtable;

import com.boyaa.application.testbundle.Logger;
import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.LuaElement;
import com.boyaa.application.testbundle.TestBundle;
import com.boyaa.application.testbundle.WDStatus;
import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;


/**
 * This handler is used to press keycode.
 * 
 */
/**
 * @author JonLiang
 *
 */
public class SetText extends com.boyaa.application.testbundle.CommandHandler {

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
	  if (command.isElementCommand()) {
		  Logger.debug("Using element passed in.");
	      // Only makes sense on an element
	      try {
	        final Hashtable<String, Object> params = command.params();
	        final LuaElement el = command.getElement();
	        boolean replace = Boolean.parseBoolean(params.get("replace").toString());
	        String text = params.get("text").toString();
	        Logger.debug("3："+text);
	        boolean pressEnter = false;
	        if (text.endsWith("\\n")) {
	          pressEnter = true;
	          text = text.replace("\\n", "");
	          Logger.debug("Will press enter after setting text");
	        }
	        boolean unicodeKeyboard = false;
	        if (params.get("unicodeKeyboard") != null) {
	          unicodeKeyboard = Boolean.parseBoolean(params.get("unicodeKeyboard").toString());
	          Logger.debug("3333333");
	        }
//	        String currText = el.getText();
	        String currText = "";
	        Logger.debug("currText3:"+currText);
	        if (!replace) {
	          text = currText + text;
	        }
	        final boolean result = el.setText(text, unicodeKeyboard);
	        if (!result) {
	            return getErrorResult("el.setText() failed!");
	          }
	          if (pressEnter) {
//	            final UiDevice d = UiDevice.getInstance();
	            TestBundle.getTheInstrumentation().sendKeyDownUpSync(66);
	          }
	        return getSuccessResult(result);
	      } catch (final LuaObjectNotFoundException e) {
	        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
	            e.getMessage());
	      } catch (final Exception e) { // handle NullPointerException
	        return getErrorResult("Unknown error");
	      }
	    } else {
	      return getErrorResult("Unable to set text without an element.");
	    }
	  }
}
