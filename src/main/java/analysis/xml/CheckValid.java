package analysis.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

public class CheckValid {

    private CheckValid() {}

    public static boolean validate(File xml, File xsd){
        try {
            if (validXsd(new InputSource(new FileInputStream(xml)), xsd))
                return true;
        } catch (FileNotFoundException e) {
            return false;
        }
        return false;
    }

    public static boolean validate(String xml, File xsd ) {
        if (validXsd(new InputSource(new StringReader(xml)), xsd))
            return true;
        return false;
    }

    private static boolean validXsd(InputSource xml, File xsd){
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
            // Статичный объект newInstance() позволяет создавать объекты SchemaFactory используя заданную XML схему
            SchemaFactory shemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // Загрузка схемы
            Schema schema  = shemaFactory.newSchema(new StreamSource(xsd));
            // CheckValid используется для проверки правильности документа.
            Validator validator = schema.newValidator();
            // Поверка правильности дерева DOM.
            validator.validate(new DOMSource(document));
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (ParserConfigurationException e) {
            return false;
        }
        return true;
    }

}
