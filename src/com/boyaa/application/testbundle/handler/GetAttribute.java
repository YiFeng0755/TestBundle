package com.boyaa.application.testbundle.handler;

import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.CommandHandler;
import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.LuaElement;
import com.boyaa.application.testbundle.WDStatus;
import com.boyaa.application.testbundle.exceptions.NoAttributeFoundException;

import org.json.JSONException;

import java.util.Hashtable;

/**
 * This handler is used to get an attribute of an element.
 * 
 */
public class GetAttribute extends CommandHandler {

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
      // only makes sense on an element
      final Hashtable<String, Object> params = command.params();

      try {
        final LuaElement el = command.getElement();
        final String attr = params.get("attribute").toString();
        if (attr.equals("name") || attr.equals("text")
            || attr.equals("className")) {
          return getSuccessResult(el.getStringAttribute(attr));
        } else {
          return getSuccessResult(String.valueOf(el.getBoolAttribute(attr)));
        }
      } catch (final NoAttributeFoundException e) {
        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
            e.getMessage());
      } catch (final LuaObjectNotFoundException e) {
        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
            e.getMessage());
      } catch (final Exception e) { // el is null
        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
            e.getMessage());
      }
    } else {
      return getErrorResult("Unable to get attribute without an element.");
    }
  }
}
