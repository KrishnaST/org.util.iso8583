package org.util.iso8583.format;

import org.util.iso8583.api.ISOFormat;

public final class CustomFormat extends ISOFormat {

	public CustomFormat(String name) {
		super(name);
	}

	public CustomFormat(String name, int messageLengthLength, char bcdPadChar) {
		super(name, messageLengthLength, bcdPadChar);
	}
}
