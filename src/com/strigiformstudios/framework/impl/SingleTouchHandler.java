package com.strigiformstudios.framework.impl;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import android.view.View;

import com.strigiformstudios.framework.Input.TouchEvent;
import com.strigiformstudios.framework.Pool;
import com.strigiformstudios.framework.Pool.PoolObjectFactory;

public class SingleTouchHandler implements TouchHandler {
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;

	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);

		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			TouchEvent touchEvent = touchEventPool.newObject();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}

			touchEvent.x = touchX = (int) (event.getX() * scaleX);
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);

			return true;
		}
	}

	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if (pointer == 0)
				return isTouched;
			else
				return false;
		}
	}

	// The book does not include the if/else check for a single pointer (see
	// page 203).
	// He mentions it in the text and I think it's important to add here.
	public int getTouchX(int pointer) {
		synchronized (this) {
			if (pointer == 0)
				return touchX;
			else
				return 0;
		}
	}

	// The book does not include the if/else check for a single pointer (see
	// page 203).
	// He mentions it in the text and I think it's important to add here.
	public int getTouchY(int pointer) {
		synchronized (this) {
			if (pointer == 0)
				return touchY;
			else
				return 0;
		}
	}

	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}

}
