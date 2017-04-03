
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
        String data;
        String component;
        //Check if there is an existing connection to DB otherwise connect to default
        if(!Base.hasConnection()){
            dbConnector.connect();
        }
        List<Model> records = dbConnector.fetchDataFromTable(tableName,sql, params);
        for(Model u: records){
            System.out.println(u.getString(TECore.templateXMLField));
        }
    }

    public void getTemplates(String sql, Object... params){
        getTemplates(dbConnector.tableName,sql,params);
    }

    public void parseXML(Element e){
        NodeList children = e.getChildNodes();
        //HashMap<Node,Integer> map = new HashMap<Node, Integer>();
        Object[] arr = new Object [2];
        for (int i = 0; i < children.getLength(); i++) {
            //Node n = children.item(i);
            Node n = null;
            if(getNextElementNode(children,i)!= null){
                arr = getNextElementNode(children,i);
                n = (Node)arr[0];
                i = (Integer)arr[1];
                e = (Element) n;
                if (e.getTagName() == "value"){
                    if(e.getAttribute("name").contains(":Name")){
                        System.out.print(getElementXpath(e).substring(0,getElementXpath(e).lastIndexOf("/")+1));
                        System.out.print(e.getTextContent()+" = ");
                        if(getNextElementNode(children,i)!= null) {
                            arr = getNextElementNode(children, i+1);
                            n = (Node) arr[0];
                            i = (Integer) arr[1];
                            e = (Element) n;
                            System.out.println(e.getTextContent());
                        }

                    } else {
                        System.out.print(getElementXpath(e)+"/");
                        System.out.println(e.getAttribute("name")+" = "+e.getTextContent());
                    }
                }
                parseXML(e);
            }
        }
    }

    private static Object[] getNextElementNode(NodeList nodeList, Integer index){
        Object[] arr = new Object[2];
        for(;index < nodeList.getLength(); ++index){
            Node n = nodeList.item(index);
            if (n instanceof Element) {
                arr[0]= n;
                arr[1]= index;
                return arr;
            }
        } return null;
    }

    /**
     * Get Xpath to the provided element
     * @param elt Element that path will be constucted for
     * @return
     */
    private static String getElementXpath(Element elt){
        String path = "";
        try{
            for (; elt != null; elt = (Element) elt.getParentNode()){
                int idx = getElementIndex(elt);
                String xname = elt.getTagName().toString();

                if (idx >= 1) xname += "[" + idx + "]";
                path = "/" + xname + path;
            }
        }catch(Exception ee){
        }
        return path;
    }

    /**
     * Get Element index for XPath construction
     * @param element Element node
     * @return
     */
    private static int getElementIndex(Element element) {
        int count = 1;

        for (Node node = element.getPreviousSibling(); node != null;
             node = node.getPreviousSibling()) {
            if (node instanceof Element) {
                Element e = (Element) node;
                if (e.getTagName().equals(element.getTagName())) {
                    count++;
                }
            }
        }
        return count;
    }

    private void storeTemplates() {

    }

    private void substituteParameters(){

    }





}
