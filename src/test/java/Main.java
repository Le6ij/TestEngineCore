import org.antlr.runtime.RecognitionException;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tables.SD_TRANSACTION_LOG;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        //String [] fields = new String[] {TECore.templateXMLField, "COMPONENT", "TRANSACTION_LOG_ID"};
        tm.getTemplates(sql,params);
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(new File("src/main/resources/sample.xml"));
//        Element root = doc.getDocumentElement();
//        parse(root);
        //System.out.println(node.getTextContent());

    }


    public void test(){

    }


    private static void parse(Element e) {
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
                parse(e);
            }
        }
    }

    private static Object[] getNextElementNode(NodeList nodeList, Integer index){
    //private static Node getNextElementNode(NodeList nodeList, Integer index){
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

    public static String getElementXpath(Element elt){
        String path = "";
        try{
            for (; elt != null; elt = (Element) elt.getParentNode()){
                int idx = getElementIndex(elt);
                String xname = elt.getTagName().toString();

                //if (idx >= 1) xname += "[" + idx + "]";
                path = "/" + xname + path;
            }
        }catch(Exception ee){
        }
        return path;
    }
    public static int getElementIndex(Element original) {
        int count = 1;

        for (Node node = original.getPreviousSibling(); node != null;
             node = node.getPreviousSibling()) {
            if (node instanceof Element) {
                Element element = (Element) node;
                if (element.getTagName().equals(original.getTagName())) {
                    count++;
                }
            }
        }
        return count;
    }

    public static NodeList getNodesByXpath(Document doc, String xPath){
        XPath path = XPathFactory.newInstance().newXPath();
        NodeList nodes = null;
        try {
            nodes = (NodeList)path.evaluate(xPath,
                    doc.getDocumentElement(), XPathConstants.NODESET);
                    return nodes;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
