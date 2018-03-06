package com.boyaa.application.testbundle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author JonLiang
 *
 */
public class LuaSelector {
	private Map<String, String> attributes = new HashMap<String, String>();
	
	public LuaSelector() {
	}
	public LuaSelector xpath(String text) {
		this.attributes.put("xpath", text);
		return this;
	}
	
	public LuaSelector className(String luaClass) {
		this.attributes.put("className", luaClass);
		return this;
	}

	public LuaSelector name(String xmlValue) {
		this.attributes.put("name", xmlValue);
		return this;
	}
	
	public LuaSelector text(String xmlValue) {
		this.attributes.put("text", xmlValue);
		return this;
	}	
	public LuaSelector selected(String xmlValue) {
		this.attributes.put("selected", xmlValue);
		return this;
	}
	
	public LuaSelector drawingId(String drawingId) {
		LuaSelector selector = new LuaSelector();
		selector.attributes.put("drawingId", drawingId);
		return selector;
	}
	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public LuaSelector instance(int counter) {
		// TODO Auto-generated method stub
		return null;
	}


	
	public LuaSelector parent(String parentDrawingId) {
		this.attributes.put("parent", parentDrawingId);
		return this;
	}

//	public LuaSelector instance(final int i) {
//		LuaSelector selector = new LuaSelector();
//		selector.attributes.put("instance", androidClass);
//		return selector;
//	}
//	
//	public LuaSelector resourceId(String resourceId) {
//		LuaSelector selector = new LuaSelector();
//		selector.attributes.put("resourceId", resourceId);
//		return selector;
//	}
//
//	public LuaSelector index(int counter) {
//		LuaSelector selector = new LuaSelector();
//		selector.attributes.put("index", androidClass);
//		return selector;
//	}
	


}
