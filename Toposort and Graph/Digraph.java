import java.util.ArrayList;

public class Digraph 
{
	
	private ArrayList<Edge> digraph; //The underlying data structure holds edges!
	private ArrayList<Edge> reverseDigraph; //Storing this so we don't have to calculate it a bunch of times!
	private int[] vertices; //All unique vertices!
	private int numVertices; //The number of vertices! Used to allocate space for arrays in several methods!
	private ArrayList<ArrayList<Integer>> strongComponents;
	private ArrayList<ArrayList<Integer>> adjacencyList;
	private ArrayList<ArrayList<Integer>> reverseAdjList;
	public int[] postfix;
	public int[] reversepostfix;
	protected boolean printAdjacencyList = false;
	protected boolean printRevList = false;
	
	//add an adjacency list
	/**
	 * Constructor for types of digraph.
	 */
	public Digraph(ArrayList<Edge> edges,boolean printMatrix)
	{
		digraph = edges; //digraph is the collection of edges
		vertices = getVertices(); //A collection of all vertices
		numVertices = vertices.length; //the number of unique vertices
		adjacencyList = adjacencyList(edges); //create the adjacency list
		reverseDigraph = revDigraph(digraph); //Creates the reverse digraph.
		reverseAdjList = adjacencyList(reverseDigraph);
		postfix = sortTopologically(); //stores the postfix order
		printAdjacencyList = printMatrix;

		
		/*
		 * Below is all the code associated with reporting the data of the calculations.
		 */

		if(printAdjacencyList)
		{
			System.out.println("The graph you have entered has the following connections amongst it's vertices ");
			
			//Prints all adjacent vertices
			int counter = 0;
			for(ArrayList<Integer> adjacent: adjacencyList)
			{
				System.out.print(counter + "'s rev adj list: ");
				for(Integer vertex : adjacent)
					System.out.print(vertex + " ");
				
				counter++;
				System.out.println();
			}
		}	
		if(printRevList)
		{
			int revcounter = 0;
			for(ArrayList<Integer> adjacent: reverseAdjList)
			{
				System.out.print(revcounter + "'s adj list: ");
				for(Integer vertex : adjacent)
					System.out.print(vertex + " ");
				
				revcounter++;
				System.out.println();
			}
		}
		
		//Prints T/F for presence of cycles
		System.out.print("Cycle(s): ");
		if(isDirectedCycle())
			System.out.print("Yes");
		else
			System.out.print("No");
		
		System.out.println();
		
		//Prints the postfix order
		System.out.println("Postfix Order: ");
		for(int i=0;i<postfix.length;i++)
		{
			if(i+1!=postfix.length)
				System.out.print(postfix[i] + ",");
			else
				System.out.println(postfix[i]);
		}
		
		reversepostfix = reverseTopoSort(); //stores the reverse postfix order
		System.out.println("Reverse-Postfix Order: ");
		for(int i=0;i<reversepostfix.length;i++)
		{
			if(i+1!=reversepostfix.length)
				System.out.print(reversepostfix[i] + ",");
			else
				System.out.println(reversepostfix[i]);
		}
		
		strongComponents = new ArrayList<ArrayList<Integer>>(); //holds strong components
			
		if(isDirectedCycle())
		{
			System.out.println("Directed Cycle detected. Finding strong components");
			psudoTopologically(); //call to place strong components inside the strongComponents field
			System.out.println("Strong components identified. There are " + strongComponents.size() + " strong components present in this graph.");
			for(int i=0;i<strongComponents.size();i++)
			{
				System.out.print("Strong component " + i + ": ");
				for(Integer vertex : strongComponents.get(i))
					System.out.print(vertex + " ");
				System.out.println();
			}
		}
	}
	
