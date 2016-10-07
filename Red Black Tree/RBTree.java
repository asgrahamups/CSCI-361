/**
 * Implementation of a standard Red Black Tree. 
 * @author Andrew
 *
 * @param <K> keys
 * @param <V> values
 */
class RBTree<K extends Comparable<K>, V> extends OrderedSymbolTable<K,V> {

	protected static final boolean BLACK = false;
	protected static final boolean RED = true;
	protected Node<K,V> root;
	protected int size;

	/**
	 * Rotates right. helper method used to maintain black balance
	 * @param a-the parent node
	 * b-the child node
	 */
	private Node<K,V> rotateRight(Node<K,V> h)
	{
		System.out.println("Rotate right");
		Node<K,V> x = h.leftChild;
		h.leftChild = x.rightChild;
		x.rightChild = h;
		x.setColor(h.getColor()); //x.color = h.color;
		h.setColor(RED);//h.color = RED;
		x.setSubTreeSize(h.subTreeSize);//x.N = h.N;
		h.setSubTreeSize(1+h.leftChild.subTreeSize+h.rightChild.subTreeSize);//h.N = 1 + size(h.left)+ size(h.right);
		return x;
	}
	/**
	 * Rotates left. helper method used to maintain black balance
	 */
	private Node<K,V> rotateLeft(Node<K,V> h)
	{
		System.out.println("Rotate left");
		Node<K,V> x = h.rightChild;
		h.setRightChild(x.leftChild); //h.right = x.left;
		x.setLeftChild(h); //x.left = h;
		x.setColor(h.color); //x.color = h.color;
		h.setColor(RED); //h.color = RED;
		x.setSubTreeSize(h.subTreeSize);//x.N = h.N;
		h.setSubTreeSize(1+h.leftChild.subTreeSize+h.rightChild.subTreeSize);//h.N = 1 + size(h.left) + size(h.right);
		return x;
	} 
	/**
	 * Flips the color of the node with the color of it's children. This method
	 * is used for maintaining black balance.
	 * @param node
	 */
	private void colorFlip(Node<K,V> node)
	{
		node.color = RED;
		if(node.leftChild!=null)
			node.leftChild.color = BLACK;
		if(node.rightChild!=null)
			node.rightChild.color = BLACK;

	}

	/**
	 * Gets an item at a specific key from the Black-Red Tree
	 * @param the key to get from
	 */
	public int size() 
	{
		return size;
	}

	@Override
	public void put(K key, V value) {
		root =put(root,key,value);
		root.color = BLACK;	
	}
	
	private Node<K,V> put(Node<K,V> h, K key, V val)
	{
		if (h == null) // Do standard insert, with red link to parent.
		{
			size++;
			return new Node<K,V>(key, val, RED, 1);
		}

		int cmp = key.compareTo(h.key);
		if (cmp < 0)
		{
			h.subTreeSize++;
			h.leftChild = put(h.leftChild, key, val);
		}
		else if (cmp > 0)
		{
			h.subTreeSize++;
			h.rightChild = put(h.rightChild, key, val);
		}
		else
		{
			h.value = val;
		}
		if (isRed(h.rightChild) && !isRed(h.leftChild)) h = rotateLeft(h);
		if (isRed(h.leftChild) && isRed(h.leftChild.leftChild)) h = rotateRight(h);
		if (isRed(h.leftChild) && isRed(h.rightChild)) colorFlip(h);
		//h.subTreeSize = h.leftChild.subTreeSize + h.rightChild.subTreeSize+1;
		return h;
	} 
	/**
	 * 
	 * @param node
	 * @return
	 */
	private boolean isRed(Node<K,V> node)
	{
		if(node == null)
			return false;

		if(node.getColor()==RED)
			return true;

		return false;
	}
	/**
	 * Gets a value from the tree
	 * @param the key to look for in the tree
	 */
	public V get(K key)
	{
		Node<K,V> use = root;

		while(use!=null)
		{
			int cmp = key.compareTo(use.key);
			if(cmp>0)
			{
				use = use.rightChild;
			}
			else if(cmp<0)
			{
				use = use.leftChild;
			}
			else
			{
				return use.value;
			}
		}
		return null;
	}
	/**
	 * Helper method used to get a node at a specific location
	 * @param key
	 * @return
	 */
	private Node<K,V> getNode(K key)
	{
		Node<K,V> use = root;

		while(use!=null)
		{
			int cmp = key.compareTo(use.key);
			if(cmp>0)
			{
				use = use.rightChild;
			}
			else if(cmp<0)
			{
				use = use.leftChild;
			}
			else
			{
				return use;
			}
		}
		System.out.println("Failed to find");
		return null;
	}

