import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class wordfreq2 
{

	public static void main(String[] args)
	{
		if(args.length>1 || args.length==0)
		{
			System.out.println("Invalid number of command line arguements, please just input a filepath");
			System.exit(1);
		}

		RBTree<String, Integer> tree = new RBTree<String, Integer>();

		try
		{
			File file = new File(args[0]);
			Scanner scanner = new Scanner(file);
			double startTime = System.currentTimeMillis();

			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				String noPunct = line.replaceAll("[^a-zA-Z]"," "); //Code taken from http://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
				noPunct = noPunct.toLowerCase();
				String[] tokens = noPunct.split(" "); //split line into areas of spaces.
				noPunct = noPunct.toLowerCase();
				for(int i=0;i<tokens.length;i++)
				{
					Integer value = tree.get(tokens[i]);

					if(value==null)//if it isn't already there
						tree.put(tokens[i],1);

					else//if we've seen it before we have to increment our counter.
						tree.put(tokens[i], value.intValue()+1);

				}
			}
			double endTime = System.currentTimeMillis();
			System.out.println("It took  " + (endTime-startTime) + " Milliseconds to make this tree.");
			System.out.println("There are " + tree.size() + " distinct words in your text");

			System.out.println("If you wish, please type in a word you would like to know the frequency of.");
			System.out.println("Or, if you have a particular distate for a word, proceed the word with a - to delete it from the tree");
			scanner.close();//Close the file parsing scanner

			Scanner sc = new Scanner(System.in);

			try
			{
				while(sc.hasNextLine())
				{
					String input = sc.nextLine();
					input = input.toLowerCase();

					if(input.length()==0)
					{
						System.out.println("Thank you for using this word frequency program. Have a wonderful day.");
						System.exit(0);
					}

					if(input.charAt(0)=='-')
					{
						String noPunct = input.replaceAll("[^a-zA-Z]","");
						tree.delete(noPunct);
						System.out.println(noPunct + " Has been removed from the tree");
					}

					else if(input.charAt(0)=='<' && input.length() == 1)
					{
						System.out.println("The first alphabetic word in this text is " + tree.getMinKey());
					}
					else if(input.charAt(0)=='<')
					{
						String noPunct = input.replaceAll("[^a-zA-Z]","");
						System.out.println("The word that comes before " + noPunct + " is " + tree.findPredecessor(noPunct));
					}
					else if(input.charAt(0)=='>' && input.length() == 1)
					{
						System.out.println("The last alphabetic word in this text is " + tree.getMaxKey());
					}
					else if(input.charAt(0)=='>')
					{
						String noPunct = input.replaceAll("[^a-zA-Z]","");
						System.out.println("The word that comes after " + noPunct + " is " + tree.findSuccessor(noPunct));
					}

					else
					{
						if(!(tree.get(input)==null))
							System.out.println("The word " + input + " appears " + tree.get(input) + " times");
						else
							System.out.println("The word " + input + " does not appear in the given text, or you removed it.");
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("An error occurded");	
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found. Please try a different filepath");
			System.exit(1);
		}
	}

}
