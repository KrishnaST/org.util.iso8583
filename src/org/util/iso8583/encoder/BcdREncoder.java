package org.util.iso8583.encoder;

import java.util.Arrays;

import org.util.iso8583.api.Encoder;
import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.Index;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.PackedBCD;
import org.util.iso8583.util.Strings;

public final class BcdREncoder extends Encoder {

	private static final BcdREncoder BCDR_ENCODER = new BcdREncoder();
	
	public static final BcdREncoder getInstance() {
		return BCDR_ENCODER;
	}
	
	private BcdREncoder() {}
	
	@Override
	public final String name() {
		return "BCDR";
	}

	@Override
	public final int varLength() {
		return 0;
	}
	
	@Override
	public final byte[] encode(final Index index, final ISOFormat format, final String data) {
		final int maxlen = format.length[index.fIndex];
		final int datalen = data.length();
		if(datalen > maxlen) throw new RuntimeException(name()+" : "+index.fIndex+" : data length "+datalen+" greater configured length "+maxlen);
		return PackedBCD.toBCD(Strings.padLeft(data, format.getBCDPadChar(), ((maxlen+1)/2)*2));
	}

	@Override
	public final String decode(final Index index, final ISOFormat format, final byte[] bytes) {
		final int maxlen = format.length[index.fIndex];
		final int actlen = (maxlen + 1)/2;
		if(index.bIndex + actlen > bytes.length) throw new RuntimeException(name()+" : "+index.fIndex+" : length "+maxlen+" exceeds available data with byte index "+index.bIndex+" data "+ByteHexUtil.byteToHex(bytes));
		index.bIndex  = index.bIndex  + actlen;
		final String data = PackedBCD.bcdToString(Arrays.copyOfRange(bytes, index.bIndex - actlen, index.bIndex));
		return data.substring(0, maxlen);
	}

}
