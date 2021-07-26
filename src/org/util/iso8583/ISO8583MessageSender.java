package org.util.iso8583;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.Index;

public final class ISO8583MessageSender {

	public static final ISO8583Message send(final String host, final int port, final ISO8583Message request, final ISOFormat format, final int timeoutInMs) throws IOException  {
		try(final Socket socket = new Socket(host, port);
			final InputStream in = socket.getInputStream();
			final OutputStream out = socket.getOutputStream()){
			socket.setSoTimeout(timeoutInMs);
			final byte[] bytes = EncoderDecoder.encode(format, request);
			out.write(bytes);
			out.flush();
			return readMessage(format, in);
		} 
	}
	
	public static final ISO8583Message send(final Socket socket, final ISO8583Message request, final ISOFormat format, final int timeoutInMs) throws IOException  {
		final OutputStream out = socket.getOutputStream();
		final InputStream in = socket.getInputStream();
		socket.setSoTimeout(timeoutInMs);
		final byte[] requestBytes = EncoderDecoder.encode(format, request);
		out.write(requestBytes);
		out.flush();
		return readMessage(format, in);
	}

	public static final ISO8583Message readMessage(final ISOFormat format, final Socket socket, final int timeoutInMs) throws IOException {
		socket.setSoTimeout(timeoutInMs);
		return readMessage(format, socket.getInputStream());
	}
	
	public static final ISO8583Message readMessage(final ISOFormat format, final InputStream in) throws IOException {
		return EncoderDecoder.decode(format, read(format, in));
	}

	public static final byte[] read(final ISOFormat format, final InputStream in) throws IOException {
		final int lenLen = format.getMessageLengthLength();
		final byte[] lenBytes = new byte[lenLen];
		int readCount = in.read(lenBytes);
		if(readCount != lenLen) throw new RuntimeException("inputstream has insufficient data for message length header.");
		final int length = format.getMessageLengthEncoder().decode(lenBytes, new Index(), lenLen);
		final byte[] bytes = new byte[length];
		readCount = in.read(bytes);
		if(readCount != lenLen) throw new RuntimeException("inputstream has insufficient data for message length of "+length);
		return bytes;
	}
	
	
}
