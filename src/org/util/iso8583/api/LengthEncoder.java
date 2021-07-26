package org.util.iso8583.api;

public abstract class LengthEncoder {

	public abstract String name();
	
	public abstract byte[] encode(final int dataLen, final int lenLen);
	
	public abstract int decode(final byte[] bytes, Index index, final int lenLen);

}
