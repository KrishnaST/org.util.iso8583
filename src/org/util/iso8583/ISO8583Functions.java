package org.util.iso8583;

import java.util.Map;

import org.util.iso8583.api.ISO8583Function;
import org.util.iso8583.constants.MTI;
import org.util.iso8583.util.Bitmap;

public class ISO8583Functions {

	public static final ISO8583Function<String> UNIQUE_KEY_FUNCTION = new ISO8583Function<String>() {
		@Override
		public final String apply(String[] data, Bitmap bitmap, Map<String, Object> extra) {
			final StringBuilder key = new StringBuilder(40);
			if (data[0] != null) key.append(MTI.getResponseMTI(data[0]));
			if (data[2] != null) key.append(data[2]);
			if (data[7] != null) key.append(data[7]);
			if (data[11] != null) key.append(data[11]);
			if (data[32] != null) key.append(data[32]);
			if (data[37] != null) key.append(data[37]);
			if (data[41] != null) key.append(data[41]);
			return key.toString();
		}
	};
	
	public static final ISO8583Function<String> TRANSACTION_KEY_FUNCTION = new ISO8583Function<String>() {
		@Override
		public final String apply(String[] data, Bitmap bitmap, Map<String, Object> extra) {
			final StringBuilder key = new StringBuilder(40);
			if (data[2] != null) key.append(data[2]);
			if (data[32] != null) key.append(data[32]);
			if (data[37] != null) key.append(data[37]);
			if (data[41] != null) key.append(data[41]);
			return key.toString();
		}
	};
	
	public static final ISO8583Function<String> MAB_FUNCTION = new ISO8583Function<String>() {
		@Override
		public final String apply(String[] data, Bitmap bitmap, Map<String, Object> extra) {
			final StringBuilder mab = new StringBuilder(40);
			if (data[0] != null) mab.append(data[0]);
			if (data[2] != null) mab.append(data[2]);
			if (data[3] != null) mab.append(data[3]);
			if (data[4] != null) mab.append(data[4]);
			if (data[11] != null) mab.append(data[11]);
			if (data[12] != null) mab.append(data[12]);
			if (data[37] != null) mab.append(data[37]);
			if (data[38] != null && ISO8583MessageHelper.isResponse(data[0])) mab.append(data[38]);
			if (data[39] != null && ISO8583MessageHelper.isResponse(data[0])) mab.append(data[39]);
			return mab.toString();
		
		}
	};
}
