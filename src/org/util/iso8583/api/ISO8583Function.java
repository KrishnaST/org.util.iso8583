package org.util.iso8583.api;

import java.util.Map;

import org.util.iso8583.util.Bitmap;

@FunctionalInterface
public interface ISO8583Function<R>{

	public R apply(String[] data, Bitmap bitmap, Map<String, Object> extra);
}
