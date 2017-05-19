import difflib.Delta;
import difflib.Patch;
import difflib.Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by alekbaga on 06.04.2017.
 */
public class XMLUtils {

    public static Object[] getNextElementNode(NodeList nodeList, Integer index){
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
     * @return path Absolute XPath to element
     */
    public static String getElementXpath(Element elt, Boolean byAttributes){
        String path = "";
        try{
            for (; elt != null; elt = (Element) elt.getParentNode()){
                int idx = getElementIndex(elt);
                String xname;
                if (byAttributes){
                    xname = elt.getAttribute("name");
                    if (xname.isEmpty()) {
                        xname = elt.getTagName();
                    }
                } else {
                     xname = elt.getTagName();
                }
                if (idx >= 1) xname += "[" + idx + "]";
                path = "/" + xname + path;
            }
        }catch(Exception ee){
            //TODO
        }
        return path;
    }

    public static String getElementXpath(Element elt){
        return getElementXpath(elt,false);
    }

    /**
     * Get Element index for XPath construction
     * @param element Element node
     * @return count Index of requested the element
     */
    public static int getElementIndex(Element element) {
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

    /**
     * Remove Null values from a String Array
     * @param array Sting array
     * @return array without null values
     */
    public static String[] removeNullValue( String[] array ) {
        array = Arrays.stream(array)
                .filter(s -> (s != null && s.length() > 0))
                .toArray(String[]::new);
        return array;

    }

    public static List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // ignore ... any errors should already have been
                    // reported via an IOException from the final flush.
                }
            }
        }
        return lines;
    }
}
