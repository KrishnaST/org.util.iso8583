package org.util.iso8583.encoder;

import java.util.Arrays;

import org.util.iso8583.api.Encoder;
import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.Index;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.Strings;

public final class BinEncoder extends Encoder {

	private static final BinEncoder BIN_ENCODER = new BinEncoder();
	
	public static final BinEncoder getInstance() {
		return BIN_ENCODER;
	}
	
	private BinEncoder() {}
	
	@Override
	public final String name() {
		return "BIN";
	}

	@Override
	public final int varLength() {
		return 0;
	}
	
	@Override
	public final byte[] encode(final Index index, final ISOFormat format, final String data) {
		final int maxlen = format.length[index.fIndex];
		final int datalen = (data.length() + 1)/2;
		if(datalen > maxlen) throw new RuntimeException(name()+" : "+index.fIndex+" : data length "+datalen+" is greater than configured length "+maxlen);
		return ByteHexUtil.hexToByte(Strings.padLeft(data, '0', maxlen*2));
	}

	@Override
	public final String decode(final Index index, final ISOFormat format, final byte[] bytes) {
		final int maxlen = format.length[index.fIndex];
		if(index.bIndex + maxlen > bytes.length) throw new RuntimeException(name()+" : "+index.fIndex+" : length "+maxlen+" exceeds available data with byte index "+index.bIndex+" data "+ByteHexUtil.byteToHex(bytes));
		index.bIndex  = index.bIndex  + maxlen;
		return ByteHexUtil.byteToHex(Arrays.copyOfRange(bytes, index.bIndex - maxlen, index.bIndex));
	}

}
