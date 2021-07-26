package org.util.iso8583.api;

import org.util.iso8583.lencoder.BcdLengthEncoder;
import org.util.iso8583.lencoder.BinLengthEncoder;
import org.util.iso8583.lencoder.CharLengthEncoder;

//@formatter:off
public enum LengthEncoding {

	CHAR(CharLengthEncoder.getInstance()),
	BCD(BcdLengthEncoder.getInstance()),
	BIN(BinLengthEncoder.getInstance());

	//NUM, 	LLNUM, 		LLLNUM, 
	public final LengthEncoder encoder;

	private LengthEncoding(LengthEncoder encoder) {
		this.encoder = encoder;
	}

}
