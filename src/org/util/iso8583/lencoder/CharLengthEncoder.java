package org.util.iso8583.lencoder;

import org.util.iso8583.internals.Index;
import org.util.iso8583.internals.LengthEncoder;

public final class CharLengthEncoder extends LengthEncoder {

	private static final CharLengthEncoder CHAR_LENGTH_ENCODER = new CharLengthEncoder();
	
	public static final CharLengthEncoder getInstance() {
		return CHAR_LENGTH_ENCODER;
	}
	
	private CharLengthEncoder() {}
	
	@Override
	public final String name() {
		return "CHAR";
	}

	@Override
	public final byte[] encode(final int dataLen, final int lenLen) {
		final byte[] bytes = new byte[lenLen];
		if(lenLen == 2) {
			bytes[0] = (byte) ((dataLen/10) + '0');
			bytes[1] = (byte) ((dataLen%10) + '0');
		}
		else if(lenLen == 3) {
			bytes[0] = (byte) ((dataLen/100) + '0');
			bytes[1] = (byte) (((dataLen%100)/10) + '0');
			bytes[2] = (byte) ((dataLen%10) + '0');
		}
		else return new byte[0];
		return bytes;
	}

	@Override
	public final int decode(final byte[] bytes, Index index, int lenLen) {
		int len = 0;
		for (int i = 0; i < lenLen; i++) {
			len = len * 10 + (bytes[index.bIndex++] - '0');
		}
		return len;
	}
	
	
	public static void main(String[] args) {
		CharLengthEncoder encoder = new CharLengthEncoder();
		final byte[] bytes2 = encoder.encode(99, 2);
		final byte[] bytes3 = encoder.encode(37, 3);
		System.out.println(new String(bytes2));
		System.out.println(new String(bytes3));
		System.out.println(encoder.decode(bytes3, new Index(), 3));
	}
	
}
