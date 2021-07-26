package org.util.iso8583.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@formatter:off
public final class ByteHexUtil {

	/*
	 * converts a character to a nibble
	 */
	public static final int hexToNibble(final char ch) {
		if ('0' <= ch && ch <= '9') return ch - '0';
		else if ('A' <= ch && ch <= 'F') return ch - 'A' + 10;
		else if ('a' <= ch && ch <= 'f') return ch - 'a' + 10;
		else throw new RuntimeException("hexToNibble : invalid hexchar input : '" + ch + "'");
	}

	private static final String   validHexChars              = "0123456789ABCDEFabcdef";
	private static final byte[]   emptyByteArray             = new byte[0];
	private static final int[]    hexCharIndexRelativeToZero = getHexCharsRelativeToZero(); //basically 'A'- '0'
	private static final byte[][] hexPairByteIndex           = getHexPairByteIndices();
	private static final char[][] byteToHexPair              = byteToHexPair();
	private static final char[][] byteToHexPairUpper         = byteToHexPairUpper();

	private static final char[][] byteToHexPair() {
		final char[][] temp = new char[256][2];
		for (int i = 0; i < 256; i++) {
			temp[i][0] = nibbleToHexChar((i & 0xF0) >> 4);
			temp[i][1] = nibbleToHexChar((i & 0x0F));
		}
		return temp;
	}
	
	private static final char[][] byteToHexPairUpper() {
		final char[][] temp = new char[256][2];
		for (int i = 0; i < 256; i++) {
			temp[i][0] = Character.toUpperCase(nibbleToHexChar((i & 0xF0) >> 4));
			temp[i][1] = Character.toUpperCase(nibbleToHexChar((i & 0x0F)));
		}
		return temp;
	}

	private static final int[] getHexCharsRelativeToZero() {
		final char[] hexChars = validHexChars.toCharArray();
		final int[]  temp     = new int[55];
		//default to -1 for non hex char gaps in ASCII scheme
		Arrays.fill(temp, -1);
		//fill hexCharIndexRelativeToZero with indexes relative to character zero
		for (int i = 0; i < hexChars.length; i++) {
			temp[hexChars[i] - '0'] = i; //
		}
		return temp;
	}

	private static final byte[][] getHexPairByteIndices() {
		//fill hexPairIndex with byte representing two hex chars relative to hexCharIndexRelativeToZero
		final char[]   hexChars = validHexChars.toCharArray();
		final byte[][] temp     = new byte[22][22];
		for (int i = 0; i < hexChars.length; i++) {
			for (int j = 0; j < hexChars.length; j++) {
				final int indexOfI = hexCharIndexRelativeToZero[hexChars[i] - '0'];
				final int indexOfJ = hexCharIndexRelativeToZero[hexChars[j] - '0'];
				temp[indexOfI][indexOfJ] = (byte) (hexToNibble(hexChars[i]) << 4 | hexToNibble(hexChars[j]));
			}
		}
		return temp;
	}

	public static final boolean isValidHexChar(final char ch) {
		if (('0' <= ch && ch <= '9') || ('A' <= ch && ch <= 'F') || 'a' <= ch && ch <= 'f') return true;
		return false;
	}

	public static final char nibbleToHexChar(final int nb) {
		if (nb >= 0 && nb < 10) return (char) (nb + '0');
		if (nb >= 10 && nb < 16) return (char) (nb - 10 + 'a');
		throw new RuntimeException("nibbleToHexChar : invalid nibble input : " + nb);
	}

	/*
	 * converts character pair to a unsigned int(cast to make a signed byte)
	 */
	public static final int hexPairToByte(final char c1, final char c2) {
		return hexToNibble(c1) << 4 | hexToNibble(c2);
	}

	//converts even length hex string to byte array
	public static final byte[] hexToByte(final char[] chars) {
		if (chars == null) throw new NullPointerException("hexToByte : hex input is null.");
		if (chars.length % 2 == 1) throw new RuntimeException("hexToByte : hex input of odd length.");
		final byte[] bytes = new byte[chars.length / 2];
		int i = 0;
		try {
			while (i < chars.length) bytes[i / 2] = hexPairByteIndex[hexCharIndexRelativeToZero[chars[i++] - '0']][hexCharIndexRelativeToZero[chars[i++] - '0']];
		} catch (Exception e) {
			throw new RuntimeException("hexToByte : invalid hex input '" + chars[i - 1] + "' at index " + (i - 1));
		}
		return bytes;
	}

