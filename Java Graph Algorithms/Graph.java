import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/**
 * A class that represents an unweighted, undirected graph.
 * Has a working depth first search, but an incomplete breadth first search.
 * @author Andrew
 *
 */
class Graph {
	
	private ArrayList<Vertex> closedSet; 
	private PriorityQueue<Vertex> openSet;
	
	protected TreeMap<String, Vertex> treemap; //a treemap to go from strings to vertecies
	protected HashMap<Integer, Vertex> breadthMap;
	protected ArrayList<Vertex> verticies;
	protected int numEdges;
	protected int numVerts;
	protected int numDepthSteps;
	protected int numBreadthSteps;

	
	Graph() 
	{
		numEdges = 0;
		numVerts = 0;
		closedSet = new ArrayList<Vertex>();
		openSet = new PriorityQueue<Vertex>();
		verticies = new ArrayList<Vertex>();
		treemap = new TreeMap<String, Vertex>();
		breadthMap = new HashMap<>();
		numBreadthSteps = 0;
		//closedList = new HashMap<Integer,Integer>();
	}
	
	public boolean contains(String vertex)
	{
		return !(treemap.get(vertex)==null);
	}
	
	/**
	 * Returns the currnet number of active vertices
	 * @return
	 */
	public int countVertices()
	{
		return numVerts;
	}
	
	
	
	/**
	 * Uses a depth first search to explore the nodes,then prints the path it took
	 * @param vertex
	 * @return
	 */
	public void depthSearch(String theStartt, String end)
	{
		
		Vertex theStart = treemap.get(theStartt);
		Vertex en = treemap.get(end);
		
		if(theStart==null || en==null)
		{
			System.out.println("One of those verticies does not exist");
			return;
		}
		
		Stack<Vertex> path = new Stack<Vertex>();
		
		if(depthHelper(theStart,en,path))
		{
			System.out.print(theStartt);
			System.out.println("");
		}
		else
			System.out.println("There is no path between those two verticies");
		
		
		closedSet.clear();
		
	}
	
	/**
	 * A depth first search for a path between two vertices.
	 * @param theStartt
	 * @param end
	 * @param stack
	 * @return
	 */
	private boolean depthHelper(Vertex theStartt, Vertex end,Stack<Vertex> stack)
	{
		if(theStartt == end) //base case for recursive call
			return true;
		
		ArrayList<Vertex> adjacent = theStartt.getAdjacentVerticies(); //get all adjacent edges
		
		
		closedSet.add(theStartt);
		
		for(Vertex vertex : adjacent) //For each vertex in the adjacency list 
		{
			
			if(closedSet.contains(vertex)) continue; //if we've already seen the vertex
			
			stack.push(vertex); //push it onto the stack
			//hash active to previous
			if(depthHelper(vertex,end,stack)) //if can solve the problem with this path
			{
				while(!stack.empty())  //print out the path we took
				{
					System.out.print(stack.pop().key + "<----");
					numDepthSteps++;
				}
				return true;				
			}
			
			else
				stack.clear();
		}
		
		return false;
	}
	
	public void breadthSearch(String start, String end)
	{
		Vertex theStart = treemap.get(start); //get the start
		Vertex theEnd = treemap.get(end); //get the end
		
		if(theStart==null || theEnd==null) //if either one doesn't exist as a vertex
		{
			System.out.println("One of those verticies does not exist"); 
			return; //We're done
		}
		
		ArrayList<Vertex> openList = new ArrayList<>(); //an open list to store verticies
		HashMap<Integer,Integer> closedList = new HashMap<>(); //a hashmap to backtrack from to find an optimal path
		/*
		 * These two vertices represent the active id we are looking at
		 * And the previous id (where the active one came from)
		 */
		Vertex previous = null; //Previous does not exist yet 
		Vertex active = theStart;// Active is initially where we start
		
		openList.add(theStart);//Add the start to the open list(so we can enter the loop)
		int counter = 0;
		while(!openList.isEmpty()) //!openList.isEmpty())
		{	
			
			active = openList.remove(0); //remove head from the open list
			int current = active.id;  
			
			if(active.id == theEnd.id) //if we found the guy we are looking for
			{
				if(previous == null)
					closedList.put(current,active.id); //if destination and place are the same
				
				closedList.put(current, previous.id); //the key to the previous one is our current
				int endNum = theEnd.id;
				
				while(endNum!=theStart.id)//while we haven't back tracked enough
				{	
					String toPrint = breadthMap.get(endNum).key; //the string to print out
					endNum = closedList.get(endNum); 
					numBreadthSteps++;//increment the number of times we stepped
					if(endNum == theStart.id)
						System.out.print(toPrint +"<---");
					else
						System.out.print(toPrint + "<---");
				}
				System.out.println(theStart.key);
				
				return;
			}
			
			for(Vertex vertex: active.getAdjacentVerticies()) // add all adjacent vertexes to the open list
			{
				if(openList.contains(vertex) || closedList.containsValue(vertex) || vertex ==active)//if we have seen that node before
					continue; //go on to the next guy
				
				openList.add(vertex); //add vertex to the openList
			}
			
			if(previous == null)//if it is our first pass though
				closedList.put(active.id,active.id); //root id maps to itself in the map
			
			else
				closedList.put(current, previous.id);//otherwise our active maps to whatever came before it
		
			previous = active; //Keep track of who we came from
		}
	}

