package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class VisibleIcon extends Entity
{
	//Buttons
	private Button isVisibleButton;
	private Button notVisibleButton;
	
	//Flag to see if VisibleIcon is on
	private boolean eyeOn;
	
	/**
	 * Constructor
	 * @param image1 (BufferedImage) an image
	 * @param image2 (BufferedImage) an image
	 * @param image3 (BufferedImage) an image
	 * @param image4 (BufferedImage) an image
	 * @param x (int) x-coordinate of VisibleIcon
	 * @param y (int) y-coordinate of VisibleIcon
	 */
	public VisibleIcon(BufferedImage image1, BufferedImage image2, BufferedImage image3, BufferedImage image4, int x, int y)
	{
		this.isVisibleButton = new Button(image3, image4);
		this.isVisibleButton.setX(x);
		this.isVisibleButton.setY(y);
		
		this.notVisibleButton = new Button(image1, image2);
		this.notVisibleButton.setX(x);
		this.notVisibleButton.setY(y);
		
		this.x = x;
		this.y = y;
		
		this.width = isVisibleButton.getWidth();
		this.height = isVisibleButton.getHeight();
		
		this.eyeOn = true;
	}
	
	//Getter methods
	public boolean isEyeOn() {return eyeOn;}
	
	/**
	 * Method that updates VisibleIcon
	 */
	public void update()
	{
		if(eyeOn)
		{
			isVisibleButton.update();
			
			if(isVisibleButton.isMouseClickingButton())
			{
				isVisibleButton.setMouseClickingButton(false);
				eyeOn = false;
			}
		}
		else
		{
			notVisibleButton.update();
			
			if(notVisibleButton.isMouseClickingButton())
			{
				notVisibleButton.setMouseClickingButton(false);
				eyeOn = true;
			}
		}
	}
	
	/**
	 * Method that draws the VisibleIcon
	 * @param g (Graphics2D) the Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		//If eye is on...
		if(eyeOn)
		{
			//Draw isVisibleButton
			isVisibleButton.draw(g);
		}
		else
		{
			//Draw the notVisibleButton
			notVisibleButton.draw(g);
		}
	}
}
