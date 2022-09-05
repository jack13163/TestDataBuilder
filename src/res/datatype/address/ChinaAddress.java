package res.datatype.address;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import com.testDataBuilder.core.baseType.IType;
import com.testDataBuilder.core.baseType.TypeAdapter;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.util.BeanXMLMapping;
import com.testDataBuilder.util.RandomUtil;

public class ChinaAddress extends TypeAdapter{

    static Logger logger = Logger.getLogger(ChinaAddress.class);
    
    public ChinaAddress(){
    }

    @Override
    public Object randomValue(double min, double max) {
        return this.randomStateCity();
    }


    @Override
    public IType init(String basePath) throws BaseException {
        
        InputStream is = null; 
        
        try {
            is = this.getClass().getResourceAsStream("city.xml");
            String xml = IOUtils.toString(is);
            this.setStateList(ChinaAddress.fromXml(xml).getStateList());
        } catch (IOException e) {
            throw new BaseException(e);
        }finally{
            if(is != null){
                try {
                    is.close();
                    is = null;
                } catch (IOException e) {
                    logger.error("ChinaAddress", e);
                }
            }
        }
        
        return this;
    }
    
    private List<CityList> stateList = new ArrayList<CityList>(30);
    
    public String toXmlString() throws IOException{
        return (String) BeanXMLMapping.toXML(this);
    }
    
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).toString();
    }

    public CityList get(String state){
        CityList citys = null;
       for(CityList tempCityList : this.stateList){
           if(tempCityList.getState().equalsIgnoreCase(state)){
               citys = tempCityList;
           }
       }
       if(citys == null){
           citys = new CityList();
           citys.setState(state);
           this.stateList.add(citys);
       }
       return citys;
    }
    
    public boolean addCity(String state, String city){
        CityList citys = this.get(state);
        if(!citys.getCitys().contains(city)){
            citys.getCitys().add(city);
            return true;
        }else{
            return false;
        }
    }
    
    public void showInfo(){
        
        for(int i=0;i < this.stateList.size(); i++){
            CityList citys = stateList.get(i);
            System.out.println("================= " + citys.getState() + "=====================");
            
            for(String city : citys.getCitys()){
                System.out.println("    " + city);
            }
            System.out.println();
        }
    }
    
    public static ChinaAddress fromXml(String xml) throws IOException{
        return (ChinaAddress) BeanXMLMapping.fromXML(xml, ChinaAddress.class);
    } 
    
    public static void main(String[] args) throws Exception {
//        IDBTemplate template = TemplateFactory.getInstance()
//        .getTemplate(DBType.MS_SQL_SERVER);
//       
//          DatabaseUtil dbutil = new  DatabaseUtil();
//          Connection conn = dbutil.getConnectin(template.getDriverClass(),
//                         "jdbc:sqlserver://localhost:1433;DatabaseName=area", "sa", "sa");
//          dbutil.setBasePath("d:\\eee\\");
//          PreparedStatement s = conn.prepareStatement("select * from City");
//          ResultSet rs = s.executeQuery();
//          ChinaAddress addressList = new ChinaAddress();
//          
//          while(rs.next()){
//              String state = rs.getString("state");
//              String city = rs.getString("city"); 
//              //System.out.println(state + "->" + city);
//              addressList.addCity(state, city);              
//          }
//          System.out.println( addressList.toXmlString());
//                    
//          FileUtils.writeStringToFile(new File("D:\\city.xml"), addressList.toXmlString(), "utf-8");
//          System.out.println("success");    
//          conn.close();
        
        ChinaAddress addressMap = new ChinaAddress();
        addressMap.init(null);
        
        for(int i=0;i < 100; i++){
            String address = addressMap.randomStateCity();
            System.out.println(address);
        }
    }

    public List<CityList> getStateList() {
        return stateList;
    }

    public void setStateList(List<CityList> stateList) {
        this.stateList = stateList;
    }
   
    private int length = 0;
    
    public CityList randomState(){
        if(length == 0){
            length = stateList.size();
        }
        int index = RandomUtil.randomInt(0, length-1);
        return this.stateList.get(index);
    }
    
    public String randomStateCity(){
        return randomState().randomStateCity();
    }
    
    
}
