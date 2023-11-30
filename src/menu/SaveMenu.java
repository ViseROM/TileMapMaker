package menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import button.ImageButton;
import date.*;
import entity.Text;
import main.GamePanel;
import manager.ImageManager;
import manager.SaveManager;
import map.*;
import save.*;

/**
 * SaveMenu class represents a menu where the user can
 * choose where to save their file 
 * @author Vachia Thoj
 *
 */
public class SaveMenu extends Menu
{	
	private static final long serialVersionUID = 1L;
	
	//Maps
	private transient TileMap tileMap;
	private transient ObjectMap objectMap;
	private transient TileMap itemMap;
	private transient TileMap hitboxMap;
	
	//Colors for the SaveMenu
	private transient Color menuColor;
	private transient Color borderColor;
	
	//Flag to see if SaveMenu has a border
	private transient boolean bordered;
	
	//Texts
	private transient Text message;
	private transient Text savedMessage;
	private transient Text current;
	private transient Text[] lastSavedTexts;
	
	//Buttons
	private transient ImageButton[] saveFileButtons;
	private transient ImageButton backButton;
	
	//Save Files
	private transient SaveFile[] saveFiles;
	
	//Flag to see if a saved occur
	private transient boolean saved;
	
	//Timer for savedMessage Text
	private transient long savedMessageTimer;
	private transient long savedMessageDelay;
	
	//Default values for Color
	private static final Color DEFAULT_MENU_COLOR = Color.WHITE;
	private static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
	
	/**
	 * Constructor
	 * @param x (int) The x-coordinate of SaveMenu
	 * @param y (int) The y-coordinate of SaveMenu
	 * @param width (int) The width of the SaveMenu
	 * @param height (int) The height of the SaveMenu
	 * @param tileMap (TileMap) The tileMap to be saved
	 * @param itemMap (TileMap) The itemMap to be saved
	 * @param hitboxMap (TileMap) The hitboxMap to be saved
	 */
	public SaveMenu(int x, int y, int width, int height, TileMap tileMap, ObjectMap objectMap, TileMap itemMap, TileMap hitboxMap)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.tileMap = tileMap;
		this.objectMap = objectMap;
		this.itemMap = itemMap;
		this.hitboxMap = hitboxMap;
		
		this.visible = false;
		
		this.menuColor = DEFAULT_MENU_COLOR;
		this.borderColor = DEFAULT_BORDER_COLOR;
		this.bordered = true;
		
		this.saveFiles = SaveManager.instance().getSaveFiles();
		this.saved = false;
		
		this.savedMessageTimer = 0;
		this.savedMessageDelay = 1000;
		
		createSaveFileButtons();
		createButtons();
		createTexts();
		createLastSavedTexts();
	}
	
//////////////////////////////////////////////CREATE METHODS //////////////////////////////////////////////
	
	private void createTexts()
	{
		this.message = new Text("PICK A SLOT TO SAVE FILE");
		this.message.changeScale(2);
		this.message.setX(((x + x + width) / 2) - (message.getWidth() / 2));
		this.message.setY(y + 8);
		
		this.savedMessage = new Text("SAVED");
		this.savedMessage.changeColor(Color.RED);
		this.savedMessage.changeScale(2);
		this.savedMessage.setX(((x + x + width) / 2) - (savedMessage.getWidth() / 2));
		this.savedMessage.setY(y + 8);
		
		if(SaveManager.instance().getCurrentSaveFile() != null)
		{
			int id = SaveManager.instance().getCurrentSaveFile().getId();
			this.current = new Text("CURRENT");
			this.current.changeColor(Color.RED);
			this.current.setX(saveFileButtons[id].getX() - (current.getWidth() + 2));
			this.current.setY((((saveFileButtons[id].getY() * 2) + saveFileButtons[id].getHeight()) / 2) - (current.getHeight() / 2));
		}
		else
		{
			this.current = null;
		}
	}
	
	private void createLastSavedTexts()
	{
		lastSavedTexts = new Text[saveFiles.length];
		for(int i = 0; i < saveFiles.length; i++)
		{
			if(saveFiles[i].isEmpty() == false)
			{
				this.lastSavedTexts[i] = new Text("LAST SAVED " + saveFiles[i].getSaveDate().toString());
				lastSavedTexts[i].setX(saveFileButtons[i].getX() + 4);
				lastSavedTexts[i].setY(saveFileButtons[i].getY() + saveFileButtons[i].getHeight() - (lastSavedTexts[i].getHeight() + 2));
			}
			else
			{
				lastSavedTexts[i] = null;
			}
		}
	}
	
	private void createSaveFileButtons()
	{
		BufferedImage buttonImages[] = ImageManager.instance().getButtonImages();
		
		this.saveFileButtons = new ImageButton[saveFiles.length];
		for(int i = 0; i < saveFileButtons.length; i++)
		{
			String text;
			if(saveFiles[i].isEmpty())
			{
				text = "NO DATA";
			}
			else
			{
				text = "SAVE FILE " + (i  + 1);
			}
			
			if(i == 0)
			{
				saveFileButtons[i] = new ImageButton(
						((x + x + width) / 2) - (buttonImages[11].getWidth() / 2),
						y + 32,
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
						((x + x + width) / 2) - (buttonImages[11].getWidth() / 2),
						saveFileButtons[i - 1].getY() + saveFileButtons[i - 1].getHeight() + 4,
						buttonImages[11].getWidth(),
						buttonImages[11].getHeight(),
						buttonImages[11],
						buttonImages[12],
						text
				);
			}

		}
	}
	
	private void createButtons()
	{
		BufferedImage buttonImages[] = ImageManager.instance().getButtonImages();
		
		backButton = new ImageButton(
				((x + x + width) / 2) - (buttonImages[0].getWidth() / 2),
				(y + height) - (buttonImages[0].getHeight() + 4),
				buttonImages[0].getWidth(),
				buttonImages[0].getHeight(),
				buttonImages[0],
				buttonImages[1],
				"BACK"
		);
	}
	
	//Getter methods
	public Color getMenuColor() {return menuColor;}
	public Color getBorderColor() {return borderColor;}
	public boolean isBordered() {return bordered;}
	
	//Setter methods
	public void setMenuColor(Color menuColor) {this.menuColor = menuColor;}
	public void setBorderColor(Color borderColor) {this.borderColor = borderColor;}
	public void setBordered(boolean b) {this.bordered = b;}
	
