package utils;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLTool {

    public static Document createDocument(){
        //Create document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            System.err.println("Error in XML document creation"+ e.getMessage());
        }
        Document document = docBuilder.newDocument();
        return document;
    }

    public static void writeDocument(Document document, String fileName){
        //transform the DOM Object to an XML File

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName+".xml"));
            transformer.transform(domSource, streamResult);
        }
        catch (TransformerConfigurationException e) {
            System.err.println("Error in XML document transorm configuration"+ e.getMessage());
        }
        catch (TransformerException e) {
            System.err.println("Error in XML document transorm configuration"+ e.getMessage());
        }
    }
}
