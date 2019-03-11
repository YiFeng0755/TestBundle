package com.boyaa.application.testbundle.handler;


import org.json.JSONException;

import android.app.Instrumentation;
import android.graphics.PointF;

import com.boyaa.application.testbundle.CommandHandler;
import com.boyaa.application.testbundle.Logger;
import com.boyaa.application.testbundle.LuaCommand;
import com.boyaa.application.testbundle.LuaCommandResult;
import com.boyaa.application.testbundle.LuaElement;
import com.boyaa.application.testbundle.LuaElementsHash;
import com.boyaa.application.testbundle.PositionHelper;
import com.boyaa.application.testbundle.exceptions.ElementNotFoundException;
import com.boyaa.application.testbundle.exceptions.InvalidCoordinatesException;
import com.boyaa.application.testbundle.utils.InstrumentationHelper;
import com.boyaa.application.testbundle.utils.Point;

import java.util.Hashtable;

/**
 * This handler is used to swipe.
 * 
 */
public class Swipe extends CommandHandler {
	

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
	Logger.verbose("start swipe1:"+ command);
    final Hashtable<String, Object> params = command.params();
    final Point start = new Point(params.get("startX"), params.get("startY"));
    Logger.verbose("start swipe2:"+ start.x+" " +start.y);
    final Point end = new Point(params.get("endX"), params.get("endY"));
    Logger.verbose("start swipe2:"+ end.x+" " +end.y);
    final Integer steps = (Integer) params.get("steps");
    Logger.verbose("steps:"+ steps);
//    final UiDevice device = UiDevice.getInstance();
    LuaElement el = null;
    final boolean rv;
    InstrumentationHelper instHelper = new InstrumentationHelper();
    LuaElementsHash device = LuaElementsHash.getInstance();
    Point absStartPos = new Point();
    Point absEndPos = new Point();

    try {
     
      if (command.isElementCommand()) {
        el = command.getElement();
        Logger.verbose("el:"+ el.getBounds());
        absStartPos = el.getAbsolutePosition(start);
        absEndPos = el.getAbsolutePosition(end);
      } else {
    	Logger.verbose("233333");
//        absStartPos = PositionHelper.getDeviceAbsPos(start);
//        Logger.verbose("233334");
//        Logger.verbose("absStartPos:"+ absStartPos);
//        Logger.verbose("233335");
//        absEndPos = PositionHelper.getDeviceAbsPos(end);
//        Logger.verbose("absEndPos:"+ absEndPos);
      }
      Logger.debug("Swiping from " + start.toString() + " to " + end.toString() + " with steps: " + steps.toString());
      rv = instHelper.generateSwipeGesture(start.x.floatValue(),start.y.floatValue(), end.x.floatValue(),end.y.floatValue(),steps);
//    	    this..generateSwipeGesture(pointX+pointWidth/2, pointY+pointHeight/2);
    if (!rv) {
      return getErrorResult("The swipe did not complete successfully");
    }
    } catch (final ElementNotFoundException e) {
      return getErrorResult(e.getMessage());
    } catch (final InvalidCoordinatesException e) {
      return getErrorResult(e.getMessage());
    } catch (final Exception e) { // handle NullPointerException
      return getErrorResult("Unknown error");
    }
    return getSuccessResult(rv);
  }
}
