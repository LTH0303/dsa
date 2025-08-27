package adt;

@SuppressWarnings("unchecked")
public class HashMap<K, V> implements Map<K, V> {
	private static class Entry<K, V> {
		final K key; V value; Entry<K, V> next;
		Entry(K key, V value, Entry<K,V> next) { this.key = key; this.value = value; this.next = next; }
	}

	private Entry<K, V>[] table;
	private int count;

	public HashMap() { this.table = (Entry<K,V>[]) new Entry[16]; this.count = 0; }

	private int indexFor(Object key, int length) {
		int h = (key == null) ? 0 : key.hashCode();
		h ^= (h >>> 16);
		if (h < 0) h = -h;
		return h % length;
	}

	private void resizeIfNeeded() {
		if (count < table.length * 0.75) return;
		Entry<K,V>[] old = table;
		table = (Entry<K,V>[]) new Entry[old.length * 2];
		count = 0;
		for (Entry<K,V> eHead : old) {
			Entry<K,V> e = eHead;
			while (e != null) { put(e.key, e.value); e = e.next; }
		}
	}

	@Override
	public int size() { return count; }

	@Override
	public boolean isEmpty() { return count == 0; }

	@Override
	public boolean put(K key, V value) {
		resizeIfNeeded();
		int idx = indexFor(key, table.length);
		Entry<K,V> head = table[idx];
		for (Entry<K,V> e = head; e != null; e = e.next) {
			if ((key == null && e.key == null) || (key != null && key.equals(e.key))) {
				e.value = value; return false; // replaced
			}
		}
		table[idx] = new Entry<>(key, value, head);
		count++;
		return true;
	}

	@Override
	public V get(K key) {
		int idx = indexFor(key, table.length);
		for (Entry<K,V> e = table[idx]; e != null; e = e.next) {
			if ((key == null && e.key == null) || (key != null && key.equals(e.key))) return e.value;
		}
		return null;
	}

	@Override
	public boolean containsKey(K key) { return get(key) != null; }

	@Override
	public V remove(K key) {
		int idx = indexFor(key, table.length);
		Entry<K,V> prev = null;
		Entry<K,V> e = table[idx];
		while (e != null) {
			if ((key == null && e.key == null) || (key != null && key.equals(e.key))) {
				if (prev == null) table[idx] = e.next; else prev.next = e.next;
				count--;
				return e.value;
			}
			prev = e; e = e.next;
		}
		return null;
	}

	@Override
	public void clear() { for (int i = 0; i < table.length; i++) table[i] = null; count = 0; }

	@Override
	public Object[] keys() {
		Object[] arr = new Object[count];
		int p = 0;
		for (Entry<K,V> eHead : table) {
			Entry<K,V> e = eHead;
			while (e != null) { arr[p++] = e.key; e = e.next; }
		}
		return arr;
	}
}
