import java.util.Scanner;

/**
 * An abstract class representing the basic functions of a union
 * finder.
 * 
 * @author- Andrew Graham
 * @version 1/27/2014
 */

abstract class UnionFinder {

	
protected boolean cmdPromptCall = false;
protected int size;
protected int[] id;
protected int[] unions;
protected int[] elements;

public static void main(String[] args)
{
	
	System.out.println("Weclome!");
	
	char[] a = (args[0].toCharArray());
	
		if(a[0]=='f' || a[0]=='F')
			QFUnionFinder.main(args);
		
		else if(a[0]=='u' || a[0]=='U')
			QUUnionFinder.main(args);
		
		else if(a[0]=='W' || a[0]=='W')
			WQUUnionFinder.main(args);
		
		else
			System.out.println("Whoops! Looks like an error happened. Check your command line arguements and try again!");
		
}

public void setElements(int[] elements) {
	this.elements = elements;
}

public void setSize(int size) {
	this.size = size;
}

public UnionFinder(int s)
{
	size = s;
	id = new int[size];
	elements = new int[size];
	unions = new int[size];
}

public abstract void unite(int p, int q);//Puts p and q into the same group
public abstract int find(int p); //looks up a component based on it's id
public abstract boolean isConnected(int p, int q);//hecks to see if two values are connected
public abstract int countComponents(); //returns the number of groups

public int getSize(){
	return size;
}

public int[] getId() {
	return id;
}

public void setId(int[] id) {
	this.id = id;
}

public int[] getUnions() {
	return unions;
}

public void setUnions(int[] unions) {
	this.unions = unions;
}

public int[] getElements() {
	return elements;
}

}
