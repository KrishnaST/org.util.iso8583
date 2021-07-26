package org.util.iso8583.internals;

import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.encoder.BcdLEncoder;
import org.util.iso8583.encoder.BcdREncoder;
import org.util.iso8583.encoder.BinEncoder;
import org.util.iso8583.encoder.CharEncoder;
import org.util.iso8583.encoder.VarBcdLEncoder;
import org.util.iso8583.encoder.VarBcdREncoder;
import org.util.iso8583.encoder.VarBinEncoder;
import org.util.iso8583.encoder.VarCharEncoder;

public abstract class Encoder {

	public abstract String name();
	
	public final boolean isVarType() {
		return varLength() != 0;
	}
	
	public abstract int varLength();
	
	public abstract byte[] encode(final Index index, final ISOFormat format, final String data);
	
	public abstract String decode(final Index index, final ISOFormat format, final byte[] bytes);
	
	public static final Encoder ofName(final String name) {
		if("CHAR".equalsIgnoreCase(name)) return CharEncoder.getInstance();
		else if("BIN".equalsIgnoreCase(name)) return BinEncoder.getInstance();
		else if("BCDL".equalsIgnoreCase(name)) return BcdLEncoder.getInstance();
		else if("BCDR".equalsIgnoreCase(name)) return BcdREncoder.getInstance();
		else if("LLCHAR".equalsIgnoreCase(name)) return VarCharEncoder.getInstanceOfDouble();
		else if("LLLCHAR".equalsIgnoreCase(name)) return VarCharEncoder.getInstanceOftriple();
		else if("LLBIN".equalsIgnoreCase(name)) return VarBinEncoder.getInstanceOfDouble();
		else if("LLLBIN".equalsIgnoreCase(name)) return VarBinEncoder.getInstanceOftriple();
		else if("LLBCDL".equalsIgnoreCase(name)) return VarBcdLEncoder.getInstanceOfDouble();
		else if("LLLBCDL".equalsIgnoreCase(name)) return VarBcdLEncoder.getInstanceOftriple();
		else if("LLBCDR".equalsIgnoreCase(name)) return VarBcdREncoder.getInstanceOfDouble();
		else if("LLLBCDR".equalsIgnoreCase(name)) return VarBcdREncoder.getInstanceOftriple();
		else throw new RuntimeException("encoder of name "+name+" not found.");
	}
}
