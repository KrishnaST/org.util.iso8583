package org.util.iso8583.util;

import java.util.Arrays;

public final class PackedBCD {

	private static final byte[] decToBCD = decToBcdUpTo99();
	private static final int[] bcdToDec = bcdToDecUpToFF();
	private static final char[][] bcdToCharPair = bcdToCharPair();
	
	private static final byte[] decToBcdUpTo99() {
		final byte[] decToBCD= new byte[100];
		for (int i = 0; i < 100; i++) {
			decToBCD[i] = (byte) ((i/10 << 4) | (i % 10));
		}
		return decToBCD;
	}
	
	private static final int[] bcdToDecUpToFF() {
		final int[] bcdToDec= new int[(0x99 & 0xFF) + 1];
		Arrays.fill(bcdToDec, -1);
		for (int i = 0; i < 100; i++) {
			bcdToDec[decToBCD[i] & 0xFF] = i;
		}
		return bcdToDec;
	}
	
	private static final char[][] bcdToCharPair() {
		final char[][] bcdToDec= new char[256][2];
		for (int i = 0; i < 256; i++) {
			bcdToDec[i] = null;
		}
		for (int i = 0; i < 100; i++) {
			bcdToDec[decToBCD[i] & 0xFF] = String.format("%02d", i).toCharArray();
		}
		for (int i = 0; i < 256; i++) {
			//System.out.println(i+" : "+(bcdToDec[i] == null ? null : new String(bcdToDec[i])));
		}
		return bcdToDec;
	}
	
	
	public static final byte[] toBCD(long dec) {
		if(dec < 0) throw new RuntimeException("input "+dec+" is negative for BCD conversion.");
		final byte[] bytes = new byte[calcPackedBCDSize(dec)];
		for (int i = bytes.length-1; i >= 0; i--) {
			bytes[i] = decToBCD[(int) (dec % 100)];
			dec = dec / 100;
		}
		return bytes;
	}
	
	public static final byte[] toBCD(long dec, int outlen) {
		if(dec < 0) throw new RuntimeException("input "+dec+" is negative for BCD conversion.");
		final int calcLen = calcPackedBCDSize(dec);
		if(outlen < calcLen) throw new RuntimeException("outlen "+outlen+" is smaller than computed required length "+calcLen);
		final byte[] bytes = new byte[outlen];
		for (int i = bytes.length-1; i >= 0; i--) {
			bytes[i] = decToBCD[(int) (dec % 100)];
			dec = dec / 100;
		}
		return bytes;
	}
	
	//'0123456789'
	public static final byte[] toBCD(final char[] chars) {
		if(chars.length == 0) throw new RuntimeException("zero length char array input for bcd conversion.");
		if(chars.length % 2 == 1) throw new RuntimeException("odd length char array input for bcd conversion : "+new String(chars));
		final int len = chars.length;
		final byte[] bytes = new byte[len/2];
		for (int i = len-1; i >= 0; i--) {
			final int a = chars[i] - '0';
			final int b = chars[--i] - '0';
			if(a < 0 || a > 9 || b < 0 || b > 9) throw new RuntimeException("invalid character in char array at "+(i+1)+"+ in "+new String(chars));
			bytes[i/2] = (byte) (a | b << 4);
		}
		return bytes;
	}
	
	public static final byte[] toBCDPadLeft(final char[] chars, final char pad) {
		if(chars.length == 0) throw new RuntimeException("zero length  input for bcd conversion.");
		if(pad < '0' || pad > '9') throw new RuntimeException("invalid pad char : "+pad);
		final int len = chars.length;
		int i = len-1;
		final byte[] bytes = new byte[(len+1)/2];
		for (; i >= len % 2; i--) {
			final int a = chars[i] - '0';
			final int b = chars[--i] - '0';
			if(a < 0 || a > 9 || b < 0 || b > 9) throw new RuntimeException("invalid character in char array at "+(i+1)+"+ in "+new String(chars));
			bytes[(i+1)/2] = (byte) (a | b << 4);
		}
		if(i == 0) {
			final int a = chars[i] - '0';
			final int b = pad - '0';
			if(a < 0 || a > 9) throw new RuntimeException("invalid character in char array at "+(i+1)+"+ in "+new String(chars));
			bytes[0] = (byte) (a | b << 4);
		}
		return bytes;
	}
	
