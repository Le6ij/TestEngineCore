
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;


/**
 * Created by alekbaga on 15.03.2017.
 */
public class TemplateManager{
    DBConnector dbConnector = new DBConnector();

    /**
     * Constructor of the class to maintain an existing DBConnector instance with all variables
     * @param db Existing DBConnector instance
     */
    public TemplateManager(DBConnector db){
        this.dbConnector = db;
        System.out.println(dbConnector.getTable());
    }
    public TemplateManager(){
    }

    public DBConnector getDBConnector(){
        return this.dbConnector;
    }

    public void setDbConnector(DBConnector dbConnector){
        this.dbConnector = dbConnector;
    }

    public void getTemplates(String tableName, String sql, Object... params){
        //Check if there is an existing connection to DB otherwise connect to default
        if(!Base.hasConnection()){
            dbConnector.connect();
        }
        List<Model> records = dbConnector.fetchDataFromTable(tableName,sql, params);
        for(Model u: records){
            System.out.println(u.getString(TECore.templateXMLField));
        }
    }

    public static void maskDynamicFields(Element e){
        NodeList children = e.getChildNodes();
        //HashMap<Node,Integer> map = new HashMap<Node, Integer>();
        Object[] arr;
        for (int i = 0; i < children.getLength(); i++) {
            //Node n = children.item(i);
            Node n;
            if(XMLUtils.getNextElementNode(children,i)!= null){
                arr = XMLUtils.getNextElementNode(children,i);
                n = (Node)arr[0];
                i = (Integer)arr[1];
                e = (Element) n;
                if (e.getTagName() == "value"){
                    if(e.getAttribute("name").contains(":Name")){
                        System.out.print(XMLUtils.getElementXpath(e,true).substring(0,XMLUtils.getElementXpath(e,true).lastIndexOf("/")+1));
                        System.out.print(e.getTextContent()+" = ");
                        if(XMLUtils.getNextElementNode(children,i)!= null) {
                            arr = XMLUtils.getNextElementNode(children, i+1);
                            n = (Node) arr[0];
                            i = (Integer) arr[1];
                            e = (Element) n;
                            System.out.println(e.getTextContent());
                        }
                    } else {
                        System.out.print(XMLUtils.getElementXpath(e,true)+"/");
                        System.out.println(e.getAttribute("name")+" = "+e.getTextContent());

                    }
                }
                maskDynamicFields(e);
            }
        }
    }

    public void getTemplates(String sql, Object... params){
        getTemplates(dbConnector.tableName,sql,params);
    }



    private void storeTemplates() {

    }

    private void substituteParameters(){

    }





}
