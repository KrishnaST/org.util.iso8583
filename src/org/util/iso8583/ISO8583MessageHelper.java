package org.util.iso8583;

public final class ISO8583MessageHelper {

	public static final boolean isRequest(final String mti) {
		return (mti.charAt(2) & 0x01) == 0;
	}

	public static final boolean isResponse(final String mti) {
		return (mti.charAt(2) & 0x01) == 1;
	}
	
	public static final boolean isCopy(final ISO8583Message message) {
		if(message != null) return Boolean.parseBoolean(message.getExtra(String.class, PropertyName.IS_COPY));
		return false;
	}
	
	public static final void setCopyFlag(final ISO8583Message message) {
		if(message != null) message.putExtra(PropertyName.IS_COPY, Boolean.toString(true));
	}
	
	public static final void setStaticFlag(final ISO8583Message message) {
		if(message != null) message.putExtra(PropertyName.IS_STATIC_RESPONSE, Boolean.toString(true));
	}
	
	public static final boolean isStaticResponse(final ISO8583Message message) {
		return Boolean.parseBoolean(message.getExtra(String.class, PropertyName.IS_STATIC_RESPONSE));
	}
	
	public static final void setRawRequest(final ISO8583Message message, final byte[] bytes) {
		if(message != null) message.putExtra(PropertyName.RAW_REQUEST, bytes);
	}
	
	public static final void setRawResponse(final ISO8583Message message, final byte[] bytes) {
		if(message != null) message.putExtra(PropertyName.RAW_RESPONSE, bytes);
	}

	public static final byte[] getRawRequest(final ISO8583Message message) {
		if(message != null) return message.getExtra(byte[].class, PropertyName.RAW_REQUEST);
		return null;
	}
	
	public static final byte[] getRawResponse(final ISO8583Message message) {
		if(message != null) return message.getExtra(byte[].class, PropertyName.RAW_RESPONSE);
		return null;
	}
	
	public static final void setExecutionHalted(final ISO8583Message message) {
		if(message != null) message.putExtra(PropertyName.HALT_FILTER_EXECUTION, Boolean.toString(true));
	}
	
	public static final boolean isExcutionHalted(final ISO8583Message message) {
		return Boolean.parseBoolean(message.getExtra(String.class, PropertyName.HALT_FILTER_EXECUTION));
	}
	
	
	public static final String getTransactionType(final ISO8583Message message) {
		if (message != null) return message.getExtra(String.class, PropertyName.TRANSACTION_TYPE);
		return null;
	}

	public static final void setTransactionType(final ISO8583Message message, final String type) {
		if(message != null) message.putExtra(PropertyName.TRANSACTION_TYPE, type);
	}
	
	public static void main(String[] args) {
		try {
			final byte[] bytes = getRawRequest(new ISO8583Message());
			System.out.println(bytes);
		} catch (Exception e) {e.printStackTrace();}
	}
}
