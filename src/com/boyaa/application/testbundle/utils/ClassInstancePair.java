package com.boyaa.application.testbundle.utils;

import com.boyaa.application.testbundle.LuaSelector;

/**
 * Simple class for holding a String 2-tuple. An android class, and instance number, used for finding elements by xpath.
 */
public class ClassInstancePair {

  private String androidClass;
  private String instance;

  public ClassInstancePair(String clazz, String inst) {
    androidClass = clazz;
    instance = inst;
  }

  public String getAndroidClass() {
    return androidClass;
  }

  public String getInstance() {
    return instance;
  }

  public LuaSelector getSelector() {
    String androidClass = getAndroidClass();
    String instance = getInstance();

    return new LuaSelector().className(androidClass).instance(Integer.parseInt(instance));
  }
}
