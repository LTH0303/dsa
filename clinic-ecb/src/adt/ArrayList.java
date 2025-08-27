package adt;

@SuppressWarnings("unchecked")
public class ArrayList<T> implements List<T> {
	private T[] elements;
	private int count;

	public ArrayList() {
		this.elements = (T[]) new Object[10];
		this.count = 0;
	}

	public ArrayList(int initialCapacity) {
		if (initialCapacity <= 0) initialCapacity = 10;
		this.elements = (T[]) new Object[initialCapacity];
		this.count = 0;
	}

	@Override
	public int size() { return count; }

	@Override
	public boolean isEmpty() { return count == 0; }

	private void ensureCapacity(int minCapacity) {
		if (minCapacity <= elements.length) return;
		int newCap = Math.max(elements.length * 2, minCapacity);
		T[] newArr = (T[]) new Object[newCap];
		for (int i = 0; i < count; i++) newArr[i] = elements[i];
		elements = newArr;
	}

	@Override
	public void add(T element) {
		ensureCapacity(count + 1);
		elements[count++] = element;
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > count) throw new IndexOutOfBoundsException();
		ensureCapacity(count + 1);
		for (int i = count; i > index; i--) elements[i] = elements[i - 1];
		elements[index] = element;
		count++;
	}

	@Override
	public T get(int index) { if (index < 0 || index >= count) throw new IndexOutOfBoundsException(); return elements[index]; }

	@Override
	public T set(int index, T element) { if (index < 0 || index >= count) throw new IndexOutOfBoundsException(); T old = elements[index]; elements[index] = element; return old; }

	@Override
	public T remove(int index) {
		if (index < 0 || index >= count) throw new IndexOutOfBoundsException();
		T old = elements[index];
		for (int i = index; i < count - 1; i++) elements[i] = elements[i + 1];
		elements[--count] = null;
		return old;
	}

	@Override
	public boolean remove(T element) {
		int idx = indexOf(element);
		if (idx >= 0) { remove(idx); return true; }
		return false;
	}

	@Override
	public int indexOf(T element) {
		if (element == null) {
			for (int i = 0; i < count; i++) if (elements[i] == null) return i;
		} else {
			for (int i = 0; i < count; i++) if (element.equals(elements[i])) return i;
		}
		return -1;
	}

	@Override
	public boolean contains(T element) { return indexOf(element) >= 0; }

	@Override
	public void clear() {
		for (int i = 0; i < count; i++) elements[i] = null;
		count = 0;
	}
}
