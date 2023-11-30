package button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * CustomIcon class
 * @author Vachia Thoj
 */
public class CustomIcon extends Button
{
	private static final long serialVersionUID = 1L;
	
	//Images of CustomIcon
	private transient BufferedImage buttonImage1;
	private transient BufferedImage buttonImage2;
	private transient BufferedImage iconImage;
	
	//Current image of CustomIcon
	private transient BufferedImage currentButtonImage;
	
	//Flag to see if image within Icon is visible
	private transient boolean iconVisible;
	
	/**
	 * Constructor
	 * @param buttonImage1 (BufferedImage) An image of CustomIcon
	 * @param buttonImage2 (BufferedImage) An image of CustomIcon
	 * @param iconImage (BufferedImage) An image within the Icon
	 */
	public CustomIcon(BufferedImage buttonImage1, BufferedImage buttonImage2, BufferedImage iconImage)
	{
		this.buttonImage1 = buttonImage1;
		this.buttonImage2 = buttonImage2;
		this.iconImage = iconImage;
		
		this.currentButtonImage = buttonImage1;
		
		this.width = currentButtonImage.getWidth();
		this.height = currentButtonImage.getHeight();
		
		this.visible = true;
		this.disabled = false;
		
		this.iconVisible = true;
	}
	
	//Getter methods
	public boolean isIconVisible() {return iconVisible;}
	
	//Setter methods
	public void setIconVisible(boolean b) {this.iconVisible = visible;}
	
	/**
	 * Method that updates CustomIcon
	 */
	public void update()
	{
		if(disabled == true || visible == false)
		{
			return;
		}
		
		//Check if mouse is touching CustomIcon
		checkIfMouseTouchingButton();
		
		//Check if mouse is clicking on CustomIcon
		checkIfMouseClickingButton();
		
		//Change image of CustomIcon if mouse is touching CustomIcon
		if(mouseTouchingButton && currentButtonImage != buttonImage2)
		{
			currentButtonImage = buttonImage2;
			width = currentButtonImage.getWidth();
			height = currentButtonImage.getHeight();
		}
		else if(!mouseTouchingButton && currentButtonImage != buttonImage1)
		{
			currentButtonImage = buttonImage1;
			width = currentButtonImage.getWidth();
			height = currentButtonImage.getHeight();
		}
	}
	
	/**
	 * Method that draws the current Button image
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawCurrentButtonImage(Graphics2D g)
	{
		g.drawImage(currentButtonImage, x, y, null);
	}
	
	/**
	 * Method to draw the Icon image
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawIconImage(Graphics2D g)
	{
		if(iconVisible)
		{
			g.drawImage(iconImage, x, y, null);
		}
	}
	
	/**
	 * Method that draws the CustonIcon Button
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		if(visible)
		{
			drawCurrentButtonImage(g);
			drawIconImage(g);
		}
	}
}
