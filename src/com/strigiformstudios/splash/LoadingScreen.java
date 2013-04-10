package com.strigiformstudios.splash;

import com.strigiformstudios.splash.MainMenuScreen;
import com.strigiformstudios.framework.Graphics;
import com.strigiformstudios.framework.Graphics.PixmapFormat;
import com.strigiformstudios.splash.Assets;
import com.strigiformstudios.framework.Game;
import com.strigiformstudios.framework.Screen;

public class LoadingScreen extends Screen {
	
	public LoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		
		Assets.menuBackground = g.newPixmap("menubackground.png", PixmapFormat.RGB565);
		Assets.mainMenu = g.newPixmap("mainmenu.png", PixmapFormat.ARGB4444);
		Assets.title = g.newPixmap("title.png", PixmapFormat.ARGB4444);
		Assets.level1Background = g.newPixmap("level1Background.png", PixmapFormat.ARGB4444);
		
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void present(float deltaTime) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
