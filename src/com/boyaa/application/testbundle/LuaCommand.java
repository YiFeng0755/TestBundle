package com.boyaa.application.testbundle;

import com.boyaa.application.testbundle.exceptions.CommandTypeException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * This proxy embodies the command that the handlers execute.
 * 
 */
/**
 * @author JonLiang
 *
 */
public class LuaCommand {

  JSONObject         json;
  LuaCommandType cmdType;

  public LuaCommand(final String jsonStr) throws JSONException,
      CommandTypeException {
    json = new JSONObject(jsonStr);
    setType(json.getString("cmd"));
  }

  /**
   * Return the action string for this command.
   * 
   * @return String
   * @throws JSONException
   */
  public String action() throws JSONException {
    if (isElementCommand()) {
    	Logger.debug("testbundle:"+json.getString("action").substring(8));
      return json.getString("action").substring(8);
    }
    Logger.debug("testbundle:"+json.getString("action"));
    return json.getString("action");
  }

  public LuaCommandType commandType() {
    return cmdType;
  }

  /**
   * Get the {@link LuaElement destEl} this command is to operate on (must
   * provide the "desElId" parameter).
   * 
   * @return {@link LuaElement}
   * @throws JSONException
   */
  public LuaElement getDestElement() throws JSONException {
    String destElId = (String) params().get("destElId");
    return LuaElementsHash.getInstance().getElement(destElId);
  }

  /**
   * Get the {@link LuaElement element} this command is to operate on (must
   * provide the "elementId" parameter).
   * 
   * @return {@link LuaElement}
   * @throws JSONException
   */
  public LuaElement getElement() throws JSONException {
    String elId = (String) params().get("elementId");
    return LuaElementsHash.getInstance().getElement(elId);
  }

  /**
   * Returns whether or not this command is on an element (true) or device
   * (false).
   * 
   * @return boolean
   */
  public boolean isElementCommand() {
    if (cmdType == LuaCommandType.ACTION) {
      try {
    	 Logger.debug("testbundle11:"+json.getString("action").startsWith("element:"));
        return json.getString("action").startsWith("element:");
        
      } catch (final JSONException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Return a hash table of name, value pairs as arguments to the handlers
   * executing this command.
   * 
   * @return Hashtable<String, Object>
   * @throws JSONException
   */
  public Hashtable<String, Object> params() throws JSONException {
    final JSONObject paramsObj = json.getJSONObject("params");
    final Hashtable<String, Object> newParams = new Hashtable<String, Object>();
    final Iterator<?> keys = paramsObj.keys();

    while (keys.hasNext()) {
      final String param = (String) keys.next();
      newParams.put(param, paramsObj.get(param));
    }
    return newParams;
  }

  /**
   * Set the command {@link LuaCommandType type}
   * 
   * @param stringType
   *          The string of the type (i.e. "shutdown" or "action")
   * @throws CommandTypeException
   */
  public void setType(final String stringType) throws CommandTypeException {
    if (stringType.equals("shutdown")) {
      cmdType = LuaCommandType.SHUTDOWN;
    } else if (stringType.equals("action")) {
      cmdType = LuaCommandType.ACTION;
    } else {
      throw new CommandTypeException("Got bad command type: " + stringType);
    }
  }
}