	int countEdges()
	{
		return numEdges;
	}
	public void resetSteps()
	{
		numBreadthSteps = 0;
		numDepthSteps = 0;
	}
	/**
	 * Updates the relationship arrays of both verticies
	 * @param v1
	 * @param v2
	 */
	public void addEdge(String v1, String v2)	// add edge
	{	
		
		Vertex one = treemap.get(v1);
		Vertex two = treemap.get(v2);
		
		if(!one.getAdjacentVerticies().contains(v2))
		{
			one.getAdjacentVerticies().add(two); //add v2 to the relationship list of v1
			two.getAdjacentVerticies().add(one); //add v1 to the relationship list of v2
		}
		numEdges++;
		//System.out.println("The adjacentcy list of " + one.key + " is: " + concatonateArrayList(treemap.get(v1).getAdjacentVerticies())); 
	}
	
	public String[] getVerticies()
	{
		 ArrayList<String> toReturn = new ArrayList<String>();
		 for(Vertex vertex: verticies)
			 toReturn.add(vertex.key);
		 
		 String[] a = new String[toReturn.size()];
		 for(int i=0;i<a.length;i++)
			 a[i] = toReturn.get(i);
		 
		 return a;
	}
	public void addVertex(int v1, String key)
	{
		
		Vertex vertex = new Vertex(v1,key);
		treemap.put(key,vertex);
		vertex = treemap.get(vertex.key);
		vertex.id = numVerts;
		this.breadthMap.put(vertex.id, vertex);
		numVerts++;
		verticies.add(vertex);
		
		System.out.println("Adding vertex: " + vertex.key + " with value: " + v1 + "and ID" + vertex.id);
	}
	
	public Vertex getVertexById(int id)
	{
		return breadthMap.get(id);
	}
	
	private Vertex getVertex(String key)
	{
		return treemap.get(key);
	}
	/**
	 * Removes the connection between two edges
	 * @param v1
	 * @param v2
	 */
	public void deleteEdge(Vertex v1, Vertex v2)
	{
		if(areAdjacent(v1,v2))
		{
			treemap.get(v1).getAdjacentVerticies().remove(v2);
			treemap.get(v2).getAdjacentVerticies().remove(v1);
		}
	}
	
	public boolean areAdjacent(Vertex v1, Vertex v2)
	{ 
	 return v1.getAdjacentVerticies().contains(v2);
	}
	
	public ArrayList<Vertex> getAdjacencyList(int vertex)
	{
		ArrayList<Vertex> list = treemap.get(vertex).getAdjacentVerticies();
		return list;
	}
	

	private class Vertex
	{
		protected ArrayList<Vertex> adjacentVerticies;
		protected int id;
		protected String key;

		public Vertex(int id,String key)
		{
			this.key = key;
			this.id = id;
			adjacentVerticies = new ArrayList<Vertex>();
		}
		public Vertex(int id, ArrayList<Vertex> adjList)
		{
			this.id = id;
			adjacentVerticies = adjList;
		}
		public ArrayList<Vertex> getAdjacentVerticies()
		{
			return adjacentVerticies;
		}
	}
}