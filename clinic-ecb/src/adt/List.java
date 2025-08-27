package adt;

public interface List<T> {
	int size();
	boolean isEmpty();
	void add(T element);
	void add(int index, T element);
	T get(int index);
	T set(int index, T element);
	T remove(int index);
	boolean remove(T element);
	int indexOf(T element);
	boolean contains(T element);
	void clear();
}
