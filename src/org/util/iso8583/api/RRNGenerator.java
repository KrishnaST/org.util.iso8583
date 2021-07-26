package org.util.iso8583.api;

import java.time.LocalDateTime;
import java.util.Date;

import org.util.iso8583.ISO8583DateFormat;

public abstract class RRNGenerator {

	public abstract String nextRRN();
	
	public abstract String nextRRN(final Date date);
	
	public abstract String nextRRN(final LocalDateTime date);

	public static RRNGenerator newInstance(final StanGenerator stanGenerator) {
		return new RRNGenerator() {
			public final String nextRRN() {
				return ISO8583DateFormat.getRRNSuffix(LocalDateTime.now()) + stanGenerator.nextStan();
			}

			@Override
			public final String nextRRN(final Date date) {
				return ISO8583DateFormat.getRRNSuffix(date) + stanGenerator.nextStan();
			}

			@Override
			public final String nextRRN(final LocalDateTime date) {
				return ISO8583DateFormat.getRRNSuffix(date) + stanGenerator.nextStan();
			}
		};
	}
}
