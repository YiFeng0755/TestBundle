package com.boyaa.application.testbundle;

import org.json.JSONException;

import com.boyaa.application.testbundle.exceptions.ElementNotFoundException;
import com.boyaa.application.testbundle.exceptions.InvalidCoordinatesException;
import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.utils.Point;

import android.graphics.Rect;
import android.util.Log;

public abstract class PositionHelper {
  
  /**
   * Given a position, it will return either the position based on percentage
   * (by passing in a double between 0 and 1) or absolute position based on the
   * coordinates entered.
   *
   * @param pointCoord The position to translate.
   * @param length Length of side to use for percentage positions.
   * @param offset Position offset.
   * @return
   */
  private static double translateCoordinate(double pointCoord, double length, double offset) {
    double translatedCoord = 0;
    
    if (pointCoord == 0) {
      translatedCoord = length * 0.5;
    } else if (Math.abs(pointCoord) > 0 && Math.abs(pointCoord) < 1) {
      translatedCoord = length * pointCoord;
    } else {
      translatedCoord = pointCoord;
    }
    
    return translatedCoord + offset;
  }
  
  /**
   * Translates coordinates relative to an element rectangle into absolute coordinates.
   *
   * @param point A point in relative coordinates.
   * @param displayRect The display rectangle to which the point is relative.
   * @param offsets X and Y values by which to offset the point. These are typically
   * the absolute coordinates of the display rectangle.
   * @param shouldCheckBounds Throw if the translated point is outside displayRect?
   * @return
   * @throws UiObjectNotFoundException
   * @throws InvalidCoordinatesException
   */
  public static Point getAbsolutePosition(final Point point, final Rect displayRect, 
                                          final Point offsets, final boolean shouldCheckBounds)
      throws ElementNotFoundException, InvalidCoordinatesException {
    final Point absolutePosition = new Point();
    
    absolutePosition.x = translateCoordinate(point.x, displayRect.width(), offsets.x);
    absolutePosition.y = translateCoordinate(point.y, displayRect.height(), offsets.y);
    
    if (shouldCheckBounds &&
        !displayRect.contains(absolutePosition.x.intValue(), absolutePosition.y.intValue())) {
      throw new InvalidCoordinatesException("Coordinate " + absolutePosition.toString() +
          " is outside of element rect: " + displayRect.toShortString());
    }
      
    return absolutePosition;
  }

  public static Point getDeviceAbsPos(final Point point)
      throws ElementNotFoundException, InvalidCoordinatesException, LuaObjectNotFoundException, JSONException {
//    final UiDevice d = UiDevice.getInstance();
	  Logger.debug("1110000:"+point);
	 final LuaElementsHash d = LuaElementsHash.getInstance();
     final Rect displayRect = new Rect(0, 0, d.getDisplayWidth(), d.getDisplayHeight());
     Logger.debug("Display bounds: " + displayRect.toShortString());
    
    return getAbsolutePosition(point, displayRect, new Point(), true);
  }
  
}
