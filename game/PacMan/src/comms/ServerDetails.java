package comms;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * comms.ServerDetails
 * A class which handles reading and saving server address and port information
 * stored in xml files.
 * Created: 17-Mar-2009
 * @author wob
 */
public class ServerDetails {

	/**
	 *  Parses the information from an XML file into a Document object.
	 * @param location The filename and location of the xml file.
	 * @return A document object representing a server address and port.
	 */
	private static Document parseFileToDocument(String location) {

		DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		/* Ignore comments while parsing the file. */
		dBF.setIgnoringComments(true);

		/* Create the DocumentBuilder. */
		try {
			builder = dBF.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		/* Set the input source to the required file. */
		InputSource input = new InputSource(location);
		Document doc = null;

		/* Parses the file into a Document. */
		try {
			doc = builder.parse(input);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Error: The xml document containing the server" +
					" details was not found at " + location);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Gets the details (address and port) of a server from a specially
	 * formatted XML file.
	 * @param fileName The filename of an xml file containing 
	 * server details.
	 * @return The server address and port in an array of Strings:
	 * slot 0 contains the server address and slot 1 contains the port number.
	 */
	public static String[] getServerDetails(String fileName) {
            String[] result = null;
            try {
                result = documentToString(parseFileToDocument(fileName + ".xml"));
            }
            catch (NullPointerException e) {
                //e.printStackTrace();
            }
            
            return result;
	}

	/**
	 * Converts a Document object representing a server into an array of strings
	 * containing the server details.
	 * @param doc A Document object created from an XML file.
	 * @return An array of Strings representing a server address and port.
	 */
	private static String[] documentToString(Document doc) {
		String[] details = new String[2];

		/* Get the root of the tree. */
		Element root = doc.getDocumentElement();

		/* Get a list of servers. */
		NodeList serverDetails = root.getElementsByTagName("server");

		/* Get the first server in the list (there should only be one). */
		Element serverElement = (Element) serverDetails.item(0);

		/* Retrieves the server address for that element. */
		NodeList addressList = serverElement.getElementsByTagName("address");
		Element addressElement = (Element) addressList.item(0);
		Text addressText = (Text) addressElement.getFirstChild();
		/* Store a String representation of the server address in the array. */
		details[0] = addressText.getNodeValue();

		NodeList portList = serverElement.getElementsByTagName("port");
		Element portElement = (Element) portList.item(0);
		Text portText = (Text) portElement.getFirstChild();
		/* Store a String representation of the port number in the array. */
		details[1] = portText.getNodeValue();


		return details;
	}

	/**
	 * 
	 * @param address The address of a server.
	 * @param port The port number of a server.
	 * @param fileName The name you wish to 
	 */
	public static void saveServerToFile(String address, String port, String fileName) {
		Document doc = null;
		DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = dBF.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException parserException) {
			parserException.printStackTrace();
		}

		/* Create root */
		Element root = doc.createElement("Servers");
		doc.appendChild(root);

		/* Add comment. */
		Comment comment = doc.createComment("Server Details");
		root.appendChild(comment);

		/* Create elements for the server details. */
		Element addressElement = doc.createElement("address");
		Element portElement = doc.createElement("port");

		/* Add Strings to those elements. */
		addressElement.appendChild(doc.createTextNode(address));
		portElement.appendChild(doc.createTextNode(port));

		/* Create the server element and append the details. */
		Element serverElement = doc.createElement("server");
		serverElement.appendChild(addressElement);
		serverElement.appendChild(portElement);

		/* Define the element as a node and append it to the root. */
		Node serverNode = serverElement;
		root.appendChild(serverNode);

		/* Output the xml to the disk. */
		try {
			/* Create a DOMSource for source XML document */
			Source source = new DOMSource(doc);

			/* Create StreamResult to store the result. */
			Result result = new StreamResult(new FileOutputStream(fileName + ".xml"));

			/* Create TransformerFactory. */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			/* Create the transformer. */
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes"); // Indent it so I can read it please.

			/* Do some transforming. */
			transformer.transform(source, result);
		} /* Catches for Transformer exceptions */ catch (TransformerFactoryConfigurationError factoryError) {
			factoryError.printStackTrace();
		} catch (TransformerException transformerError) {
			transformerError.printStackTrace();
                } catch (FileNotFoundException e) {
                        System.out.println("Invalid filename, server could not " +
                                "be saved.");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
