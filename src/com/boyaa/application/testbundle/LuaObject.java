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
import android.R.string;
import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;

/**
 * @author JonLiang
 *
 */
public class LuaObject {
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
	private boolean is_selected;
	private boolean is_displayed;
	private InstrumentationHelper instHelper = new InstrumentationHelper();
	private String xpath;;
	private static ContentResolver resolver;
	private static Uri uri = Uri.parse("content://com.boyaa.test.providers.element/elements");
	private static AsyncQueryHandler asyncQuery;  
	private Context myContext = null;  
	LuaSelector selector;
	
//    private static void asyncQuery(String selector) {  
//        // TODO Auto-generated method stub  
////        Uri uri = Uri.parse("content://com.android.contacts/data/phones");    
////        String[] projection = { "_id", "display_name", "data1", "sort_key" };    
//        asyncQuery.startQuery(0, null, uri, null, selector, null,null);  
//    }  
//    
	private static ContentResolver getContentResolver() {
		if (resolver == null) {
			resolver = TestBundle.getTargetContext().getContentResolver();
		}
		return resolver;
	}
	public static ArrayList<LuaObject> getLuaObjects(LuaSelector selector) throws LuaObjectNotFoundException {
		
		ArrayList<LuaObject> luaObjectsArray = new ArrayList<LuaObject>();
		try {	//控件加载会有延时
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject selection = ConvertSelectorToContentProviderSelection(selector);
		Logger.verbose("selection after convertion:" + selection);
		Logger.verbose("url:" + uri);
		String luaString = "";
	    try {
//	    	asyncQuery = new MyAsyncQueryHandler(getContentResolver()); 
//	    	asyncQuery.startQuery(0, null, uri, null, selection.toString(), null,null);  
//	    	asyncQuery(selection.toString());
	    	
//	    	 Cursor cursor = null;
//	    	 cursor.close();
	    	 Cursor cursor = LuaObject.getContentResolver().query(uri, null, selection.toString(), null, null);
	    	 Logger.verbose("cursor:" + cursor.toString() +" "+cursor.getCount());
	    	 Thread.sleep(1000);
	    	 cursor.moveToFirst();
//	    	 cursor.moveToLast();
	    	 if (cursor != null) {
	    		 do {
	    			 int count = cursor.getColumnCount();
	    			 Logger.verbose("cursor1: " +  count);
	    			 String string = cursor.getColumnName(0);
	    			 Logger.verbose("cursor1: " +  cursor.getColumnName(0));
	    			 luaString = luaString + string;
	    			 Logger.verbose(luaString);
				
	    		 } while (cursor.moveToNext());
	    		 	cursor.close();
//	    		 	LuaObject.getContentResolver().delete(uri, null, null);
//	     
	    	 }else {
	 			Logger.verbose("failed to get lua object by the selector!cursor got from game is null");
				throw new LuaObjectNotFoundException("failed to get lua object by the selector!cursor got from game is null");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	    luaObjectsArray = luatoLuaObject(luaString);
		return luaObjectsArray;
	}
	
	public static ArrayList<LuaObject> luatoLuaObject(String string){
//		LuaObject luaObject = null;
		int x2 = 0;int y2 = 0;int height2 = 0;int width2 = 0;String text2 = null;String name2 = null;boolean is_diplayed = false;String type ;
		boolean is_selected = false;
		ArrayList<LuaObject> luaObjectsArray = new ArrayList<LuaObject>();
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
				if (jsonObject.has("text")) {
					text2 = jsonObject.getString("text");
//					Logger.verbose("name2:"+name2);
				}
				if (jsonObject.has("type")) {
					type = jsonObject.getString("type");
//					Logger.verbose("tag:"+tag);
				}
				if (jsonObject.has("visible")) {
					is_diplayed = jsonObject.getString("visible") != null;
//					Logger.verbose("is_diplayed"+is_diplayed);
				}
				if (jsonObject.has("selected")) {
//					Logger.verbose("11:"+jsonObject.toString());
//					Logger.verbose("11before:"+is_selected);
//					Logger.verbose("getstring:"+jsonObject.getString("selected"));
					is_selected = Boolean.parseBoolean(jsonObject.getString("selected"));
					Logger.verbose("is_selected11:"+is_selected);
//					Logger.verbose("is_diplayed"+is_diplayed);
				}
				if (jsonObject.has("type")) {
					type = jsonObject.getString("type");
//					Logger.verbose("tag:"+tag);
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
				luaObjectsArray.add(new LuaObject(x2, y2, height2, width2, text2, name2, is_diplayed,is_selected));
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
	
	public LuaObject(int x2, int y2, int height2, int width2,
			String text2, String name2, boolean is_diplayed, boolean is_selected) {
		this.x = x2;
		this.y = y2;
		this.height = height2;
		this.width = width2;
		this.text = text2;
		this.name = name2;
		this.is_displayed = is_diplayed;
		this.is_selected = is_selected;
	}
	
	public LuaObject(String drawingID, int x, int y, int height, int width, 
			boolean is_pickable, String text, String name, boolean is_displayed, boolean is_selected) {
		this.drawingID = drawingID;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.is_pickable = is_pickable;
		this.text = text;
		this.name = name;
		this.is_displayed = is_displayed;
		this.is_selected = is_selected;
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
	
//	public boolean swipe(final int destX, final int destY, final int destX1, final int destY1, final int steps) {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Logger.debug("luaobject swipe");
//		Float pointX = Float.valueOf(x);
//		float pointY = Float.valueOf(y);	
//		float pointHeight = Float.valueOf(height);
//		float pointWidth = Float.valueOf(width);
//		this.instHelper.generateSwipeGesture(destX, destY, destX1, destY1);
//		return true;
//	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public boolean isDisplayed() {
		return is_displayed;
	}
	public boolean isSelected() {
		Logger.verbose("is_selected:"+is_selected);
		return this.is_selected;
	}

	public boolean isPickable() {
		return is_pickable;
	}

	public String getText() {
		Logger.verbose(this.text);
		return this.text;
	}
//	private String getValue(String propName) {
//		assert(propName != drawingID);	//不能设置drawing ID
//
//		Map<String, String> elementAttrs = new HashMap<String, String>();
//		elementAttrs.put("name", name);
//		elementAttrs.put(propName, value);
//		String selection = new JsonUtil(elementAttrs).toString();
//		Logger.debug(selection);
//		getContentResolver().query(uri, null, selection, null, null);
//		return true;
//	}
//	
//	
//	public String getText(String encodedText){
//		String value = getValue(encodedText);
////		TestBundle.getTheInstrumentation().sendStringSync(text);
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return value;
//		
//	}

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
	public ArrayList<LuaObject> getChildren(LuaSelector sel) throws LuaObjectNotFoundException {
		LuaSelector newsel = sel.parent(this.drawingID);
		return getLuaObjects(newsel);
	}

    public static class MyAsyncQueryHandler extends AsyncQueryHandler {  
//      private String selection;
		public MyAsyncQueryHandler(ContentResolver cr) {  
          super(cr);  
          // TODO Auto-generated constructor stub  
      }
//  public void onAsyncQueryDataLister(String selector) {    
//      // TODO Auto-generated method stub    
//      Log.i(TESTBUNDLE_TAG, "Controller--onAsyncQueryDataLister");    
//      asyncQuery.startQuery(0, null, uri, null, selection, null,null);    
//  }   
  @Override  
  protected void onQueryComplete(int token, Object cookie, Cursor cursor) {  
 	 	Logger.verbose("cursor:" + cursor.toString() +" "+cursor.getCount());
 	    String luaString = "";
 	 	cursor.moveToFirst();
 		 do {
 			 String string = cursor.getColumnName(0);
 			 luaString = luaString + string;
 			 Logger.verbose(luaString);
 		 } while (cursor.moveToNext());
 		 	cursor.close();  
  	}  
  } 
}
