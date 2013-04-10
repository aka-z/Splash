package com.strigiformstudios.splash;

import java.util.List;

import com.strigiformstudios.splash.Assets;
import com.strigiformstudios.splash.LevelSelectScreen;
import com.strigiformstudios.framework.Graphics;
import com.strigiformstudios.framework.Input.TouchEvent;
import com.strigiformstudios.framework.Game;
import com.strigiformstudios.framework.Screen;

public class MainMenuScreen extends Screen {

	public MainMenuScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents(); // this just empties the KeyEvent
										// buffer. I never use KeyEvents in this
										// game

		int len = touchEvents.size();

		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, x, x, x, x)) {
					game.setScreen(new LevelSelectScreen(game));
					return;
				}
			}
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void present(float deltaTime) {

	}

	@Override
	public void pause() {
		// save here, in case the OS kills the app while it is paused.
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
