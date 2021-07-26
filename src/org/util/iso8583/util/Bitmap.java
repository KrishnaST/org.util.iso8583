package org.util.iso8583.util;

public final class Bitmap {

	private static final long one = Long.MIN_VALUE;
	private static final long op  = 0xFF;

	private long primap = 0;
	private long secmap = 0;

	public Bitmap() {}

	public Bitmap(final String primap, final String secmap) {
		this.primap = Long.parseUnsignedLong(primap, 16);
		this.secmap = Long.parseUnsignedLong(secmap, 16);
	}

	public Bitmap(final String hex) {
		this.primap = Long.parseUnsignedLong(hex.substring(0, 16), 16);
		if (hex.length() > 16) this.secmap = Long.parseUnsignedLong(hex.substring(16), 16);
	}

	public Bitmap(final long primap, final long secmap) {
		this.primap = primap;
		this.secmap = secmap;
	}

	public void init(final String hex) {
		this.primap = Long.parseUnsignedLong(hex.substring(0, 16), 16);
		if (hex.length() > 16) this.secmap = Long.parseUnsignedLong(hex.substring(16), 16);
	}

	public final void set(final int i) {
		if (i > 0 && i <= 64) primap |= (one >>> (i - 1));
		else if (i > 64 && i <= 128) secmap |= (one >>> (i - 65));
	}

	public final void remove(final int i) {
		if (i > 0 && i <= 64) primap &= ~(one >>> (i - 1));
		else if (i > 64 && i <= 128) secmap &= ~(one >>> (i - 65));
	}

	public final void setPrimaryHex(final String primap) {
		this.primap = Long.parseUnsignedLong(primap, 16);
	}

	public final void setSecondaryHex(final String secmap) {
		this.secmap = Long.parseUnsignedLong(secmap, 16);
	}

	public final void setLong(final long primap, final long secmap) {
		this.primap = primap;
		this.secmap = secmap;
	}

	public final void setPrimaryLong(final long primap) {
		this.primap = primap;
	}

	public final void setSecondaryLong(final long secmap) {
		this.secmap = secmap;
	}

	public final void setPrimaryBytes(final byte[] bytes) {
		primap = 0;
		for (int i = 0; i < bytes.length; i++) { primap = primap | ((long) (bytes[i] & 0xFF) << (7 - i) * 8); }
	}

	public final void setSecondaryBytes(final byte[] bytes) {
		secmap = 0;
		for (int i = 0; i < bytes.length; i++) { secmap = secmap | ((long) (bytes[i] & 0xFF) << (7 - i) * 8); }
	}

	public final boolean get(final int i) {
		if (i > 0 && i <= 64) return (primap & (one >>> (i - 1))) == (one >>> (i - 1));
		else if (i > 64 && i <= 128) return (secmap & (one >>> (i - 65))) == (one >>> (i - 65));
		return false;
	}

	
	public final boolean hasSecondary() {
		return secmap != 0;
	}

	public final long getPrimaryLong() {
		if(secmap != 0) set(1);
		return primap;
	}

	public final long getSecondaryLong() {
		return secmap;
	}

	public final String toHexString() {
		if(secmap != 0) set(1);
		final String p = Long.toHexString(primap);
		return secmap == 0 ? p : p + Long.toHexString(secmap);
	}
	
	public final byte[] toBytes() {
		byte[] bitmap = null;
		if (secmap == 0) {
			bitmap = new byte[8];
			for (int i = 0; i < 8; i++) { bitmap[i] = (byte) ((primap >>> ((7 - i) * 8)) & op); }
		} else {
			set(1);
			bitmap = new byte[16];
			for (int i = 0; i < 8; i++) { bitmap[i] = (byte) ((primap >>> ((7 - i) * 8)) & op); }
			for (int i = 0; i < 8; i++) { bitmap[8 + i] = (byte) ((secmap >>> ((7 - i) * 8)) & op); }
		}
		return bitmap;
	}

	public final Bitmap clone() {
		return new Bitmap(primap, secmap);
	}

	public final boolean isEmpty() {
		return (primap == 0 && secmap == 0);
	}

	public static final long parseUnsignedLong(final String hex) {
		boolean issigned = false;
		String finalString = "";
		if (hex.length() > 16) throw new NumberFormatException("cannot parse string exceeds long capacity.");
		else if (hex.length() == 16) {
			int sign = Integer.parseInt("0" + hex.charAt(0), 16);
			if (sign >= 8) {
				issigned = true;
				sign     = sign - 8;
				finalString = sign + hex.substring(1);
			}
			else finalString = hex;
		}
		final long l = Long.parseLong(finalString, 16);
		return issigned ? l | Long.MIN_VALUE : l;
	}

	
	
}
