package org.util.iso8583;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.util.iso8583.util.Bitmap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class XMLEncoderDecoder {

	private static final boolean INDENT_XML           = true;
	private static final boolean OMIT_XML_DECLARATION = true;

	private static final String ROOT_TAG   = "isomsg";
	private static final String HEADER_TAG = "header";
	private static final String FIELD_TAG  = "field";

	private static final String ID_ATTRIBUTE    = "id";
	private static final String VALUE_ATTRIBUTE = "value";

	public static final ISO8583Message decode(final InputStream in) throws Exception {
		final ISO8583Message         message  = new ISO8583Message();
		final DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		final DocumentBuilder        builder  = factory.newDocumentBuilder();
		final Document               document = builder.parse(in);
		document.getDocumentElement().normalize();
		final Element root = document.getDocumentElement();
		final String  name = root.getTagName();
		if (!ROOT_TAG.equals(name)) throw new RuntimeException("unknown root node. root node not mached with '" + ROOT_TAG + "'");
		final NodeList headerNodes = root.getElementsByTagName(HEADER_TAG);
		if (headerNodes.getLength() > 0) {
			final Element netHeaderElement = (Element) headerNodes.item(0);
			final String  netHeader    = netHeaderElement.getTextContent();
			message.putNetHeader(netHeader);
		}
		final NodeList fieldNodes = root.getElementsByTagName(FIELD_TAG);
		final int      length     = fieldNodes.getLength();
		for (int i = 0; i < length; i++) {
			final Element field = (Element) fieldNodes.item(i);
			final String  id    = field.getAttribute(ID_ATTRIBUTE);
			final String  value = field.getAttribute(VALUE_ATTRIBUTE);
			message.put(Integer.parseInt(id), value);
		}
		return message;
	}

	public static final String encode(final ISO8583Message message) throws Exception {
		final DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		final DocumentBuilder        builder  = factory.newDocumentBuilder();
		final Document               document = builder.newDocument();
		final Element                root     = document.createElement(ROOT_TAG);
		document.appendChild(root);
		final String header = (String) message.getNetHeader();
		if (header != null) {
			final Element headerElement = document.createElement(HEADER_TAG);
			headerElement.setTextContent(header);
			root.appendChild(headerElement);
		}
		final Bitmap bitmap = message.bitmap;
		final Element mtiElement = document.createElement(FIELD_TAG);
		mtiElement.setAttribute(ID_ATTRIBUTE, Integer.toString(0));
		mtiElement.setAttribute(VALUE_ATTRIBUTE, message.get(0));
		root.appendChild(mtiElement);
		for (int i = 0; i < 129; i++) {
			if (bitmap.get(i)) {
				final Element element = document.createElement(FIELD_TAG);
				element.setAttribute(ID_ATTRIBUTE, Integer.toString(i));
				element.setAttribute(VALUE_ATTRIBUTE, message.get(i));
				root.appendChild(element);
			}
		}
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		final Transformer        transformer        = transformerFactory.newTransformer();
		if (INDENT_XML) transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		if (OMIT_XML_DECLARATION) transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	
		final StringWriter writer = new StringWriter();
		final DOMSource    source = new DOMSource(document);
		final StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		return writer.toString();
	}

}
