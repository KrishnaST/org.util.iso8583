package org.util.iso8583.api;

import java.util.concurrent.atomic.AtomicInteger;

import org.util.iso8583.internals.ThreadLocals;

@FunctionalInterface
public interface StanGenerator {

	public abstract String nextStan();

	public static StanGenerator newInstanceOfRandom() {
		return new StanGenerator() {
			public final String nextStan() {
				return String.format("%06d", ThreadLocals.RANDOM.get().nextInt(999999));
			}
		};
	}

	public static StanGenerator newInstanceOfSequencial() {
		return new StanGenerator() {
			private final AtomicInteger counter = new AtomicInteger(1);
			public final String nextStan() {
				final int    stanInt = counter.getAndIncrement() & 0xFFFFFFFF;
				final String stan    = String.format("%06d", stanInt & 999999);
				return stan.substring(stan.length() - 6);
			}
		};
	}
	
	public static StanGenerator newInstanceOfSequencial(final int seed) {
		return new StanGenerator() {
			private final AtomicInteger counter = new AtomicInteger(seed);
			public final String nextStan() {
				final int    stanInt = counter.getAndIncrement() & 0xFFFFFFFF;
				final String stan    = String.format("%06d", stanInt & 999999);
				return stan.substring(stan.length() - 6);
			}
		};
	}

}
