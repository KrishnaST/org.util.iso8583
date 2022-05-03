package org.util.iso8583.internals;

public class Track2 {

	public String pan;
	public String expiry;
	public String servicecode;
	public String cvv;
	public String data;

	public static final Track2 parse(String string) {

		if (string == null) return null;
		try {
			Track2 track2 = new Track2();
			int    index  = string.indexOf('=');
			if (index == -1) index = string.indexOf('D');
			if (index == -1) index = string.indexOf('d');
			if (index == -1) return null;
			track2.pan         = string.substring(0, index);
			track2.expiry      = string.substring(index + 1, index + 5);
			track2.servicecode = string.substring(index + 5, index + 8);
			track2.cvv         = string.substring(index + 8, index + 11);
			track2.data        = string.substring(index + 11);
			return track2;
		} catch (Exception e) {}
		return null;
	}

	public final String buildTrack2() {
		return new StringBuilder(37).append(pan).append("=").append(expiry).append(servicecode).append(cvv).append(data).toString();
	}

	public final String buildMaskedTrack2() {
		return new StringBuilder(37).append(getMaskedPAN(pan)).append("=").append(expiry).append(servicecode).append("XXX").append(data).toString();
	}

	private static final String getMaskedPAN(String pan) {
		if (pan == null || pan.length() < 6) return "";
		else return new StringBuilder(16).append(pan.substring(0, 6)).append(String.format("%" + (pan.length() - 10) + "s", "").replaceAll(" ", "X"))
				.append(pan.substring(pan.length() - 4)).toString();
	}

	@Override
	public final String toString() {
		return "Track2 [pan=" + pan + ", expiry=" + expiry + ", servicecode=" + servicecode + ", cvv=" + cvv + ", data=" + data + "]";
	}
}
