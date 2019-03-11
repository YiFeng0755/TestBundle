package com.boyaa.application.testbundle.exceptions;

//import com.boyaa.application.testbundle.utils.LuaSelectorParser;

@SuppressWarnings("serial")
public class LuaSelectorSyntaxException extends Exception {

  /**
   * An exception involving an {@link LuaSelectorParser}.
   *
   * @param msg
   *          A descriptive message describing the error.
   */
  public LuaSelectorSyntaxException(final String msg) {
    super(msg);
  }
}
