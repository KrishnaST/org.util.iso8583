package org.util.iso8583;

import java.util.Arrays;

import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.Index;
import org.util.iso8583.internals.ByteBuffer;
import org.util.iso8583.internals.LoggerUtils;
import org.util.iso8583.util.Bitmap;
import org.util.iso8583.util.ByteHexUtil;
import org.util.iso8583.util.Strings;

//@formatter:off
public final class EncoderDecoder {
	
	private static final int     lwidth     = 60;
	private static final String  dassedLine = Strings.repeat(new StringBuilder(lwidth * 2 + 20).append(" "), '-', lwidth * 2 + 17).append(" \r\n").toString();
	private static final String  spaces     = Strings.repeat(new StringBuilder(), ' ', lwidth + 8).toString();
	
	
	public static boolean debug = false;

	public static final byte[] encode(final ISOFormat format, final ISO8583Message message) {
		if(message == null) throw new RuntimeException("iso message can not be null.");
		if(message.data[0] == null) throw new RuntimeException("mti can not be null.");
		final Index index = new Index();
		try {
			final Bitmap     bitmap = message.bitmap;
			final String[]   data   = message.data;
			final ByteBuffer buffer = new ByteBuffer(200); 
			buffer.write(new byte[format.getMessageLengthLength()]);
			if (message.getNetHeader() != null && format.getNetHeaderLength() != 0) {
				if(debug) System.out.println("NetHeader encoded : "+message.getNetHeader());
				index.fIndex = ISOFormat.NET_HEADER_INDEX;
				buffer.write(format.encoder[index.fIndex].encode(index, format, message.getNetHeader()));
			}
			index.fIndex = 0;
			buffer.write(format.encoder[0].encode(index, format, data[0]));
			if(debug) System.out.println("MTI encoded : "+data[0]);
			index.fIndex = 1;
			final byte[] bitsmap = bitmap.toBytes();
			data[1] = ByteHexUtil.byteToHex(bitsmap);
			buffer.write(format.encoder[1].encode(index, format, data[1].substring(0, 16)));
			if(debug) System.out.println("bitmap encoded : "+data[1]);
			if ((bitsmap[0] & 0x80) == 0x80) buffer.write(format.encoder[1].encode(index, format, data[1].substring(16)));
			for (int i = 2; i < 129; i++) {
				index.fIndex = i;
				if (bitmap.get(i)) {
					buffer.write(format.encoder[i].encode(index, format, data[i]));
					if(debug) System.out.println(i+" encoded : "+data[i]);
				}
			}
			final byte[] bytes    = buffer.toByteArray();
			final byte[] lenbytes = format.getMessageLengthEncoder().encode(bytes.length - format.getMessageLengthLength(), format.getMessageLengthLength());
			System.out.println("lenbytes : "+ByteHexUtil.byteToHex(lenbytes));
			System.arraycopy(lenbytes, 0, bytes, 0, lenbytes.length);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException("error accured while encoding field no "+index.fIndex, e);
		}
	}

	public static final ISO8583Message decode(final ISOFormat format, final byte[] bytes) {
		if (bytes == null || bytes.length < 10) throw new RuntimeException("input byte array is null or too short.");
		final Index index = new Index();
		try {
			final ISO8583Message message = new ISO8583Message();
			final Bitmap   bitmap  = message.bitmap;
			final String[] data    = message.data;
			if (format.getNetHeaderLength() != 0) {
				index.fIndex = ISOFormat.NET_HEADER_INDEX;
				message.putNetHeader(format.getNetHeaderEncoding().decode(index, format, bytes));
				if(debug) System.out.println("net header decoded : "+message.getNetHeader());
			} 
			index.fIndex = 0;
			data[0] = format.encoder[0].decode(index, format, bytes);
			if(debug) System.out.println("mti decoded : "+data[0]);
			index.fIndex = 1;
			data[1] = format.encoder[1].decode(index, format, bytes);
			if(debug) System.out.println("primary bitmap decoded : "+data[1]);
			boolean hasSecBitmap = (ByteHexUtil.hexPairToByte(data[1].charAt(0), data[1].charAt(1)) & 0x80) == 0x80;
			if (hasSecBitmap) data[1] = data[1] + format.encoder[1].decode(index, format, bytes);
			if(debug) System.out.println("pri+sec bitmap decoded : "+data[1]);
			bitmap.init(data[1]);
			for (index.fIndex = 2; index.fIndex < 129; index.fIndex++) {
				if (bitmap.get(index.fIndex)) {
					data[index.fIndex] = format.encoder[index.fIndex].decode(index, format, bytes);
					if(debug) System.out.println(index.fIndex+" : "+data[index.fIndex]);
				}
			}
			if (bytes.length > index.bIndex) throw new RuntimeException("message data remaining after decoding " + ByteHexUtil.byteToHex(Arrays.copyOfRange(bytes, index.bIndex, bytes.length)));
			return message;
		} catch (Exception e) {
			throw new RuntimeException("error accured while encoding field no "+index.fIndex, e);
		}
	}


	public static StringBuilder log(ISO8583Message message) {
		if (message == null) return null;
		final StringBuilder sb = new StringBuilder(2000);
		message.data[1] = ByteHexUtil.byteToHex(message.bitmap.toBytes());
		boolean toggle = true;
		sb.append("\r\n");
		sb.append(dassedLine);
		for (int i = 0; i < 129; i++) {
			if (message.bitmap.get(i)) {
				sb.append(LoggerUtils.DE[i]);
				Strings.padRightSpecial(sb, message.data[i], ' ', lwidth);
				if (toggle ^= true) sb.append("|\r\n");
			}
		}
		if (toggle ^= true) {
			sb.append("|");
			sb.append(spaces);
			sb.append("|\r\n");
		}
		return sb.append(dassedLine);
	
	}

}
