package com.boyaa.application.testbundle.handler;

import com.boyaa.application.testbundle.CommandHandler;
import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.LuaElement;
import com.boyaa.application.testbundle.WDStatus;
import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;

import org.json.JSONException;

/**
 * This handler is used to get the text of elements that support it.
 * 
 */
public class GetText extends CommandHandler {

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
  public LuaCommandResult execute(final LuaCommand command)
      throws JSONException {
    if (command.isElementCommand()) {
      // Only makes sense on an element
      try {
        final LuaElement el = command.getElement();
        return getSuccessResult(el.getText());
      } catch (final LuaObjectNotFoundException e) {
        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
            e.getMessage());
      } catch (final Exception e) { // handle NullPointerException
        return getErrorResult("Unknown error");
      }
    } else {
      return getErrorResult("Unable to get text without an element.");
    }
  }
}
