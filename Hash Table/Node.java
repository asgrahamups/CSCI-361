
class Node {

	protected Object value;//The object this node holds
	protected Object key;//In case of a tie, use the entry priority

	/*
	 * Constructor for type node.
	 * @param
	 * Ob -- the object the node takes
	 * Id -- The key it takes
	 */
	public Node(Object value, Object key)
	{
		this.value = value;
		this.key = key;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value){
		this.value = value;
	}

	public Object getKey()
	{
		return key;
	}
	public void setKey(Object key)
	{
		this.key = key;
	}
}
