package graph;

import java.util.ArrayList;

import entity.Tile;
import map.TileMap;

/**
 * Graph class represents a graph data structure
 * @author Vachia Thoj
 */
public class Graph
{
	private TileMap tileMap;
	private ArrayList<Node> nodeList;
	
	/**
	 * Constructor
	 * @param tileMap (TileMap) A TileMap object; to be turned into a graph data structure
	 */
	public Graph(TileMap tileMap)
	{
		this.tileMap = tileMap;
		this.nodeList = new ArrayList<Node>();
		
		createNodeList();
	}
	
	private void createNodeList()
	{
		//A temporary list (array) for the Nodes
		Node[][] tempNodeList = new Node[tileMap.getNumRows()][tileMap.getNumCols()];
		
		//Obtain the map from the TileMap object
		Tile [][] map = tileMap.getTileMap();
		
		//Obtain the locations (columns and rows) on the map where we need to start and end
		int startCol = tileMap.getStartCol();
		int endCol = tileMap.getEndCol();
		int startRow = tileMap.getStartRow();
		int endRow = tileMap.getEndRow();
		
		//Create the Nodes
		for(int i = startRow; i <= endRow; i++)
		{
			for(int j = startCol; j <= endCol; j++)
			{
				//Create a Node and add it the tempNodeList
				Node newNode = new Node(j, i, map[i][j].getValue());
				tempNodeList[i][j] = newNode;
			}
		}
		
		//Add (find) each Node's neighbors
		for(int i = startRow; i <= endRow; i++)
		{
			for(int j = startCol; j <= endCol; j++)
			{				
				//Check for left neighbor
				if(j - 1 >= startCol)
				{
					tempNodeList[i][j].addNeighbor(tempNodeList[i][j - 1]);
				}
				
				//Check for right neighbor
				if(j + 1 <= endCol)
				{
					tempNodeList[i][j].addNeighbor(tempNodeList[i][j + 1]);
				}
				
				//Check for up neighbor
				if(i - 1 >= startRow)
				{
					tempNodeList[i][j].addNeighbor(tempNodeList[i - 1][j]);
				}
				
				//Check for down neighbor
				if(i + 1 <= endRow)
				{
					tempNodeList[i][j].addNeighbor(tempNodeList[i + 1][j]);
				}
			}
		}
		
		//Add the Nodes to nodeList
		for(int i = startRow; i <= endRow; i++)
		{
			for(int j = startCol; j <= endCol; j++)
			{
				nodeList.add(tempNodeList[i][j]);
			}
		}
	}
	
	/**
	 * Method that finds a Node within the Graph
	 * @param col (int) The column value for a Node
	 * @param row (int) The row value for a Node
	 * @return a Node object if Node was found, otherwise returns null
	 */
	public Node findNode(int col, int row)
	{
		for(int i = 0; i < nodeList.size(); i++)
		{
			if(nodeList.get(i).getCol() == col && nodeList.get(i).getRow() == row)
			{
				return nodeList.get(i);
			}
		}
		return null;
	}
	
	//Getter methods
	public ArrayList<Node> getNodeList() {return nodeList;}
}