	public static final byte[] hexToByte(final String string) {
		if (string == null) throw new NullPointerException("hexToByte : hex input is null.");
		if (string.length() % 2 == 1) throw new RuntimeException("hexToByte : hex input of odd length.");
		return hexToByte(string.toCharArray());
	}

	public static final byte[] hexToBytePadLeft(final String string, final char pad) {
		if (string == null) throw new NullPointerException("hexToBytePadLeft : hex input is null.");
		if (!isValidHexChar(pad)) throw new RuntimeException("hexToBytePadLeft : invalid pad hex char input : '" + pad + "'");
		final int len = string.length();
		if (len == 0) return emptyByteArray;
		char[] chars = null;
		if (len % 2 == 1) {
			chars    = new char[len + 1];
			chars[0] = pad;
			string.getChars(0, len, chars, 1);
		} else chars = string.toCharArray();
		return hexToByte(chars);
	}

	public static final byte[] hexToBytePadRight(final String string, final char pad) {
		if (string == null) throw new NullPointerException("hexToBytePadRight : hex input is null.");
		if (!isValidHexChar(pad)) throw new RuntimeException("hexToBytePadRight : invalid pad hex char input : '" + pad + "'");
		final int len = string.length();
		if (len == 0) return emptyByteArray;
		char[] chars = null;
		if (len % 2 == 1) {
			chars      = new char[len + 1];
			chars[len] = pad;
			string.getChars(0, len, chars, 0);
		} else chars = string.toCharArray();
		return hexToByte(chars);
	}

	public static final String byteToHex(final byte bite) {
		return new String(byteToHexPair[bite & 0xFF]);
	}
	
	public static final String byteToHex(final byte[] bytes) {
		if (bytes == null) throw new NullPointerException("byteToHex : byte array input is null.");
		final char[] sb = new char[bytes.length << 1];
		int c  = 0;
		for (final byte b : bytes) {
			sb[c++] = byteToHexPair[b & 0xFF][0];
			sb[c++] = byteToHexPair[b & 0xFF][1];
		}
		return new String(sb);
	}

	public static final String byteToHexUpperCase(final byte[] bytes) {
		if (bytes == null) throw new NullPointerException("byteToHex : byte array input is null.");
		final char[] sb = new char[bytes.length << 1];
		int c  = 0;
		for (final byte b : bytes) {
			sb[c++] = byteToHexPairUpper[b & 0xFF][0];
			sb[c++] = byteToHexPairUpper[b & 0xFF][1];
		}
		return new String(sb);
	}
	
	public static final byte[] xor(final byte[] b1, final byte[] b2) {
		if(b1 == null || b2 == null) throw new NullPointerException("xor : one of byte array is null.");
		if(b1.length != b2.length) new RuntimeException("xor : byte arrays with different length provided.");
		final byte[] xored = new byte[b1.length];
		for (int i = 0; i < b1.length; i++) xored[i] = (byte) (b1[i] ^ b2[i]);
		return xored;
	}
	
	public static final String xor(final String hex1, final String hex2) {
		if(hex1 == null || hex2 == null) throw new NullPointerException("xor : one of byte array is null.");
		if(hex1.length() != hex2.length()) new RuntimeException("xor : byte arrays with different length provided.");
		return byteToHex(xor(hexToByte(hex1), hexToByte(hex2)));
	}
	
	public static final byte[] and(final byte[] b1, final byte[] b2) {
		if(b1 == null || b2 == null) throw new NullPointerException("and : one of byte array is null.");
		if(b1.length != b2.length) new RuntimeException("and : byte arrays with different length provided.");
		final byte[] anded = new byte[b1.length < b2.length ? b1.length : b2.length];
		for (int i = 0; i < b1.length; i++) anded[i] = (byte) (b1[i] & b2[i]);
		return anded;
	}

	public static final String and(final String hex1, final String hex2) {
		if(hex1 == null || hex2 == null) throw new NullPointerException("and : one of byte array is null.");
		if(hex1.length() != hex2.length()) new RuntimeException("and : byte arrays with different length provided.");
		return byteToHex(and(hexToByte(hex1), hexToByte(hex2)));
	}
	
