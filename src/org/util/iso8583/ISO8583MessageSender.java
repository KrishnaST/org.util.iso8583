package org.util.iso8583;

import static org.util.iso8583.api.ISO8583ExceptionCause.SOCKET_CONNECT_ERROR;
import static org.util.iso8583.api.ISO8583ExceptionCause.SOCKET_WRITE_ERROR;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.util.iso8583.api.ISO8583Exception;
import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.util.ByteHexUtil;

public final class ISO8583MessageSender {

	public static final ISO8583Message send(final String host, final int port, final ISO8583Message request, final ISOFormat format, final int timeoutInMs) {
		try(final Socket socket = new Socket(host, port); 
			final InputStream in = socket.getInputStream(); 
			final OutputStream out = socket.getOutputStream()) {
			socket.setSoTimeout(timeoutInMs);
			final byte[] bytes = EncoderDecoder.encode(format, request);
			if(EncoderDecoder.debug) System.out.println("request bytes : "+ByteHexUtil.byteToHex(bytes));
			try {
				out.write(bytes);
				out.flush();
			} catch (Exception e) {
				throw new ISO8583Exception(SOCKET_WRITE_ERROR, "socket write error.", e);
			}
			return EncoderDecoder.readMessage(format, in);
		} catch (ISO8583Exception e) {
			throw e;
		} catch (Exception e) {
			throw new ISO8583Exception(SOCKET_CONNECT_ERROR, "socket connect error.", e);
		}
	}

	public static final ISO8583Message send(final Socket socket, final ISO8583Message request, final ISOFormat format, final int timeoutInMs) {
		try {
			final OutputStream out = socket.getOutputStream();
			final InputStream  in  = socket.getInputStream();
			socket.setSoTimeout(timeoutInMs);
			final byte[] bytes = EncoderDecoder.encode(format, request);
			if(EncoderDecoder.debug) System.out.println("request bytes : "+ByteHexUtil.byteToHex(bytes));
			try {
				out.write(bytes);
				out.flush();
			} catch (Exception e) {
				throw new ISO8583Exception(SOCKET_WRITE_ERROR, "socket write error.", e);
			}
			return EncoderDecoder.readMessage(format, in);
		} catch (ISO8583Exception e) { throw e;			
		} catch (Exception e) {
			throw new ISO8583Exception(SOCKET_CONNECT_ERROR, "socket connect error.", e);
		}
	}

}
