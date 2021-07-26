package org.util.iso8583.lencoder;

import org.util.iso8583.internals.Index;
import org.util.iso8583.internals.LengthEncoder;
import org.util.iso8583.util.ByteHexUtil;

public final class BinLengthEncoder extends LengthEncoder {

	private static final BinLengthEncoder BIN_LENGTH_ENCODER = new BinLengthEncoder();
	
	public static final BinLengthEncoder getInstance() {
		return BIN_LENGTH_ENCODER;
	}
	
	private BinLengthEncoder() {}
	
	@Override
	public final String name() {
		return "BIN";
	}

	@Override
	public final byte[] encode(final int dataLen, final int outlen) {
		final byte[] bytes = new byte[outlen];
		int temp = dataLen;
		for (int i = outlen-1; i >= 0; i--) {
			bytes[i] = (byte) (temp % 256);
			temp = temp/256;
		}
		return bytes;
	}

	@Override
	public final int decode(final byte[] bytes, Index index, int lenLen) {
		int len = 0;
		for (int i = 0; i < lenLen; i++) {
			len = len * 256 + (bytes[index.bIndex++] & 0xFF);
		}
		return len;
	}
	
	
	public static void main(String[] args) {
		BinLengthEncoder encoder = new BinLengthEncoder();
		final byte[] bytes2 = encoder.encode(99, 2);
		final byte[] bytes3 = encoder.encode(37, 3);
		System.out.println(ByteHexUtil.byteToHex(bytes2));
		System.out.println(ByteHexUtil.byteToHex(bytes3));
		System.out.println(encoder.decode(bytes2, new Index(), 2));
		System.out.println(encoder.decode(bytes3, new Index(), 3));
	}
	
}
