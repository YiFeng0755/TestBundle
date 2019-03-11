package com.boyaa.application.testbundle.handler;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Rect;

import com.boyaa.application.testbundle.CommandHandler;
import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.LuaElement;
import com.boyaa.application.testbundle.WDStatus;


/**
 * This handler is used to press back.
 * 
 */
public class GetLocation extends CommandHandler {

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
	    if (!command.isElementCommand()) {
	      return getErrorResult("Unable to get location without an element.");
	    }

	    try {
	      final JSONObject res = new JSONObject();
	      final LuaElement el = command.getElement();
	      final Rect bounds = el.getBounds();
	      res.put("x", bounds.left);
	      res.put("y", bounds.top);
	      return getSuccessResult(res);
	    } catch (final Exception e) {
	      return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT, e.getMessage());
	    }
	  }
	}
