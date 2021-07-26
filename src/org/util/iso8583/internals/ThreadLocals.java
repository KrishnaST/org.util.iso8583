package org.util.iso8583.internals;

import java.security.SecureRandom;

public final class ThreadLocals {

	public static final ThreadLocal<SecureRandom> RANDOM = ThreadLocal.withInitial(()-> new SecureRandom());
		
}
