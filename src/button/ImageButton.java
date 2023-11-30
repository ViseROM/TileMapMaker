package button;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Text;

/**
 * ImageButton class represents a Button that has an image 
 * @author Vachia Thoj
 *
 */
public class ImageButton extends Button
{
	private static final long serialVersionUID = 1L;
	
	//Name of Button
	private transient String name;
	
	//The text within ImageButton
	private transient Text nameText;
	
	//Images for ImageButton
	private transient BufferedImage image1;
	private transient BufferedImage image2;
	private transient BufferedImage currentImage;
	
	/**
	 * Constructor
	 * @param x (int) The x-coordinate of ImageButton
	 * @param y (int) The y-coordinate of ImageButton
	 * @param width (int) The width of the ImageButton
	 * @param height (int) The height of the ImageButton
	 * @param image1 (BufferedImage) An image of ImageButton
	 * @param image2 (BufferedImage) An image of ImageButton
	 * @param name (String) The name of the ImageButton
	 */
	public ImageButton(int x, int y, int width, int height, BufferedImage image1, BufferedImage image2, String name)
	{
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image1 = image1;
		this.image2 = image2;
		this.name = name;
		createNameText();
		this.currentImage = image1;
		
		this.visible = true;
		this.disabled = false;
	}
	
	/**
	 * Constructor
	 * @param image1 (BufferedImage) An image of ImageButton
	 * @param image2 (BufferedImage) An image of ImageButton
	 */
	public ImageButton(BufferedImage image1, BufferedImage image2)
	{
		this.image1 = image1;
		this.image2 = image2;
		
		this.currentImage = image1;
		this.width = currentImage.getWidth();
		this.height = currentImage.getHeight();
		this.name = null;
		this.nameText = null;
		
		this.visible = true;
		this.disabled = false;
	}
	
	private void createNameText()
	{
		this.nameText = new Text(name);
		this.nameText.setX(((x + x + width) / 2) - (nameText.getWidth() / 2));
		this.nameText.setY(((y + y + height) / 2) - (nameText.getHeight() / 2)); 
	}
	
	//Getter Methods
	public String getName() {return name;}
	public BufferedImage getImage1() {return image1;}
	public BufferedImage getImage2() {return image2;}
	public BufferedImage getCurrentImage() {return currentImage;}
	
	//Setter methods
	public void setCurrentImage(BufferedImage currentImage) {this.currentImage = currentImage;}
	
	/**
	 * Method that updates ImageButton
	 */
	public void update()
	{
		if(disabled == true || visible == false)
		{
			return;
		}
		
		//Check if mouse has touched ImageButton
		checkIfMouseTouchingButton();
		
		//Check if mouse has clicked on ImageButton
		checkIfMouseClickingButton();
		
		//Change image of ImageButton if mouse is touching ImageButton
		if(mouseTouchingButton)
		{
			currentImage = image2;
			width = image2.getWidth();
			height = image2.getHeight();
		}
		else
		{
			currentImage = image1;
			width = image2.getWidth();
			height = image2.getHeight();
		}
	}
	
	/**
	 * Method that draws the current image of ImageButton
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawCurrentImage(Graphics2D g)
	{
		g.drawImage(currentImage, x, y, null);
	}
	
	/**
	 * Method that draws the name text of the ImageButton
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	private void drawNameText(Graphics2D g)
	{
		if(nameText != null)
		{
			nameText.draw(g);
		}	
	}
	
	/**
	 * Method that draws ImageButton
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		if(visible)
		{
			drawCurrentImage(g);
			drawNameText(g);
		}
	}
}