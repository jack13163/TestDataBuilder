package res.datatype;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.testDataBuilder.core.baseType.IType;
import com.testDataBuilder.core.baseType.TypeAdapter;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.util.RandomUtil;

public class ChineseName extends TypeAdapter {

    static Logger logger = Logger.getLogger(ChineseName.class);
    
    String firstNames = null;
    String lastNames = null;
    int firstNameLength = 0;
    int lastNameLength = 0;
    
    public ChineseName(){       
       
    }
    
    public IType init(String path) throws BaseException {
        InputStream is = null; 
        
        try {
            is = this.getClass().getResourceAsStream("firstName.txt");
            firstNames = IOUtils.toString(is);            
            firstNameLength = firstNames.length();
        } catch (IOException e) {
            throw new BaseException(e);
        }finally{
            if(is != null){
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    logger.error("ChineseName", e);
                }
            }
        }
        
        try {
            is = this.getClass().getResourceAsStream("lastName.txt");
            lastNames = IOUtils.toString(is);
            lastNameLength = lastNames.length();
        } catch (IOException e) {
            throw new BaseException(e);
        }finally{
            if(is != null){
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    logger.error("ChineseName", e);
                }
            }
        }
        return this;
    }

    @Override
    public Object randomValue(double min, double max) {        
        if(firstNames != null && lastNames != null){
            return randomFirstName() + randomLastName();
        }
        return null;
    }

    private String randomFirstName(){
        int pos = randomInt(0, firstNameLength);        
        return firstNames.substring(pos, pos + 1);   
    }
    private String randomLastName(){
        int pos = randomInt(0, lastNameLength);
        int length = 2;
        if(pos %3 == 0){
            length = 1;
        }
        if(length == 1){
            return lastNames.substring(pos, pos + 1);
        }else{
            int pos2 = randomInt(0, lastNameLength);
            return lastNames.substring(pos, pos + 1) + lastNames.substring(pos2, pos2 + 1);
        }
    }
    private int randomInt(int min, int max){
        return RandomUtil.randomInt(min,max);
    }
    
    public static void main(String[] args) throws SQLException, IOException {

//       IDBTemplate template = TemplateFactory.getInstance()
//       .getTemplate(DBType.MS_SQL_SERVER);
//      
//         DatabaseUtil dbutil = new  DatabaseUtil();
//         Connection conn = dbutil.getConnectin(template.getDriverClass(),
//                        "jdbc:sqlserver://localhost:1433;DatabaseName=DrmPolicy23", "sa", "sa");
//         dbutil.setBasePath("d:\\eee\\");
//         PreparedStatement state = conn.prepareStatement("select * from V_ALLSTAFF");
//         ResultSet rs = state.executeQuery();
//         List<String> firstNameList = new LinkedList<String>();
//         List<String> lastNameList = new LinkedList<String>();
//         StringBuffer sbLastName = new StringBuffer(); 
//         StringBuffer sbXing = new StringBuffer();
//         while(rs.next()){
//             String name = rs.getString("STAFF_NAME");
//             String xing = name.substring(0, 1);
//             String lastName1 = name.substring(1,2);
//             String lastName2 = null;
//             if(name.length() >= 3){
//                 lastName2 = name.substring(2,3);
//             }
//             if(!firstNameList.contains(xing)){
//                 firstNameList.add(xing);
//                 sbXing.append(xing);
//             }
//             if(!lastNameList.contains(lastName1)){
//                 lastNameList.add(lastName1);
//                 sbLastName.append(lastName1);
//             }
//             if(lastName2 != null && !lastNameList.contains(lastName2)){
//                 lastNameList.add(lastName2);
//                 sbLastName.append(lastName2);
//             }
//         }
//         System.out.println("姓:" + firstNameList.size());
//         System.out.println("ming:" + lastNameList.size());
//         
//         System.out.println(sbXing);
//         System.out.println(sbLastName);
//         FileUtils.writeStringToFile(new File("D:\\firstName.txt"), sbXing.toString());
//         FileUtils.writeStringToFile(new File("D:\\lastName.txt"), sbLastName.toString());
//         
//         conn.close();
     
    }
   

}
