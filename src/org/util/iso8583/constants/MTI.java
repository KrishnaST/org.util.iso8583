package org.util.iso8583.constants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//@formatter:off
public final class MTI {

	public static final String AUTH_REQUEST                = "0100";
	public static final String AUTH_RESPONSE               = "0110";
	public static final String AUTH_ADVICE_REQUEST         = "0120";
	public static final String AUTH_ADVICE_RESPONSE        = "0130";
	public static final String TRANS_REQUEST               = "0200";
	public static final String TRANS_RESPONSE              = "0210";
	public static final String TRANS_ADVICE_REQUEST        = "0220";
	public static final String TRANS_ADVICE_RESPONSE       = "0230";
	public static final String ISR_FILE_UPDT_REQUEST       = "0302";
	public static final String ISR_FILE_UPDT_RESPONSE      = "0312";
	public static final String ISS_REVERSAL_REQUEST        = "0420";
	public static final String ISS_REVERSAL_REQUEST_REPEAT = "0421";
	public static final String ISS_REVERSAL_RESPONSE       = "0430";
	public static final String NET_MGMT_REQUEST            = "0800";
	public static final String NET_MGMT_RESPONSE           = "0810";

	public static final boolean isRequestMTI(final String mti) {
		return (mti.charAt(2) & 0x01) == 0;
	}

	public static final boolean isResponseMTI(final String mti) {
		return (mti.charAt(2) & 0x01) == 1;
	}

	public static final String getCounterMTI(final String mti) {
		if(isRequestMTI(mti)) return getResponseMTI(mti);
		else return getRequestMTI(mti);
	}

	public static final String getRequestMTI(final String mti) {
		return mti.substring(0, 2) + (char) (mti.charAt(2) & 0xFE) + mti.charAt(3);
	}

	public static final String getResponseMTI(final String mti) {
		return mti.substring(0, 2) + (char) (mti.charAt(2) | 0x01) + (char) (mti.charAt(3) & 0xFE);
	}

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		final List<String> mtis = new ArrayList<>();
		Class<MTI> classz = MTI.class;
		Field[] fileds = classz.getDeclaredFields();
		for (Field field : fileds) {
			mtis.add((String) field.get(null));
		}
		mtis.forEach( mit -> System.out.println(mit+" : "+MTI.getCounterMTI(mit)));
	}

}