////////////////////////////////////////////// UPDATE METHODS //////////////////////////////////////////////
	
	/**
	 * Method that updates Buttons
	 */
	private void updateButtons()
	{
		for(int i = 0; i < saveFileButtons.length; i++)
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
		for(int i = 0; i < saveFileButtons.length; i++)
		{
			if(saveFileButtons[i].isMouseClickingButton())
			{
				//Set mouseclicking flag to false
				saveFileButtons[i].setMouseClickingButton(false);
				
				//Obtain the date and time the saved happened
				Date savedDate = new Date();
				savedDate.calculateDate();
				
				//Attempt to save files
				SaveManager.instance().save(i, tileMap, objectMap, itemMap, hitboxMap, savedDate);
				
				//Obtain file that was just saved
				saveFiles[i] = SaveManager.instance().getSaveFile(i);
				
				//Set currentSaveFile to the one we just saved to
				SaveManager.instance().setCurrentSaveFile(SaveManager.instance().getSaveFile(i));
				
				//Flag saved to true
				saved = true;
				
				//Start savedMessageTimer
				savedMessageTimer = System.nanoTime();
				
				//Recreate saveFileButtons; "Update" them because we just saved
				createSaveFileButtons();
				
				//Recreate current Text location
				this.current = new Text("CURRENT");
				this.current.changeColor(Color.RED);
				current.setX(saveFileButtons[i].getX() - (current.getWidth() + 2));
				current.setY((((saveFileButtons[i].getY() * 2) + saveFileButtons[i].getHeight()) / 2) - (current.getHeight() / 2));
				
				//Recreate lastSavedTexts
				createLastSavedTexts();
				
				return;
			}
		}
		
		if(backButton.isMouseClickingButton())
		{
			backButton.setMouseClickingButton(false);
			visible = false;
		}
	}
	
	/**
	 * Method that updates Texts
	 */
	private void updateTexts()
	{
		if(saved)
		{
			if((System.nanoTime() - savedMessageTimer) / 1000000 > savedMessageDelay)
			{
				saved = false;
			}
		}
	}
	
	/**
	 * Method that updates SaveMenu
	 */
	public void update()
	{
		if(visible)
		{
			updateButtons();
			updateTexts();
		}
	}
	
////////////////////////////////////////////// DRAW METHODS //////////////////////////////////////////////
	
	/**
	 * Method that draws the background
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawBackground(Graphics2D g)
	{
		g.setColor(new Color(0, 0, 0, 175));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	
	/**
	 * Method that draws the Menu
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawMenu(Graphics2D g)
	{
		g.setColor(menuColor);
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * Method that draws the border
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawBorder(Graphics2D g)
	{
		g.setColor(borderColor);
		g.drawRect(x, y, width - 1, height - 1);
	}
	
	/**
	 * Method that draws the message texts
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawMessageTexts(Graphics2D g)
	{
		if(saved)
		{
			savedMessage.draw(g);
		}
		else
		{
			message.draw(g);
		}
	}
	
	/**
	 * Method that draws the current text
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	private void drawCurrentText(Graphics2D g)
	{
		if(current != null)
		{
			current.draw(g);
		}
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
		for(int i = 0; i < saveFileButtons.length; i++)
		{
			saveFileButtons[i].draw(g);
		}
		
		backButton.draw(g);
	}
	
	/**
	 * Method that draws the SaveMenu
	 * @param g (Graphics2D g) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		if(visible)
		{
			drawBackground(g);
			drawMenu(g);
			
			if(bordered)
			{
				drawBorder(g);
			}
			
			drawMessageTexts(g);
			drawCurrentText(g);
			drawButtons(g);
			drawLastSavedTexts(g);
		}
	}
}
