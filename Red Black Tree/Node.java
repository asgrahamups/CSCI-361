//2-3 Search Trees
//In case of trinary nodes(which hold two nodes in one), compare desired search with both nodes.
//When we add when we are considering adding something to a leaf, if the node we are adding to isn't a trinary node,
//Then we make it a trinary node with the 'left' node being the lesser between the existing value at that leaf and
//the value we are adding.
class Node<K,V>{// implements Comparable<K> {
	protected V value;//The object this node holds
	protected K key;//In case of a tie, use the entry priority
	protected int subTreeSize; //Used for determining rank
	protected Node<K,V> leftChild; //The node's left child (potentially non-existent)
	protected Node<K,V> rightChild; //The node's right child (potentially non-existent)
	protected boolean isRoot;
	protected boolean color;
	public static final boolean RED = true;
	public static final boolean BLACK = false;
	//protected boolean leaf;
	/*
	 * Constructor for type node.
	 * @param
	 * Ob -- the object the node takes
	 * Id -- The key it takes
	 */
	public Node (K key, V value, boolean color)
	{
		this.value = value;
		this.key = key;
		isRoot = false;
		color = BLACK; //maybe
		// leaf = true;
	}
	public Node(K key, V value, boolean color, int subSize)
	{
		this.value = value;
		this.key = key;
		this.subTreeSize = subSize;
		isRoot = false;
		color = false; //maybe
		// leaf = true;
	}
	public int size()
	{
		return subTreeSize;
	}
	public int children()
	{
		int numChildren = 0;
		if(rightChild!=null) numChildren++;
		if(leftChild!=null) numChildren++;
		return numChildren;
	}
	// public boolean isLeaf()
	// {
	// return leaf;
	// }
	//public void 
	public void setLeftChild (Node<K,V> newLeft)
	{
		leftChild = newLeft;
	}
	public void setRightChild (Node<K,V> newRight)
	{
		leftChild = newRight;
	}
	public Node<K,V> getLeftChild()
	{ 
		return leftChild;
	}
	public Node<K,V> getRightChild()
	{ 
		return rightChild;
	}
	public V getValue() 
	{
		return value;
	}
	public void setValue(V value)
	{
		this.value = value;
	}
	public K getKey()
	{
		return key;
	}
	public void setKey(K key)
	{
		this.key = key;
	}
	public int getSubTreeSize()
	{
		return this.subTreeSize;
	}
	public void setSubTreeSize(int size)
	{
		this.subTreeSize = size;
	}
	public boolean getColor()
	{
		return color;
	}
	public void setColor(boolean color)
	{
		color = color;
	}
	public void setBlack()
	{
		color = false;
	}
}