package res.datatype.address;

import java.util.ArrayList;
import java.util.List;

import com.testDataBuilder.util.RandomUtil;

public class CityList {

    private String state;

    private int length = 0;
    
    private List<String> citys = new ArrayList<String>();
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<String> getCitys() {
        return citys;
    }

    public void setCitys(List<String> citys) {
        this.citys = citys;
    }
    
    public String randomCity(){
        if(length == 0){
            length = citys.size();
        }
        int index = RandomUtil.randomInt(0, length-1);
        return this.citys.get(index);
    }
    
    public String randomStateCity(){
        if(length == 0){
            length = citys.size();
        }
        int index = RandomUtil.randomInt(0, length-1);
        return this.getState() + "," + this.citys.get(index);
    }
}
