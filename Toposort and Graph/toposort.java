import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
public class toposort 
{
	public static void main(String[] args)
	{
		if(args.length>1)
		{
			System.out.println("too many command line arguements please try again");
			System.exit(1);
		}
		else if(args.length<1)
		{
			System.out.println("Please insert a file path");
			System.exit(1);
		}
		try
		{
			File file = new File(args[0]);
			Scanner scanner = new Scanner(file);
			System.out.println("File was made");
			int counter = 0;
			ArrayList<Edge> edges = new ArrayList<Edge>();
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				//if the line we are looking at is the opening line of the file.
				if(line.isEmpty())
					continue;
				if(line.charAt(0)==('-')) 
					continue;
				
				if(line.isEmpty())
				{
					System.out.println("that line is empty");
					continue;	
				}
				
				//Otherwise we should parse it 

				String[] tokens = line.split("\t");
				
				for(int i=0;i<tokens.length;i++)
				{
					Edge edge = new Edge(counter,Integer.parseInt(tokens[i]));
					edges.add(edge);
				}
					counter++;
			}
			scanner.close();
			
			Scanner input = new Scanner(System.in);
			System.out.println("File scanned succuessfully, would you like to view the Adjacency Matrix? Y/N");
			String userInput = input.nextLine();
			boolean printMatrix = false;
			if(userInput.equals("Y") || userInput.equals("Yes") || userInput.equals("yes") || userInput.equals("y")) 
				printMatrix = true;
			
			Digraph digraph = new Digraph(edges,printMatrix);
		}
		catch(Exception e)
		{
			System.out.println("File not found");
			e.printStackTrace();
			System.exit(0);
		}
	}
}