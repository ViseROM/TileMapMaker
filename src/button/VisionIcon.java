package button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * VisionIcon class
 * @author Vachia Thoj
 *
 */
public class VisionIcon extends Button
{
	private static final long serialVersionUID = 1L;
	
	//Images of VisionIcon
	private transient BufferedImage buttonImage1;
	private transient BufferedImage buttonImage2;
	private transient BufferedImage iconImage;
	
	//Current VisionIcon image
	private transient BufferedImage currentButtonImage;
	
	//Flag to see if VisionIcon has vision
	private transient boolean vision;
	
	/**
	 * Constructor
	 * @param buttonImage1 (BufferedImage) An image of VisionIcon
	 * @param buttonImage2 (BufferedImage) An image of VisionIcon
	 * @param iconImage (BufferedImage) An image of the icon
	 */
	public VisionIcon(BufferedImage buttonImage1, BufferedImage buttonImage2, BufferedImage iconImage)
	{
		this.buttonImage1 = buttonImage1;
		this.buttonImage2 = buttonImage2;
		this.iconImage = iconImage;
		
		this.currentButtonImage = buttonImage1;
		this.width = currentButtonImage.getWidth();
		this.height = currentButtonImage.getHeight();
		
		this.vision = true;
		this.visible = false;
		this.disabled = true;
	}
	
	/**
	 * 
	 * @param x (int) The x-coordinate of VisionIcon
	 * @param y (int) The y-coordinate of VisionIcon
	 * @param buttonImage1 (BufferedImage) An image of VisionIcon
	 * @param buttonImage2 (BufferedImage) An image of VisionIcon
	 * @param iconImage (BufferedImage) An image of the icon
	 */
	public VisionIcon(int x, int y, BufferedImage buttonImage1, BufferedImage buttonImage2, BufferedImage iconImage)
	{
		this.x = x;
		this.y = y;
		this.buttonImage1 = buttonImage1;
		this.buttonImage2 = buttonImage2;
		this.iconImage = iconImage;
		
		this.currentButtonImage = buttonImage1;
		this.width = currentButtonImage.getWidth();
		this.height = currentButtonImage.getHeight();
		
		this.vision = true;
		this.visible = false;
		this.disabled = true;
	}
	
	public boolean hasVision() {return vision;}
	
	public void setVision(boolean b) {this.vision = b;}
	
	/**
	 * Method that updates VisionIcon
	 */
	public void update()
	{
		if(disabled == true || visible == false)
		{
			return;
		}
		
		//Check if mouse is touching VisionIcon
		checkIfMouseTouchingButton();
		
		//Check if mouse is clicking on VisionIcon
		checkIfMouseClickingButton();
		
		//Change image of VisionIcon if mouse is touching VisionIcon
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
	 * Method that draws VisionIcon
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		g.drawImage(currentButtonImage, x, y, null);
		
		if(vision)
		{
			g.drawImage(iconImage, x, y, null);
		}
	}
	
	
}