	/**
	 * Forms an adjacency list from a collection of edges.
	 * @return an adjacency list
	 */
	public ArrayList<ArrayList<Integer>> adjacencyList(ArrayList<Edge> edges)
	{
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		
		/*
		 * Creates an ArrayList of type Integer for each vertex, then goes
		 * through a collection of edges to determine all adjacent vertices.
		 */
		for(int i=0;i<numVertices;i++)
		{
			ArrayList<Integer> toAdd = new ArrayList<Integer>(); //add a new list to hold the entries in
			
			for(Edge edge: edges)
				if(edge.getVertexOne()==i)
				{
					if(toAdd.contains(edge.getVertexTwo())) continue; //if we've seen that edge before
					toAdd.add(edge.getVertexTwo());
				}
			temp.add(toAdd);
		}
		return temp;
	}
	/**
	 * Returns a list of all unique vertices in the digraph.
	 * @return
	 */
	private int[] getVertices()
	{
		ArrayList<Integer> seen = new ArrayList<Integer>();
		for(Edge edge: digraph)
		{
			//get the vertices of the edge
			int vertexOne = edge.getVertexOne(); 
			int vertexTwo = edge.getVertexTwo();
			//add them if they aren't already added
			if(!(seen.contains(vertexOne)))
				seen.add(vertexOne);
			if(!(seen.contains(vertexTwo)))
				seen.add(vertexTwo);
		}

		//return the list of unique vertices
		int[] array = new int[seen.size()];
		for(int i=0;i<seen.size();i++)
			array[i]=seen.get(i);
		return array;
	}
	/**
	 * Detects a cycle in a digraph
	 * @return true if there's a cycle, false if there is not
	 */
	public boolean isDirectedCycle() 
	{
		// allocation
		boolean[] marked = new boolean[numVertices];
		boolean[] onStack = new boolean[numVertices];
		// look for cycles, 1 component at a time
		for (int v : getVertices()) {
			if (marked[v]) continue;
			if (doCycleFindingDFT(v, marked, onStack))
				return true;
		}
		// if no cycles, return false
		return false;
	}
	/**
	 * Recursive helper for finding a cycle.
	 * @param vertex
	 * @param marked
	 * @param onStack
	 * @return
	 */
	private boolean doCycleFindingDFT(int vertex, boolean[] marked, boolean[] onStack) 
	{
		marked[vertex] = true; // mark the element off & add it to the stack
		onStack[vertex] = true; // see if any neighbors are on the stack, explore
		for (int v : getAdjacencyList(vertex)) 
		{
			if (onStack[v]) return true;
			if (!marked[v]) {
				if (doCycleFindingDFT(v, marked, onStack))
					return true;
			}
		}
		// if we get here, we only found dead ends
		onStack[vertex] = false;
		return false;
	}
	/**
	 * Sorts a graph topologically. You wrote this!
	 * @return a topologically sorted list of vertices in the digraph.
	 */
	public int[] sortTopologically() 
	{
		// allocation
		boolean[] marked = new boolean[numVertices];
		int[] list = new int[numVertices];
		int[] index = new int[1];
		index[0] = list.length-1;
		// traverse
		for (int v : vertices) {
			if (marked[v])continue;
			doTopoSortDFT(v, marked, list, index);
			// return the list
		}
		return list;
	}

	/**
	 * Recursive helper to call a DFT on each vertex! You wrote this!
	 * @param vertex The active vetex we are exploring
	 * @param marked A list of vertices we have already explored
	 * @param list The list from the traversal
	 * @param index Change all stuffs inside a single method call!
	 */
	private void doTopoSortDFT(int vertex, boolean[] marked, int[] list, int[] index) 
	{
		// mark the element
		marked[vertex] = true;
		// explore
		for (int v : getAdjacencyList(vertex)) {
			if (!marked[v]) {
				doTopoSortDFT( v, marked, list, index);
			}
		}
		// at the end, add this vertex to the list
		list[index[0]] = vertex;
		index[0]--;
	}
	/**
	 * Arranges the vertices in the graph into their strong components by using
	 * A DFT of each element in the Reverse Graph's reverse postfix order.
	 * @return an arraylist of strong components
	 * 
	 * DATASTUCTURE CHOICE NOTE: I am using an arraylist because we really don't know
	 * how many strong components there will be, so it's hard to give a size to an array
	 * to do such things. 
	 */
	public void psudoTopologically()
	{
		//An arraylist to hold all the strong components
		ArrayList<ArrayList<Integer>> strongComps = new ArrayList<ArrayList<Integer>>();
		boolean[] marked = new boolean[numVertices];
		
		int[] reverseTopoOrder = reverseTopoSort();
		
		for(int v:reverseTopoOrder)
		{
			if(marked[v]) continue;
			//Otherwise we need to do a DFS on that element to identify the strong group
			ArrayList<Integer> list = new ArrayList<Integer>();
			doDFT(v,marked,list);
			strongComps.add(list);
		}
		strongComponents = strongComps;
	}
	/**
	 * Performs a DFT.
	 * @param vertex
	 * @param marked
	 * @param list
	 */
	public void doDFT(int vertex, boolean[] marked,ArrayList<Integer> list) 
	{
		marked[vertex] = true;

		ArrayList<Integer> adjacents = getAdjacencyList(vertex);
		
		for (Integer v : adjacents) 
			if (!marked[v])
			{
				list.add(v);
				doDFT(v, marked,list);
			}
	}
	
