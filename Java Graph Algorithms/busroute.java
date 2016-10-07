import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;


class busroute {
	
	public static void main(String[] args)
	{
		if (args.length == 0 || args.length > 1)  //if we have an invalid number of arguments
		{
			System.out.println("Invalid command line arguements. Exiting!");
			System.exit(1);
		}
		
		Graph graph = new Graph();
		
		try 
		{
			File file = new File(args[0]);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				String[] tokens = line.split(" "); //split based on space
				if(!graph.contains(tokens[0]))
					graph.addVertex(0, tokens[0]);
				
				if(!graph.contains(tokens[1]))
					graph.addVertex(0, tokens[1]);
				
				graph.addEdge(tokens[0], tokens[1]);
			}
			scanner.close();
			
			Scanner io = new Scanner(System.in);
			
			System.out.println("The graph has been calculated.");
			
			System.out.println("Please enter two cities you'd like to find a path between.");
			System.out.println("Hit enter with an empty prompt to exit.");
			System.out.println("For a list of stations, type *stations");
			
			
			while(io.hasNextLine())
			{
				String line = io.nextLine();
				String[] tokens = line.split(" ");
				if(line.equals(""))
				{
					io.close(); //open scanners bad
					System.out.println("Thank you for using Andrew Graham bus routes.");
					System.exit(0); //donezo
				}
				
				else if(line.equals("*stations"))//If they are requesting a station list
				{
					String[] list = graph.getVerticies();
					Arrays.sort(list);
					Arrays.sort(graph.getVerticies());
					System.out.println("A comprehensive list of all stations is: ");
					for(int i=0;i<list.length;i++)
						System.out.println(list[i]);
				}
				else if(tokens.length==2) //they are requesting a route
				{
					if(graph.contains(tokens[0]) && graph.contains(tokens[1]))
					{
						System.out.println("Path using depth first is: ");
						graph.depthSearch(tokens[0], tokens[1]);
						System.out.println("It took " + graph.numDepthSteps + " steps");
						System.out.println("Path using breadth first is: ");
						graph.breadthSearch(tokens[0], tokens[1]);
						System.out.println("It took " + graph.numBreadthSteps + " steps");
						graph.resetSteps();
					}
					else
					{
						System.out.println("One of those vertecies does not exist, please try again.");
					}
				}
				else
				{
					System.out.println("Incorrect input, please use proper format");
				}
			}
		} 
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
		}
		
		
		
	}
}
