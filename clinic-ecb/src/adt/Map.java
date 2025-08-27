package adt;

public interface Map<K, V> {
	int size();
	boolean isEmpty();
	boolean put(K key, V value);
	V get(K key);
	boolean containsKey(K key);
	V remove(K key);
	void clear();
	Object[] keys();
}
