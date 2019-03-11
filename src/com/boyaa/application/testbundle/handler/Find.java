package com.boyaa.application.testbundle.handler;

 import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.*;
import com.boyaa.application.testbundle.exceptions.ElementNotFoundException;
import com.boyaa.application.testbundle.exceptions.InvalidSelectorException;
import com.boyaa.application.testbundle.exceptions.InvalidStrategyException;
import com.boyaa.application.testbundle.exceptions.LuaSelectorSyntaxException;
import com.boyaa.application.testbundle.selector.Strategy;
import com.boyaa.application.testbundle.utils.ClassInstancePair;
import com.boyaa.application.testbundle.utils.ElementHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.parsers.ParserConfigurationException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

//import static com.boyaa.application.testbundle.utils.API.API_18;

/**
 * This handler is used to find elements in the Android UI.
 * <p/>
 * Based on which {@link Strategy}, {@link LuaSelector}, and optionally the
 * contextId, the element Id or Ids are returned to the user.
 */
public class Find extends CommandHandler {
  // These variables are expected to persist across executions.
  LuaElementsHash elements = LuaElementsHash.getInstance();
  static JSONObject apkStrings = null;
  /**
   * java_package : type / name
   *
   * com.example.Test:id/enter
   *
   * ^[a-zA-Z_]      - Java package must start with letter or underscore
   * [a-zA-Z0-9\._]* - Java package may contain letters, numbers, periods and underscores
   * :               - : ends the package and starts the type
   * [^\/]+          - type is made up of at least one non-/ characters
   * \\/             - / ends the type and starts the name
   * [\S]+$          - the name contains at least one non-space character and then the line is ended
   */
  static final Pattern resourceIdRegex = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9\\._]*:[^\\/]+\\/[\\S]+$");

  /*
   * @param command The {@link AndroidCommand} used for this handler.
   * 
   * @return {@link AndroidCommandResult}
   * 
   * @throws JSONException
   * 
   * @see com.boyaa.application.testbundle.CommandHandler#execute(io.appium.android.
   * bootstrap.AndroidCommand)
   */
  @Override
  public LuaCommandResult execute(final LuaCommand command)
      throws JSONException {
    final Hashtable<String, Object> params = command.params();

    // only makes sense on a device
    final Strategy strategy;
    try {
      strategy = Strategy.fromString((String) params.get("strategy"));
    } catch (final InvalidStrategyException e) {
      return new LuaCommandResult(WDStatus.UNKNOWN_COMMAND, e.getMessage());
    }

    final String contextId = (String) params.get("context");
    final String text = (String) params.get("selector");
    final boolean multiple = (Boolean) params.get("multiple");

    Logger.debug("Finding " + text + " using " + strategy.toString()
        + " with the contextId: " + contextId + " multiple: " + multiple);

    try {
      Object result = null;
      List<LuaSelector> selectors = getSelectors(strategy, text, multiple);
      if (!multiple) {
        for (final LuaSelector sel : selectors) {
          try {
            //Logger.debug("Using: " + sel.toString());
            sel.getAttributes();
            Logger.debug("fetchElement sel:"+sel.getAttributes());
            result = fetchElement(sel, contextId);
            Logger.debug("fetchElement result:"+result);
          } catch (final ElementNotFoundException ignored) {
          }
          if (result != null) {
            break;
          }
        }
      } else {
        List<LuaElement> foundElements = new ArrayList<LuaElement>();
        for (final LuaSelector sel : selectors) {
          // With multiple selectors, we expect that some elements may not
          // exist.
          try {
            //Logger.debug("Using: " + sel.toString());
        	  
            List<LuaElement> elementsFromSelector = fetchElements(sel, contextId);
            foundElements.addAll(elementsFromSelector);
          } catch (final ElementNotFoundException ignored) {
          }
        }
        result = elementsToJSONArray(foundElements);
      }
      // If there are no results, then return an error.
      if (result == null) {
        return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT,
            "No element found");
      }

      return getSuccessResult(result);
    } catch (final InvalidStrategyException e) {
      return getErrorResult(e.getMessage());
    } catch (final LuaSelectorSyntaxException e) {
      return new LuaCommandResult(WDStatus.UNKNOWN_COMMAND, e.getMessage());
    } catch (final ElementNotFoundException e) {
      return new LuaCommandResult(WDStatus.NO_SUCH_ELEMENT, e.getMessage());
    } catch (ParserConfigurationException e) {
      return getErrorResult("Error parsing xml hierarchy dump: " + e.getMessage());
    } catch (InvalidSelectorException e) {
      return new LuaCommandResult(WDStatus.INVALID_SELECTOR, e.getMessage());
    }
  }

  /**
   * Get the element from the {@link LuaElementsHash} and return the element
   * id using JSON.
   *
   * @param sel
   *     A LuaSelector that targets the element to fetch.
   * @param contextId
   *     The Id of the element used for the context.
   * @return JSONObject
   * @throws JSONException
   * @throws ElementNotFoundException
   */
  private JSONObject fetchElement(final LuaSelector sel, final String contextId)
      throws JSONException, ElementNotFoundException {
    final JSONObject res = new JSONObject();
    final LuaElement el = elements.getElement(sel, contextId);
    return res.put("ELEMENT", el.getId());
  }

  /**
   * Get an array of AndroidElement objects from the {@link LuaElementsHash}
   *
   * @param sel
   *     A LuaSelector that targets the element to fetch.
   * @param contextId
   *     The Id of the element used for the context.
   * @return ArrayList<AndroidElement>
   * @throws LuaObjectNotFoundException
 * @throws ElementNotFoundException 
   */
  private ArrayList<LuaElement> fetchElements(final LuaSelector sel, final String contextId)
      throws ElementNotFoundException {

    return elements.getElements(sel, contextId);
  }

  /**
   * Get a JSONArray to represent a collection of AndroidElements
   *
   * @param els collection of AndroidElement objects
   * @return elements in the format which appium server returns
   * @throws JSONException
   */
  private JSONArray elementsToJSONArray(List<LuaElement> els) throws JSONException {
    final JSONArray resArray = new JSONArray();
    for (LuaElement el : els) {
      resArray.put(ElementHelpers.toJSON(el));
    }
    return resArray;
  }

  /**
   * Create and return a LuaSelector based on the strategy, text, and how many
   * you want returned.
   *
   * @param strategy
   *     The {@link Strategy} used to search for the element.
   * @param text
   *     Any text used in the search (i.e. match, regex, etc.)
   * @param many
   *     Boolean that is either only one element (false), or many (true)
   * @return LuaSelector
   * @throws InvalidStrategyException
   * @throws ElementNotFoundException
   */
  private List<LuaSelector> getSelectors(final Strategy strategy,
                                        final String text, final boolean many) throws InvalidStrategyException,
          ElementNotFoundException, LuaSelectorSyntaxException, ParserConfigurationException, InvalidSelectorException {
    final List<LuaSelector> selectors = new ArrayList<LuaSelector>();
    LuaSelector sel = new LuaSelector();

    switch (strategy) {
//      case XPATH:
//        for (LuaSelector selector : getXPathSelectors(text, many)) {
//          selectors.add(selector);
//        }
//        break;
    case XPATH:
    	sel = sel.xpath(text);
    	selectors.add(sel);
      case CLASS_NAME:
        sel = sel.className(text);
//        if (!many) {
//          sel = sel.instance(0);
//        }
        selectors.add(sel);
        break;
      case ID:
        sel = sel.drawingId(text);
//        if (!many) {
//          sel = sel.instance(0);
//        }
        selectors.add(sel);
        break;
      case NAME:
        sel = new LuaSelector().name(text);
//        if (!many) {
//          sel = sel.instance(0);
//        }
        selectors.add(sel);
        break;
      case SELECTED:
          sel = sel.selected(text);
//          if (!many) {
//            sel = sel.instance(0);
//          }
          selectors.add(sel);
          break;
      case TEXT:
          sel = new LuaSelector().text(text);
//          if (!many) {
//            sel = sel.instance(0);
//          }
          selectors.add(sel);
          break;
//      case ANDROID_UIAUTOMATOR:
//        List<LuaSelector> parsedSelectors;
//        try {
//          parsedSelectors = uiAutomatorParser.parse(text);
//        } catch (final LuaSelectorSyntaxException e) {
//          throw new LuaSelectorSyntaxException(
//              "Could not parse LuaSelector argument: " + e.getMessage());
//        }
//
//        for (LuaSelector selector : parsedSelectors) {
//          selectors.add(selector);
//        }
//
//        break;
      case LINK_TEXT:
		  sel = new LuaSelector().text(text);
//	      if (!many) {
//	        sel = sel.instance(0);
//	      }
	      selectors.add(sel);
	      break;
      case PARTIAL_LINK_TEXT:
      case CSS_SELECTOR:
      default:
        throw new InvalidStrategyException("Sorry, we don't support the '"
            + strategy.getStrategyName() + "' locator strategy yet");
    }

    return selectors;
  }

//  /** returns List of LuaSelectors for an xpath expression **/
//  private List<LuaSelector> getXPathSelectors(final String expression, final boolean multiple) throws ElementNotFoundException, ParserConfigurationException, InvalidSelectorException {
//    List<LuaSelector> selectors = new ArrayList<LuaSelector>();
//
//    ArrayList<ClassInstancePair> pairs = XMLHierarchy.getClassInstancePairs(expression);
//
//    if (!multiple) {
//      if (pairs.size() == 0) {
//        throw new NoSuchElementException();
//      }
//      selectors.add(pairs.get(0).getSelector());
//    } else {
//      for (ClassInstancePair pair : pairs) {
//        selectors.add(pair.getSelector());
//      }
//    }
//
//    return selectors;
//  }
}
