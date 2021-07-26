package org.util.iso8583.util;


public class MiscUtil {

	public static final String getResponseMTI(final String mti) {
		return mti.substring(0, 2) + (char) (mti.charAt(2) | 0x01) + (char) (mti.charAt(3) & 0xFE);
	}
	
	public static void main(String[] args) {
		System.out.println(getResponseMTI("0201"));
	}
}
