package org.util.iso8583;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public final class ISO8583DateFormat {

	private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
	
	private static final ThreadLocal<SimpleDateFormat> localTimeFormat    = ThreadLocal.withInitial(() -> new SimpleDateFormat("HHmmss"));
	private static final ThreadLocal<SimpleDateFormat> localDateFormat    = ThreadLocal.withInitial(() -> new SimpleDateFormat("MMdd"));
	private static final ThreadLocal<SimpleDateFormat> transmissionFormat = ThreadLocal.withInitial(() -> {
		final SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		sdf.setTimeZone(UTC_TIME_ZONE);
		return sdf;
	});
	
	private static final ThreadLocal<SimpleDateFormat> rrnSuffixFormat    = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyDDDHH"));
	
	private static final DateTimeFormatter localTime    = DateTimeFormatter.ofPattern("HHmmss");
	private static final DateTimeFormatter localDate    = DateTimeFormatter.ofPattern("MMdd");
	private static final DateTimeFormatter transmission = DateTimeFormatter.ofPattern("MMddHHmmss").withZone(ZoneOffset.UTC);
	private static final DateTimeFormatter rrnSuffix    = DateTimeFormatter.ofPattern("yDDDHH");
	private static final DateTimeFormatter expiryYYMM   = DateTimeFormatter.ofPattern("yyyyMMdd");

	//DateTimeFormatter.ofPattern("yyyyMMdd");

	public static final String getLocalTime(final LocalDateTime time) {
		return time.format(localTime);
	}
	
	public static final String getLocalTime(final Date time) {
		return localTimeFormat.get().format(time);
	}

	public static final String getLocalDate(final LocalDateTime date) {
		return date.format(localDate);
	}

	public static final String getLocalDate(final Date time) {
		return localDateFormat.get().format(time);
	}

	public static final String getTransmissionTime(final LocalDateTime time) {
		return ZonedDateTime.of(time, ZoneId.systemDefault()).format(transmission);
	}
	
	public static final ZonedDateTime toTransmissionTime(final String time) {
		return ZonedDateTime.parse(time, transmission);
	}

	public static final String getTransmissionTime(final Date time) {
		return transmissionFormat.get().format(time);
	}

	public static final String getRRNSuffix(final LocalDateTime time) {
		return time.format(rrnSuffix).substring(3);
	}
	
	public static final String getRRNSuffix(final Date time) {
		return rrnSuffixFormat.get().format(time).substring(3);
	}

	public static final boolean isExpiredYYMM(final String yymm) {
		final LocalDate date = LocalDate.parse("20" + yymm + "01", expiryYYMM);
		final LocalDate end  = date.with(lastDayOfMonth());
		return end.isBefore(LocalDate.now());
	}

	public static void main(String[] args) {
		System.out.println(isExpiredYYMM("2001"));
	}

}
