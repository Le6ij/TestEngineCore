/**
 * Created by alekbaga on 14.03.2017.
 */
public class TECore {
    public static String projectPath = System.getProperty("user.dir");
    public static String endpoint = "SD2INT";
    public static String commonTable = "SD_TRANSACTION_LOG";
    public static String baselineSQL = "global_id = ? and (((data_type like '%OutgoingRequest' or data_type like '%ExternalRequest')"
                                        + " and transaction_type <> 'EVENT')"
                                        + " or data_type = 'se.tele2.customerorderinterface.request.doc:customerOrderRequestInterfaceIncomingRequest')"
                                        + " order by transaction_log_id";
    public static String templateXMLField = "DATA";
    public static String[] dynamicFields = {"MessageId","ConversationId","BusinessProcessId","Timestamp",
                                                "OrderId","OrderItem","OrderItemId","OrderItemRefId","ProductInstanceId","","","","","",""};
}
