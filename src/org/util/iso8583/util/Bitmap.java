package org.util.iso8583.util;

import java.util.Arrays;
import java.util.function.Consumer;

public final class Bitmap {

	private static final long min = Long.MIN_VALUE;

	private long primap = 0;
	private long secmap = 0;

	public Bitmap() {}

	public Bitmap(final String hex) {
		this.primap = parseHexToLong(hex.substring(0, Math.min(hex.length(), 16)));
		if (hex.length() > 16) this.secmap = parseHexToLong(hex.substring(16));
		this.primap = this.primap & ~min;
	}

	public Bitmap(final long primap, final long secmap) {
		this.primap = primap;
		this.secmap = secmap;
		this.primap = this.primap & ~min;
	}

	public void init(final String hex) {
		if(hex.length() > 32) throw new RuntimeException("hex input can not be larger than 32 hex characters.");
		this.primap = parseHexToLong(hex.substring(0, Math.min(hex.length(), 16)));
		if (hex.length() > 16) this.secmap = parseHexToLong(hex.substring(16));
		this.primap = this.primap & ~min;
	}
	
	public void init(final byte[] bytes) {
		if(bytes.length > 16) throw new RuntimeException("bytes input can not be larger than 16 bytes.");
		this.primap = bytesToLong(Arrays.copyOfRange(bytes, 0, Math.min(bytes.length, 8)));
		if (bytes.length > 8) this.secmap = bytesToLong(Arrays.copyOfRange(bytes, 8, bytes.length));
		this.primap = this.primap & ~min;
	}


	public final boolean get(final int i) {
		if (i > 1 && i < 65) return (primap & (min >>> (i - 1))) == (min >>> (i - 1));
		else if (i > 64 && i < 129) return (secmap & (min >>> (i - 65))) == (min >>> (i - 65));
		else if(i == 1) return secmap != 0;
		else return false;
	}
	
	public final void set(final int i) {
		if (i > 1 && i < 65) primap |= (min >>> (i - 1));
		else if (i > 64 && i < 129) secmap |= (min >>> (i - 65));
	}
	
	public final void remove(final int i) {
		if (i > 1 && i < 65) primap &= ~(min >>> (i - 1));
		else if (i > 64 && i < 129) secmap &= ~(min >>> (i - 65));
	}

	public final boolean hasSecondary() {
		return secmap != 0;
	}

	public final String toHexString() {
		final boolean hasSec = secmap != 0;
		final String primary = String.format("%16s", Long.toUnsignedString(hasSec ? primap | min : primap, 16));
		final String secondary = hasSec ? String.format("%16s", Long.toUnsignedString(secmap, 16)) : "";
		return (primary + secondary).replaceAll(" ", "0");
	}
	
	public final byte[] toBytes() {
		final boolean hasSec = secmap != 0;
		final byte[] bitmap = new byte[hasSec ? 16 : 8];;
		System.arraycopy(longToBytes(hasSec ? primap | min : primap), 0, bitmap, 0, 8);
		if(hasSec) System.arraycopy(longToBytes(secmap), 0, bitmap, 8, 8);
		return bitmap;
	}
	
	public final void forEach(final Consumer<Integer> consumer) {
		if(secmap != 0) consumer.accept(1);
		forEachData(consumer);
	}
	
	public final void forEachData(final Consumer<Integer> consumer) {
		for (int i = 2; i < 65; i++) {
			if((primap & (min >>> (i - 1))) == (min >>> (i - 1))) consumer.accept(i);
		}
		for (int i = 65; i < 129; i++) {
			if((secmap & (min >>> (i - 65))) == (min >>> (i - 65))) consumer.accept(i);
		}
	}

	public final Bitmap clone() {
		return new Bitmap(primap, secmap);
	}

	public final boolean isEmpty() {
		return (primap == 0 && secmap == 0);
	}

	public final void clear() {
		primap = 0;
		secmap = 0;
	}
	
	public final boolean equals(final Bitmap bitmap) {
		if(bitmap == null) return false;
		if(bitmap.primap == primap && bitmap.secmap == secmap) return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "Bitmap["+toHexString()+"]";
	}
	
	private static final long parseHexToLong(final String hex) {
		long result = 0;
		for (int i = 0; i < hex.length(); i++) {
			result = (result << 4) | hexToNibble(hex.charAt(i)) ;
		}
		return result;
	}

	private static final int hexToNibble(final char ch) {
		if ('0' <= ch && ch <= '9') return ch - '0';
		else if ('A' <= ch && ch <= 'F') return ch - 'A' + 10;
		else if ('a' <= ch && ch <= 'f') return ch - 'a' + 10;
		else throw new RuntimeException("hexToNibble : invalid hexchar input : '" + ch + "'");
	}
	
	private static final long bytesToLong(final byte[] bytes) {
		long val = 0;
		for (int i = 0; i < bytes.length; i++) { 
			val = val | ((long) (bytes[i] & 0xFF) << (bytes.length-i-1) * 8);
		}
		return val;
	}
	
	private static final byte[] longToBytes(final long val) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; i++) { bytes[i] = (byte) ((val >>> ((7 - i) * 8)) & 0xFF); }
		return bytes;
	}

	public static void main(String[] args) {
		Bitmap bitmap = new Bitmap();
		bitmap.set(12);
		bitmap.set(13);
		bitmap.set(11);
		bitmap.set(128);
		
		System.out.println("bitmap bytes : "+ByteHexUtil.byteToHex(bitmap.toBytes()));
		System.out.println("bitmap   hex : "+bitmap.toHexString());
		System.out.println(parseHexToLong("7FFFFFFFFFFFFFFF"));
		System.out.println(Long.parseUnsignedLong("7FFFFFFFFFFFFFFF",16));
		System.out.println(bitmap.toHexString());
	}
}
