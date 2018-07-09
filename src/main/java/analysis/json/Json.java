package analysis.json;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.wiztools.xsdgen.ParseException;
import org.wiztools.xsdgen.XsdGen;

import java.io.*;

public class Json {

    private File file;

    public Json(String file) {
        this.file = new File(file);
    }

    public String toString() {
        StringBuilder newJson = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try (FileInputStream f = new FileInputStream(file);
             BufferedReader br = new BufferedReader(new InputStreamReader(f));) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                newJson.append(strLine);
                newJson.append(ls);
            }
            if (newJson.toString().length() != 0)
                return newJson.toString();
            else
                return null;
        } catch (IOException e) {
            return null;
        }
    }

    public void getXsd(String strJson, String pathXml, String pathXsd) {
        JSONObject jsonToXml = new JSONObject(strJson);
        String xml = XML.toString(jsonToXml);
        //преобразуем в xml
        try ( BufferedWriter writer = new BufferedWriter( new FileWriter(pathXml));)
        {
            writer.write(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //преобразуем в xsd
         try(BufferedWriter wr = new BufferedWriter( new FileWriter(pathXsd));) {
             String xsd = new XsdGen().parse(new File(pathXml)).toString();
             wr.write(xsd);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
