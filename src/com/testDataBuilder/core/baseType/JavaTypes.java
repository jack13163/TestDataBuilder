package com.testDataBuilder.core.baseType;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Date;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.core.role.Role;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.util.Global;

/**
 * java类型.
 * <p>Title：JavaType.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-6-11
 * @version 1.0
 */
public class JavaTypes {

    static Logger logger = Logger.getLogger(JavaTypes.class);
    
    private JavaTypes(){
        
    }
    
    private static JavaTypes instance;
    
    public static JavaTypes getInstance(){
        if(instance == null){
            instance = new JavaTypes();
        }
        return instance;
    }
    
    public Class loadTypeClass(String className) throws ClassNotFoundException, MalformedURLException{
        if(urlClassLoader == null){
            File dir = new File(path);
            URL[] urls = new URL[1];
            if(dir.exists()){   
               urls[0] =dir.toURL();
            }
            urlClassLoader = new URLClassLoader(urls,Role.class.getClassLoader());
        }        
        
        return urlClassLoader.loadClass(className);
    }
    
    URLClassLoader urlClassLoader = null;
    String path = null;
    
    public static final String TYPE_CONFIG_FILE_SUFFIX = ".type.txt";
    public static final FilenameFilter typeConfigFilter 
                    = new SuffixFileFilter(TYPE_CONFIG_FILE_SUFFIX); 
    
    public boolean init(String path){
        this.path = path;        
        File dir = new File(path);
        logger.info(String.format("从目录%s下加载怎定义类型信息.", dir.getAbsolutePath()));
        if(dir.exists()){
            File[] typeConfigFiles = dir.listFiles(typeConfigFilter);
            for(File typeConfigFile : typeConfigFiles){
                try {
                    String configs = FileUtils.readFileToString(typeConfigFile, "utf-8");
                    String[] classNames = configs.split(";");
                    for(String className : classNames){
                        if(StringUtils.isNotEmpty(className)){
                            className = className.trim();
                            
                            try{
                                Class clazz = this.loadTypeClass(className);
                                IType type = (IType)clazz.newInstance();                            
                                type.init(path);
                                this.addType(type);
                            }catch(BaseException ex){
                                logger.error("initialization new type[" + className +  "] error", ex);
                            }  catch (ClassNotFoundException ex) {
                                logger.error("configuration file[" + typeConfigFile.getAbsolutePath() 
                                        + "] type[" +  className + "]not exist。or class file not in classpath。" 
                                        , ex);
                            } catch (InstantiationException ex) {
                                logger.error("JavaTypes", ex);
                            } catch (IllegalAccessException ex) {
                                logger.error("JavaTypes", ex);
                            }
                        }
                    }
                } catch (IOException e) {
                    logger.error("JavaTypes", e);
                }
            }
        }else{
            logger.info(String.format("目录[%s]不存在", dir.getAbsolutePath()));
        }
        return true;
    }
    //[start]常量字段。
    public static final Class BOOLEAN = Boolean.class;
    
    public static final Class BYTE = Byte.class;
    
    public static final Class DOUBLE = Double.class;
    
    public static final Class FLOAT = Float.class;
    
    public static final Class INT = Integer.class;
    
    public static final Class LONG = Long.class;
    
    public static final Class OBJECT = Object.class;
    
    public static final Class STRING = String.class;
    
    public static final Class SHORT = Short.class;
    
    public static final Class DATE = Date.class;
    //[end]
//    public static final String IP = "ip";
    
    private Map<Class,IType> addedTypes = null;
    
    public Map<Class, IType> getAddedTypes(){
        if(addedTypes == null){
            addedTypes = new HashMap<Class,IType>();
        }
        return addedTypes;
    }

    
    public IType addType(IType type){
        return getAddedTypes().put(type.getClass(), type);
    }
    
    public IType getAddedType(Class clazz){
        IType itype = getAddedTypes().get(clazz);
        if(itype == null){
            logger.error("type [" + clazz + "] not exits!");
        }
        return itype; 
    }

    private List<Class> typeList = null;
    
        
    public List<Class> getAllTypes(){
        if(typeList == null){
            typeList = new LinkedList<Class>();
            typeList.add(BOOLEAN);
            typeList.add(BYTE);
            typeList.add(DOUBLE);
            typeList.add(FLOAT);
            typeList.add(INT);
            typeList.add(LONG);
            typeList.add(OBJECT);
            typeList.add(STRING);
            typeList.add(SHORT);
            typeList.add(DATE);
            Map<Class, IType> addedTypeMap = getAddedTypes();
            if(addedTypeMap != null){
                for(IType type : addedTypeMap.values()){
                    typeList.add(type.getClass());
                }
            }
        }        
        return typeList;
    }
    
