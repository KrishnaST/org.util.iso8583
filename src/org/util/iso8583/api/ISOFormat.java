package org.util.iso8583.api;

public abstract class ISOFormat {

	public static final int MESSAGE_LENGTH_ENCODER_INDEX = 0;
	public static final int CHAR_LENGTH_ENCODER_INDEX    = 1;
	public static final int BIN_LENGTH_ENCODER_INDEX     = 2;
	public static final int BCD_LENGTH_ENCODER_INDEX     = 3;

	public static final int NET_HEADER_INDEX = 129;

	protected boolean isBitmapUpperCase   = false;
	protected char    bcdPadChar          = '0';
	protected int     messageLengthLength = 2;

	public final String          name;
	public final int[]           length   = new int[130];
	public final Encoder[]       encoder  = new Encoder[130];
	public final LengthEncoder[] lencoder = new LengthEncoder[4];

	protected ISOFormat(final String name) {
		this.name = name;
	}

	public ISOFormat(String name, int messageLengthLength, char bcdPadChar) {
		this.bcdPadChar          = bcdPadChar;
		this.messageLengthLength = messageLengthLength;
		this.name                = name;
	}

	public final int getNetHeaderLength() {
		return length[NET_HEADER_INDEX];
	}

	public final Encoder getNetHeaderEncoding() {
		return encoder[NET_HEADER_INDEX];
	}

	public final LengthEncoder getCHARLengthEncoder() {
		return lencoder[CHAR_LENGTH_ENCODER_INDEX];
	}

	public final LengthEncoder getBINLengthEncoder() {
		return lencoder[BIN_LENGTH_ENCODER_INDEX];
	}

	public final LengthEncoder getBCDLengthEncoder() {
		return lencoder[BCD_LENGTH_ENCODER_INDEX];
	}

	public final char getBCDPadChar() {
		return bcdPadChar;
	}

	public final int getMessageLengthLength() {
		return messageLengthLength;
	}

	public final LengthEncoder getMessageLengthEncoder() {
		return lencoder[MESSAGE_LENGTH_ENCODER_INDEX];
	}

	public final boolean isBitmapUpperCase() {
		return isBitmapUpperCase;
	}

	public final boolean equals(final ISOFormat format) {
		for (int i = 0; i < encoder.length; i++) {
			if (encoder[i] != format.encoder[i]) return false;
		}
		for (int i = 0; i < lencoder.length; i++) {
			if (lencoder[i] != format.lencoder[i]) return false;
		}
		for (int i = 0; i < length.length; i++) {
			if (length[i] != format.length[i]) return false;
		}
		if (bcdPadChar != format.bcdPadChar) return false;
		if (messageLengthLength != format.messageLengthLength) return false;
		return true;
	}
}
