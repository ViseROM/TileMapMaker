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
	
	//BufferedImages
	private BufferedImage[] hitboxImages;
	private BufferedImage[] tileImages;
	private BufferedImage[] itemImages;
	private BufferedImage[] asciiImages;
	private BufferedImage[] buttonImages;
	private BufferedImage[] treeImages;
	private BufferedImage[] bushImages;
	private BufferedImage[] rockImages;
	private BufferedImage[] structureImages;
	private BufferedImage[] buildingImages;
	private BufferedImage[] miscImages;
	private BufferedImage[] tutorialImages;
	
	/**
	 * Constructor
	 */
	private ImageManager()
	{
		//Load image sheets
		BufferedImage hitboxSheet = loadImage("/images/HitboxSheet.png");
		BufferedImage tileSheet = loadImage("/images/TileSheet.png");
		BufferedImage itemSheet = loadImage("/images/ItemSheet.png");
		BufferedImage asciiSheet = loadImage("/images/AsciiSheet.png");
		BufferedImage buttonSheet = loadImage("/images/ButtonSheet.png");
		BufferedImage treeSheet = loadImage("/images/TreeSheet.png");
		BufferedImage bushSheet = loadImage("/images/BushSheet.png");
		BufferedImage rockSheet = loadImage("/images/RockSheet.png");
		BufferedImage structureSheet = loadImage("/images/StructureSheet.png");
		BufferedImage buildingSheet = loadImage("/images/BuildingSheet.png");
		BufferedImage miscSheet = loadImage("/images/MiscSheet.png");
		BufferedImage tutorialSheet = loadImage("/images/TutorialSheet.png");
		
		//Obtain individual images within each image sheet
		loadHitboxImages(hitboxSheet);
		loadTileImages(tileSheet);
		loadItemImages(itemSheet);
		loadAsciiImages(asciiSheet);
		loadButtonImages(buttonSheet);
		loadTreeImages(treeSheet);
		loadBushImages(bushSheet);
		loadRockImages(rockSheet);
		loadStructureImages(structureSheet);
		loadBuildingImages(buildingSheet);
		loadMiscImages(miscSheet);
		loadTutorialImages(tutorialSheet);
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
	
	private void loadTileImages(BufferedImage sheet)
	{
		tileImages = new BufferedImage[59];
		tileImages[0] = hitboxImages[0];
		
		int index = 1;
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 14; j++)
			{
				tileImages[index] = sheet.getSubimage(j * 16, i * 16, 16, 16);
				++index;
			}
		}
		
		for(int i = 0; i < 2; i++)
		{
			tileImages[index] = sheet.getSubimage(i * 16, 64, 16, 16);
			++index;
		}
	}
	
	private void loadItemImages(BufferedImage sheet)
	{
		itemImages = new BufferedImage[29];
		itemImages[0] = hitboxImages[0];
		
		int index = 1;
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 14; j++)
			{
				itemImages[index] = sheet.getSubimage(j * 16, i * 16, 16, 16);
				++index;
			}
		}
	}
	
	private void loadHitboxImages(BufferedImage sheet)
	{
		hitboxImages = new BufferedImage[2];
		int index = 0;
		
		for(int i = 0;i < 2; i++)
		{
			hitboxImages[index] = sheet.getSubimage(i * 16, 0, 16, 16);
			++index;
		}
	}
	
	private void loadAsciiImages(BufferedImage sheet)
	{
		asciiImages = new BufferedImage[43];
		
		int index = 0;
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 13; j++)
			{
				if(index == 0)
				{
					asciiImages[index] = sheet.getSubimage(j * 6, i * 9, 4, 9);
				}
				else
				{
					asciiImages[index] = sheet.getSubimage(j * 6, i * 9, 6, 9);
				}
				
				++index;
				
			}
		}
		
		for(int i = 0; i < 4; i++)
		{
			asciiImages[index] = sheet.getSubimage(i * 6, 27, 6, 9);
			++index;
		}
			
	}
	
	private void loadButtonImages(BufferedImage sheet)
	{
		buttonImages = new BufferedImage[13];
		int index = 0;
		for(int i = 0; i < 2; i++)
		{
			buttonImages[index] = sheet.getSubimage(i * 80, 0, 80, 16);
			++index;
		}
		
		for(int i = 0; i < 2; i++)
		{
			buttonImages[index] = sheet.getSubimage(i * 64, 16, 64, 16);
			++index;
		}
		
		for(int i = 0; i < 7; i++)
		{
			buttonImages[index] = sheet.getSubimage(i * 16, 32, 16, 16);
			++index;
		}
		
		buttonImages[11] = sheet.getSubimage(0, 48, 320, 64);
		buttonImages[12] = sheet.getSubimage(0, 112, 320, 64);
	}
	
	private void loadTreeImages(BufferedImage sheet)
	{
		treeImages = new BufferedImage[5];
		
		treeImages[0] = hitboxImages[0];
		treeImages[1] = sheet.getSubimage(0, 0, 16, 32);
		treeImages[2] = sheet.getSubimage(16, 0, 16, 32);
		treeImages[3] = sheet.getSubimage(32, 0, 32, 32);
		treeImages[4] = sheet.getSubimage(64, 0, 32, 32);
	}
	
	private void loadBushImages(BufferedImage sheet)
	{
		bushImages = new BufferedImage[4];
		
		bushImages[0] = hitboxImages[0];
		int index = 1;
		for(int i = 0; i < 3; i++)
		{
			bushImages[index] = sheet.getSubimage(i * 16, 0, 16, 16);
			++index;
		}
	}
	
	private void loadRockImages(BufferedImage sheet)
	{
		rockImages = new BufferedImage[3];
		
		rockImages[0] = hitboxImages[0];
		rockImages[1] = sheet.getSubimage(0, 0, 16, 16);
		rockImages[2] = sheet.getSubimage(16, 0, 16, 16);
	}
	
	private void loadStructureImages(BufferedImage sheet)
	{
		structureImages = new BufferedImage[10];
		
		structureImages[0] = hitboxImages[0];
		structureImages[1] = sheet.getSubimage(0, 0, 16, 16);
		structureImages[2] = sheet.getSubimage(16, 0, 16, 16);
		structureImages[3] = sheet.getSubimage(32, 0, 32, 16);
		structureImages[4] = sheet.getSubimage(64, 0, 32, 16);
		structureImages[5] = sheet.getSubimage(96, 0, 16, 32);
		structureImages[6] = sheet.getSubimage(112, 0, 16, 32);
		structureImages[7] = sheet.getSubimage(128, 0, 16, 32);
		structureImages[8] = sheet.getSubimage(144, 0, 32, 32);
		structureImages[9] = sheet.getSubimage(176, 0, 32, 32);
	}
	
	private void loadBuildingImages(BufferedImage sheet)
	{
		buildingImages = new BufferedImage[3];
		
		buildingImages[0] = hitboxImages[0];
		buildingImages[1] = sheet.getSubimage(0, 0, 48, 48);
		buildingImages[2] = sheet.getSubimage(48, 0, 32, 48);
	}
	
	private void loadMiscImages(BufferedImage sheet)
	{
		miscImages = new BufferedImage[2];
		
		miscImages[0] = hitboxImages[0];
		miscImages[1] = sheet.getSubimage(0, 0, 16, 16);
	}
	
	private void loadTutorialImages(BufferedImage sheet)
	{
		tutorialImages = new BufferedImage[3];
		
		for(int i = 0; i < tutorialImages.length; i++)
		{
			tutorialImages[i] = sheet.getSubimage(0, i * 128, 256, 128);
		}
	}
	
	//Getter methods
	public BufferedImage[] getHitboxImages() {return hitboxImages;}
	public BufferedImage[] getTileImages() {return tileImages;}
	public BufferedImage[] getItemImages() {return itemImages;}
	public BufferedImage[] getAsciiImages() {return asciiImages;}
	public BufferedImage[] getButtonImages() {return buttonImages;}
	public BufferedImage[] getTreeImages() {return treeImages;}
	public BufferedImage[] getBushImages() {return bushImages;}
	public BufferedImage[] getRockImages() {return rockImages;}
	public BufferedImage[] getStructureImages() {return structureImages;}
	public BufferedImage[] getBuildingImages() {return buildingImages;}
	public BufferedImage[] getMiscImages() {return miscImages;}
	public BufferedImage[] getTutorialImages() {return tutorialImages;}
	
	public BufferedImage getAsciiImage(int index) 
	{
		if(index < 0 || index > 42)
		{
			return null;
		}
		
		return asciiImages[index];
	}
}