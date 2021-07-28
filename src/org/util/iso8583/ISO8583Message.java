package org.util.iso8583;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.util.iso8583.api.ISO8583Function;
import org.util.iso8583.api.ISO8583Type;
import org.util.iso8583.util.Bitmap;

public final class ISO8583Message {

	protected final Bitmap              bitmap;
	protected final String[]            data;
	protected final Map<String, Object> extra = new HashMap<>();

	public ISO8583Message() {
		bitmap = new Bitmap();
		data   = new String[130];
	}
	
	private ISO8583Message(final String[] data, final Bitmap bitmap) {
		this.bitmap = bitmap;
		this.data   = data;
	}

	public final String get(final int i) {
		return data[i];
	}

	public final String getNetHeader() {
		return data[129];
	}

	public final void put(final int i, final String s) {
		if (s == null) return;
		data[i] = s;
		bitmap.set(i);
	}

	public final void put(final int i, final ISO8583Type isoType) {
		if (isoType == null) return;
		data[i] = isoType.toISOString();
		if (data[i] != null) bitmap.set(i);
	}

	public final void putNetHeader(final String s) {
		if (s == null) return;
		data[129] = s;
	}

	public final String remove(int i) {
		bitmap.remove(i);
		final String temp = data[i];
		data[i] = null;
		return temp;
	}

	public final void removeAll(final int... i) {
		for (int j = 0; j < i.length; j++) {
			bitmap.remove(i[j]);
			data[i[j]] = null;
		}
	}

	public final void removeAll(final Set<Integer> indexSet) {
		for (Integer index : indexSet) {
			bitmap.remove(index);
			data[index] = null;
		}
	}

	public final Object getExtra(final String key) {
		return extra.get(key);
	}

	@SuppressWarnings("unchecked")
	public final <T> T getExtra(final Class<T> classz, final String key) {
		return (T) extra.get(key);
	}

	public final void putExtra(final String key, final Object value) {
		if (key == null) return;
		extra.put(key, value);
	}

	public final boolean containsExtra(final String key) {
		return extra.containsKey(key);
	}

	public final <R> R apply(final ISO8583Function<R> function) {
		return function.apply(data, bitmap, extra);
	}

	public final void clear() {
		if (!bitmap.isEmpty()) { bitmap.setLong(0, 0); }
		extra.clear();
		Arrays.fill(data, null);
	}

	public final boolean isEmpty() {
		return bitmap.isEmpty();
	}

	public final boolean isRequest() {
		return (data[0].charAt(2) & 0x01) == 0;
	}

	public final boolean isResponse() {
		return (data[0].charAt(2) & 0x01) == 1;
	}

	public final void forEach(final BiConsumer<Integer, String> consumer) {
		consumer.accept(0, data[0]);
		data[1] = bitmap.toHexString();
		consumer.accept(1, data[1]);
		for (int i = 2; i < data.length; i++) { if (bitmap.get(i)) consumer.accept(i, data[i]); }
	}

	public final ISO8583Message clone() {
		final ISO8583Message message = new ISO8583Message(this.data.clone(), this.bitmap.clone());
		ISO8583MessageHelper.setCopyFlag(message);
		message.extra.putAll(this.extra);
		return message;
	}

	public final ISO8583Message clone(final Set<Integer> indexSet) {
		final ISO8583Message message = clone();
		for (Integer i : indexSet) {
			message.remove(i);
		}
		return message;
	}

	public final ISO8583Message clone(final Set<Integer> indexSet, final Set<String> propertySet) {
		final ISO8583Message message = new ISO8583Message();
		for (Integer i : indexSet) {
			message.put(i, data[i]);
		}
		for (String k : propertySet) {
			message.putExtra(k, extra.get(k));
		}
		return message;
	}

	public final Map<Integer, String> toMap() {
		final HashMap<Integer, String> map = new HashMap<>();
		if (data[0] != null) map.put(0, data[0]);
		for (int i = 2; i < data.length; i++) {if (bitmap.get(i)) map.put(i, data[i]); }
		return map;
	}

	public final boolean equals(final ISO8583Message message) {
		for (int i = 0; i < data.length; i++) {
			if (bitmap.get(i) && !data[i].equals(message.data[i])) return false;
		}
		return true;
	}
}
