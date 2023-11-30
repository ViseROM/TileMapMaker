package graph;

import java.util.ArrayList;

/**
 * FillPath class
 * @author Vachia Thoj
 */
public class FillPath 
{
	//A Graph data structure containing Nodes
	private Graph graph;
	
	//Data to be used to find the starting Node within the Graph
	private int col;
	private int row;
	
	//A queue to store all Nodes in our graph that we want to visit
	private Queue queue;
	
	//A list of the Nodes that we want to "fill"
	private ArrayList<Node> nodesToFill;
	
	/**
	 * Constructor
	 * @param graph (Graph) A Graph object
	 * @param col (int) The starting column
	 * @param row (int) The starting row
	 */
	public FillPath(Graph graph, int col, int row)
	{
		this.graph = graph;
		this.col = col;
		this.row = row;
		
		this.queue = new Queue();
		
		this.nodesToFill = new ArrayList<Node>();
	}
	
	public ArrayList<Node> getNodesToFill() {return nodesToFill;}
	
	/**
	 * Method that performs breadth first search to find all
	 * the Nodes within our Graph that we want to fill
	 */
	public void bfs()
	{
		//Obtain the starting Node
		Node startNode = graph.findNode(col, row);
		
		if(startNode == null)
		{
			return;
		}
		
		//Obtain the start Node's value data
		int value = startNode.getValue();
		
		//Visit the starting Node
		startNode.setVisisted(true);
		
		queue.enqueue(startNode);
		
		//While the queue is not empty
		while(!queue.isEmpty())
		{
			//Dequeue Node from queue
			Node currentNode = queue.dequeue();
			
			//Add this Node to the list of Nodes we have to "fill"
			nodesToFill.add(currentNode);
			
			//Obtain the Node's neighbors
			ArrayList<Node> neighbors = currentNode.getNeightbors();
			
			//Go through all of currentNode's neighbors
			for(int i = 0; i < neighbors.size(); i++)
			{
				//If we have not visited a neighbor of currentNode...
				if(!neighbors.get(i).isVisited())
				{
					//Visit that neighbor
					neighbors.get(i).setVisisted(true);
					
					//If that neighbor has the value data we want...
					if(neighbors.get(i).getValue() == value)
					{
						//Add that neighbor to the queue
						//So that we can check the neighbors of that Node later in the loop
						queue.enqueue(neighbors.get(i));
					}
				}
			}
		}
	}
}
