package main;

import javax.swing.JFrame;

/**
 * Main is the class that you run/execute in order to start the program
 *  
 * @author Vachia Thoj
 */
public class Main 
{
	public static void main(String[] args)
	{
		//Create Window
		JFrame window = new JFrame("Tile Map Maker");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Create GamePanel
		GamePanel gamePanel = new GamePanel();
		
		//Store gamePanel to content pane
		window.setContentPane(gamePanel);
		
		window.pack();
		
		//Do not window to be resizeable
		window.setResizable(false);
		
		//Center window to computer screen
		window.setLocationRelativeTo(null);
		
		//Make window visible
		window.setVisible(true);
	}
}