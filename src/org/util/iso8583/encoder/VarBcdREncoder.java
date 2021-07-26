package org.util.iso8583.encoder;

import java.util.Arrays;

import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.internals.Encoder;
import org.util.iso8583.internals.Index;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.PackedBCD;

public final class VarBcdREncoder extends Encoder {

	private static final VarBcdREncoder DOUBLE_BCDL_ENCODER = new VarBcdREncoder(2);
	private static final VarBcdREncoder TRIPLE_BCDL_ENCODER = new VarBcdREncoder(3);
	
	public static final VarBcdREncoder getInstanceOfDouble() {
		return DOUBLE_BCDL_ENCODER;
	}
	
	public static final VarBcdREncoder getInstanceOftriple() {
		return TRIPLE_BCDL_ENCODER;
	}
	
	private final int lenLen;
	private final String lenSuffix;
	
	public VarBcdREncoder(int lenLen) {
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
		final int datalen = data.length();
		if(datalen > maxlen) throw new RuntimeException(name()+" : "+index.fIndex+" : data length "+datalen+" no equal to configured length "+maxlen);
		final byte[] lbytes = format.getBCDLengthEncoder().encode(datalen, lenLen);
		final byte[] bytes = new byte[lbytes.length + (datalen+1)/2];
		if(lbytes.length >= 0) bytes[0] = lbytes[0];
		if(lbytes.length >= 2) bytes[1] = lbytes[1];
		if(lbytes.length >= 3) bytes[2] = lbytes[2];
		final byte[] dbytes = PackedBCD.toBCDPadRight(data, format.getBCDPadChar());
		System.arraycopy(dbytes, 0, bytes, lbytes.length, dbytes.length);
		return bytes;
	}

	@Override
	public final String decode(final Index index, final ISOFormat format, final byte[] bytes) {
		final int datalen = format.getBCDLengthEncoder().decode(bytes, index, lenLen);
		final int actlen = (datalen + 1)/2;
		if(index.bIndex + actlen > bytes.length) throw new RuntimeException(name()+" : "+index.fIndex+" : length "+actlen+" exceeds available data with byte index "+index.bIndex+" data "+ByteHexUtil.byteToHex(bytes));
		index.bIndex  = index.bIndex  + actlen;
		final String data = PackedBCD.bcdToString(Arrays.copyOfRange(bytes, index.bIndex - actlen, index.bIndex));
		return data.substring(0, datalen);
	}

}
