import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.RecognitionException;
import org.javalite.activejdbc.Base;
import edu.gatech.gtri.orafile.*;


/**
 * Created by alekbaga on 14.03.2017.
 */

/**
 * This class is a class that is responsible for managing connections and requests to DB
 *
 * @author Aleksejs Bagajevs
 */
public class DBConnector {

    /**
     * Name of the table system is currently working with
     *                  can be set with setTable(String tableName)
     */
    public String tableName=TECore.commonTable;

    public DBConnector(){
    }
    /**
     *Initiates a connection with DB only by DB alias
     *
     * @param alias name of the DB server you want to connect uses aliases provided in tnsnames.ora
     * @throws Exception
     */
    public void connect(String alias){
        try {
            System.setProperty("oracle.net.tns_admin", TECore.projectPath+"/src/main/resources");
            String dbURL = "jdbc:oracle:thin:@"+alias;
            Map<String, String> connectionDetails = getConnDetails(alias);
            Base.open("oracle.jdbc.driver.OracleDriver", dbURL, connectionDetails.get("usr"), connectionDetails.get("pwd"));
        } catch (Exception e) {
            System.out.println("Unable to fetch connection details");
            e.printStackTrace();
        }
    }

    public void connect(){
        connect(TECore.endpoint);
    }

    private static Map<String, String> getConnDetails(String alias){
        Map<String,String> connectionDetails = new HashMap<String,String>();

        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(TECore.projectPath+"/src/main/resources/tnsnames.ora")));
        } catch (IOException e) {
            System.out.println("Unable to read tnsnames.ora");
            e.printStackTrace();
        }
        OrafileDict tns = null;
        try {
            tns = Orafile.parse(content);
        } catch (RecognitionException e) {
            System.out.println("Unable to parse tnsnames.ora");
            e.printStackTrace();
        }
        OrafileVal aliasDetails = tns.get(alias).get(0);
        List<Map<String, String>> values = aliasDetails
                .findParamAttrs("CONNECT_DATA", Arrays.asList("name", "password"));
        Map<String, String> value = values.get(0);

        connectionDetails.put("usr",value.get("name"));
        connectionDetails.put("pwd",value.get("password"));
        return connectionDetails;

    }

    public void setTable(String tableName){
        this.tableName = tableName;
    }

    public String getTable(){
        return this.tableName;
    }

    public List fetchDataFromTable(String sql, Object... params){
        return fetchDataFromTable(this.tableName, sql, params);
    }

    /***
     * @brief Get records from DB
     *
     * Performs a request to DB and fetches found records. Is used when
     *          where(String sql, Object... params) and found(String sql, Object... params)
     *          can not be used because Table object can not be used or is unknown.
     *          The main purpose is to give possibility to call tables dynamically.
     *          Implemented with the use of Reflection API
     * @param tableName name of the table that you want to fetch your records from
     * @param sql sql statement that will be executed
     * @param params parameters with values needed for sql statement
     * @return generic List with records fetched from DB as it's elements
     * @throws Exception
     */
    public List fetchDataFromTable(String tableName, String sql, Object... params){
        tableName = "tables."+tableName;
        Class<?> cls = null;
        try {
            ///Try to find class with the provided name.
            cls = Class.forName(tableName);
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to find matching table class");
            e.printStackTrace();
        }
        Method method = null;
        try {
            ///Try to find method "where" in the  found class.
            // Must define init param types, String and Object array in this case.
            method = cls.getDeclaredMethod ("where", String.class, Object[].class);
        } catch (NoSuchMethodException e) {
            System.out.println("Unable to find matching method in table class");
            e.printStackTrace();
        }
        List fetchedRows = null;
        try {
            ///Try to invoke found method "where" with provided parameters.
            fetchedRows = (List) method.invoke (tableName, sql, params);
        } catch (IllegalAccessException e) {
            System.out.println("Unable to convert fetched results to List");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("Unable to use Reflection to call matching method");
            e.printStackTrace();
        }
        return fetchedRows;
    }

}