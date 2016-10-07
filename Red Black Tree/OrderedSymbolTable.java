// This abstract class defines an ordered symbol table, such as a
// binary search tree. It is pretty close to being an interface, save
// for a couple of simple methods near the end.
abstract class OrderedSymbolTable<K extends Comparable<K>,V> {

abstract public void put(K key, V value); // map key to value

abstract public V get(K key);// return key's value

abstract public void delete(K key);// remove key-value pair

abstract public int size();// how many pairs?

abstract public K getMinKey();// what's the first key?

abstract public K getMaxKey();// what's the last key?

abstract public K findPredecessor(K key);// what key comes before this one? (return null if it's the first)

abstract public K findSuccessor(K key);// what key comes after this one? (return null if it's the last)

abstract public int findRank(K key);// how many keys come before this one?

abstract public K select(int rank);// return the key with rank keys before it.

public boolean contains(K key) { return (get(key) != null); }// is key present?

public boolean isEmpty() { return (size()==0); } // is table empty?
}