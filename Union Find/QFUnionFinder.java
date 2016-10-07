import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;


	class QFUnionFinder extends UnionFinder {
		
	//you have access to the inherited abstract fields size and groupId
	protected int groups;
	
	public QFUnionFinder(int s) {
		super(s);

		groups = size;
		
		for(int i=0;i<size;i++)//initialize id values so every entry has a group
			id[i] = i; 
	}

	
	/*
	 * Main method, takes command line argument
	 * for a file to load.
	 * Start timer
	 * Create data structure
	 * Calculate distinct groups after pairing
	 * Calculate number of groups
	 */
	public static void main(String args[])
	{
		try {
			
			String toUse = "";
			double a = System.currentTimeMillis();
			
			if(args[0].contains("Finder")||args[0].contains("finder"))//then we are dealing with cmd call from this class
				toUse = args[0];
			else
				toUse=args[1];
			
			File file = new File(toUse);
			Scanner sc = new Scanner(file);
			
			int n = sc.nextInt();
			QFUnionFinder finder = new QFUnionFinder(n);
			
			while(sc.hasNext())
			{
				int p = sc.nextInt();
				int q = sc.nextInt();
				if(finder.isConnected(p, q)) continue;
				finder.unite(p, q);
			}
			
			System.out.println("The number of groups is: " + finder.countComponents());
			System.out.println("It took: " + ((double)System.currentTimeMillis()-a) + "milliseconds");
			
			System.out.println("Input any two numbers in the list to see if they are connected");

			int p=0;
			int q=0;
			
			Scanner reader = new Scanner(System.in);
			
			while(reader.hasNext())
			{	
				boolean valid= false;
				while(!valid)
				{
					valid = true;
					
					p = reader.nextInt();
					q = reader.nextInt();
					
					if(p>n||q>n||p<0||q<0)
					{
						System.out.println("Invalid input");
						valid = false;
					}
				}
				
					
				System.out.println(finder.isConnected(p, q));
			}
				
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}

	public int getGroups() {
		return groups;
	}


	public void setGroups(int groups) {
		this.groups = groups;
	}


	@Override
	public void unite(int p, int q)
	{
		int idOne = find(p);
		int idTwo = find(q);
		
		if(idOne==idTwo)
			return;

		for(int i=0;i<id.length;i++)
			if(id[i] == idOne)
				id[i] = idTwo;
		
		groups--;
	}

	@Override
	public int find(int p)
	{
		return id[p];
	}

	@Override
	public boolean isConnected(int p, int q)
	{	
		return find(p)==find(q);
	}

	@Override
	public int countComponents() 
	{
		return groups;
	}
}