	public static final byte[] toBCDPadRight(final char[] chars, final char pad) {
		if(chars.length == 0) throw new RuntimeException("zero length  input for bcd conversion.");
		if(pad < '0' || pad > '9') throw new RuntimeException("invalid pad char '"+pad+"' for bcd conversion.");
		final int len = chars.length;
		int i = len-1;
		final byte[] bytes = new byte[(len+1)/2];
		if(i % 2 == 0) {
			final int a = pad - '0';
			final int b = chars[i--] - '0';
			if(a < 0 || a > 9) throw new RuntimeException("invalid character in char array at "+(i+1)+"+ in "+new String(chars));
			bytes[(i+1)/2] = (byte) (a | b << 4);
		
		}
		for (; i >= 0; i--) {
			final int a = chars[i] - '0';
			final int b = chars[--i] - '0';
			if(a < 0 || a > 9 || b < 0 || b > 9) throw new RuntimeException("invalid character in char array at "+(i+1)+"+ in "+new String(chars));
			bytes[(i+1)/2] = (byte) (a | b << 4);
		}
		return bytes;
	}
	
	public static final byte[] toBCD(final String string) {
		if(string == null) throw new RuntimeException("null char array input for bcd conversion");
		if(string.length() % 2 == 1) throw new RuntimeException("odd length char array input for bcd conversion : "+string);
		return toBCD(string.toCharArray());
	}
	
	public static final byte[] toBCDPadLeft(final String string, final char pad) {
		if(string == null) throw new RuntimeException("null char array input for bcd conversion");
		if(pad < '0' || pad > '9') throw new RuntimeException("invalid pad char : "+pad);
		final int len = string.length();
		char[] chars = null;
		if (len % 2 == 1) {
			chars    = new char[len + 1];
			chars[0] = pad;
			string.getChars(0, len, chars, 1);
		} else chars = string.toCharArray();
		return toBCD(chars);
	}
	
	public static final byte[] toBCDPadRight(final String string, final char pad) {
		if(string == null) throw new RuntimeException("null char array input for bcd conversion");
		if(pad < '0' || pad > '9') throw new RuntimeException("invalid pad char : "+pad);
		final int len = string.length();
		char[] chars = null;
		if (len % 2 == 1) {
			chars      = new char[len + 1];
			chars[len] = pad;
			string.getChars(0, len, chars, 0);
		} else chars = string.toCharArray();
		return toBCD(chars);
	}
	
	// probably will overflow long value if input greater than 9 bytes
	public static final long bcdToLong(final byte[] bcd) {
		if(bcd == null) throw new RuntimeException("bcd array can not be null.");
		final int len = bcd.length;
		int i = 0;
		long dec = 0;
		try {
			for (; i < len; i++) {
				final int decI = bcdToDec[bcd[i] & 0xFF];
				if(decI < 0) new RuntimeException("invalid bcd byte "+bcd[i]+" at index "+i+" in "+Arrays.toString(bcd));
				dec = dec * 100 + decI;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("invalid bcd byte "+bcd[i]+" at index "+i+" in "+Arrays.toString(bcd));
		}
		return dec;
	}
	
	public static final char[] bcdToCharArray(final byte[] bcd) {
		if(bcd == null) throw new RuntimeException("bcd array can not be null.");
		int i = 0;
		final char[] chars = new char[bcd.length*2]; 
		final int len = chars.length;
		try {
			for (; i < len; i++) {
				final char[] decI = bcdToCharPair[bcd[i/2] & 0xFF];
				if(decI == null) new RuntimeException("invalid bcd byte "+bcd[i]+" at index "+i+" in "+Arrays.toString(bcd));
				chars[i] = decI[0];
				chars[++i] = decI[1];
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new RuntimeException("invalid bcd byte "+bcd[i]+" at index "+i+" in "+Arrays.toString(bcd), e);
		}
		return chars;
	}
	
	public static final String bcdToString(final byte[] bcd) {
		return new String(bcdToCharArray(bcd));
	}
	
	private static final int calcPackedBCDSize(final long dec) {
		if(dec == 0) return 1;
		return (int) (Math.log10(dec)/2)+1;
	}
	
	public static void main(String[] args) {
		try {
			final byte[] bcd = toBCDPadRight("1", '5');
			System.out.println(ByteHexUtil.byteToHex(bcd) + " : "+new String(bcdToCharArray(new byte[0])));
		} catch (Exception e) {
			e.printStackTrace();}
		
	}
}
