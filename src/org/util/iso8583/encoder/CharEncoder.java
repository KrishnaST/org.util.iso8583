package org.util.iso8583.encoder;

import java.nio.charset.StandardCharsets;

import org.util.iso8583.api.Encoder;
import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.Index;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.Strings;

public final class CharEncoder extends Encoder {

	private static final CharEncoder CHAR_ENCODER = new CharEncoder();
	
	public static final CharEncoder getInstance() {
		return CHAR_ENCODER;
	}
	
	private CharEncoder() {}
	
	@Override
	public final String name() {
		return "CHAR";
	}

	@Override
	public final int varLength() {
		return 0;
	}
	
	@Override
	public final byte[] encode(final Index index, final ISOFormat format, final String data) {
		final int maxlen = format.length[index.fIndex];
		if(data.length() > maxlen) throw new RuntimeException(name()+" : "+index.fIndex+" : data length "+data.length()+" no equal to configured length "+maxlen);
		return Strings.padRight(data, ' ', maxlen).getBytes(StandardCharsets.US_ASCII);
	}

	@Override
	public final String decode(final Index index, final ISOFormat format, final byte[] bytes) {
		final int maxlen = format.length[index.fIndex];
		if(index.bIndex + maxlen > bytes.length) throw new RuntimeException(name()+" : "+index.fIndex+" : length "+maxlen+" exceeds available data with byte index "+index.bIndex+" data "+ByteHexUtil.byteToHex(bytes));
		index.bIndex  = index.bIndex  + maxlen;
		return new String(bytes, index.bIndex - maxlen, maxlen);
	}

}
