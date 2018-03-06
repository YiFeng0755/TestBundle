package com.boyaa.application.testbundle;

import com.boyaa.application.testbundle.exceptions.LuaObjectNotFoundException;
import com.boyaa.application.testbundle.utils.InstrumentationHelper;
import com.boyaa.application.testbundle.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

/**
 * @author JonLiang
 *
 */
public class LuaObject1 {
	public static final String TESTBUNDLE_TAG = "TESTBUNDLE_TAG";
	private String exist;
	private String drawingID;
	private int x;
	private int y;
	private int height;
	private int width;
	private boolean is_pickable;
	private String text;
	private String name;
	private boolean is_displayed;
	private InstrumentationHelper instHelper = new InstrumentationHelper();
	private String xpath;;
	private static ContentResolver resolver;
	private static Uri uri = Uri.parse("content://com.boyaa.test.providers.element/elements");
	private AsyncQueryHandler asyncQuery = null;  
	private Context myContext = null;  
	
    public LuaObject1(Context myContext){  
        this.myContext = myContext;   
        this.asyncQuery = new MyAsyncQueryHandler(myContext.getContentResolver());   
    }  
    private class MyAsyncQueryHandler extends AsyncQueryHandler {  
        public MyAsyncQueryHandler(ContentResolver cr) {  
            super(cr);  
            // TODO Auto-generated constructor stub  
        }  
    }
    
