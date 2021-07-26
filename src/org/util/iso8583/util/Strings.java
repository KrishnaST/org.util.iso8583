package org.util.iso8583.util;


public final class Strings {

	public static final String padLeft(String string, char pad, int len) {
		if (string == null || string.length() >= len) return string;
		final StringBuilder sb = new StringBuilder(len);
		while (sb.length() != (len - string.length())) sb.append(pad);
		sb.append(string);
		return sb.toString();
	}

	public static final String padRight(String string, char padChar, int len) {
		if (string == null || string.length() >= len) return string;
		StringBuilder sb = new StringBuilder(len);
		sb.append(string);
		while (sb.length() != len) sb.append(padChar);
		return sb.toString();
	}
	
	public static final StringBuilder repeat(StringBuilder sb, final char c, final int len) {
		final char[] chars = new char[len];
		for (int i = 0; i < len; i++) {
			chars[i] = c;
		}
		return sb.append(chars); 
	}

	public static StringBuilder padRightSpecial(final StringBuilder sb, final String s, final char padChar, final int expLen) {
		if (s.length() >= expLen) return sb.append(s).append("'");
		sb.append(s);
		sb.append("'");
		int padLen = expLen - s.length();
		for (int i = 0; i < padLen; i++) sb.append(padChar);
		return sb;
	}
}
