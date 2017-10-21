/**
 * 
 * @author Jyoti Sharma
 *
 */

package edu.neu.controller;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.digester.Digester;
import org.apache.lucene.document.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

public class FileParser {

	public Path checkFile(Path path) throws Exception {
		if (path.toString().contains("txt")) {
			try {
				Files.newInputStream(path);
				return path;
			} catch (Exception e) {

			}

		} else if (path.toString().contains("html")) {
			InputStream stream = Files.newInputStream(path);
			Tidy tidy = new Tidy();
			tidy.setQuiet(true);
			tidy.setShowWarnings(false);
			org.w3c.dom.Document root = tidy.parseDOM(stream, null);
			Element rawDoc = root.getDocumentElement();

			new Document();
			getTitle(rawDoc);
			getBody(rawDoc);
			return path;

		} else if ((path.toString().contains("xml"))) {

			Digester dig = new Digester();
			dig.setValidating(false);
			InputStream stream = Files.newInputStream(path);
			path = (Path) dig.parse(stream);
			return path;

		} else if ((path.toString().contains("pdf"))) {
			
			return path;

		} else {
			System.out.println("Wrong file type;");
		}

		return null;
	}

	protected static String getTitle(Element rawDoc) {
		if (rawDoc == null) {
			return null;
		}

		String title = "";

		NodeList children = rawDoc.getElementsByTagName("title");
		if (children.getLength() > 0) {
			Element titleElement = ((Element) children.item(0));
			Text text = (Text) titleElement.getFirstChild();
			if (text != null) {
				title = text.getData();
			}
		}
		return title;
	}

	protected static String getBody(Element rawDoc) {
		if (rawDoc == null) {
			return null;
		}

		String body = "";
		NodeList children = rawDoc.getElementsByTagName("body");
		if (children.getLength() > 0) {
			body = getText(children.item(0));
		}
		return body;
	}

	protected static String getText(Node node) {
		NodeList children = node.getChildNodes();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			switch (child.getNodeType()) {
			case Node.ELEMENT_NODE:
				sb.append(getText(child));
				sb.append(" ");
				break;
			case Node.TEXT_NODE:
				sb.append(((Text) child).getData());
				break;
			}
		}
		return sb.toString();
	}

}
