import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class WQUUnionFinder extends UnionFinder {

	int[] treeSize;
	int groups;

	public WQUUnionFinder(int s) {

		super(s);
		treeSize = new int[s];
		groups = s;
		for(int i=0;i<groups;i++)
			id[i] = i;
		for(int i=0;i<groups;i++)
			treeSize[i]=1;

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
			WQUUnionFinder finder = new WQUUnionFinder(n);
			
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

	@Override
	public void unite(int p, int q) {

		int pRoot = find(p);
		int qRoot = find(q);

		if(pRoot == qRoot)
			return;

		if(treeSize[pRoot]<treeSize[qRoot])
		{
			id[pRoot] = qRoot;
			treeSize[qRoot] +=treeSize[pRoot];
		}
		else
		{
			id[qRoot]=pRoot;
			treeSize[pRoot]+=treeSize[qRoot];
		}
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
