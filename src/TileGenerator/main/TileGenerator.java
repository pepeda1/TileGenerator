package TileGenerator.main;

import TileGenerator.ui.screens.GameWindow;

public class TileGenerator {

	public static void main(String[] args) {
		
		//Create screen
		System.setProperty("sun.java2d.uiScale", "1");
		Constants.GAME_WINDOW = new GameWindow();
		
	}

}