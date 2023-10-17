package manager;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * ImageManager class attempts to load image files and keeps track 
 * of them
 * 
 * @author Vachia Thoj
 *
 */
public class ImageManager 
{
	//For singleton
	private static ImageManager imageManager;
	
	//Images
	private BufferedImage[] bigButtonImages;
	private BufferedImage[] smallButtonImages;
	private BufferedImage[] controlImages;
	private BufferedImage[] tileImages;
	private BufferedImage[] itemImages;
	
	/**
	 * Constructor (private)
	 */
	private ImageManager()
	{
		//Load image sheets
		BufferedImage bigButtonSheet = loadImage("/images/BigButtonSheet.png");
		BufferedImage smallButtonSheet = loadImage("/images/SmallButtonSheet.png");
		BufferedImage controlImageSheet = loadImage("/images/ControlsSheet.png");
		BufferedImage tileSheet = loadImage("/images/TileSheet.png");
		BufferedImage itemSheet = loadImage("/images/ItemSheet.png");
		
		//Obtain individual images from image sheets
		loadBigButtonImages(bigButtonSheet);
		loadSmallButtonImages(smallButtonSheet);
		loadControlImages(controlImageSheet);
		loadTileImages(tileSheet);
		loadItemImages(itemSheet);
		
		bigButtonSheet = null;
		smallButtonSheet = null;
		controlImageSheet = null;
		tileSheet = null;
		itemSheet = null;
	}
	
	/**
	 * Method to be called to obtain ImageManager object (Singleton)
	 * @return ImageManager object 
	 */
	public static ImageManager instance()
	{
		if(imageManager == null)
		{
			imageManager = new ImageManager();
		}
		
		return imageManager;
	}
	
	/**
	 * Method that attempts to open/obtain an image
	 * 
	 * @param address (String) the address location of the image
	 * @return BufferedImage from the address provided, will return null if image cannot be found opened
	 */
	private BufferedImage loadImage(String address)
	{
		BufferedImage imageSheet = null;
		
		//Try to obtain an image
		try {
			//Obtain image from address
			imageSheet = ImageIO.read(getClass().getResourceAsStream(address));
			
			return imageSheet;
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading image");
			System.exit(0);
		}
		return null;
	}
	
	/**
	 * Method that obtains Big Button images from an image sheet
	 * @param sheet (BufferedImage) an image sheet
	 */
	private void loadBigButtonImages(BufferedImage sheet)
	{
		bigButtonImages = new BufferedImage[10];
		int index = 0;
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				bigButtonImages[index] = sheet.getSubimage(j * 192, 48 * i, 192, 48);
				++index;
			}
		}
		
		for(int i = 0; i < 2; i++)
		{
			bigButtonImages[index] = sheet.getSubimage(i * 192, 96, 192, 48);
			++index;
		}
	}
	
	/**
	 * Method that obtains Small Button images from an image sheet
	 * @param sheet (BufferedImage) an image sheet
	 */
	private void loadSmallButtonImages(BufferedImage sheet)
	{
		smallButtonImages = new BufferedImage[30];
		int index = 0;
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				smallButtonImages[index] = sheet.getSubimage(j * 96, i * 32, 96, 32);
				++index;
			}
		}
		
		for(int i = 0; i < 2; i++)
		{
			smallButtonImages[index] = sheet.getSubimage(i * 96, 64, 96, 32);
			++index;
		}
		
		for(int i = 0; i < 8; i++)
		{
			smallButtonImages[index] = sheet.getSubimage(i * 96, 96, 96, 32);
			++index;
		}
		
		for(int i = 0; i < 4; i++)
		{
			smallButtonImages[index] = sheet.getSubimage(i * 32, 128, 32, 32);
			++index;
		}
	}
	
	/**
	 * Method that obtains Control images from an image sheet
	 * @param sheet (BufferedImage) an image sheet
	 */
	private void loadControlImages(BufferedImage sheet)
	{
		controlImages = new BufferedImage[4];
		int index = 0;
		
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 2; j++)
			{
				controlImages[index] = sheet.getSubimage(j * 320, 160 * i, 320, 160);
				++index;
			}
		}
	}
	
	/**
	 * Method that obtains Tile images from an image sheet
	 * @param sheet (BufferedImage) an image sheet
	 */
	private void loadTileImages(BufferedImage sheet)
	{
		tileImages = new BufferedImage[42];
		int index = 0;
		
		for(int i = 0; i < 6; i++)
		{
			tileImages[index] = sheet.getSubimage(i * 32, 0, 32, 32);
			++index;
		}
		
		for(int i = 0; i < 14; i++)
		{
			tileImages[index] = sheet.getSubimage(i * 32, 32, 32, 32);
			++index;
		}
		
		for(int i = 0; i < 13; i++)
		{
			tileImages[index] = sheet.getSubimage(i * 32, 64, 32, 32);
			++index;
		}
		
		for(int i = 0; i < 6; i++)
		{
			tileImages[index] = sheet.getSubimage(i * 32, 96, 32, 32);
			++index;
		}
		
		for(int i = 0; i < 3; i++)
		{
			tileImages[index] = sheet.getSubimage(i * 32, 128, 32, 32);
			++index;
		}
	}
	
	/**
	 * Method that obtains Item images from an image sheet
	 * @param sheet (BufferedImage) an image sheet
	 */
	private void loadItemImages(BufferedImage sheet)
	{
		itemImages = new BufferedImage[4];
		int index = 0;
		
		for(int i = 0; i < 4; i++)
		{
			itemImages[index] = sheet.getSubimage(i * 32, 0, 32, 32);
			++index;
		}
	}

	//Getter methods
	public BufferedImage[] getBigButtonImages() {return bigButtonImages;}
	public BufferedImage[] getSmallButtonImages() {return smallButtonImages;}
	public BufferedImage[] getControlImages() {return controlImages;}
	public BufferedImage[] getTileImages() {return tileImages;}
	public BufferedImage[] getItemImages() {return itemImages;}
}