	/**
	 * Recursive helper method which gets the smallest node
	 * @param node
	 * @return
	 */
	private Node<K,V> getMin (Node<K,V> node)
	{
		if(node.leftChild!=null)
			getMin(node.leftChild);

		//		else if(node.rightChild!=null)
		//			getMin(node.rightChild);

		return node;
	}

	public K getMinKey() 
	{
		Node<K,V> use = root;
		System.out.println(use.key);
		while(use!=null)
		{
			
			if(use.leftChild!=null)
				use = use.leftChild;
			
			if(use.leftChild==null)
				return use.key;
		}
		if (use == root)
			return root.key;
		
		return null;
	}

	private Node<K,V> getMax(Node<K,V> node)
	{
		if(node.rightChild!=null)
		{
			System.out.println(node.key);
			getMin(node.rightChild);
		}

		return node;
	}
	public K getMaxKey() 
	{
		return getMax(root).key;
	}

	/**
	 * Finds the predecessor of a given key
	 * @param the key to find
	 * @return the key of the predecessor
	 */
	public K findPredecessor(K key) 
	{
		Node<K,V> node = getNode(key);

		if (node.leftChild==null)
			return null;

		node = node.leftChild; //start looking at the left child

		while(node.rightChild!=null)
			node = node.rightChild;

		return node.key;

	}
	//Successor is the least node with a value greater than the current node's key
	public K findSuccessor(K key) 
	{
		Node<K,V> node = getNode(key);

		if (node.rightChild == null)
			return null;

		node = node.rightChild;

		while(node.leftChild!=null)
			node = node.leftChild;

		return node.key;
	}

	public int findRank(K key) {
		Node<K,V> use = root;
		int count = 0;
		while(use!=null)
		{
			int cmp = key.compareTo(use.key);
			if(cmp>0)
			{
				count+= use.subTreeSize;
				use = use.rightChild;
			}
			else if(cmp<0)
			{
				use = use.leftChild;
			}
			else
			{
				return count;
			}
		}
		return 0;
	}

	public K select(int rank) {
		if (rank > size)
			return null;
		Node<K,V> use = root;
		while(use!=null)
		{
			if(rank<use.subTreeSize)
			{
				use = use.leftChild;
			}
			if(rank>use.subTreeSize)
			{
				rank-=use.leftChild.subTreeSize;
			}
			else
			{
				return use.key;
			}
		}
		return null;
	}

	/**
	 * Calls the recursive method for deleting the smallest element in the tree
	 */
	public void deleteMin() 
	{
		if (!isRed(root.leftChild) && !isRed(root.rightChild)) //push redness down from the root
			root.color = RED;

		root = deleteMin(root);
		if (!isEmpty())
			root.color = BLACK; 
	}

	/**
	 * Deletes the smallest element in the tree
	 * @param node
	 * @return
	 */
	private Node<K,V> deleteMin(Node<K,V> node) 
	{ 
		if (node.leftChild == null)
			return null;

		if (!isRed(node.leftChild) && !isRed(node.leftChild.leftChild))
			node = moveRedLeft(node);

		node.leftChild = deleteMin(node.leftChild);
		return balance(node);
	}



