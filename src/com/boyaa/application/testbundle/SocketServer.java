package com.boyaa.application.testbundle;

import com.boyaa.application.testbundle.exceptions.CommandTypeException;
import com.boyaa.application.testbundle.exceptions.SocketServerException;
import com.boyaa.application.testbundle.handler.UpdateStrings;
import org.json.JSONException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The SocketServer class listens on a specific port for commands from Appium,
 * and then passes them on to the {@link LuaCommandExecutor} class. It will
 * continue to listen until the command is sent to exit.
 */
class SocketServer {

  ServerSocket   server;
  Socket         client;
  BufferedReader in;
  BufferedWriter out;
  boolean        keepListening;
  private final LuaCommandExecutor executor;

  /**
   * Constructor
   *
   * @param port
   * @throws SocketServerException
   */
  public SocketServer(final int port) throws SocketServerException {
    keepListening = true;
    executor = new LuaCommandExecutor();
    try {
      server = new ServerSocket(port);
      Logger.debug("Socket opened on port " + port);
    } catch (final IOException e) {
      throw new SocketServerException(
          "Could not start socket server listening on " + port);
    }

  }

  /**
   * Constructs an @{link AndroidCommand} and returns it.
   *
   * @param data
   * @return @{link AndroidCommand}
   * @throws JSONException
   * @throws CommandTypeException
   */
  private LuaCommand getCommand(final String data) throws JSONException,
      CommandTypeException {
    return new LuaCommand(data);
  }

  private StringBuilder input = new StringBuilder();

  /**
   * When data is available on the socket, this method is called to run the
   * command or throw an error if it can't.
   *
   * @throws SocketServerException
   */
  private void handleClientData() throws SocketServerException {
    try {
      input.setLength(0); // clear
      String res;
      int a;
      // (char) -1 is not equal to -1.
      // ready is checked to ensure the read call doesn't block.
      while ((a = in.read()) != -1 && in.ready()) {
        input.append((char) a);
      }
      String inputString = input.toString();
      Logger.debug("Got data from client: " + inputString);
      try {
        LuaCommand cmd = getCommand(inputString);
        Logger.debug("Got command of type " + cmd.commandType().toString());
        res = runCommand(cmd);
        Logger.debug("Returning result: " + res);
      } catch (final CommandTypeException e) {
        res = new LuaCommandResult(WDStatus.UNKNOWN_ERROR, e.getMessage())
            .toString();
      } catch (final JSONException e) {
        res = new LuaCommandResult(WDStatus.UNKNOWN_ERROR,
            "Error running and parsing command").toString();
      }
      out.write(res);
      out.flush();
    } catch (final IOException e) {
      throw new SocketServerException("Error processing data to/from socket ("
          + e.toString() + ")");
    } catch (final Exception e) {
    	Logger.error("jondebug:" + e.getMessage());
    }
  }
  /**
   * Listens on the socket for data, and calls {@link #handleClientData()} when
   * it's available.
   *
   * @throws SocketServerException
   */
  /**
 * @throws SocketServerException
 */
public void listenForever() throws SocketServerException {
    Logger.debug("Appium Socket Server Ready");
    UpdateStrings.loadStringsJson();

    try {
        client = server.accept();
        Logger.debug("Client connected");
        in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
        out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), "UTF-8"));
        while (keepListening) {
          handleClientData();
        }
        in.close();
        out.close();
        client.close();
        Logger.debug("Closed client connection");
      } catch (final IOException e) {
        throw new SocketServerException("Error when client was trying to connect");
      }
  }

  /**
   * When {@link #handleClientData()} has valid data, this method delegates the
   * command.
   *
   * @param cmd
   *     AndroidCommand
   * @return Result
   */
  private String runCommand(final LuaCommand cmd) {
    LuaCommandResult res;
    if (cmd.commandType() == LuaCommandType.SHUTDOWN) {
      keepListening = false;
      res = new LuaCommandResult(WDStatus.SUCCESS, "OK, shutting down");
    } else if (cmd.commandType() == LuaCommandType.ACTION) {
      try {
    	Logger.debug("cmd:"+cmd);
        res = executor.execute(cmd);
      } catch (final Exception e) { // Here we catch all possible exceptions and return a JSON Wire Protocol UnknownError
                                    // This prevents exceptions from halting the bootstrap app
        Logger.debug("jondebug1:"+e.toString()+" message:"+e.getMessage());
    	res = new LuaCommandResult(WDStatus.UNKNOWN_ERROR, e.getMessage());
      }
    } else {
      // this code should never be executed, here for future-proofing
      res = new LuaCommandResult(WDStatus.UNKNOWN_ERROR,
          "Unknown command type, could not execute!");
    }
    return res.toString();
  }
}
