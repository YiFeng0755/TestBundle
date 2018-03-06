package com.boyaa.application.testbundle.exceptions;

import com.boyaa.application.testbundle.LuaObject;

@SuppressWarnings("serial")
public class LuaObjectNotFoundException extends Exception {

  /**
   * An exception involving an {@link LuaObject}.
   *
   * @param msg
   *          A descriptive message describing the error.
   */
  public LuaObjectNotFoundException(final String msg) {
    super(msg);
  }
}
