package org.util.iso8583.encoder;

import java.util.Arrays;

import org.util.iso8583.api.Encoder;
import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.Index;
import org.util.iso8583.util.ByteHexUtil;

public final class VarBinEncoder extends Encoder {

	private static final VarBinEncoder DOUBLE_BIN_ENCODER = new VarBinEncoder(2);
	private static final VarBinEncoder TRIPLE_BIN_ENCODER = new VarBinEncoder(3);
	
	public static final VarBinEncoder getInstanceOfDouble() {
		return DOUBLE_BIN_ENCODER;
	}
	
	public static final VarBinEncoder getInstanceOftriple() {
		return TRIPLE_BIN_ENCODER;
	}
	
	private final int lenLen;
	private final String lenSuffix;
	
	public VarBinEncoder(int lenLen) {
		this.lenLen = lenLen;
		lenSuffix = lenLen == 2 ? "LL" : "LLL";
	}

	@Override
	public final String name() {
		return lenSuffix + "BIN";
	}

	@Override
	public final int varLength() {
		return lenLen;
	}
	
	@Override
	public final byte[] encode(final Index index, final ISOFormat format, final String data) {
		final int maxlen = format.length[index.fIndex];
		final int datalen = (data.length()+1)/2;
		if(datalen > maxlen) throw new RuntimeException(name()+" : "+index.fIndex+" : data length "+datalen+" exceeds maximum configured capacity "+maxlen);
		final byte[] lbytes = format.getBINLengthEncoder().encode(datalen, lenLen);
		final byte[] bytes = new byte[datalen+lbytes.length];
		if(lbytes.length >= 0) bytes[0] = lbytes[0];
		if(lbytes.length >= 2) bytes[1] = lbytes[1];
		if(lbytes.length >= 3) bytes[2] = lbytes[2];
		final byte[] dbytes = ByteHexUtil.hexToByte(data);
		System.arraycopy(dbytes, 0, bytes, lbytes.length, dbytes.length);
		return bytes;
	}

	@Override
	public final String decode(final Index index, final ISOFormat format, final byte[] bytes) {
		final int datalen = format.getBINLengthEncoder().decode(bytes, index, lenLen);
		if(index.bIndex + datalen > bytes.length) throw new RuntimeException(name()+" : "+index.fIndex+" : length "+datalen+" exceeds available data with byte index "+index.bIndex+" data "+ByteHexUtil.byteToHex(bytes));
		index.bIndex = index.bIndex + datalen; 
		return ByteHexUtil.byteToHex(Arrays.copyOfRange(bytes, index.bIndex - datalen, datalen));
	}

}
