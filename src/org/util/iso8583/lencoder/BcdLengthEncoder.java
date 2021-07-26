package org.util.iso8583.lencoder;

import java.util.Arrays;

import org.util.iso8583.api.Index;
import org.util.iso8583.api.LengthEncoder;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.PackedBCD;

public final class BcdLengthEncoder extends LengthEncoder {
	
	private static final BcdLengthEncoder BCD_LENGTH_ENCODER = new BcdLengthEncoder();
	
	public static final BcdLengthEncoder getInstance() {
		return BCD_LENGTH_ENCODER;
	}
	
	private BcdLengthEncoder() {}
	
	@Override
	public final String name() {
		return "BCD";
	}

	@Override
	public final byte[] encode(final int dataLen, final int lenLen) {
		if(lenLen == 2) return PackedBCD.toBCD(dataLen, 1);
		else if(lenLen == 3) return PackedBCD.toBCD(dataLen, 2);
		else return new byte[0];
	}

	@Override
	public final int decode(final byte[] bytes, Index index, int lenLen) {
		int bcdLen = (lenLen + 1)/2;
		index.bIndex = index.bIndex + bcdLen;
		return (int) PackedBCD.bcdToLong(Arrays.copyOfRange(bytes, index.bIndex - bcdLen, index.bIndex));
	}
	
	
	public static void main(String[] args) {
		BcdLengthEncoder encoder = new BcdLengthEncoder();
		final byte[] bytes2 = encoder.encode(99, 2);
		final byte[] bytes3 = encoder.encode(1137, 3);
		System.out.println(ByteHexUtil.byteToHex(bytes2));
		System.out.println(ByteHexUtil.byteToHex(bytes3));
		System.out.println(encoder.decode(bytes2, new Index(), 2));
		System.out.println(encoder.decode(bytes3, new Index(), 3));
	}
	
}
