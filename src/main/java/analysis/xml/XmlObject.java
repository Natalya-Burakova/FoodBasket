package analysis.xml;

import org.wiztools.xsdgen.ParseException;
import org.wiztools.xsdgen.XsdGen;

import java.io.*;

public class XmlObject {

    private File file;

    public XmlObject(String file) {
        this.file = new File(file);
    }

    public File getXml(){
        return file;
    }

}