	/**
	 * Recursively calls deleteMax with the parameter for the delete at the root
	 */
	public void deleteMax() 
	{
		if (!isRed(root.leftChild) && !isRed(root.rightChild)) //push redness down from the root
			root.color = RED;

		root = deleteMax(root);
		if (!isEmpty()) 
			root.color = BLACK;   
	}

	/**
	 * Deletes the largest key in the tree
	 * @param node
	 * @return
	 */
	private Node<K,V> deleteMax(Node<K,V> node) { 
		if (isRed(node.leftChild))
			node = rotateRight(node);

		if (node.rightChild == null)
			return null;

		if (!isRed(node.rightChild) && !isRed(node.rightChild.leftChild))
			node = moveRedRight(node);

		node.rightChild = deleteMax(node.rightChild);
		return balance(node);
	}

	/**
	 * Deletes a K-V Pair from the tree
	 * @param key - the key to delete
	 */
	public void delete(K key) { 

		if (!isRed(root.leftChild) && !isRed(root.rightChild)) //push redness down from the root
			root.color = RED;

		root = delete(root, key);
		if (!isEmpty())
			root.color = BLACK;

	}

	private Node<K,V> delete(Node<K,V> node, K key) 
	{ 

		if (key.compareTo(node.key) < 0)  
		{
			if (!isRed(node.leftChild) && !isRed(node.leftChild.leftChild))
				node = moveRedLeft(node);

			node.leftChild = delete(node.leftChild, key);
		}

		else
		{
			//rotate to push redness down
			if (isRed(node.leftChild))
				node = rotateRight(node);

			if (key.compareTo(node.key) == 0 && (node.rightChild == null))
				return null;

			//move redness into the proper position so we can go to it
			if (!isRed(node.rightChild) && !isRed(node.rightChild.leftChild))
				node = moveRedRight(node);


			if (key.compareTo(node.key) == 0)
			{
				Node<K,V> temp = getMin(node.rightChild);
				node.key = temp.key;
				node.value = temp.value;
				node.rightChild = deleteMin(node.rightChild);
			}
			//if we weren't successful we need to do the operation again
			else 
				node.rightChild = delete(node.rightChild, key);
		}
		return balance(node);
	}




	/**
	 * Restores the properties of the RB tree
	 * @param node
	 * @return
	 */
	private Node<K,V> balance(Node<K,V> node) 
	{
		//Having a red child on the right side violates RB tree rules
		if (isRed(node.rightChild))
			node = rotateLeft(node);
		//A red child with a red child violates RB tree rules
		if (isRed(node.leftChild) && isRed(node.leftChild.leftChild))
			node = rotateRight(node);

		//If we have two red children we need to colorflip
		if (isRed(node.leftChild) && isRed(node.rightChild))
			colorFlip(node);

		//Adjust size
		node.subTreeSize = node.leftChild.subTreeSize + node.rightChild.subTreeSize + 1;
		return node;
	}

	/**
	 * Helper method used to move redness left when traversing down the tree
	 *  
	 * @param Node the node we want to push the redness to the right of
	 * @return the node after colors have been changed
	 * @param node
	 * @return
	 */
	private Node<K,V> moveRedLeft(Node<K,V> node) 
	{
		colorFlip(node);
		if (isRed(node.rightChild.leftChild)) 
		{ 
			node.rightChild = rotateRight(node.rightChild);
			node = rotateLeft(node);
		}
		return node;
	}

	/**
	 * Helper method used to move redness right when traversing down the tree
	 * @param Node the node we want to push the redness to the right of
	 * @return the node after colors have been changed
	 * @param node
	 * @return
	 */
	private Node<K,V> moveRedRight(Node<K,V> node)
	{
		colorFlip(node);
		if (isRed(node.leftChild.leftChild)) 
		{ 
			node = rotateRight(node);
		}
		return node;
	}
}
