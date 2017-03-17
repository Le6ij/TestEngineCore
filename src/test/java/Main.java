import org.antlr.runtime.RecognitionException;
import org.javalite.activejdbc.Model;
import tables.SD_TRANSACTION_LOG;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by alekbaga on 14.03.2017.
 */
public class Main {
    public static void main(final String[] args) throws Exception {
        DBConnector db = new DBConnector();
        db.connect();
//        List<SD_TRANSACTION_LOG> transactionLog = SD_TRANSACTION_LOG.where("global_id = ? and receiver = ?", "9bc8ebee-a6d3-4c1e-8383-d8b9b7d", "Provident");
//        //lazy print
//        for(SD_TRANSACTION_LOG u: transactionLog){
//            System.out.println(u);
//        }
//
//        SD_TRANSACTION_LOG transaction = transactionLog.get(0);
//        System.out.println(transaction.get("COMPONENT"));
        String sql = "global_id = ? and (((data_type like '%OutgoingRequest' or data_type like '%ExternalRequest')"
                +" and transaction_type <> 'EVENT')"
                +" or data_type = 'se.tele2.customerorderinterface.request.doc:customerOrderRequestInterfaceIncomingRequest')"
                +" order by transaction_log_id";
        Object[] params = new Object[]{"AC4C3F68-5FA1-11e6-8402-001A4A10231F"};//global_id
        List<Model> rows = DBConnector.fetchDataFromTable("SD_TRANSACTION_LOG",sql,params);
        for(Model u: rows){
            System.out.println(u.getString("TRANSACTION_LOG_ID"));
            u.getString("DATA");
        }
//        //System.out.println(rows);
//        String data;
//        for(int i =0; i<rows.size();i++){
//            System.out.println(rows.get(i));
//            (Model) ;
//            DBConnector.getColumnValueAsString("SD_TRANSACTION_LOG","GLOBAL_ID");
//
//        }

    }
}
