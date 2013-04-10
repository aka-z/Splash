package com.strigiformstudios.splash;

import com.strigiformstudios.framework.Screen;
import com.strigiformstudios.framework.impl.AndroidGame;

//extending AndroidGame makes this class an activity. It's the launcher activity in the manifest,
//so it's onCreate method is called automatically (this is an inherited method)
public class SplashGame extends AndroidGame{

	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}


}
