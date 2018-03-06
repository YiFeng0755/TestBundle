package com.boyaa.application.testbundle.handler;

import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.CommandHandler;
import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.LuaElement;
import com.boyaa.application.testbundle.WDStatus;

import org.json.JSONException;


/**
 * This handler is used to click elements in the Android UI.
 * 
 * Based on the element Id, click that element.
 * 
 */
public class Click extends CommandHandler {

  /*
   * @param command The {@link AndroidCommand}
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
      try {
        final LuaElement el = command.getElement();
        el.click();
        return getSuccessResult(true);
      } catch (final LuaObjectNotFoundException e) {
        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
            e.getMessage());
      } catch (final Exception e) { // handle NullPointerException
        return getErrorResult("Unknown error");
      }
    } else {
//      final Hashtable<String, Object> params = command.params();
//      final Double[] coords = { Double.parseDouble(params.get("x").toString()),
//          Double.parseDouble(params.get("y").toString()) };
//      final ArrayList<Integer> posVals = absPosFromCoords(coords);
//      final boolean res = UiDevice.getInstance().click(posVals.get(0),
//          posVals.get(1));
//      return getSuccessResult(res);
    	return getErrorResult("Unknown error");
    }
  }
}
