package manager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import map.TileMap;

/**
 * SaveManager class manages save files
 * @author Vachia Thoj
 *
 */
public class SaveManager 
{
	//For singleton
	private static SaveManager saveManager;
	
	//TileMaps
	private TileMap layer1;
	private TileMap layer2;
	private TileMap collisionLayer;
	private TileMap itemLayer;
	
	//Flag to see if data has been saved
	private boolean saved;
	
	/**
	 * Constructor (private)
	 */
	private SaveManager()
	{
		this.layer1 = null;
		this.layer2 = null;
		this.collisionLayer = null;
		this.itemLayer = null;
		
		this.saved = false;
	}
	
	/**
	 * Method to be called in order to obtain SaveManager object (Singleton)
	 * @return SaveManager object
	 */
	public static SaveManager instance()
	{
		if(saveManager == null)
		{
			saveManager = new SaveManager();
		}
		
		return saveManager;
	}
	
	//Getter methods
	public TileMap getLayer1() {return layer1;}
	public TileMap getLayer2() {return layer2;}
	public TileMap getCollisionLayer() {return collisionLayer;}
	public TileMap getItemLayer() {return itemLayer;}
	
	public boolean isSaved() {return saved;}
	
	//Setter methods
	public void setLayer1(TileMap layer1) {this.layer1 = layer1;}
	public void setLayer2(TileMap layer2) {this.layer2 = layer2;}
	public void setCollisionLayer(TileMap collisionLayer) {this.collisionLayer = collisionLayer;}
	public void setItemLayer(TileMap itemLayer) {this.itemLayer = itemLayer;}
	public void setSaved(boolean b) {this.saved = b;}
	
	/**
	 * Method that attempts to load save files
	 */
	public void load()
	{
		try {
			
			//Obtain files as streams
			InputStream layer1Stream = getClass().getResourceAsStream("/saves/Layer1.txt");
			InputStream layer2Stream = getClass().getResourceAsStream("/saves/Layer2.txt");
			InputStream collisionLayerStream = getClass().getResourceAsStream("/saves/CollisionLayer.txt");
			InputStream itemLayerStream = getClass().getResourceAsStream("/saves/ItemLayer.txt");
			
			if(layer1Stream == null || layer2Stream == null || collisionLayerStream == null || itemLayerStream == null)
			{		
				System.out.println("Failed to load file");
				return; 
			}
			
			//Convert files to TileMap objects
			ObjectInputStream ios = new ObjectInputStream(layer1Stream);
			this.layer1 = (TileMap) ios.readObject();
			
			ios = new ObjectInputStream(layer2Stream); 
			this.layer2 = (TileMap) ios.readObject();
			
			ios = new ObjectInputStream(collisionLayerStream);
			this.collisionLayer = (TileMap) ios.readObject();
			
			ios = new ObjectInputStream(itemLayerStream);
			this.itemLayer = (TileMap) ios.readObject();
			
			//Close streams
			layer1Stream.close();
			layer2Stream.close();
			collisionLayerStream.close();
			itemLayerStream.close();
			ios.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that attempts to save TileMaps to files
	 * @param layer1 (TileMap) a TileMap
	 * @param layer2 (TileMap) a TileMap
	 * @param collisionLayer (TileMap) a TileMap
	 * @param itemLayer (TileMap) a TileMap
	 */
	public void save(TileMap layer1, TileMap layer2, TileMap collisionLayer, TileMap itemLayer)
	{
		try {
			
			//Save tileMap; Write TileMap objects to files
			FileOutputStream file = new FileOutputStream("./resources/saves/Layer1.txt");
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(layer1);
			
			file = new FileOutputStream("./resources/saves/Layer2.txt");
			out = new ObjectOutputStream(file);
			out.writeObject(layer2);
			
			file = new FileOutputStream("./resources/saves/CollisionLayer.txt");
			out = new ObjectOutputStream(file);
			out.writeObject(collisionLayer);
			
			file = new FileOutputStream("./resources/saves/ItemLayer.txt");
			out = new ObjectOutputStream(file);
			out.writeObject(itemLayer);
			
			file.close();
			out.close();
			
			saved = true;
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
