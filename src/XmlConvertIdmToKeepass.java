import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlConvertIdmToKeepass extends DefaultHandler {

	private boolean textOutputFLg = false;
	private boolean itemFlg = false;
	private StringBuilder bufstr = null;
	private StringBuilder output = null;

	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
		String inputFileName = null;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
			inputFileName = "input.xml";
		}
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxParserFactory.newSAXParser();
		saxParser.parse(new File(inputFileName), new XmlConvertIdmToKeepass());
		System.out.println("終了");
	}

	public void startDocument() {// [10]
		output = new StringBuilder();
		output.append("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>");
		output.append("\n");
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) {// [20]
		// System.out.println("[21] 要素開始 = " + qName);// [21]
		bufstr = new StringBuilder();
		if ("idmData".equals(qName)) {
			output.append("<KeePassFile><Root><Group>");
			output.append("\n");
			// System.out.println("<String><Key></Key><Value></Value></String>");
		} else if ("folder".equals(qName)) {
			output.append("<Group>");
			output.append("\n");
			if (attributes.getValue("name") != null) {
				output.append("<Name>" + attributes.getValue("name") + "</Name>");
				output.append("\n");
			}
		} else if ("item".equals(qName)) {
			output.append("<Entry>");
			output.append("\n");
			if (attributes.getValue("name") != null) {
				output.append("<String><Key>Title</Key><Value>" + attributes.getValue("name") + "</Value></String>");
				output.append("\n");
			}
			itemFlg = true;
		} else if ("account".equals(qName)) {
			bufstr.append("<String>");
			bufstr.append("<Key>UserName</Key>");
			textOutputFLg = true;
		} else if ("password".equals(qName)) {
			bufstr.append("<String>");
			bufstr.append("<Key>Password</Key>");
			textOutputFLg = true;
		} else if ("comment".equals(qName)) {
			bufstr.append("<String>");
			bufstr.append("<Key>Notes</Key>");
			textOutputFLg = true;
		} else if ("url".equals(qName)) {
			bufstr.append("<String>");
			bufstr.append("<Key>URL</Key>");
			textOutputFLg = true;
		} else if ("e-mail".equals(qName)) {
			bufstr.append("<String>");
			bufstr.append("<Key>e-mail</Key>");
			textOutputFLg = true;
		} else if ("serialNumber".equals(qName)) {
			bufstr.append("<String>");
			bufstr.append("<Key>SerialNumber</Key>");
			textOutputFLg = true;
		} else if (itemFlg && !"expirationDate".equals(qName) && !"issueDate".equals(qName)
				&& !"pasteType".equals(qName)) {
			if (attributes.getValue("name") != null) {
				bufstr.append("<String><Key>" + qName + "_Label</Key><Value>" + attributes.getValue("name") + "</Value></String>");
				bufstr.append("\n");
			}
			bufstr.append("<String>");
			bufstr.append("<Key>" + qName + "</Key>");
			textOutputFLg = true;
		}
	}

	public void characters(char[] ch, int offset, int length) {// [30]
		if (textOutputFLg) {
			String tmp = new String(ch, offset, length).trim();
			if (tmp.length() > 0) {
				output.append(bufstr.toString() + "<Value>" + tmp + "</Value></String>");
			}
		}
		textOutputFLg = false;

	}

	public void endElement(String uri, String localName, String qName) {//
		if ("idmData".equals(qName)) {
			output.append("</Group></Root></KeePassFile>");
		} else if ("folder".equals(qName)) {
			output.append("</Group>");
		} else if ("item".equals(qName)) {
			output.append("</Entry>");
			itemFlg = false;
		}
		output.append("\n");
	}

	public void endDocument() {
		// System.out.println(output.toString());
		File outputFile = new File("output.xml");
		// FileWriter filewriter;
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(output.toString());
			// filewriter = new FileWriter(outputFile);
			// filewriter.write(output.toString());
			bw.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	}

}
