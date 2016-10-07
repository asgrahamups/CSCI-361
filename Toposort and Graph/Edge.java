/**
 * An edge class to represent a connection between two vertices in a graph.
 * The graph is actually just a list of edges!
 * @author Andrew
 *
 */
public class Edge
{
	private int vertexOne; //The first vertex!
	private int vertexTwo; //The second vertex!
	public Edge(int v1, int v2)
	{
		vertexOne = v1;
		vertexTwo = v2;
	}
	public int getVertexOne()
	{
		return vertexOne;
	}
	public int getVertexTwo()
	{
		return vertexTwo;// either vertex
	}
	public void setVertexOne(int newVertex)
	{
		vertexOne = newVertex;
	}
	public void setVertexTwo(int newVertex)
	{
		vertexTwo = newVertex;
	}
}