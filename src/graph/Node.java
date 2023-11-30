package graph;

import java.util.ArrayList;

/**
 * Node class represents a Node data
 * @author Vachia Thoj
 *
 */
public class Node 
{
	//Possible values within the Node
	private int col;	//Column
	private int row;	//Row
	private int value;	//Integer value
	
	//A list of neighbors next to this Node
	private ArrayList<Node> neighbors;
	
	//Flag to indicate if the Node has been visited
	private boolean visited;
	
	/**
	 * Constructor
	 * @param col (int) A column value
	 * @param row (int) A row value
	 * @param value (int) An integer value
	 */
	public Node(int col, int row, int value)
	{
		this.col = col;
		this.row = row;
		this.value = value;
	
		this.neighbors = new ArrayList<Node>();
		
		this.visited = false;
	}
	
	//Getter methods
	public int getCol() {return col;}
	public int getRow() {return row;}
	public int getValue() {return value;}
	public ArrayList<Node> getNeightbors() {return neighbors;}
	public boolean isVisited() {return visited;}
	
	//Setter methods
	public void setVisisted(boolean b) {this.visited = b;}
	public void addNeighbor(Node newNeighbor) {this.neighbors.add(newNeighbor);}
}