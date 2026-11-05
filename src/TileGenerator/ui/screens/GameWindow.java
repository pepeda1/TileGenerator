package TileGenerator.ui.screens;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	
	private static GameScreen gameScreen = new GameScreen();
	
	public GameWindow() {
		
		//Frame
		getContentPane().setBackground(new Color(255, 255, 255));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setTitle("TileGenerator");
		setSize(1000, 600);
		setResizable(true);
		setLayout(new BorderLayout());
		setVisible(true);
		
		//Adding components
		add(gameScreen, BorderLayout.CENTER);
		gameScreen.requestFocusInWindow();
		validate();
		
	}
	
}