package org.util.iso8583;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.util.iso8583.format.NPCIFormat;
import org.util.iso8583.format.TestFormat;
import org.util.iso8583.util.ByteHexUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
		final List<String> list = Files.lines(new File("coreconnect.log").toPath()).filter(l -> l.contains("write to npci : ") || l.contains("read from npci : ")).collect(Collectors.toList());
		System.out.println("list size : "+list.size());
		for (String string : list) {
			String messageString = string.contains("write to npci : ") ? string.split("write to npci : ")[1].substring(4) : string.split("read from npci : ")[1]; 
			//System.out.println((i++)+" : "+messageString);
			final byte[] bytes = ByteHexUtil.hexToByte(messageString);
			final ISO8583Message message = EncoderDecoder.decode(NPCIFormat.getInstance(), bytes);
			final byte[] bytes1 = EncoderDecoder.encode(TestFormat.getInstance(), message);
			System.out.println("bytes1 : "+ByteHexUtil.byteToHex(bytes1));
			final ISO8583Message message1 = EncoderDecoder.decode(TestFormat.getInstance(), Arrays.copyOfRange(bytes1, TestFormat.getInstance().getMessageLengthSize(), bytes1.length));
			System.out.println(EncoderDecoder.log(message));
			System.out.println(EncoderDecoder.log(message1));
			if(!message.equals(message1)) throw new RuntimeException("false");
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}
}
