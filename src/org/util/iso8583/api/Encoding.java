package org.util.iso8583.api;

import org.util.iso8583.encoder.BcdLEncoder;
import org.util.iso8583.encoder.BcdREncoder;
import org.util.iso8583.encoder.BinEncoder;
import org.util.iso8583.encoder.CharEncoder;
import org.util.iso8583.encoder.NumEncoder;
import org.util.iso8583.encoder.VarBcdLEncoder;
import org.util.iso8583.encoder.VarBcdREncoder;
import org.util.iso8583.encoder.VarBinEncoder;
import org.util.iso8583.encoder.VarCharEncoder;
import org.util.iso8583.encoder.VarNumEncoder;

//@formatter:off
public enum Encoding {
	NUM(NumEncoder.getInstance()), 	 LLNUM(VarNumEncoder.getInstanceOfDouble()),   LLLNUM(VarNumEncoder.getInstanceOftriple()), 
	CHAR(CharEncoder.getInstance()), LLCHAR(VarCharEncoder.getInstanceOfDouble()), LLLCHAR(VarCharEncoder.getInstanceOftriple()),
	BCDL(BcdLEncoder.getInstance()), LLBCDL(VarBcdLEncoder.getInstanceOfDouble()), LLLBCDL(VarBcdLEncoder.getInstanceOftriple()),
	BCDR(BcdREncoder.getInstance()), LLBCDR(VarBcdREncoder.getInstanceOfDouble()), LLLBCDR(VarBcdREncoder.getInstanceOftriple()), 
	BIN(BinEncoder.getInstance()),	 LLBIN(VarBinEncoder.getInstanceOfDouble()),   LLLBIN(VarBinEncoder.getInstanceOftriple());

	public final Encoder encoder;

	private Encoding(Encoder encoder) {
		this.encoder = encoder;
	}

}
