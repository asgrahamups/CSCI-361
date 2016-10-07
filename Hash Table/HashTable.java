/**
 * A simple Hash Table. Utilizes a resizing matrix for use of chaining and
 * an array for use of linear probing.
 * 
 * @author Andrew
 * 
 * @param <K>
 *            The type used for keys
 * @param <V>
 *            The values associated with those keys
 */

class HashTable<K, V> extends SymbolTable<K,V> {

	protected Object[] keys; //An array of objects that represents keys when using linear probing.
	protected Node[][] chainingValues; //A matrix which holds the values when using chaining.
	protected int reallocateSize; //The resizing factor for the field 'values'.
	protected int reallocateChainSize; //The resizing factor for row growth in the 'chainingValues' field.
	protected int numElems; //The number of elements when using chaining
	
	/**
	 * Constructor for types Hash Table.
	 * Sets all default values.
	 */
	public HashTable() 
	{
		numElems = 0;
		keys = new Object[97];
		chainingValues = new Node[137][97];
		reallocateSize = 100;
		reallocateChainSize = 10;
	}

	/**
	 * Places an item into the Hash Table. Uses chaining by default, but can also perform linear probing.
	 * 
	 * @param key
	 *            The key to which the value is bound
	 * @param value
	 *            The value to place into the HashTable
	 */
	public void put(Object key, Object value) 
	{
		int hashKey = key.hashCode();//our key to store inside the node
		hashKey = hashKey & 0x7FFFFFFF;//make sure no negative indexes
		int index = hashKey % chainingValues.length;//our index into the array
		
		Node insert = new Node(value,key);//Make a new node to insert
		
			for (int j = 0; j < chainingValues[index].length; j++) //go through each available space in the row
			{
				if (chainingValues[index][j]==null)//if we find a space
				{
					chainingValues[index][j] = insert;//put it there
					numElems++;
					return; //method complete cached the hash
				}
				if(chainingValues[index][j].getKey().equals(key))//key
				{
					chainingValues[index][j].setValue(value);
					return;
				}
			}
			
			growMatrixRow(index); //expand the row because there were no empty spaces
			
			for(int i=0;i<chainingValues[index].length;i++)
			{
				if(chainingValues[index][i]==null)//maybe we need to be checking for matches first
					{
						chainingValues[index][i] = insert;
						numElems++;
						return;
					}
					if(chainingValues[index][i].getKey().equals(key))
					{
						chainingValues[index][i].setValue(value);
						numElems++;
						return;
					}
			}
			return;
		}

	/**
	 * Gets an object from the hash table using a provided key
	 * @param Object key
	 * 		  The object which represents the key
	 */
	@SuppressWarnings("unchecked")
	public V get(Object key) 
	{
		int hashKey = key.hashCode(); //our key we use to make sure we get the right object
		hashKey = hashKey & 2147483647;
	
		int index = hashKey % chainingValues.length; //the index we will look in inside our array

			for(int i=0;i<chainingValues[index].length;i++)//look for the object by array number.
			{
				if(!(chainingValues[index][i]==null))
					if(chainingValues[index][i].getKey().equals(key))
						return (V) chainingValues[index][i].getValue();
			}
		return null;
	}
	
	/**
	 * Grows the given row of the hash table in case we fill up one of its rows
	 * @param i 
	 * 		The row of the hash table to grow
	 * @return
	 * 		A new array of objects with increased length
	 */
	
	private void growMatrixRow(int i)
	{
		Node[] resized = new Node[chainingValues[i].length+reallocateChainSize];
		
		for(int j=0;j<chainingValues[i].length;j++)
			resized[j]=chainingValues[i][j];
		
		chainingValues[i] = resized;
	}

	/**
	 * Removes an item from the hash table
	 */
	public void delete(Object key) 
	{
		put(key, null);
		numElems--;
	}

	public int size() {
		return numElems;
	}
}
