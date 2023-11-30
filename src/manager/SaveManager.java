package manager;

import date.*;
import map.*;
import save.*;

/**
 * SaveManager class manages save files
 * @author Vachia Thoj
 *
 */
public class SaveManager 
{
	//Singleton
	private static SaveManager saveManager;
	
	//Save files
	private SaveFile[] saveFiles;
	
	//The current save file
	private SaveFile currentSaveFile;
	
	//Maximum number of save files
	private static final int MAX_SAVE_FILES = 4;

	/**
	 * Constructor
	 */
	private SaveManager()
	{
		createSaveFiles();
		this.currentSaveFile = null;
	}
	
	private void createSaveFiles()
	{
		saveFiles = new SaveFile[MAX_SAVE_FILES];
		for(int i = 0; i < saveFiles.length; i++)
		{
			//Save file paths
			String tileMapFilePath = "/saves" + "/file" + (i + 1) + "/TileMap.ser";
			String objectMapFilePath = "/saves" + "/file" + (i + 1) + "/ObjectMap.ser";
			String itemMapFilePath = "/saves" + "/file" + (i + 1) + "/ItemMap.ser";
			String hitboxMapFilePath = "/saves" + "/file" + (i + 1) + "/HitboxMap.ser";
			String saveDateFilePath = "/saves" + "/file" + (i + 1) + "/SaveDate.ser";
			
			saveFiles[i] = new SaveFile(
					i,
					tileMapFilePath,
					objectMapFilePath,
					itemMapFilePath,
					hitboxMapFilePath,
					saveDateFilePath
			);
			
			//Load save files
			saveFiles[i].load();
		}
	}
	
	/**
	 * Method that attempts to save a file
	 * @param id (int) The id of the save file
	 * @param tileMap (TileMap) The tileMap to be saved
	 * @param objectMap (ObjectMap) The objectMap to be saved
	 * @param itemMap (TileMap) The itemMap to be saved
	 * @param hitboxMap (TileMap) The hitboxMap to be saved
	 * @param saveDate (Date) The date of the save was created
	 */
	public void save(int id, TileMap tileMap, ObjectMap objectMap, TileMap itemMap, TileMap hitboxMap, Date saveDate)
	{
		if(id > -1 && id < MAX_SAVE_FILES)
		{
			saveFiles[id].save(tileMap, objectMap, itemMap, hitboxMap, saveDate);
			currentSaveFile = saveFiles[id];
		}
	}
	
	/**
	 * Method that loads all save files into memory
	 */
	public void loadAll()
	{
		for(int i = 0; i < saveFiles.length; i++)
		{
			saveFiles[i].load();
		}
	}
	
	/**
	 * Method that loads a save file
	 * @param id (int) The id of the save file to load
	 */
	public void load(int id)
	{
		if(id > -1 && id < MAX_SAVE_FILES)
		{
			//Load a save file
			saveFiles[id].load();
			
			//Set the loaded save file as the current save file
			currentSaveFile = saveFiles[id];
		}
	}
	
	/**
	 * Method to be called to obtain SaveManager object (Singleton)
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
	public SaveFile getCurrentSaveFile() {return currentSaveFile;}
	public SaveFile[] getSaveFiles() {return saveFiles;}
	
	public SaveFile getSaveFile(int id)
	{
		if(id > -1 && id < MAX_SAVE_FILES)
		{
			return saveFiles[id];
		}
		
		return null;
	}
	
	//Setter methods
	public void setCurrentSaveFile(SaveFile currentSaveFile) {this.currentSaveFile = currentSaveFile;}
}