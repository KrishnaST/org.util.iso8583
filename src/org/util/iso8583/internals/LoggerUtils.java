package org.util.iso8583.internals;


public class LoggerUtils {

	public static final String[] DE = getFieldIndexes();

	private static final String[] getFieldIndexes() {
		final String[] DE = new String[129];
		for (int i = 0; i < DE.length; i++) {
			DE[i] = "|"+String.format("%03d", i)+" : '";
		}
		return DE;
		
	}
	
	

	public static final String getMaskedPAN(final String pan) {
		if (pan == null || pan.length() < 6) return "";
		else return new StringBuilder(16).append(pan.substring(0, 6)).append(String.format("%" + (pan.length() - 10) + "s", "").replaceAll(" ", "X"))
				.append(pan.substring(pan.length() - 4)).toString();
	}
	

}