    /**
     * 得到一个数据类型的最小值.
     * <p><code>getMin</code></p>
     * @param javaType
     * @return
     * @author LiuXiaojie 2007-6-11
     */
    public double getMin(Class javaType, int size, int decimalDigits){
        double min = 0;
        if(javaType != null){
            if(javaType.equals(STRING)){
                min = 0;
            }else if(javaType.equals(BYTE)){
                min = Byte.MIN_VALUE;
            }else if(javaType.equals(DOUBLE)){
                if(size == 0 && decimalDigits == 0){
                    min = Double.MIN_VALUE;
                }else{
                    int m = size - decimalDigits + 1;
                    min = -1 * (Math.pow(10, m) - 1);
                }
            }else if(javaType.equals(FLOAT)){
                min = Float.MIN_VALUE;
            }else if(javaType.equals(INT)){
                min = Integer.MIN_VALUE;
            }else if(javaType.equals(LONG)){
                min = Long.MIN_VALUE;
            }else if(javaType.equals(SHORT)){
                min = Short.MIN_VALUE;
            }else if(javaType.equals(DATE)){
                min = java.sql.Date.valueOf("0-1-1").getTime();
            }else if(javaType.equals(BOOLEAN)){
                min = 0;
            }else if(javaType.equals(OBJECT)){
                min = 0;
            }else if(IType.class.isAssignableFrom(javaType)){
                IType type = this.getAddedType(javaType);
                min = type.getMin(); 
            }else{
                logger.error("JavaTypes, unknow type:" + javaType.getName());
            }
        }
        return min;
    }
    
    /**
     * 得到一个数据类型的最大值.(如果是字段串，返回最大长度.这里默认为:16)
     * <p><code>getMax</code></p>
     * @param javaType
     * @return
     * @author LiuXiaojie 2007-6-11
     */
    public double getMax(Class javaType, int size, int decimalDigits){
        double max = 16;
        if(javaType != null){
            if(javaType.equals(STRING)){
                
            }else if(javaType.equals(BYTE)){
                max = Byte.MAX_VALUE;
            }else if(javaType.equals(DOUBLE)){
                if(size == 0 && decimalDigits == 0){
                    max = Double.MAX_VALUE;
                }else{
                    int m = size - decimalDigits + 1;
                    max = Math.pow(10, m) - 1;                    
                }
            }else if(javaType.equals(FLOAT)){
                max = Float.MAX_VALUE;
            }else if(javaType.equals(INT)){
                max = Integer.MAX_VALUE;
            }else if(javaType.equals(LONG)){
                max = Long.MAX_VALUE;
            }else if(javaType.equals(SHORT)){
                max = Short.MAX_VALUE;
            }else if(javaType.equals(DATE)){
                max = java.sql.Date.valueOf("9999-12-31").getTime();
            }else if(javaType.equals(BOOLEAN)){
                max = 1;
            }else if(javaType.equals(OBJECT)){
                max = 0;
            }else if(IType.class.isAssignableFrom(javaType)) {
                IType type = this.getAddedType(javaType);
                max = type.getMax();
            }else{
                logger.error("JavaTypes, unknow type:" + javaType.getName());
            }
        }
        return max;
    }
    
    /**
     * 将一个字符串按类型返回.
     * <p><code>getValue</code></p>
     * @param value
     * @param javaType
     * @return
     * @author LiuXiaojie 2007-6-16
     * @throws BaseException 
     */
    public Object valueOf(Class javaType, String value) throws BaseException{
        Object retValue = null;
        if(javaType != null){
            if(javaType.equals(BOOLEAN)){
                retValue = Boolean.valueOf(value);
            }else if(javaType.equals(BYTE)){
                retValue = Byte.valueOf(value);
            }else if(javaType.equals(DOUBLE)){
                retValue = Double.valueOf(value);
            }else if(javaType.equals(FLOAT)){
                retValue = Float.valueOf(value);
            }else if(javaType.equals(INT)){
                retValue = Integer.valueOf(value);
            }else if(javaType.equals(LONG)){
                retValue = Long.valueOf(value);
            }else if(javaType.equals(SHORT)){
                retValue = Short.valueOf(value);
            }else if(javaType.equals(STRING)){
                retValue = value;
            }else if(javaType.equals(DATE)){
                retValue = Global.getInstance().parseDateTime(value);           
            }else if(javaType.equals(OBJECT)){
                retValue = value;
            }else if(IType.class.isAssignableFrom(javaType)){
                IType type = this.getAddedType(javaType); 
                retValue = type.valueOf(value);
            }else{
                logger.error("JavaTypes, unknow type:" + javaType.getName());
            }
        }
        return retValue;
    }
    
