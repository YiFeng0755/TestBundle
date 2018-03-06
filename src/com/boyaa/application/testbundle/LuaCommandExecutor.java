package com.boyaa.application.testbundle;

import com.boyaa.application.testbundle.handler.*;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Command execution dispatch class. This class relays commands to the various
 * handlers.
 *
 */
/**
 * @author JonLiang
 *
 */
class LuaCommandExecutor {

  private static HashMap<String, CommandHandler> map = new HashMap<String, CommandHandler>();

  static {
    map.put("click", new Click());
    map.put("getText", new GetText());
    map.put("getAttribute", new GetAttribute());
    map.put("find", new Find());
    map.put("pressBack", new PressBack());
    map.put("pressKeyCode", new PressKeyCode());
    map.put("setText", new SetText());
    map.put("getLocation", new GetLocation());
    map.put("getSize", new GetSize());
    map.put("swipe", new Swipe());
  }

  /**
   * Gets the handler out of the map, and executes the command.
   *
   * @param command
   *          The {@link LuaCommand}
   * @return {@link LuaCommandResult}
   */
  public LuaCommandResult execute(final LuaCommand command) {
    try {
      Logger.debug("Got command action: " + command.action());
      if (map.containsKey(command.action())) {
    	 Logger.debug("111:"+command);
        return map.get(command.action()).execute(command);
      } else {
    	  Logger.debug("112");
        return new LuaCommandResult(WDStatus.UNKNOWN_COMMAND,
            "Unknown command: " + command.action());
      }
    } catch (final JSONException e) {
      Logger.debug("113");
      Logger.error("Could not decode action/params of command");
      return new LuaCommandResult(WDStatus.JSON_DECODER_ERROR,
          "Could not decode action/params of command, please check format!");
    }
  }
}
