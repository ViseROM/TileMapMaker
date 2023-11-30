package save;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import date.*;
import map.*;

/**
 * Save File class represents a save file for the program
 * @author Vachia Thoj
 *
 */
public class SaveFile 
{
	//Id of SaveFile
	private int id;
	
	//File paths of files to be saved
	private String tileMapFilePath;
	private String objectMapFilePath;
	private String itemMapFilePath;
	private String hitboxMapFilePath;
	private String saveDateFilePath;
	
	//Maps to be saved
	private TileMap tileMap;
	private ObjectMap objectMap;
	private TileMap itemMap;
	private TileMap hitboxMap;
	
	//Date of when SaveFile was last saved
	private Date saveDate;
	
	//Flag to see if SaveFile is empty
	private boolean empty;
	
	/**
	 * 
	 * @param id (int) The id of SaveFile
	 * @param tileMapFilePath (String) The file path of the tile map file
	 * @param objectMapFilePath (String) The file path of the object map file
	 * @param itemMapFilePath (String) The file path of the item map file
	 * @param hitboxMapFilePath (String) The file path of the hitbox map file
	 * @param saveDateFilePath (String) The file path of the save date file
	 */
	public SaveFile(int id, String tileMapFilePath, String objectMapFilePath, String itemMapFilePath, String hitboxMapFilePath, String saveDateFilePath)
	{
		this.id = id;
		this.tileMapFilePath = tileMapFilePath;
		this.objectMapFilePath = objectMapFilePath;
		this.itemMapFilePath = itemMapFilePath;
		this.hitboxMapFilePath = hitboxMapFilePath;
		this.saveDateFilePath = saveDateFilePath;
		
		this.tileMap = null;
		this.objectMap = null;
		this.itemMap = null;
		this.hitboxMap = null;
		this.saveDate = null;
		
		this.empty = true;
	}
	
	//Getter methods
	public int getId() {return id;}
	public boolean isEmpty() {return empty;}
	public TileMap getTileMap() {return tileMap;}
	public ObjectMap getObjectMap() {return objectMap;}
	public TileMap getItemMap() {return itemMap;}
	public TileMap getHitboxMap() {return hitboxMap;}
	public Date getSaveDate() {return saveDate;}
	
	//Setter methods
	public void setId(int id) {this.id = id;}
	public void setEmpty(boolean b) {this.empty = b;}
	
	/**
	 * Method that attempts to save data to save files
	 * @param tileMap (TileMap) The tileMap to be saved
	 * @param objectMap (ObjectMap) The objectMap to be saved
	 * @param itemMap (TileMap) The itemMap to be saved
	 * @param hitboxMap (TileMap) The hitbox to be saved
	 * @param saveDate (Date) The date of when the saved occurred
	 */
	public void save(TileMap tileMap, ObjectMap objectMap, TileMap itemMap, TileMap hitboxMap, Date saveDate)
	{
		try {
			
			//Save tileMap to a file
			FileOutputStream file = new FileOutputStream("./resources" + tileMapFilePath);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(tileMap);
			this.tileMap = tileMap;
			
			//Save itemMap to a file
			file = new FileOutputStream("./resources" + itemMapFilePath);
			out = new ObjectOutputStream(file);
			out.writeObject(itemMap);
			this.itemMap = itemMap;
			
			//Save objectMap to a file
			file = new FileOutputStream("./resources" + objectMapFilePath);
			out = new ObjectOutputStream(file);
			out.writeObject(objectMap);
			this.objectMap = objectMap;
			
			//Save hitboxMap to a file
			file = new FileOutputStream("./resources" + hitboxMapFilePath);
			out = new ObjectOutputStream(file);
			out.writeObject(hitboxMap);
			this.hitboxMap = hitboxMap;
			
			//Save saveDate to a file
			file = new FileOutputStream("./resources" + saveDateFilePath);
			out = new ObjectOutputStream(file);
			out.writeObject(saveDate);
			this.saveDate = saveDate;
			
			file.close();
			out.close();
			
			this.empty = false;
			
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("FAILED to SAVE");
		}
	}
	
	/**
	 * Method that attempts to load a file
	 */
	public void load()
	{
		try {
			
			//Load files
			InputStream tileMapStream = getClass().getResourceAsStream(tileMapFilePath);
			InputStream objectMapStream = getClass().getResourceAsStream(objectMapFilePath);
			InputStream itemMapStream = getClass().getResourceAsStream(itemMapFilePath);
			InputStream hitboxMapStream = getClass().getResourceAsStream(hitboxMapFilePath);
			InputStream saveDateStream = getClass().getResourceAsStream(saveDateFilePath);
			
			if(tileMapStream == null || objectMapStream == null || itemMapStream == null || hitboxMapStream == null || saveDateStream == null)
			{	
				this.empty = true;
				return;
			}
			
			//Convert files to TileMap objects
			ObjectInputStream ios = new ObjectInputStream(tileMapStream);
			this.tileMap = (TileMap) ios.readObject();
			
			ios = new ObjectInputStream(objectMapStream);
			this.objectMap = (ObjectMap) ios.readObject();
			
			ios = new ObjectInputStream(itemMapStream); 
			this.itemMap = (TileMap) ios.readObject();
			
			ios = new ObjectInputStream(hitboxMapStream);
			this.hitboxMap = (TileMap) ios.readObject();
			
			ios = new ObjectInputStream(saveDateStream);
			this.saveDate = (Date) ios.readObject();
			
			tileMapStream.close();
			objectMapStream.close();
			itemMapStream.close();
			hitboxMapStream.close();
			saveDateStream.close();
			
			this.empty = false;
			
		}catch(IOException e) {
			this.empty = true;
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			this.empty = true;
			e.printStackTrace();
		}
	}
}