    public double getDbValue(Class javaType, String value) throws BaseException{        
        double retValue = 0;
        if(javaType != null){
            if(javaType.equals(DOUBLE) || javaType.equals(BYTE)
                    ||javaType.equals(FLOAT) ||javaType.equals(INT) || javaType.equals(LONG)
                    || javaType.equals(SHORT)|| javaType.equals(STRING)
                    || javaType.equals(BOOLEAN)){
                retValue = Double.valueOf(value).doubleValue();
            }else if(javaType.equals(DATE)){
                retValue = Global.getInstance().parseDateTime(value).getTime();
            }else if(javaType.equals(OBJECT)){
                throw new BaseException("");
           }else if(IType.class.isAssignableFrom(javaType)){
                IType type = getAddedType(javaType);
                if(type != null){
                    retValue = type.stringTodb(value);
                }
            }else{
                logger.error("JavaTypes, unknow type:" + javaType.getName());
            }
        }
        return retValue;
    }
    
    
    
    public String getStringValue(Class javaType, double value){
        String retValue = null;
        if(javaType.equals(DOUBLE)){
            retValue = value + ""; 
        }else if(javaType.equals(BYTE)){
            retValue = Double.valueOf(value).byteValue() + "";
        }else if(javaType.equals(FLOAT)){
            retValue = Double.valueOf(value).floatValue() + "";
        }else if(javaType.equals(INT)){
            retValue = Double.valueOf(value).intValue() + "";
        }else if(javaType.equals(LONG)){
            retValue = Double.valueOf(value).longValue() + "";
        }else if(javaType.equals(SHORT)){
            retValue = Double.valueOf(value).shortValue() + "";
        }else if(javaType.equals(STRING)){
            retValue = Double.valueOf(value).longValue() + "";
        }else if(javaType.equals(BOOLEAN)){
            retValue = Double.valueOf(value).byteValue() + "";
        }else if(javaType.equals(DATE)){
            retValue = Global.getInstance().dataFormater.format(new Date((long) value));            
        }else if(javaType.equals(OBJECT)){
            
        }else if(IType.class.isAssignableFrom(javaType)){
            IType type = getAddedType(javaType);
            if(type != null){
                retValue = type.dbToString(value);
            }else{
                logger.error("type[" + javaType + "] not found!");
            }
        }  else{
            logger.error("JavaTypes, unknow type:" + javaType.getName());
        }
        return retValue;
    }
    
    public char randomChar(){
        int random = (int) (Math.random() * (90 -65) + 65);
        return (char)random;
    }
    
    public Object random(Class javaType, double min, double max, int decimalDigits){
        Object randomObj = null;
        
        if(javaType != null){
            if(javaType.equals(BYTE)){
                randomObj = Byte.valueOf((byte) (min + Math.random() * (max - min)));
            }else if(javaType.equals(DOUBLE)){
                randomObj = Double.valueOf((double) (min + Math.random() * (max - min)));
                StringBuilder strFormat = new StringBuilder(".");
                for(int i=0;i < decimalDigits; i++){
                    strFormat.append("0");
                }
                NumberFormat format =  new  java.text.DecimalFormat(strFormat.toString());   
                format.setMaximumFractionDigits(decimalDigits);
                String strDB = format.format(randomObj);
                randomObj =  Double.valueOf(strDB);
            }else if(javaType.equals(FLOAT)){
                randomObj = Float.valueOf((float) (min + Math.random() * (max - min)));
            }else if(javaType.equals(INT)){
                randomObj = Integer.valueOf((int) (min + Math.random() * (max - min)));
            }else if(javaType.equals(LONG)){
                randomObj = Long.valueOf((long) (min + Math.random() * (max - min)));
            }else if(javaType.equals(SHORT)){
                randomObj = Short.valueOf((short) (min + Math.random() * (max - min)));
            }else if(javaType.equals(STRING)){
                int size = (int) (min + Math.random() * (max - min));
                if(size == 0){
                    size = (int)max;
                }               
                randomObj = RandomStringUtils.randomAlphabetic(size);
            }else if(javaType.equals(DATE)){                
                randomObj = new Date(Long.valueOf((long) (min + Math.random() * (max - min))).longValue());
            }else if(javaType.equals(BOOLEAN)){
                randomObj = Boolean.valueOf(Math.random() < 0.5);
            }else if(javaType.equals(OBJECT)){
                
            }else if(IType.class.isAssignableFrom(javaType)){
                IType iType = getAddedType(javaType);
                randomObj = iType.randomValue(min, max);
            }else{
                logger.error("JavaTypes, unknow type:" + javaType.getName());
            }
        }       
        return randomObj;
    }
    
    public String getTypeName(Class javaType){
        if(IType.class.isAssignableFrom(javaType)){
            IType iType = getAddedType(javaType);
            return iType.getName();
        }else{
            return javaType.getSimpleName();
        }
    }
//    public List<String> getAllTypeNames(){
//        return RadixConverter.toList(getAllTypes().keySet());
//    }
//    
//    public Class getClass(String className){
//        return (Class) getAllTypes().get(className);
//    }

    public static Date now(){
        return new Date(System.currentTimeMillis());
    }
    
    
    public static void main(String[] args) {
        
    }

}
