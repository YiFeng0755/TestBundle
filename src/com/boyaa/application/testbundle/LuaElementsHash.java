package com.boyaa.application.testbundle;

import android.graphics.Rect;
import android.util.Log;
import android.view.Display;

import com.boyaa.application.testbundle.LuaObject;
import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.exceptions.ElementNotFoundException;
import com.boyaa.application.testbundle.utils.Point;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Pattern;

import org.json.JSONException;

/**
 * A cache of elements that the app has seen.
 *
 */
/**
 * @author JonLiang
 *
 */
public class LuaElementsHash {

  private static final Pattern endsWithInstancePattern = Pattern.compile(".*INSTANCE=\\d+]$");

  public static LuaElementsHash getInstance() {
    if (LuaElementsHash.instance == null) {
      LuaElementsHash.instance = new LuaElementsHash();
    }
    return LuaElementsHash.instance;
  }

  private final Hashtable<String, LuaElement> elements;
  private       Integer                           counter;

  private static LuaElementsHash instance;

  /**
   * Constructor
   */
  public LuaElementsHash() {
    counter = 0;
    elements = new Hashtable<String, LuaElement>();
  }

  /**
   * @param element
   * @return
   */
  public LuaElement addElement(final LuaObject element) {
    counter++;
    final String key = counter.toString();
    final LuaElement el = new LuaElement(key, element);
    elements.put(key, el);
    return el;
  }

  /**
   * Return an element given an Id.
   * 
   * @param key
   * @return {@link LuaElement}
   */
  public LuaElement getElement(final String key) {
    return elements.get(key);
  }

  /**
   * Return an elements child given the key (context id), or uses the selector
   * to get the element.
   * 
   * @param sel
   * @param key
   *          Element id.
   * @return {@link LuaElement}
   * @throws ElementNotFoundException
   */
  public LuaElement getElement(final LuaSelector sel, final String key)
      throws ElementNotFoundException {
    LuaElement baseEl;
    baseEl = elements.get(key);
    LuaObject el;
    try {
	    if (baseEl == null) {
	      el = LuaObject.getLuaObjects(sel).get(0);
	    } else {
	      el = baseEl.getChildren(sel).get(0);
	    }
    } catch (final LuaObjectNotFoundException e) {
        throw new ElementNotFoundException();
    }
    if (el.exists()) {
      return addElement(el);
    } else {
      throw new ElementNotFoundException();
    }
  }

  /**
   * Same as {@link #getElement(LuaSelector, String)} but for multiple elements
   * at once.
   * 
   * @param sel
   * @param key
   * @return ArrayList<{@link LuaElement}>
   * @throws LuaObjectNotFoundException
 * @throws ElementNotFoundException 
   */
  public ArrayList<LuaElement> getElements(final LuaSelector sel,
                                               final String key) throws ElementNotFoundException {
	  
    final ArrayList<LuaElement> elements = new ArrayList<LuaElement>();

    // If sel is LuaSelector[CLASS=android.widget.Button, INSTANCE=0]
    // then invoking instance with a non-0 argument will corrupt the selector.
    //
    // sel.instance(1) will transform the selector into:
    // LuaSelector[CLASS=android.widget.Button, INSTANCE=1]
    //
    // The selector now points to an entirely different element.

    final LuaElement baseEl = this.getElement(key);
    try {
	    if (baseEl == null) {
	    	ArrayList<LuaObject> luaObjectsArray = LuaObject.getLuaObjects(sel);
	    	for(LuaObject luaObject:luaObjectsArray) {
	    		 elements.add(addElement(luaObject));
	    	}
	    } else {
	    	ArrayList<LuaObject> luaChildObjectsArray = baseEl.getChildren(sel);
	    	for(LuaObject luaObject:luaChildObjectsArray) {
	    		 elements.add(addElement(luaObject));
	    	}
	    }
    } catch (final LuaObjectNotFoundException e) {
        throw new ElementNotFoundException();
    }

//    LuaSelector tmp = null;
//    int counter = 0;
//    while (keepSearching) {
//      if (baseEl == null) {
//        Logger.debug("Element[" + key + "] is null: (" + counter + ")");
//
//        if (useIndex) {
//          Logger.debug("  using index...");
//          tmp = sel.index(counter);
//        } else {
//          tmp = sel.instance(counter);
//        }
//
//        Logger.debug("getElements tmp selector:" + tmp.toString());
//        lastFoundObj = new LuaObject(tmp);
//      } else {
//        Logger.debug("Element[" + key + "] is " + baseEl.getId() + ", counter: "
//            + counter);
//        lastFoundObj = baseEl.getChild(sel.instance(counter));
//      }
//      counter++;
//      if (lastFoundObj != null && lastFoundObj.exists()) {
//        elements.add(addElement(lastFoundObj));
//      } else {
//        keepSearching = false;
//      }
//    }
    return elements;
  }
  public int getDisplayWidth() throws LuaObjectNotFoundException, JSONException {
	  LuaCommand command = null;
      final LuaElement el = command.getElement();
      final Rect bounds = el.getBounds();
      return bounds.width();
  }
  public int getDisplayHeight() throws LuaObjectNotFoundException, JSONException {
	  LuaCommand command = null;
      final LuaElement el = command.getElement();
      final Rect bounds = el.getBounds();
      return bounds.height();
  }
}