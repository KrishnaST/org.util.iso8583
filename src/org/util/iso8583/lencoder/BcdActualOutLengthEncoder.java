package org.util.iso8583.lencoder;

import java.util.Arrays;

import org.util.iso8583.api.Index;
import org.util.iso8583.api.LengthEncoder;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.PackedBCD;

public final class BcdActualOutLengthEncoder extends LengthEncoder {
	
	private static final BcdActualOutLengthEncoder BCD_LENGTH_ENCODER = new BcdActualOutLengthEncoder();
	
	public static final BcdActualOutLengthEncoder getInstance() {
		return BCD_LENGTH_ENCODER;
	}
	
	private BcdActualOutLengthEncoder() {}
	
	@Override
	public final String name() {
		return "BCD_ACTUAL_OUTLEN";
	}

	@Override
	public final byte[] encode(final int dataLen, final int lenLen) {
		return PackedBCD.toBCD(dataLen, lenLen);
	}

	@Override
	public final int decode(final byte[] bytes, Index index, int lenLen) {
		index.bIndex = index.bIndex + lenLen;
		return (int) PackedBCD.bcdToLong(Arrays.copyOfRange(bytes, index.bIndex - lenLen, index.bIndex));
	}
	
	
	public static void main(String[] args) {
		BcdActualOutLengthEncoder encoder = new BcdActualOutLengthEncoder();
		final byte[] bytes2 = encoder.encode(99, 2);
		final byte[] bytes3 = encoder.encode(77, 3);
		System.out.println(ByteHexUtil.byteToHex(bytes2));
		System.out.println(ByteHexUtil.byteToHex(bytes3));
		System.out.println(encoder.decode(bytes2, new Index(), 2));
		System.out.println(encoder.decode(bytes3, new Index(), 3));
	}
	
}
