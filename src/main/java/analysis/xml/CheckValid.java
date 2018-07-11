package analysis.xml;

import org.w3c.dom.Document;
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
import java.io.File;
import java.io.IOException;

public class CheckValid {

    private CheckValid() {}

    public static boolean validate(File xml, File xsd ){
        try {
            // Статичный объект newInstance() позволяет создавать объекты SchemaFactory используя заданную XML схему
            SchemaFactory shemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // Загрузка схемы
            Schema schema = shemaFactory.newSchema(new StreamSource(xsd));
            // CheckValid используется для проверки правильности документа.
            Validator validator = schema.newValidator();
            //  парсер, который создает деревья объектов DOM из документов XML.
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(xml);
            // Поверка правильности дерева DOM.
            validator.validate(new DOMSource(document));

        } catch (ParserConfigurationException e) {
            return false;
        } catch (SAXParseException e) {
            return false;
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
