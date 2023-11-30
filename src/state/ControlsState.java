package state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import button.ImageButton;
import entity.Text;
import main.GamePanel;
import manager.ImageManager;
import manager.MouseManager;
import manager.StateManager;
import transition.*;

/**
 * ControlsState class shows the possible controls for the program
 * @author Vachia Thoj
 *
 */
public class ControlsState extends State 
{
	//ImageManager
	private ImageManager imageManager;
	
	//The next state to go to
	private StateType nextState;
	
	//Tutorial images
	private BufferedImage[] tutorialImages;
	
	//Texts
	private Text title;
	private Text[] tutorialTexts;
	
	//Buttons
	private ImageButton backButton;
	
	//Transition
	private FadeToBlack fadeToBlack;
	
	/**
	 * Constructor
	 */
	public ControlsState()
	{
		this.imageManager = ImageManager.instance();
		
		this.nextState = null;
		
		this.tutorialImages = imageManager.getTutorialImages();
		
		//Initialize and set various objects and variables
		createButtons();
		createTexts();
		createTransitions();
	}
	
////////////////////////////////////////////// CREATE METHODS //////////////////////////////////////////////
	private void createButtons()
	{
		BufferedImage[] buttonImages = imageManager.getButtonImages(); 
		backButton = new ImageButton(
				(GamePanel.WIDTH / 2) - (buttonImages[0].getWidth() / 2),
				GamePanel.HEIGHT - (buttonImages[0].getHeight() + 4),
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"BACK"
		);
	}
	
	private void createTexts()
	{
		title = new Text("CONTROLS");
		title.changeScale(2);
		title.setX((GamePanel.WIDTH / 2) - (title.getWidth() / 2));
		title.setY(8);
		tutorialTexts = new Text[4];
		
		tutorialTexts[0] = new Text("CLICK MOUSE TO PLACE A TILE");
		tutorialTexts[0].setX(72);
		tutorialTexts[0].setY(40);
		tutorialTexts[1] = new Text("HOLD SHIFT AND MOVE MOUSE TO");
		tutorialTexts[1].setX(328);
		tutorialTexts[1].setY(40);
		tutorialTexts[2] = new Text("QUICKLY PLACE TILES");
		tutorialTexts[2].setX(328);
		tutorialTexts[2].setY(50);
		
		tutorialTexts[3] = new Text("PRESS ARROW KEYS TO MOVE MAP");
		tutorialTexts[3].setX(72);
		tutorialTexts[3].setY(184);
	}
	
	private void createTransitions()
	{
		this.fadeToBlack = new FadeToBlack(GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
////////////////////////////////////////////// UPDATE METHODS //////////////////////////////////////////////
	
	/**
	 * Method that updates the Buttons
	 */
	private void updateButtons()
	{
		backButton.update();
		
		//Perform an action if a Button has been clicked on
		performButtonAction();
	}
	
	/**
	 * Method that performs an action if a Button has been clicked on
	 */
	private void performButtonAction()
	{
		if(backButton.isMouseClickingButton())
		{
			backButton.setMouseClickingButton(false);
			
			//Run the fadeToBlack transition
			fadeToBlack.setRunning(true);
			
			//Indicate that the next state to go to is the MainState
			nextState = StateType.MAIN;
		}
	}
	
	/**
	 * Method that updates the Transitions
	 */
	private void updateTransitions()
	{
		fadeToBlack.update();
	}
	
	/**
	 * Method that checks if a change of state is necessary
	 */
	private void changeState()
	{
		//If fadeToBlack transition is done and
		//a next state has been decided..
		if(fadeToBlack.isDone() && nextState != null)
		{
			MouseManager.instance().clearPressedPoint();
			MouseManager.instance().clearReleasedPoint();
			
			//Go to the next state
			StateManager.instance().nextState(nextState);
		}
	}
	
	/**
	 * Method that updates the ControlsState
	 */
	public void update()
	{
		updateTransitions();
		if(fadeToBlack.isRunning())
		{
			return;
		}
		
		changeState();
		updateButtons();
	}
////////////////////////////////////////////// DRAW METHODS //////////////////////////////////////////////
	
	/**
	 * Method that draws the background
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
		g.setColor(new Color(245, 245, 245));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}	
	
	/**
	 * Method that draws the Transitions
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTransitions(Graphics2D g)
	{
		fadeToBlack.draw(g);
	}
	
	/**
	 * Method that draws the tutorial images
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTutorials(Graphics2D g)
	{
		g.drawImage(tutorialImages[0], 64, 32, null);
		g.drawImage(tutorialImages[1], 320, 32, null);
		g.drawImage(tutorialImages[2], 64, 176, null);
	}
	
	/**
	 * Method that draws the title text
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTitleText(Graphics2D g)
	{
		title.draw(g);
	}
	
	/**
	 * Method that draws the tutorial texts
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTutorialTexts(Graphics2D g)
	{
		for(int i = 0; i < tutorialTexts.length; i++)
		{
			tutorialTexts[i].draw(g);
		}
	}
	
	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g)
	{
		backButton.draw(g);
	}
	
	/**
	 * Method that draws the ControlsState
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		drawBackground(g);
		drawTutorials(g);
		drawTitleText(g);
		drawTutorialTexts(g);
		drawButtons(g);
		drawTransitions(g);
	}
}
