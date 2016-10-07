abstract class SymbolTable<K,V> {
    // map key to value
    abstract public void put(K key, V value);

    // return key's value
    abstract public V get(K key);

    // remove key-value pair
    abstract public void delete(K key);	

    // how many pairs?
    abstract public int size();	

    // get *all* the keys
    //abstract public Iterable<K> keys();	

    // is key present?
    public boolean contains(K key) { return (get(key) != null); }

    // is table empty?
    public boolean isEmpty() { return (size()==0); }	
}