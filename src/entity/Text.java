package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import manager.ImageManager;


/**
 * Text class represents Text/String as an image
 * @author Vachia Thoj
 *
 */
public class Text extends Entity
{
	private static final long serialVersionUID = 1L;
	
	//A String of text
	private transient String text;
	
	//The text as a BufferedImage
	private transient BufferedImage textImage;
	
	/**
	 * Constructor
	 * @param text (String)
	 */
	public Text(String text)
	{
		this.text = text;
		this.textImage = null;
		
		createTextImage();
		
		if(textImage != null)
		{
			this.width = textImage.getWidth();
			this.height = textImage.getHeight();
		}
		else
		{
			this.width = 0;
			this.height = 0;
		}
	}
	
	/**
	 * Constructor
	 * @param x (int) The x-coordinate of Text
	 * @param y (int) The y-coordinate of Text
	 * @param text (String)
	 */
	public Text(int x, int y, String text)
	{
		this.x = x;
		this.y = y;
		this.text = text;
		this.textImage = null;
		
		createTextImage();
		
		if(textImage != null)
		{
			this.width = textImage.getWidth();
			this.height = textImage.getHeight();
		}
		else
		{
			this.width = 0;
			this.height = 0;
		}
		
	}
	
	private void createTextImage()
	{
		//Convert characters of text to upper case
		text = text.toUpperCase();
		BufferedImage tempImage;
		
		for(int i = 0; i < text.length(); i++)
		{
			//Obtain an image of the character
			int asciiValue = text.charAt(i);
			if(asciiValue == 32)
			{
				tempImage = ImageManager.instance().getAsciiImage(0);
				textImage = concatenateImage(tempImage);
			}
			if(asciiValue >= 47 && asciiValue <= 58)
			{
				int index = asciiValue - 46;
				tempImage = ImageManager.instance().getAsciiImage(index);
				textImage = concatenateImage(tempImage);
			}
			else if(asciiValue >= 65 && asciiValue <= 93)
			{
				int index = asciiValue - 52;
				tempImage = ImageManager.instance().getAsciiImage(index);
				textImage = concatenateImage(tempImage);
			}
			else if(asciiValue == 124)
			{
				tempImage = ImageManager.instance().getAsciiImage(42);
				textImage = concatenateImage(tempImage);
			}
		}
	}
	
	/**
	 * Method that combines each character image into one image
	 * @param image (BufferedImage) An image to be combined
	 * @return (BufferedImage) The combined image
	 */
	private BufferedImage concatenateImage(BufferedImage image)
	{
		BufferedImage resultImage;
		if(textImage == null)
		{
			resultImage = image;
		}
		else
		{
			int newWidth = textImage.getWidth() + image.getWidth();
			int newHeight = Math.max(textImage.getHeight(), image.getHeight());
			resultImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			
			Graphics g = resultImage.getGraphics();
			g.drawImage(textImage, 0, 0, null);
			g.drawImage(image, textImage.getWidth(), 0, null);
			g.dispose();
		}
		
		return resultImage;
	}
	
	/**
	 * Method that changes the color of the textImage
	 * @param newColor (Color) The color to change textImage to
	 */
	public void changeColor(Color newColor)
	{
		if(textImage == null)
		{
			return;
		}
		
		BufferedImage newImage = new BufferedImage(
				textImage.getWidth(),
				textImage.getHeight(),
				textImage.getType()
		);
		
		for(int y = 0; y < textImage.getHeight(); y++)
		{
			for(int x = 0; x < textImage.getWidth(); x++)
			{
				int rgb = textImage.getRGB(x, y);
				
				//Extract the individual components of the original color
				int alpha = (rgb >> 24) & 0xFF;
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;
				
				//Set the new color
				red = newColor.getRed();
				green = newColor.getGreen();
				blue = newColor.getBlue();
				
				//Combine the components and set the new color in the new image
				int newRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
				newImage.setRGB(x, y, newRGB);
			}
		}
		
		textImage = newImage;
	}
	
	/**
	 * Method that changes the size of the textImage
	 * @param scale (int) How much to scale the textImage
	 */
	public void changeScale(int scale)
	{
		if(scale > 1 && textImage != null)
		{
			int newWidth = width * scale;
			int newHeight = height * scale;
			
			BufferedImage newImage = new BufferedImage(newWidth, newHeight, textImage.getType());
			Graphics2D g2d = newImage.createGraphics();
			
			AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
			g2d.drawImage(textImage, transform, null);
			
			g2d.dispose();
			
			this.textImage = newImage;
			this.width = newImage.getWidth();
			this.height = newImage.getHeight();
		}
	}
	
	//Getter methods
	public String getText() {return text;}
	public BufferedImage getTextImage() {return textImage;}
	
	public void setText(String text)
	{
		this.text = text;
		this.textImage = null;
		createTextImage();
		
		if(textImage != null)
		{
			this.width = textImage.getWidth();
			this.height = textImage.getHeight();
		}
		else
		{
			this.width = 0;
			this.height = 0;
		}
		
	}
	
	
	/**
	 * Method that draws Text
	 * @param g (Graphics2D) The Graphics2D object to be drawn on
	 */
	public void draw(Graphics2D g)
	{
		if(textImage != null)
		{
			g.drawImage(textImage, x, y, null);
		}
	}
}
