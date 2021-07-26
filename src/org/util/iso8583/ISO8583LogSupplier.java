package org.util.iso8583;

import java.util.function.Supplier;

public final class ISO8583LogSupplier implements Supplier<StringBuilder> {

	private final ISO8583Message message;

	public ISO8583LogSupplier(ISO8583Message message) {
		this.message = message;
	}

	@Override
	public final StringBuilder get() {
		return EncoderDecoder.log(message);
	}
}
