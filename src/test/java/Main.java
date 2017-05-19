import org.javalite.activejdbc.Base;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by alekbaga on 14.03.2017.
 */
public class Main {
    public static void main(final String[] args) throws Exception {
        DBConnector db = new DBConnector();
        TemplateManager tm = new TemplateManager(db);
        db.connect();
        db.setTable("SD_TRANSACTION_LOG");
        String sql = TECore.baselineSQL;
        Object[] params = new Object[]{"AC4C3F68-5FA1-11e6-8402-001A4A10231F"};//global_id
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("src/main/resources/sample.xml"));
        Element root = doc.getDocumentElement();
        tm.maskDynamicFields(root);


    }
}
