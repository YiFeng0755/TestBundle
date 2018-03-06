package com.boyaa.application.testbundle.utils;

import com.boyaa.application.testbundle.Logger;
import com.boyaa.application.testbundle.TestBundle;

import android.renderscript.Sampler.Value;
import android.util.Log;
import android.view.MotionEvent;


import android.annotation.SuppressLint;
//import com.android.uiautomator.common.ReflectionUtils;
import android.app.Instrumentation;
import android.graphics.PointF;
import android.os.SystemClock;

public class InstrumentationHelper {
	private Instrumentation inst;
	
	public InstrumentationHelper() {
		if(TestBundle.instru != null) {
			this.inst = TestBundle.instru;
		} else {
			throw new RuntimeException("Instrumentation instance is null! Please check if it is initialized correctly in TestBundle.java.");
		}
	}
	
	@SuppressLint("Recycle")
	public void clickOnScreen(float x, float y) {
		boolean successfull = false;
		int retry = 0;

		while(!successfull && retry < 10) {
			long downTime = SystemClock.uptimeMillis();
			long eventTime = SystemClock.uptimeMillis();
			MotionEvent event = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_DOWN, x, y, 0);
			MotionEvent event2 = MotionEvent.obtain(downTime, eventTime,
					MotionEvent.ACTION_UP, x, y, 0);
			Logger.verbose("x:"+String.valueOf(x)+" /y:"+String.valueOf(y));
			try{
				this.inst.sendPointerSync(event);
				this.inst.sendPointerSync(event2);
				successfull = true;
			}catch(SecurityException e){
				retry ++;
			}
		}
		Logger.info("retry:"+String.valueOf(retry));
		if(retry == 10) {
			throw new RuntimeException("Click operation Cannot be Completed!");
		}
	}
	
	@SuppressLint("NewApi")
	public Boolean generateSwipeGesture(float x1, float y1, float x2, float y2, int stes) {
//	public Boolean generateSwipeGesture(Point x1, Point y1, Point x2, Point y2) {
		try {
			Logger.debug("9999999");
		    long downTime = SystemClock.uptimeMillis();
		    long eventTime = SystemClock.uptimeMillis();
		    
////		    Double startX1 = x1.x;
//		    Double startY1 = x1.y;
//		    Double startX2 = y1.x;
//		    Double startY2 = y1.y;
//		    Double endX1 = x2.x;
//		    Double endY1 = x2.y;
//		    Double endX2 = y2.x;
//		    Double endY2 = y2.y;
		    
//		    Double x11 = startX1;
//		    Double y11 = startY1;
//		    Double x21 = startX2;
//		    Double y21 = startY2;
		    
		    MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[2];
		    MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
		    MotionEvent.PointerCoords pc2 = new MotionEvent.PointerCoords();
		    pc1.x = x1;
		    pc1.y = y1;
		    pc1.pressure = 1.0F;
		    pc1.size = 1.0F;
		    pc2.x = x2;;
		    pc2.y = y2;;
		    pc2.pressure = 1.0F;
		    pc2.size = 1.0F;
		    pointerCoords[0] = pc1;
		    pointerCoords[1] = pc2;
		    
		    MotionEvent.PointerProperties[] pointerProperties = new MotionEvent.PointerProperties[2];
		    MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
		    MotionEvent.PointerProperties pp2 = new MotionEvent.PointerProperties();
		    pp1.id = 0;
		    pp1.toolType = 1;
		    pp2.id = 1;
		    pp2.toolType = 1;
		    pointerProperties[0] = pp1;
		    pointerProperties[1] = pp2;
		    
		    MotionEvent event = MotionEvent.obtain(downTime, eventTime, 0, 1, pointerProperties, pointerCoords, 0, 0, 1.0F, 1.0F, 0, 0, 0, 0);
		    Logger.verbose("999990:"+event);
		    this.inst.sendPointerSync(event);
		    event = MotionEvent.obtain(downTime, eventTime, 5 + (pp2.id << 8), 2, pointerProperties, pointerCoords, 0, 0, 1.0F, 1.0F, 0, 0, 0, 0);
		    this.inst.sendPointerSync(event);
		    Logger.verbose("999991:"+event);
		    int numMoves = 100;
		      
		    float stepX1 = ((x2 - x1) / numMoves);
		    float stepY1 = ((y2 - y1) / numMoves);
//		    float stepX2 = ((endX2 - startX2) / numMoves);
//		    float stepY2 = ((endY2 - startY2) / numMoves);
		    for (int i = 0; i < numMoves; i++)
		    {
		      eventTime += 10L;
		      pointerCoords[0].x += stepX1;
		      pointerCoords[0].y += stepY1;
//		      pointerCoords[1].x += stepX2;
//		      pointerCoords[1].y += stepY2;
		      event = MotionEvent.obtain(downTime, eventTime, 2, 2, pointerProperties, pointerCoords, 0, 0, 1.0F, 1.0F, 0, 0, 0, 0);
		      this.inst.sendPointerSync(event);
		      Logger.verbose("999992:"+event);
		    }
			} catch (Exception e) {
				// TODO: handle exception
			}
		      return true;
		}	  
}