	public static final byte[] or(final byte[] b1, final byte[] b2) {
		if(b1 == null || b2 == null) throw new NullPointerException("or : one of byte array is null.");
		if(b1.length != b2.length) new RuntimeException("or : byte arrays with different length provided.");
		final byte[] ored = new byte[b1.length < b2.length ? b1.length : b2.length];
		for (int i = 0; i < b1.length; i++) ored[i] = (byte) (b1[i] | b2[i]);
		return ored;
	}
	
	public static final String or(final String hex1, final String hex2) {
		if(hex1 == null || hex2 == null) throw new NullPointerException("or : one of byte array is null.");
		if(hex1.length() != hex2.length()) new RuntimeException("or : byte arrays with different length provided.");
		return byteToHex(or(hexToByte(hex1), hexToByte(hex2)));
	}
	
	public static final byte[] padRight(final byte[] bytes, final byte padByte, final int padedLen) {
		if (bytes == null) throw new NullPointerException("padRight : input byte array is null.");
		if (bytes.length >= padedLen) throw new RuntimeException("padRight : input byte array length is greater than expected output length.");
		final byte[] narray = Arrays.copyOf(bytes, padedLen);
		for (int i = bytes.length; i < narray.length; i++) { narray[i] = padByte; }
		return narray;
	}
	
	public static final byte[] padLeft(final byte[] bytes, final byte padByte, final int padedLen) {
		if (bytes == null) throw new NullPointerException("padRight : input byte array is null.");
		if (bytes.length >= padedLen) throw new RuntimeException("padRight : input byte array length is greater than expected output length.");
		final byte[] narray = new byte[padedLen];
		int i = 0;
		for (i = 0; i < padedLen - bytes.length; i++) { narray[i] = padByte; }
		System.arraycopy(bytes, 0, narray, i, bytes.length);
		return narray;
	}
	
	public static final byte[] concat(final byte[] b1, final byte[] b2) {
		if(b1 == null || b2 == null) throw new NullPointerException("concat : one of byte array is null.");
		final byte[] dest = new byte[b1.length + b2.length]; 
		System.arraycopy(b1, 0, dest, 0, b1.length);
		System.arraycopy(b2, 0, dest, b1.length, b2.length);
		return dest;
	}

	public static final byte[] concat(final byte[] arr1, final int arr1Pos, final int arr1Len, final byte[] arr2, final int arr2Pos, final int arr2Len) {
		if(arr1 == null || arr2 == null) throw new NullPointerException("concat : one of array is null.");
		if(arr1.length < arr1Pos + arr1Len) throw new RuntimeException("concat : arr1 array length is less than arr1Pos + arr1Len.");
		if(arr2.length < arr2Pos + arr2Len) throw new RuntimeException("concat : arr2 array length is less than arr2Pos + arr2Len).");
		byte[] dest = new byte[arr1Len + arr2Len];
		System.arraycopy(arr1, arr1Pos, dest, 0, arr1Len);
		System.arraycopy(arr2, arr2Pos, dest, arr1Len, arr2Len);
		return dest;
	}

	public static final byte[] reverse(final byte[] bytes) {
		final byte[] reverse = new byte[bytes.length];
		for (int i = 0; i < reverse.length; i++) {
			reverse[i] = bytes[bytes.length-(i+1)];
		}
		return reverse;
	}
	
	public static final byte[][] split(final byte[] array, final byte split) {
		if (array == null) throw new RuntimeException("byte array can not be null for split.");
		if(array.length == 0) return new byte[0][0];
		final List<Integer> indexes = new ArrayList<>();
		for (int i = 0; i < array.length; i++) if (split == array[i]) indexes.add(i);
		final List<byte[]> splitList = new ArrayList<>();
		int      start = 0;
		for (int i = 0; i < indexes.size(); i++) {
			splitList.add(Arrays.copyOfRange(array, start, indexes.get(i)));
			start = indexes.get(i) + 1;
		}
		splitList.add(Arrays.copyOfRange(array, start, array.length));
		return (byte[][]) splitList.toArray(new byte[0][0]);
	}
	
	public static void main(String[] args) {
		System.err.println(byteToHex(reverse(new byte[] {0,1,2,3,4})));

	}

}