	private static ContentResolver getContentResolver() {
		if (resolver == null) {
			resolver = TestBundle.getTargetContext().getContentResolver();
		}
		return resolver;
	}
	public static ArrayList<LuaObject1> getLuaObjects(LuaSelector selector) throws LuaObjectNotFoundException {
//		String exist;
//		String drawingID;
//		int x;
//		int y;
//		int height;
//		int width;
//		boolean is_pickable;
//		String text;
//		String name;
//		boolean is_diplayed;
		
		ArrayList<LuaObject1> luaObjectsArray = new ArrayList<LuaObject1>();
		try {	//控件加载会有延时
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject selection = ConvertSelectorToContentProviderSelection(selector);
		Logger.verbose("selection after convertion:" + selection);
//		Logger.verbose("url:" + uri);
		String luaString = "";
	    try {
//	    	 Cursor cursor = null;
//	    	 cursor.close();
	    	 Cursor cursor = LuaObject1.getContentResolver().query(uri, null, selection.toString(), null, null);
	    	 Logger.verbose("cursor:" + cursor.toString() +" "+cursor.getCount());
	    	 Thread.sleep(1000);
	    	 cursor.moveToFirst();
//	    	 if (cursor != null) {
	    		 do {
//	    			 int count = cursor.getColumnCount();
//	    			 Logger.verbose("cursor1: " +  cursor.getColumnName(0));
	    			 String string = cursor.getColumnName(0);
	    			 luaString = luaString + string;
	    			 Logger.verbose(luaString);
				
	    		 } while (cursor.moveToNext());
	    		 	cursor.close();
//	    		 	LuaObject.getContentResolver().delete(uri, null, null);
//	        
//	    	 }else {
//	 			Logger.verbose("failed to get lua object by the selector!cursor got from game is null");
//				throw new LuaObjectNotFoundException("failed to get lua object by the selector!cursor got from game is null");
//			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	    luaObjectsArray = luatoLuaObject(luaString);
		return luaObjectsArray;
	}
	
	public static ArrayList<LuaObject1> luatoLuaObject(String string){
//		LuaObject luaObject = null;
		int x2 = 0;int y2 = 0;int height2 = 0;int width2 = 0;String text2 = null;String name2 = null;boolean is_diplayed = false;String tag ;
		ArrayList<LuaObject1> luaObjectsArray = new ArrayList<LuaObject1>();
		try {
//			Logger.verbose("start to luaobject:"+string);
			JSONArray jsonArray = new JSONArray(string);
			
			for (int i = 0; i < jsonArray.length(); i++) {
	            JSONObject jsonObject = jsonArray.getJSONObject(i);
	             // 初始化map数组对象
	             HashMap<String, Object> map = new HashMap<String, Object>();
	             			
				Logger.verbose("lua to luaobject2:"+jsonObject.toString());
				if (jsonObject.has("name")) {
					name2 = jsonObject.getString("name");
//					Logger.verbose("name2:"+name2);
				}
				if (jsonObject.has("tag")) {
					tag = jsonObject.getString("tag");
//					Logger.verbose("tag:"+tag);
				}
				if (jsonObject.has("visible")) {
					is_diplayed = jsonObject.getString("visible") != null;
//					Logger.verbose("is_diplayed"+is_diplayed);
				}
				if (jsonObject.has("position")) {
					String position = jsonObject.getString("position");
//					Logger.verbose("position:"+position);
					JSONObject jsonObject1 = new JSONObject(position);
//					Logger.verbose("1111");
					if (jsonObject1.has("x")) {
//						Logger.verbose("2222");
			        	x2 = (int) jsonObject1.getLong("x");
//			        	Logger.verbose("x2:"+x2);
			        }
			        if (jsonObject1.has("y")) {
			        	y2 = (int) jsonObject1.getLong("y");
//			        	Logger.verbose("y2"+y2);
			        }
				}
				if (jsonObject.has("size")) {
					String size = jsonObject.getString("size");
//					Logger.verbose("size"+size);
					JSONObject jsonObject1 = new JSONObject(size);
					if (jsonObject1.has("height")) {
						height2 = (int) jsonObject1.getLong("height");
//			        	Logger.verbose("height2:"+height2);
			        }
			        if (jsonObject1.has("width")) {
			        	width2 = (int) jsonObject1.getLong("width");
//			        	Logger.verbose("width2"+width2);
			        }
				}
			
				if (jsonObject.has("height")) {
					height2 = Integer.parseInt(jsonObject.getString("height"));
//					Logger.verbose("height"+height2);
				}
				if (jsonObject.has("width")) {
					width2 = Integer.parseInt(jsonObject.getString("width"));
//					Logger.verbose("width"+width2);
				}


				
//				luaObject = new LuaObject(x2, y2, height2, width2, xpath, text2, name2, is_diplayed);
				luaObjectsArray.add(new LuaObject1(x2, y2, height2, width2, text2, name2, is_diplayed));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return luaObjectsArray;
		
	}
	
//	public String getjsonString(String string){
//		if (string !=null) {
//			
//		}
//		
//	}
	
	private static JSONObject ConvertSelectorToContentProviderSelection(LuaSelector selector) {
		Map<String, String> elementAttrs = selector.getAttributes();
		String jsonObject = new JsonUtil(elementAttrs).toString();
		JSONObject dataJson = null;
		try {
			dataJson = new JSONObject(jsonObject);
			if (dataJson.has("xpath")) {
				dataJson.remove("className");
				Logger.debug("dataJson:"+dataJson.toString());
				
			}else {
				dataJson.put("operation", 2);
				Logger.debug("dataJson+operation:"+dataJson.toString());
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		Logger.verbose(jsonObject.toString());
		return dataJson;
	}
	
	public LuaObject1(int x2, int y2, int height2, int width2,
			String text2, String name2, boolean is_diplayed) {
		this.x = x2;
		this.y = y2;
		this.height = height2;
		this.width = width2;
		this.text = text2;
		this.name = name2;
		this.is_displayed = is_diplayed;
	}
	
	public LuaObject1(String drawingID, int x, int y, int height, int width, 
			boolean is_pickable, String text, String name, boolean is_displayed) {
		this.drawingID = drawingID;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.is_pickable = is_pickable;
		this.text = text;
		this.name = name;
		this.is_displayed = is_displayed;
	}
		
	public boolean click() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float pointX = Float.valueOf(x);
		float pointY = Float.valueOf(y);	
		float pointHeight = Float.valueOf(height);
		float pointWidth = Float.valueOf(width);
		this.instHelper.clickOnScreen(pointX+pointWidth/2, pointY+pointHeight/2);
		Logger.verbose("pointX:"+(pointX+pointWidth/2)+"pointY:"+(pointY+pointHeight/2));
		return true;
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean isDisplayed() {
		return is_displayed;
	}

	public boolean isPickable() {
		return is_pickable;
	}

	public String getText() {
		return this.text;
	}

	public boolean longClick() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean exists() {
		return true;
	}
	
	@SuppressLint("Assert")
	private boolean setValue(String propName, String value) {
		assert(propName != drawingID);	//不能设置drawing ID

		Map<String, String> elementAttrs = new HashMap<String, String>();
		elementAttrs.put("name", name);
		elementAttrs.put(propName, value);
		String selection = new JsonUtil(elementAttrs).toString();
		Logger.debug(selection);
		getContentResolver().update(uri, null, selection, null);
		return true;
	}
	
//	private boolean setValue(String value) {
//	        Logger.debug("sendText (" + text + ")");
//	        KeyEvent[] events = mKeyCharacterMap.getEvents(text.toCharArray());
//
//
//	        if (events != null) {
//	            long keyDelay = Configurator.getInstance().getKeyInjectionDelay();
//	            for (KeyEvent event2 : events) {
//	                // We have to change the time of an event before injecting it because
//	                // all KeyEvents returned by KeyCharacterMap.getEvents() have the same
//	                // time stamp and the system rejects too old events. Hence, it is
//	                // possible for an event to become stale before it is injected if it
//	                // takes too long to inject the preceding ones.
//	                KeyEvent event = KeyEvent.changeTimeRepeat(event2,
//	                        SystemClock.uptimeMillis(), 0);
//	                if (!injectEventSync(event)) {
//	                    return false;
//	                }
//	                SystemClock.sleep(keyDelay);
//	            }
//	        }
//	        return true;
//	}
	
	
	public boolean setText(String encodedText) {
		setValue("text", encodedText);
//		TestBundle.getTheInstrumentation().sendStringSync(text);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public Rect getBounds() {
		return new Rect(x, y, x+width, y+height);
	}
	public String getName() {
		return name;
	}
	public ArrayList<LuaObject1> getChildren(LuaSelector sel) throws LuaObjectNotFoundException {
		LuaSelector newsel = sel.parent(this.drawingID);
		return getLuaObjects(newsel);
	}

}
