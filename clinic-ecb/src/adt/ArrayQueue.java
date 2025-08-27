package adt;

@SuppressWarnings("unchecked")
public class ArrayQueue<T> implements Queue<T> {
	private T[] elements;
	private int head;
	private int tail;
	private int count;

	public ArrayQueue() {
		this.elements = (T[]) new Object[16];
		this.head = 0;
		this.tail = 0;
		this.count = 0;
	}

	private void ensureCapacity() {
		if (count < elements.length) return;
		int newCap = elements.length * 2;
		T[] newArr = (T[]) new Object[newCap];
		for (int i = 0; i < count; i++) newArr[i] = elements[(head + i) % elements.length];
		elements = newArr;
		head = 0; tail = count;
	}

	@Override
	public int size() { return count; }

	@Override
	public boolean isEmpty() { return count == 0; }

	@Override
	public void enqueue(T element) {
		ensureCapacity();
		elements[tail] = element;
		tail = (tail + 1) % elements.length;
		count++;
	}

	@Override
	public T dequeue() {
		if (count == 0) return null;
		T val = elements[head];
		elements[head] = null;
		head = (head + 1) % elements.length;
		count--;
		return val;
	}

	@Override
	public T peek() { return count == 0 ? null : elements[head]; }

	@Override
	public void clear() {
		for (int i = 0; i < elements.length; i++) elements[i] = null;
		head = 0; tail = 0; count = 0;
	}
}
