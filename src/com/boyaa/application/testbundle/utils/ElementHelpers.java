package com.boyaa.application.testbundle.utils;

import android.view.accessibility.AccessibilityNodeInfo;
//import com.android.uiautomator.common.ReflectionUtils;
import com.boyaa.application.testbundle.LuaObject;
import com.boyaa.application.testbundle.LuaElement;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ElementHelpers {

  private static Method findAccessibilityNodeInfo;

  private static AccessibilityNodeInfo elementToNode(LuaElement element) {
    AccessibilityNodeInfo result = null;
    try {
      result = (AccessibilityNodeInfo) findAccessibilityNodeInfo.invoke(element.getLuaObject(), 5000L);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

//  /**
//   * Remove all duplicate elements from the provided list
//   *
//   * @param elements - elements to remove duplicates from
//   * @return a new list with duplicates removed
//   */
//  public static List<LuaElement> dedupe(List<LuaElement> elements) {
//    try {
//      ReflectionUtils utils = new ReflectionUtils();
//      findAccessibilityNodeInfo = utils.getMethod(UiObject.class, "findAccessibilityNodeInfo", long.class);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    List<LuaElement> result = new ArrayList<LuaElement>();
//    List<AccessibilityNodeInfo> nodes = new ArrayList<AccessibilityNodeInfo>();
//
//    for (LuaElement element : elements) {
//      AccessibilityNodeInfo node = elementToNode(element);
//      if (!nodes.contains(node)) {
//        nodes.add(node);
//        result.add(element);
//      }
//    }
//
//    return result;
//  }

  /**
   * Return the JSONObject which Appium returns for an element
   *
   * For example, appium returns elements like [{"ELEMENT":1}, {"ELEMENT":2}]
   */
  public static JSONObject toJSON(LuaElement el) throws JSONException {
    return new JSONObject().put("ELEMENT", el.getId());
  }
}
