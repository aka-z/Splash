package com.strigiformstudios.framework.impl;

import com.strigiformstudios.framework.Audio;
import com.strigiformstudios.framework.FileIO;
import com.strigiformstudios.framework.Game;
import com.strigiformstudios.framework.Graphics;
import com.strigiformstudios.framework.Input;
import com.strigiformstudios.framework.Screen;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

//Online forum suggested that this class needs to be abstract to work since the "getStartScreen" method is not implemented.
//I don't understand why this works.
public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;

	@SuppressWarnings("deprecation") //getWidth() and getHeight() deprecation handled appropriately (I hope)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		int frameBufferWidth = isPortrait ? 640 : 480;
		int frameBufferHeight = isPortrait ? 480 : 640;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);
		
		float scaleX;
		float scaleY;
		
		//added this check because of deprecated methods
		if(Build.VERSION.SDK_INT < 13){
			scaleX = (float) frameBufferWidth
					/ getWindowManager().getDefaultDisplay().getWidth();
			scaleY = (float) frameBufferHeight
					/ getWindowManager().getDefaultDisplay().getHeight();
		}
		else{
			Point screenSize = new Point();
			scaleX = (float) frameBufferWidth
					/ screenSize.x;
			scaleY = (float) frameBufferHeight
					/ screenSize.y;
		}

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();

		if (isFinishing())
			screen.dispose();
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}

}
