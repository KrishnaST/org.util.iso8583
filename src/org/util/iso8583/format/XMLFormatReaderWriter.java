package org.util.iso8583.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.util.iso8583.api.Encoding;
import org.util.iso8583.api.ISOFormat;
import org.util.iso8583.api.LengthEncoding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class XMLFormatReaderWriter {

	public static final ISOFormat readFormat(final InputStream in) throws Exception {
		final DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		final DocumentBuilder        builder  = factory.newDocumentBuilder();
		final Document               document = builder.parse(in);
		document.getDocumentElement().normalize();
		final Element formatElement = (Element) document.getElementsByTagName("format").item(0);
		final String  name          = formatElement.getAttribute("name");

		final int  messageLengthLength = formatElement.getAttribute("message-length-length") == null ? 0 : Integer.parseInt(formatElement.getAttribute("message-length-length"));
		final char bcdPadChar          = formatElement.getAttribute("bcd-pad-char") == null ? '0' : formatElement.getAttribute("bcd-pad-char").toCharArray()[0];

		final ISOFormat format          = new CustomFormat(name, messageLengthLength, bcdPadChar);
		final String    netHeaderLength = formatElement.getAttribute("net-header-length");
		if (netHeaderLength != null) format.length[ISOFormat.NET_HEADER_INDEX] = Integer.parseInt(netHeaderLength);
		final String netHeaderEncoding = formatElement.getAttribute("net-header-encoding");
		format.encoder[ISOFormat.NET_HEADER_INDEX] = netHeaderEncoding == null ? Encoding.BIN.encoder : Encoding.valueOf(netHeaderEncoding).encoder;

		final NodeList length_elements = document.getElementsByTagName("length-encoding");
		if (length_elements.item(0) != null) {
			final Element lengthEncodings       = (Element) length_elements.item(0);
			final String  messageLengthEncoding = lengthEncodings.getAttribute("MESSAGE");
			final String  charLengthEncoding    = lengthEncodings.getAttribute("CHAR");
			final String  binLengthEncoding     = lengthEncodings.getAttribute("BIN");
			final String  bcdLengthEncoding     = lengthEncodings.getAttribute("BCD");

			format.lencoder[ISOFormat.MESSAGE_LENGTH_ENCODER_INDEX] = messageLengthEncoding == null ? LengthEncoding.BIN.encoder : LengthEncoding.valueOf(messageLengthEncoding).encoder;
			format.lencoder[ISOFormat.CHAR_LENGTH_ENCODER_INDEX]    = charLengthEncoding == null ? LengthEncoding.CHAR.encoder : LengthEncoding.valueOf(charLengthEncoding).encoder;
			format.lencoder[ISOFormat.BIN_LENGTH_ENCODER_INDEX]     = binLengthEncoding == null ? LengthEncoding.BIN.encoder : LengthEncoding.valueOf(binLengthEncoding).encoder;
			format.lencoder[ISOFormat.BCD_LENGTH_ENCODER_INDEX]     = bcdLengthEncoding == null ? LengthEncoding.BCD.encoder : LengthEncoding.valueOf(bcdLengthEncoding).encoder;
		}

		final NodeList elements = document.getElementsByTagName("element");
		for (int i = 0; i < elements.getLength(); i++) {
			final Element   element = (Element) elements.item(i);
			final int id      = Integer.parseInt(element.getAttribute("id"));
			format.length[id]  = Integer.parseInt(element.getAttribute("length"));
			format.encoder[id] = Encoding.valueOf(element.getAttribute("encoding")).encoder;
		}
		return format;
	}

	public static final ISOFormat readFormat(final File file) throws Exception {
		return readFormat(new FileInputStream(file));
	}

	public static final ISOFormat readFormat(final String filePath) throws Exception {
		return readFormat(new FileInputStream(filePath));
	}

	public static final void writeFormat(final ISOFormat format, final Writer writer) throws Exception {
		final DocumentBuilderFactory factory     = DocumentBuilderFactory.newInstance();
		final DocumentBuilder        builder     = factory.newDocumentBuilder();
		final Document               document    = builder.newDocument();
		final Element                rootElement = document.createElement("format");
		rootElement.setAttribute("name", format.name);
		rootElement.setAttribute("message-length-length", Integer.toString(format.getMessageLengthLength()));
		rootElement.setAttribute("bcd-pad-char", "" + format.getBCDPadChar());
		rootElement.setAttribute("net-header-length", Integer.toString(format.getNetHeaderLength()));
		rootElement.setAttribute("net-header-encoding", format.getNetHeaderEncoding().name());
		document.appendChild(rootElement);

		final Element lengthEncodingElement = document.createElement("length-encoding");
		lengthEncodingElement.setAttribute("MESSAGE", format.getMessageLengthEncoder().name());
		lengthEncodingElement.setAttribute("CHAR", format.getCHARLengthEncoder().name());
		lengthEncodingElement.setAttribute("BIN", format.getBINLengthEncoder().name());
		lengthEncodingElement.setAttribute("BCD", format.getBCDLengthEncoder().name());

		rootElement.appendChild(lengthEncodingElement);

		for (int i = 0; i < 129; i++) {
			final Element element = document.createElement("element");
			element.setAttribute("id", Integer.toString(i));
			element.setAttribute("length", Integer.toString(format.length[i]));
			element.setAttribute("encoding", format.encoder[i].name());
			rootElement.appendChild(element);
		}
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		final Transformer        transformer        = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		final DOMSource    source = new DOMSource(document);
		final StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
	}

	public static void main(String[] args) throws IOException, Exception {
		XMLFormatReaderWriter.writeFormat(NPCIFormat.getInstance(), new FileWriter("npci.xml"));
		
	}
}
