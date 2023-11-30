package state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import button.ImageButton;
import entity.Text;
import main.GamePanel;
import manager.ImageManager;
import manager.MouseManager;
import manager.SaveManager;
import manager.StateManager;
import save.*;
import transition.*;

/**
 * Save State class represents where the user can select a save file
 * @author Vachia Thoj
 *
 */
public class SaveState extends State 
{
	//Managers
	private ImageManager imageManager;
	private SaveManager saveManager;

	//The next state to go to
	private StateType nextState;
	
	//Texts
	private Text title;
	private Text[] lastSavedTexts;

	//Buttons
	private ImageButton[] saveFileButtons;
	private ImageButton backButton;

	//Save files
	private SaveFile[] saveFiles;

	//Transition
	private FadeToBlack fadeToBlack;

	/**
	 * Constructor
	 */
	public SaveState() 
	{
		//Obtain managers
		this.imageManager = ImageManager.instance();
		this.saveManager = SaveManager.instance();

		//Obtain save files
		this.saveFiles = saveManager.getSaveFiles();

		this.nextState = null;
		
		//Initialize and set various objects and variables
		createButtons();
		createTexts();
		createTransitions();
	}

////////////////////////////////////////////// CREATE METHODS //////////////////////////////////////////////
	private void createTexts()
	{
		this.title = new Text("SAVE FILES");
		this.title.changeScale(2);
		this.title.setX((GamePanel.WIDTH / 2) - (title.getWidth() / 2));
		this.title.setY(4);
		
		lastSavedTexts = new Text[saveFiles.length];
		for(int i = 0; i < saveFiles.length; i++)
		{
			if(saveFiles[i].isEmpty() == false)
			{
				lastSavedTexts[i] = new Text("LAST SAVED " + saveFiles[i].getSaveDate().toString());
				lastSavedTexts[i].setX(saveFileButtons[i].getX() + 4);
				lastSavedTexts[i].setY(saveFileButtons[i].getY() + saveFileButtons[i].getHeight() - (lastSavedTexts[i].getHeight() + 2));
			}
			else
			{
				lastSavedTexts[i] = null;
			}
		}
	}

	private void createButtons() 
	{
		BufferedImage[] buttonImages = imageManager.getButtonImages();

		saveFileButtons = new ImageButton[4];
		for (int i = 0; i < saveFileButtons.length; i++) 
		{
			String text;
			if (saveFiles[i].isEmpty()) 
			{
				text = "NO DATA";
			} 
			else 
			{
				text = "SAVE FILE " + (i + 1);
			}

			if (i == 0) 
			{
				saveFileButtons[i] = new ImageButton(
						(GamePanel.WIDTH / 2) - (buttonImages[11].getWidth() / 2),
						32,
						buttonImages[11].getWidth(), 
						buttonImages[11].getHeight(), 
						buttonImages[11], 
						buttonImages[12],
						text
				);
			} 
			else
			{
				saveFileButtons[i] = new ImageButton(
						(GamePanel.WIDTH / 2) - (buttonImages[11].getWidth() / 2),
						saveFileButtons[i - 1].getY() + saveFileButtons[i - 1].getHeight() + 8,
						buttonImages[11].getWidth(), 
						buttonImages[11].getHeight(), 
						buttonImages[11], 
						buttonImages[12],
						text
				);
			}
		}

		backButton = new ImageButton(
				GamePanel.WIDTH / 2 - buttonImages[0].getWidth() / 2,
				GamePanel.HEIGHT - (buttonImages[0].getHeight() * 2),
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"BACK"
		);
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
		for (int i = 0; i < saveFileButtons.length; i++) 
		{
			saveFileButtons[i].update();
		}

		backButton.update();

		//Perform an action if a Button has been clicked on
		performButtonAction();
	}

	/**
	 * Method that performs an action if a Button has been clicked on
	 */
	private void performButtonAction()
	{
		for (int i = 0; i < saveFileButtons.length; i++)
		{
			if (saveFileButtons[i].isMouseClickingButton())
			{
				saveFileButtons[i].setMouseClickingButton(false);

				if (saveFiles[i].isEmpty() == false)
				{
					//Load a save file
					saveManager.load(i);
					
					//Run the fadeToBlack Transition
					fadeToBlack.setRunning(true);
					
					//Indicate that the next state to go to is the PlayState
					nextState = StateType.PLAY;
				}

				return;
			}
		}

		if (backButton.isMouseClickingButton())
		{
			backButton.setMouseClickingButton(false);
			
			//Run the fadeToBlack Transition
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
		if (fadeToBlack.isDone() && nextState != null) 
		{
			MouseManager.instance().clearPressedPoint();
			MouseManager.instance().clearReleasedPoint();
			StateManager.instance().nextState(nextState);
		}
	}

	/**
	 * Method that updates the SaveState
	 */
	public void update()
	{
		updateTransitions();

		if (fadeToBlack.isRunning())
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
	 * Method that draws the title text
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawTitleText(Graphics2D g)
	{
		title.draw(g);
	}
	
	/**
	 * Method that draws the last saved texts
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawLastSavedTexts(Graphics2D g)
	{
		for(int i = 0; i < lastSavedTexts.length; i++)
		{
			if(lastSavedTexts[i] != null)
			{
				lastSavedTexts[i].draw(g);
			}
		}
	}

	/**
	 * Method that draws the Buttons
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawButtons(Graphics2D g) 
	{
		for (int i = 0; i < saveFileButtons.length; i++)
		{
			saveFileButtons[i].draw(g);
		}

		backButton.draw(g);
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
	 * Method that draws the SaveState
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g) 
	{
		drawBackground(g);
		drawTitleText(g);
		drawButtons(g);
		drawLastSavedTexts(g);
		drawTransitions(g);
	}
}