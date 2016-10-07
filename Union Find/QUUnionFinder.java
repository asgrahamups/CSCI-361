import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class QUUnionFinder extends UnionFinder {

	int groups;
	public QUUnionFinder(int s) {
		super(s);
		groups = s;
		//set it all to 1
		for(int i=0;i<s;i++)
			id[i] = i;
		// TODO Auto-generated constructor stub
	}
	
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
			QUUnionFinder finder = new QUUnionFinder(n);
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
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
}
	

	@Override
	public void unite(int p, int q) {
		
		int pRoot = find(p);
		int qRoot = find(q);
		if(pRoot==qRoot)
			return;
		
		id[pRoot] = qRoot;
		
		groups--;
	}

	@Override
	public int find(int p) {
		while(p!=id[p])
			p=id[p];
		return p;
	}

	@Override
	public boolean isConnected(int p, int q) {
		if(id[p]==id[q])
			return true;
		return false;
	}

	@Override
	public int countComponents() {
		return groups;
	}

}
