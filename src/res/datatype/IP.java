package res.datatype;

import org.apache.log4j.Logger;
import com.testDataBuilder.core.baseType.TypeAdapter;
import com.testDataBuilder.exception.InvalidIPValueException;
import com.testDataBuilder.util.RandomUtil;

/**
 * IP数据类型.
 * <p>Title：IP.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-1-20
 * @version 1.0
 */
public class IP extends TypeAdapter {
	
    static Logger logger = Logger.getLogger(IP.class);
    
	private String value = "127.0.0.1";

	public static final long MAX_VALUE = 4294967295L;

	public static final long MIN_VALUE = 0;
	
	public IP(){
		
	}
	
	public IP(String strIP) throws InvalidIPValueException{
		setValue(strIP);
	}
	
	public IP(long value) throws InvalidIPValueException{
		setValue(value);
	}
	public static void main(String[] args) throws InvalidIPValueException {
		System.out.println(new IP("255.255.255.255").toLong());
		
	}
	
	public boolean checkValue(String value) throws InvalidIPValueException{
        try{
            int i = Integer.valueOf(value);
            return (i >= 0 && i < 256);
        }catch(Exception ex){
            return false;
        }
	}
	
	public void checkIP(String strIP)throws InvalidIPValueException{
		if(strIP == null || strIP.equals("")){
			throw new InvalidIPValueException(strIP);
		}
		String[] ips = strIP.split("\\.");
		if(ips.length != 4){
			throw new InvalidIPValueException(strIP);
		}
		
		for(int i=0;i < ips.length; i++){            
            if(!checkValue(ips[i])){
                throw new InvalidIPValueException(strIP);
            }            
		}
	}
	
//	public int getValue(String value){
//		int iv = Integer.valueOf(value).intValue();
//		return iv;		
//	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) throws InvalidIPValueException {
		checkIP(value);
		this.value = value;
	}
	
	public void setValue(long lip) throws InvalidIPValueException {
          int[] ips = new int[4];
          ips[0] = (int) (lip >> 24) & 255;
          ips[1] = (int) (lip >> 16) & 255;
          ips[2] = (int) (lip >> 8) & 255;
          ips[3] = (int) (lip & 255);
          StringBuffer sbIP = new StringBuffer();
          sbIP.append(ips[0]);
          sbIP.append("." + ips[1]);
          sbIP.append("." + ips[2]);
          sbIP.append("." + ips[3]);		
          setValue(sbIP.toString());
	}
	
	
//	public static String long2strIP(long lip){
//		int[] ips = new int[4];
//		ips[0] = (int) (lip >> 24) & 255;
//		ips[1] = (int) (lip >> 16) & 255;
//		ips[2] = (int) (lip >> 8) & 255;
//		ips[3] = (int) (lip & 255);
//		StringBuffer sbIP = new StringBuffer();
//		sbIP.append(ips[0]);
//		sbIP.append("." + ips[1]);
//		sbIP.append("." + ips[2]);
//		sbIP.append("." + ips[3]);
//		
//		return sbIP.toString();
//	}
	
//	public static TIP long2ip(long lip){
//        TIP ip =  null;
//		try {
//            ip =  new TIP(long2strIP(lip));
//        } catch (InvalidIPValueException e) {
//            TLoger.error("TIP", e);
//        }
//        return ip;
//	}
	
	public long toLong(){
        long value = 0;
        String ipv = this.getValue();
        String[] ips = ipv.split("\\.");
        for(int i=0;i < ips.length; i++){
            int temp = Integer.valueOf(ips[i]).intValue();            
            value = value + (((long)temp)<< ((3-i) * 8));           
        }
        return value;
	}
	
	public static IP randomIP() throws InvalidIPValueException{
		return randomIP(MIN_VALUE, MAX_VALUE);
	}
	
	public static IP randomIP(long min, long max) throws InvalidIPValueException{
		long value = Long.valueOf((long) (min + Math.random() * (max - min))).longValue();
		return new IP(value);
	}
	
	public static IP randomIP(String strMin, String strMax) throws InvalidIPValueException{
		long min = new IP(strMin).toLong();
		long max = new IP(strMax).toLong();
		return randomIP(min, max);
	}
	
	public String toString(){
		if(getValue() != null){
			return this.getValue().toString();
		}
		return null;
	}

    public String dbToString(double dbValue) {        
        try {
            return new IP((long)dbValue).toString();
        } catch (InvalidIPValueException e) {
            logger.error("TIP", e);
        }
        return null;
    }

    public double getMax() {
        return MAX_VALUE;
    }

    public double getMin() {
        return MIN_VALUE;
    }

    public String getName() {
        return "IP";
    }

    public String randomValue(double min, double max) {        
        double dbRandom = RandomUtil.randomDouble(min, max);
        try {
            return new IP((long)dbRandom).toString();
        } catch (InvalidIPValueException e) {
            logger.error("TIP", e);
        }
        return null;
    }

    public double stringTodb(String value)throws InvalidIPValueException {
        return valueOf(value).toLong();
    }
    
    @Override
    public IP valueOf(String value) throws InvalidIPValueException{
        return new IP(value);
    }
}
