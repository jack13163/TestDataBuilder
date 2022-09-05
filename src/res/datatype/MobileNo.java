package res.datatype;

import com.testDataBuilder.core.baseType.TypeAdapter;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.util.RandomUtil;

public class MobileNo extends TypeAdapter {

    @Override
    public String dbToString(double dbValue) {
        return Double.valueOf(dbValue).longValue() + "";
    }

    @Override
    public double getMax() {        
        return Double.valueOf(15999999999L);
    }

    @Override
    public double getMin() {
        return Double.valueOf(13100000000L);
    }

    @Override
    public String getName() {
        return "手机号码";
    }


    @Override
    public Object randomValue(double min, double max) {        
        return RandomUtil.randomLong((long)min, (long)max);
    }

    @Override
    public double stringTodb(String value) throws BaseException {
        return Double.valueOf(value);
    }

    @Override
    public Object valueOf(String value) throws BaseException {
        return Long.valueOf(value);
    }

    public static void main(String[] args) {
        MobileNo mobileNo = new MobileNo();
        for(int i=0;i < 100;i ++){
            Object o = mobileNo.randomValue(13000000000L, 15999999999L);
            System.out.println(o);
        }
    }
    
}
