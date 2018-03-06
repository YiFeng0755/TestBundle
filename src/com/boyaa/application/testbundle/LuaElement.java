package com.boyaa.application.testbundle;

import android.graphics.Rect;
import android.view.MotionEvent.PointerCoords;

import com.boyaa.application.testbundle.LuaObject;
import com.boyaa.application.testbundle.exceptions.ElementNotFoundException;
import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.exceptions.InvalidCoordinatesException;
import com.boyaa.application.testbundle.exceptions.NoAttributeFoundException;
import com.boyaa.application.testbundle.utils.Point;
import com.boyaa.application.testbundle.utils.UnicodeEncoder;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Proxy class for LuaObject.
 *
 */
/**
 * @author JonLiang
 *
 */
public class LuaElement {

  private final LuaObject el;
  private String         id;

  LuaElement(final String id, final LuaObject el) {
    this.el = el;
    this.id = id;
  }

  public LuaElement(final LuaObject luaObj) {
    el = luaObj;
  }

  public boolean click() throws LuaObjectNotFoundException {
    return el.click();
  }
  
//  public boolean dragTo(final int destX, final int destY, final int steps)
//      throws LuaObjectNotFoundException {
//    if (API_18) {
//      return el.dragTo(destX, destY, steps);
//    } else {
//      Logger.error("Device does not support API >= 18!");
//      return false;
//    }
//  }
//
//  public boolean dragTo(final LuaObject destObj, final int steps)
//      throws LuaObjectNotFoundException {
//    if (API_18) {
//      return el.dragTo(destObj, steps);
//    } else {
//      Logger.error("Device does not support API >= 18!");
//      return false;
//    }
//  }
//
//  public Point getAbsolutePosition(final Double X, final Double Y)
//      throws LuaObjectNotFoundException, InvalidCoordinatesException {
//    final Point point = new Point(X, Y);
//    return getAbsolutePosition(point, false);
//  }
//
//  public Point getAbsolutePosition(final Double X, final Double Y,
//      final boolean boundsChecking) throws LuaObjectNotFoundException,
//      InvalidCoordinatesException {
//    final Point point = new Point(X, Y);
//    return getAbsolutePosition(point, boundsChecking);
//  }
//
//  public Point getAbsolutePosition(final Point point)
//      throws LuaObjectNotFoundException, InvalidCoordinatesException {
//    return getAbsolutePosition(point, false);
//  }
//
//  public Point getAbsolutePosition(final Point point,
//      final boolean boundsChecking) throws LuaObjectNotFoundException,
//      InvalidCoordinatesException {
//    final Rect rect = el.getBounds();
//    final Point pos = new Point();
//    Logger.debug("Element bounds: " + rect.toShortString());
//
//    if (point.x == 0) {
//      pos.x = rect.width() * 0.5 + rect.left;
//    } else if (point.x <= 1) {
//      pos.x = rect.width() * point.x + rect.left;
//    } else {
//      pos.x = rect.left + point.x;
//    }
//    if (boundsChecking) {
//      if (pos.x > rect.right || pos.x < rect.left) {
//        throw new InvalidCoordinatesException("X coordinate ("
//            + pos.x.toString() + " is outside of element rect: "
//            + rect.toShortString());
//      }
//    }
//
//    if (point.y == 0) {
//      pos.y = rect.height() * 0.5 + rect.top;
//    } else if (point.y <= 1) {
//      pos.y = rect.height() * point.y + rect.top;
//    } else {
//      pos.y = rect.left + point.y;
//    }
//    if (boundsChecking) {
//      if (pos.y > rect.bottom || pos.y < rect.top) {
//        throw new InvalidCoordinatesException("Y coordinate ("
//            + pos.y.toString() + " is outside of element rect: "
//            + rect.toShortString());
//      }
//    }
//
//    return pos;
//  }
  
  public Point getAbsolutePosition(final Point point)
	      throws ElementNotFoundException, InvalidCoordinatesException, LuaObjectNotFoundException {
	    final Rect rect = this.getBounds();
	    
	    Logger.debug("Element bounds: " + rect.toShortString());
	    
	    return PositionHelper.getAbsolutePosition(point, rect, new Point(rect.left, rect.top), false);
	  }

  public boolean getBoolAttribute(final String attr)
      throws LuaObjectNotFoundException, NoAttributeFoundException {
    boolean res;
    if (attr.equals("")) {
      res = el.isEnabled();
    } else if (attr.equals("pickable")) {
      res = el.isPickable();
    } else if (attr.equals("displayed")) {
        res = el.isDisplayed();
    } else if (attr.equals("selected")) {
        res = el.isSelected();
    } else {
      throw new NoAttributeFoundException(attr);
    }
    return res;
  }

  public Rect getBounds() throws LuaObjectNotFoundException {
    return el.getBounds();
  }
//
//  public LuaObject getChild(final UiSelector sel)
//      throws LuaObjectNotFoundException {
//    return el.getChild(sel);
//  }

  public String getId() {
    return id;
  }
  

  public String getStringAttribute(final String attr)
      throws LuaObjectNotFoundException, NoAttributeFoundException {
    String res;
    if (attr.equals("name")) {
      res = getName();
    } else if (attr.equals("text")) {
      res = getText();
    } else {
      throw new NoAttributeFoundException(attr);
    }
    return res;
  }

  private String getName() {
	return el.getName();
}
//  private String getSelected() {
//	return el.isSelected();
//}

public String getText() throws LuaObjectNotFoundException {
    return el.getText();
  }

  public LuaObject getLuaObject() {
    return el;
  }

//  public Rect getVisibleBounds() throws LuaObjectNotFoundException {
//    return el.getVisibleBounds();
//  }

  public boolean longClick() throws LuaObjectNotFoundException {
    return el.longClick();
  }

  
  public void setId(final String id) {
    this.id = id;
  }

  public ArrayList<LuaObject> getChildren(LuaSelector sel) throws LuaObjectNotFoundException {
	return el.getChildren(sel);
  }

  
  public boolean setText(final String text) throws LuaObjectNotFoundException {
    return setText(text, false);
  }

  public boolean setText(final String text, boolean unicodeKeyboard)
      throws LuaObjectNotFoundException {
    if (unicodeKeyboard && UnicodeEncoder.needsEncoding(text)) {
      Logger.debug("Sending Unicode text to element: " + text);
      String encodedText = UnicodeEncoder.encode(text);
      Logger.debug("Encoded text: " + encodedText);
      return el.setText(encodedText);
    } else {
      Logger.debug("Sending plain text to element: " + text);
      return el.setText(text);
    }
  }
}