	/**
	 * Performs a topological sort on the reversed graph. Used to find strong groups
	 * In graphs with cycles.
	 * @return
	 */
	public int[] reverseTopoSort()
	{
		// allocation
		boolean[] marked = new boolean[numVertices];
		int[] list = new int[numVertices];
		int[] index = new int[1];
		index[0] = list.length-1;
		// traverse
		for (int v : vertices) {
			if (marked[v])continue;
			doReverseTopoSortDFT(v, marked, list, index);
			// return the list
		}
		return list;
	}
	
	/**
	 * Recursive helper to call a DFT on each vertex in the reversed graph! You wrote most of this! I just reversed the graph!
	 * @param vertex The active vetex we are exploring
	 * @param marked A list of vertices we have already explored
	 * @param list The list from the traversal
	 * @param index Change all stuffs inside a single method call!
	 */
	private void doReverseTopoSortDFT(int vertex, boolean[] marked, int[] list, int[] index) 
	{
		// mark the element
		marked[vertex] = true;
		// explore
		for (int v : getReverseAdjacencyList(vertex)) {
			if (!marked[v]) {
				doTopoSortDFT( v, marked, list, index);
			}
		}
		// at the end, add this vertex to the list
		list[index[0]] = vertex;
		index[0]--;
	}
	/**
	 * Reverses the edges in a digraph.
	 * @param list The list of edges to reverse
	 * @return a list of reversed edges
	 */
	private static ArrayList<Edge> revDigraph (ArrayList<Edge> list)
	{
		ArrayList<Edge> toReturn = new ArrayList<Edge>();

		for(int i=0;i<list.size();i++)
		{
			int vertexOne = list.get(i).getVertexOne();
			int vertexTwo = list.get(i).getVertexTwo();
			toReturn.add(new Edge(vertexTwo,vertexOne));
		}
		return toReturn;
	}

	/**
	 * Gets all adjacent vertices to a given vertex in a digraph
	 * @param vertex the vertex to which other vertices might be adjacent to
	 * @return the list of all adjacent vertices
	 */
	private ArrayList<Integer> getReverseAdjacencyList(int vertex)
	{
		return reverseAdjList.get(vertex);
	}	
	/**
	 * Gets all adjacent vertices to a given vertex in a digraph
	 * @param vertex the vertex to which other vertices might be adjacent to
	 * @return the list of all adjacent vertices
	 */
	private ArrayList<Integer> getAdjacencyList(int vertex)
	{
		return adjacencyList.get(vertex);
	}

	public ArrayList<Edge> getDigraph() {
		return digraph;
	}
	public void setDigraph(ArrayList<Edge> digraph) {
		this.digraph = digraph;
	}
	public ArrayList<Edge> getReverseDigraph() {
		return reverseDigraph;
	}
	public void setReverseDigraph(ArrayList<Edge> reverseDigraph) {
		this.reverseDigraph = reverseDigraph;
	}
	public int getNumVertices() {
		return numVertices;
	}
	public void setNumVertices(int numVertices) {
		this.numVertices = numVertices;
	}
	public ArrayList<ArrayList<Integer>> getStrongComponents() {
		return strongComponents;
	}
	public void setStrongComponents(ArrayList<ArrayList<Integer>> strongComponents) {
		this.strongComponents = strongComponents;
	}
	public void setVertices(int[] vertices) {
		this.vertices = vertices;
	}
}