package TileGenerator.logic.controls;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import TileGenerator.main.Constants;

public class GameScreenKeyboardControls extends KeyAdapter {
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		Constants.KEYS_PRESSED.put(e.getKeyChar(), true);
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
		Constants.KEYS_PRESSED.put(e.getKeyChar(), false);
		
	}
	
	public static void useKeyboardControls() {

		if (Constants.KEYS_PRESSED.getOrDefault('w', false) || Constants.KEYS_PRESSED.getOrDefault('W', false)) { //Moving up
			
			Constants.SCREEN_Y += Constants.PLAYER_SPEED;																									
			
		}
		if (Constants.KEYS_PRESSED.getOrDefault('a', false) || Constants.KEYS_PRESSED.getOrDefault('A', false)) { //Moving left
					
			Constants.SCREEN_X -= Constants.PLAYER_SPEED;
					
		}
		if (Constants.KEYS_PRESSED.getOrDefault('s', false) || Constants.KEYS_PRESSED.getOrDefault('S', false)) { //Moving down
			
			Constants.SCREEN_Y -= Constants.PLAYER_SPEED;
			
		}
		if (Constants.KEYS_PRESSED.getOrDefault('d', false) || Constants.KEYS_PRESSED.getOrDefault('D', false)) { //Moving right
			
			Constants.SCREEN_X += Constants.PLAYER_SPEED;
			
		}

	}
	
}