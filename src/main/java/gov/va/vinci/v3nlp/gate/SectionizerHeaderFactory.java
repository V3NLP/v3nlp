package gov.va.vinci.v3nlp.gate;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.validator.GenericValidator;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import gate.creole.ExecutionException;
import hitex.util.Header;
import hitex.util.InitializationException;

public final class SectionizerHeaderFactory {

	private static final Logger LOG = Logger
			.getLogger(SectionizerHeaderFactory.class.getName());

	/**
	 * The name of the header node in the XML file containing section header
	 * definitions.
	 */
	public static final String HEADER_NODE = "header";

	/**
	 * The name of the attribute of the header node in the XML file containing
	 * section header definitions that contains the comma-separated list of the
	 * section header categories.jsp
	 */
	public static final String HEADER_CATEGS_ATTR = "categories";

	/**
	 * The capturing group number for the pattern defining the section header in
	 * the XML file containing section header definitions. This is a required
	 * attribute of each section header.
	 */
	public static final String HEADER_CAPT_GROUP_NUM_ATTR = "captGroupNum";

	private SectionizerHeaderFactory() {

	}

	/**
	 * Transforms the DOM tree into the list of <code>Header</code>s.
	 * 
	 * @param doc
	 *            the DOM tree to parse
	 * @return the list of <code>Header</code>s.
	 * @throws ExecutionException
	 *             if parsing the DOM tree causes errors.
	 */
	public static List<Header> getHeaders(org.w3c.dom.Document doc)
			throws ExecutionException {

		if (doc == null) {
			String message = "DOM document is null.";
			LOG.severe(message);
			throw new ExecutionException(message);
		}

		// Parse the DOM tree to extract header names, regular expressions
		// defining the headers and capturing group numbers
		Element root = doc.getDocumentElement();

		// Create a list to hold the headers
		List<Header> headers = new ArrayList<Header>();

		// Get the list of header nodes. Each of these nodes has
		// two attributes:
		// 'categories.jsp' - comma-separated list of header categories.jsp
		// 'captGroupNum' - the capturing group number defining
		// the header inside of the regular expression

		NodeList headerNodes = root.getElementsByTagName(HEADER_NODE);

		for (int i = 0; i < headerNodes.getLength(); i++) {

			// current header
			org.w3c.dom.Node headerNode = headerNodes.item(i);

			NamedNodeMap attrs = headerNode.getAttributes();

			org.w3c.dom.Node categsAttr = attrs
					.getNamedItem(HEADER_CATEGS_ATTR);

			validateNotNull(categsAttr, "Header node doesn't contain "
					+ "the required '" + HEADER_CATEGS_ATTR + "' attribute.");

			// get the categories.jsp value
			String categories = categsAttr.getNodeValue();
			if (GenericValidator.isBlankOrNull(categories)) {
				String message = "Header categories.jsp name cannot be null or empty.";
				LOG.severe(message);
				throw new ExecutionException(message);
			}
			categories = categories.trim();

			org.w3c.dom.Node captGroupNumAttr = attrs
					.getNamedItem(HEADER_CAPT_GROUP_NUM_ATTR);

			validateNotNull(captGroupNumAttr, "Header node doesn't contain "
					+ "the required '" + HEADER_CAPT_GROUP_NUM_ATTR
					+ "' attribute.");

			// get the capturing group number value
			String stringCaptGroupNum = captGroupNumAttr.getNodeValue();
			int captGroupNum = 0;
			if (!GenericValidator.isInt(stringCaptGroupNum)) {
				String message = "Capturing group number is invalid (and cannot be empty): '"
						+ stringCaptGroupNum + "'. Switching to default = 0.";
				LOG.severe(message);
			} else {
				captGroupNum = Integer.valueOf(stringCaptGroupNum);
			}

			// Retrieve the CDATA node containing the regular expression
			// defining the header
			NodeList regexList = headerNode.getChildNodes();

			validateNotNull(
					regexList,
					"Header node doesn't contain the required "
							+ "CDATA child defining the header regular expression.");

			inner: for (int j = 0; j < regexList.getLength(); j++) {

				org.w3c.dom.Node regex = regexList.item(j);

				validateNotNull(regex, "The header's child node is null!");

				if (regex.getNodeType() != org.w3c.dom.Node.CDATA_SECTION_NODE) {
					continue;
				}

				String headerName = regex.getNodeValue();

				validateNotNull(headerName,
						"Error: the regular expression defining the header "
								+ "is null; XML document is invalid");

				Header h = null;
				try {
					h = new Header(headerName, captGroupNum, categories);
					headers.add(h); // add newly created header
				} catch (InitializationException e) {
					String message = "Error creating a header from pattern '"
							+ headerName + "': " + e.getMessage()
							+ "; header was skipped.";
					LOG.warning(message);
				}

				break inner;
			}

		}
		return headers;
	}

	private static void validateNotNull(Object obj, String message)
			throws ExecutionException {
		if (obj == null) {
			LOG.severe(message);
			throw new ExecutionException(message);
		}
	}

	/**
	 * Loads the <code>Header</code>s from the external XML file and returns the
	 * <code>List</code> of them
	 * 
	 * @throws ExecutionException
	 *             if loading fails.
	 */
	public static List<Header> getHeaders(File headerFile)
			throws ExecutionException {

		if (headerFile == null) {
			String message = "Headers file is null.";
			LOG.severe(message);
			throw new ExecutionException(message);
		}

		byte[] buffer = new byte[(int) headerFile.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(headerFile));
			f.read(buffer);
		} catch (Exception e) {
			String message = "Unable to read headers from XML file: " + "'"
					+ headerFile + "'.";
			LOG.severe(message);
			throw new ExecutionException(message);
		} finally {
			try {
				f.close();
			} catch (Exception e) {

			}
		}

		String fileContents = new String(buffer);

		org.w3c.dom.Document doc = parseXmlFile(fileContents, false);

		return getHeaders(doc);
	}

	/**
	 * Parses an XML file and returns a DOM document. If validating is true, the
	 * contents is validated against the DTD specified in the file.
	 * 
	 * @param url
	 *            the url of the XML file
	 * @param validating
	 *            if true, the contents of the file is validated against the DTD
	 *            specified in the file.
	 * @throws ExecutionException
	 *             if there is a problem reading the XML file
	 */
	protected static org.w3c.dom.Document parseXmlFile(String headerFile,
			boolean validating) throws ExecutionException {

		org.w3c.dom.Document doc = null;
		ByteArrayInputStream bs = new ByteArrayInputStream(headerFile
				.getBytes());
		try {
			// Create a builder factory
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(validating);
			// Create the builder and parse the file

			doc = factory.newDocumentBuilder().parse(bs);
		} catch (Exception e) {
			// A parsing error occurred; the xml input is not valid
			LOG.severe(e.getMessage());
			throw new ExecutionException(e.getMessage());
		}
		return doc;
	}
}
