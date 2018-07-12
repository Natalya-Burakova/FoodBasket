package analysis.xml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import product.Product;
import product.category.Category;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


public class Parser {
    private ObservableList<Product> listProduct;
    private File xmlFile;

    public Parser(File xmlFile) {
        this.xmlFile= xmlFile;
    }

    public void parce() {
        listProduct = FXCollections.observableArrayList();
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList list = doc.getElementsByTagName("product");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String price = element.getElementsByTagName("price").item(0).getTextContent();
                    String info = element.getElementsByTagName("info").item(0).getTextContent();
                    String category = element.getElementsByTagName("category").item(0).getTextContent();
                    Product product = new Product(name, Category.valueOf(category), info, price);
                    listProduct.add(product);
                }
            }
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    public ObservableList<Product> getListProduct() {
        return listProduct;
    }

}
