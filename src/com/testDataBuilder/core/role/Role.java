package com.testDataBuilder.core.role;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.testDataBuilder.core.XmlUtil;
import com.testDataBuilder.core.baseType.IType;
import com.testDataBuilder.core.baseType.JavaTypes;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.util.Global;
import com.testDataBuilder.util.JDBCUtil;
import com.testDataBuilder.util.RandomUtil;

public class Role implements Comparable,Cloneable{

    static Logger logger = Logger.getLogger(Role.class);
    
	//public static final String TAG_DB_REF = "dbRef";
    public static final String TAG_JAVA_SOURCE = "javaCode";  
    
    public static final String TAG_SQL = "sql";
    public static final String TAG_SOURCE_NAME = "sourceName";    
    public static final String TAG_ROLE = "role";
    public static final String TAG_NAME = "name";
    public static final String TAG_TYPE = "type";
    public static final String TAG_MIN = "min";
    public static final String TAG_MAX = "max";
    public static final String TAG_MIN_DATE = "minDate";
    public static final String TAG_MAX_DATE = "maxDate";    
    public static final String TAG_METHOD = "method";
    public static final String TAG_DISTINCT_COUNT = "distinctCount";
    public static final String TAG_ENUM = "enum";
    public static final String TAG_PERCENT = "percent";
    public static final String TAG_VALUE = "value";    
    public static final String TAG_STEP = "step";    
	public static final String TAG_PREFIX = "prefix";
	public static final String TAG_SUFFIX = "suffix";
    public static final String TAG_SIZE = "size";
    public static final String TAG_DECIMAL_DIGITS = "decimalDigits";
    public static final String TAG_NULL_PERCENT = "nullPercent";
    
    
    /**
     * 数据产生方法,随机方式
     */
    public static final String METHOD_RANDOM = "random";
    /**
     * 数据产生方式，自动增长方式.
     */
    public static final String METHOD_INCREMENT = "increment";
    
    /**
     * 使用枚举值.
     */
    public static final String METHOD_ENUM = "enum";
    
    /**
     * 使用SQL查询语句。
     */
    public static final String METHOD_SQL_QUERY = "sqlquery";
    
    public static final String METHOD_SQL_FUNC = "sqlfunc";
    
    /**
     * 提供给高级用户的,使用Java语言生成测试数据.
     */
    public static final String METHOD_JAVA = "java";
    
    /**
     * 从配置文件中读取规则信息.
     * <p><code>configure</code></p>
     * @param roleEle 
     * @return 数据类.
     * @throws BaseException
     * @author LiuXiaojie 2007-6-16
     */
    public Role(Element roleEle) throws BaseException{
        String name = XmlUtil.getStringAttributeValue(roleEle,TAG_NAME,true);
        this.setName(name);
        
        String strType = XmlUtil.getStringAttributeValue(roleEle,TAG_TYPE,true);
        Class javaType = null;
        try {
            javaType = Global.getInstance().loadTypeClass(strType);            
            this.setType(javaType);
        } catch (ClassNotFoundException e) {
            logger.error("Role", e);
        } catch (MalformedURLException e) {
            logger.error("Role", e);
        }        
       
		String prefix = roleEle.attributeValue(TAG_PREFIX);
		this.setPrefix(prefix);
		
		String suffix = roleEle.attributeValue(TAG_SUFFIX);
		this.setSuffix(suffix);
 
        Integer step = XmlUtil.getIntAttributeValue(roleEle,TAG_STEP,false);
        if(step != null){
        	this.setStep(step);
        }
        
        Integer distinctCount = XmlUtil.getIntAttributeValue(roleEle,TAG_DISTINCT_COUNT,false);   
        if(distinctCount != null){
        	this.setDistinctCount(distinctCount);
        }
        Float nullPercent = XmlUtil.getFloatAttributeValue(roleEle,TAG_NULL_PERCENT,false);
        if(nullPercent != null){
        	this.setNullPercent(nullPercent);
        }
        
        String strMethod = roleEle.attributeValue(TAG_METHOD);
        if(StringUtils.isNotEmpty(strMethod)){
            if(strMethod.equalsIgnoreCase(METHOD_INCREMENT) 
                || strMethod.equalsIgnoreCase(METHOD_RANDOM)){
                String strMin = roleEle.attributeValue(TAG_MIN);
                double min = 0;
                if(StringUtils.isNotEmpty(strMin)){
                    try{
                        min = JavaTypes.getInstance().getDbValue(javaType, strMin);
                        this.setMin(min);
                    }catch(Exception ex){
                        throw new BaseException(name, "invalid min value [" + strMin + "]");
                    }
                }
                
                String strMax = roleEle.attributeValue(TAG_MAX);
                double max = 0;
                if(StringUtils.isNotEmpty(strMax)){
                    try{
                        max = JavaTypes.getInstance().getDbValue(javaType, strMax);
                        this.setMax(max);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        throw new BaseException(name, "invalid max value [" + strMax + "]");
                    }
                }
            }else if(strMethod.equalsIgnoreCase(METHOD_ENUM)){                
                List enums = roleEle.elements(TAG_ENUM);
                if(enums != null){
                    for(int i=0;i < enums.size(); i++){
                        Element enumEle = (Element) enums.get(i);
                        String value = enumEle.attributeValue(TAG_VALUE);
                        Object eValue = JavaTypes.getInstance().valueOf(javaType,value);
                        String strPercent = enumEle.attributeValue(TAG_PERCENT);
                        Integer percent = 1;
                        if(StringUtils.isNotEmpty(strPercent)){
                            try{
                            percent = Integer.valueOf(strPercent);
                            }catch(Exception ex){
                                logger.error("Role", ex);
                            }
                        }
                        this.addEnum(eValue, percent);
                    }
                }
            }else if(strMethod.equalsIgnoreCase(METHOD_SQL_QUERY)
            		||strMethod.equalsIgnoreCase(METHOD_SQL_FUNC)){
                Element sqlEle = roleEle.element(TAG_SQL);
                if(sqlEle != null){
                    String sql = sqlEle.getTextTrim();
                    this.setSQL(sql);
                    String sourceName = sqlEle.attributeValue(TAG_SOURCE_NAME);
                    if(StringUtils.isNotEmpty(sourceName)){
                        this.setSourceName(sourceName);
                    }
                }
            }else if(strMethod.equalsIgnoreCase(METHOD_JAVA)){            	
            	Element javaSourceEle =XmlUtil.getChildElement(roleEle, TAG_JAVA_SOURCE, true);
            	JavaSource javaSource = JavaSource.configure(javaSourceEle);
            	this.setJavaSource(javaSource);
            }else{
                throw new BaseException(name, "invalid method value[" + strMethod + "]");
            }
            this.setMethod(strMethod.toLowerCase());
        }
    }

    public Element toElement(Element roleEle){
    	Class type = this.getType();
    	roleEle.clearContent();
    	roleEle.addAttribute(TAG_NAME, this.getName());
    	roleEle.addAttribute(TAG_TYPE, type.getName());
        
        roleEle.addAttribute(TAG_METHOD, this.getMethod());
        roleEle.addAttribute(TAG_DISTINCT_COUNT, this.getDistinctCount() + "");
        roleEle.addAttribute(TAG_PREFIX, this.getPrefix());
        roleEle.addAttribute(TAG_SUFFIX, this.getSuffix());
        
        roleEle.addAttribute(TAG_MIN, this.getStringMin());
        roleEle.addAttribute(TAG_MAX, this.getStringMax());
    	
        roleEle.addAttribute(TAG_NULL_PERCENT, this.getNullPercent() + "");
        
    	for(EnumObj e:  this.getEnumerate()){
			Element enuEle = roleEle.addElement(TAG_ENUM);
			enuEle.addAttribute(TAG_VALUE, e.getValue() + "");
            enuEle.addAttribute(TAG_PERCENT, e.getPercent() + "");
		}
    	
        if(StringUtils.isNotEmpty(this.getSQL())){
            Element sqlEle = roleEle.addElement(TAG_SQL);
    		sqlEle.setText(this.getSQL());
            sqlEle.addAttribute(TAG_SOURCE_NAME, this.getSourceName());
    	}
    	
        if(this.getJavaSource() != null){
        	Element javaSourceEle = this.getJavaSource().toElement();
        	roleEle.add(javaSourceEle);
        }
        
    	return roleEle;
    }
    
    public Element toElement(){
        Element roleEle = DocumentHelper.createElement(TAG_ROLE);
        return toElement(roleEle);    	
    }
   
    
    public Role() {

    }

    public Role(String name) {
        this.setName(name);
    }
 
    public Role(Role role){
    	this.current = role.current;
    	this.distinctCount = role.distinctCount;
    	this.max = role.max;
    	this.method = role.method;
    	this.min = role.min;
    	this.name = role.name;
    	this.prefix = role.getPrefix();
    	this.step = role.step;
    	this.suffix = role.suffix;
    	this.type = role.type;
        this.nullPercent = role.nullPercent;
        this.roleFactory = role.roleFactory;
    	Map map = role.atts;
    	Iterator<String> it = map.keySet().iterator();
    	while(it.hasNext()){
    		String key = it.next();
    		Object value = map.get(key);
    		this.atts.put(key, value);
    	}
        this.getEnumerate().clear();
        Collections.copy(this.enumerate, role.enumerate);
    }

    private RoleFactory roleFactory = null;
    
    /**
     * 数据类型的名称（以便于引用.）
     */
    private String name;
    
    /**
     * 数据的类型
     */
    private Class type = String.class;
    
    /**
     * 如果是数值型，则是最小值，如果是字段串，则是最小长度.
     */
    private double min = 0;
    
    /**
     * 如果是数值型，则是最大值，如果是字段串，则是最大长度.
     */
    private double max = 10;
    
    /**
     * 数据产生方式.
     */
    private String method = METHOD_INCREMENT;
    
    private int step = 1;
    
    /**
     * 同一列中数据计数总数.
     */
    private int distinctCount = 0;
    
    private EnumList<EnumObj> enumerate = new EnumList<EnumObj>();
 

    public List<ComplexObj> tempDBObj = null;

    public List<ComplexObj> getTempDBObj(){
        if(tempDBObj == null){
            tempDBObj = new LinkedList<ComplexObj>();
        }
        return tempDBObj;
    }
    
    public void setTempDBObj(List<ComplexObj> tempDBObj){
        this.tempDBObj = tempDBObj;
    }
     
	private String prefix;
	
	private String suffix;
	

    /**
     * 插入Null值的百分比，0表示不插入
     */
    
    private float nullPercent = 0;
    
    private boolean nullable = true;
    
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
    public Object random(){
        if(this.getDecimalDigits() == null){
            return JavaTypes.getInstance().random(type, min, max, 0);
        }else{
            return JavaTypes.getInstance().random(type, min, max,this.getDecimalDigits());
        }
    }
    
    public Object getIncValue(double step){
        Object value =  JavaTypes.getInstance().getStringValue(type, step);
        if(value == null){
            value = random();
        }
        return value;
    }
    
    private Map<String, Object> atts = new HashMap<String,Object>();

    public void init(Connection conn, int maxRows) throws BaseException, SQLException{
    	
       getTempDBObj().clear();
        
        //从数据库中读取数据.
        if(this.isSQLQueryMethod()){
        	PreparedStatement statement = null;
        	ResultSet result = null;
             try {
                statement = conn.prepareStatement(this.getSQL());
                statement.setMaxRows(maxRows);
                result = statement.executeQuery();
                List fields = JDBCUtil.getAllFields(this.getSQL());
                //System.out.println("=========================================");
                while(result.next()){
                    ComplexObj complex = new ComplexObj();
                    for(int i=0; i < fields.size(); i++){
                        complex.addValue((String)fields.get(i),
                                result.getObject((String) fields.get(i)));  
                    }
                    //System.out.println(complex);
                    this.getTempDBObj().add(complex);
                }
                this.min = 0;
                this.max = getTempDBObj().size();
             } finally {
                if(this.max < 1.0){
                    logger.info("sql[" + this.getSQL() + "]no any result!");
                }
                if(!this.isNullable() && this.max < 1.0){
                    throw new BaseException(String.format("SQL[%s]returned null", getSQL()));
                }
            	try {
	            	if(result != null){
						result.close();					
	            	}
	            	if(statement != null){
	            		statement.close();
	            	}
            	} catch (SQLException e) {
					logger.error("close", e);
				}
            }
        }
        
        this.current = this.min;
    }
   
    
    private double current = 0;    
    
    private JavaSource javaSource = null;
    
    public Object getValueByRole() throws BaseException, SQLException  {
        // 生成NULL值。
        if (Math.random() < this.nullPercent) {
            return null;
        }

        Object retObj = null;

        if (isEnumMethod()) {
            retObj = this.getEnumerate().randomByPercent();
        } else if (isSQLQueryMethod()) {
            int dbSize = this.getTempDBObj().size();
            if (dbSize == 0) {
                return null;
            } else {
            	if(current < 0){
            		current = 0;
            	}
            	
                //int r = RandomUtil.randomInt(0, dbSize);
                retObj = this.getTempDBObj().get(Double.valueOf(current).intValue());
                current++;
                current %= (dbSize);
            }
        }else if(isSQLFuncMethod()){
        	retObj = this.getSQL();
        }else if(isJavaMethod()){
        	retObj = getJavaSource().getObjectByJava();
        } else if (isIncreamentMethod()) {
            if (current < min) {
                current = min;
            }
            if (this.type.equals(JavaTypes.STRING)) {
                retObj = random();
            } else if (IType.class.isAssignableFrom(type)) {
                retObj = getIncValue(current);
            } else if (this.type.equals(JavaTypes.DOUBLE) || type.equals(JavaTypes.FLOAT)) {
                retObj = Double.valueOf(current);
            } else if (type.equals(JavaTypes.BOOLEAN)) {
                retObj = Boolean.valueOf(current % 2 == 0);
                step = 1;
            } else if (type.equals(JavaTypes.DATE)) {
                retObj = new java.sql.Date(Double.valueOf(current).longValue());
            } else {
                retObj = Long.valueOf((long) current);
            }
            current += step;
            if (max != 0) {
                current %= (max + 1);
            }

        } else {
            retObj = random();
        }

        if (!(retObj instanceof ComplexObj) &&  retObj != null) {
            if (StringUtils.isNotEmpty(prefix)) {
                retObj = prefix + retObj.toString();
            }
            if (StringUtils.isNotEmpty(suffix)) {
                retObj = retObj.toString() + suffix;
            }
        }
        return retObj;
    }
    
   //[start]
    
    public boolean isRandomMethod(){
    	return this.getMethod().equalsIgnoreCase(Role.METHOD_RANDOM);
    }
    
    public boolean isIncreamentMethod(){
    	return this.getMethod().equalsIgnoreCase(Role.METHOD_INCREMENT);
    }
    
    public boolean isEnumMethod(){
        return this.getMethod().equalsIgnoreCase(Role.METHOD_ENUM);
    }
    
    public boolean isSQLQueryMethod(){
        return this.getMethod().equalsIgnoreCase(Role.METHOD_SQL_QUERY);
    }
    
    public boolean isSQLFuncMethod(){
    	return this.getMethod().equalsIgnoreCase(Role.METHOD_SQL_FUNC);
    }
    
    public boolean isJavaMethod(){
    	return this.getMethod().equalsIgnoreCase(Role.METHOD_JAVA);
    }
    
	public String getSQL(){
        return (String) this.atts.get(TAG_SQL);
    }
    
    public String getSourceName(){
        return (String)this.atts.get(TAG_SOURCE_NAME);
    }
    
    public void setSourceName(String sourceName){
        this.atts.put(TAG_SOURCE_NAME, sourceName);
    }
    
    public Integer getSize(){
        return (Integer) this.atts.get(TAG_SIZE);        
    }
    
    public void setSize(Integer size){
        this.atts.put(TAG_SIZE, size);
    }
    
    public Integer getDecimalDigits(){
        return (Integer)this.atts.get(TAG_DECIMAL_DIGITS);
    }
    
    public void setDecimalDigits(Integer decimalDigits){
        this.atts.put(TAG_DECIMAL_DIGITS, decimalDigits);
    }
    
    public void setSQL(String sql){
        this.atts.put(TAG_SQL, sql);
    }
    
    public int getDistinctCount() {
        return distinctCount;
    }

    public void setDistinctCount(int distinctCount) {
        this.distinctCount = distinctCount;
    }

    public void addEnum(Object obj, Integer percent){
        EnumObj e = new EnumObj(obj, percent);
        this.getEnumerate().add(e);
    }
    
    public EnumList<EnumObj> getEnumerate() {        
        return enumerate;
    }
    
    public int getEnumSize(){
    	return this.getEnumerate().size();
    }

    public static EnumList<EnumObj> cloneEnumList(EnumList<EnumObj> enumList){
        if(enumList != null){
            EnumList<EnumObj> tempEnumList = new EnumList<EnumObj>();
            for(EnumObj enumObj : enumList){
                tempEnumList.add(enumObj.clone());
            }
            return tempEnumList;
        }
        return enumList;
    }
    
    public void setEnumerate(EnumList enumerate) {
        this.enumerate = enumerate;
    }

    public double getMax() {
        return max;
    }

    public String getStringMax() {
        String strValue = null;
    	if(this.type != null && !type.equals(JavaTypes.OBJECT)){
    		strValue = JavaTypes.getInstance().getStringValue(this.getType(), this.getMax());
    	}
    	return strValue;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMaxDate(Date maxDate){
    	this.max = maxDate.getTime();
    }
    
    public void setMinDate(Date minDate){
    	this.min = minDate.getTime();
    }
    
    public Date getMaxDate(){
    	return new Date((long) this.max);
    }


    public Date getMinDate(){
    	return new Date((long) this.min);
    }
    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
    	this.method = method;
    }

    public double getMin() {
        return min;
    }

    public String getStringMin() {
    	String strValue = null;
    	if(this.type != null && !type.equals(JavaTypes.OBJECT)){
    		strValue = JavaTypes.getInstance().getStringValue(this.getType(), this.getMin());
    	}
    	return strValue;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
    /**
     * 设置时间的增长方式,
     * 
     * <p><code>setDateStep</code></p>
     * @param part 取值可为: TDate.YEAR,TDate.MONTH,TDate.DAY,TDate.HOUR,
     * 						TDate.MINUTE,TDate.SECOND.
     * @param num 数量级.
     * @author LiuXiaojie 2007-6-16
     */
    public void setDateStep(int part, int num){
    	this.step = part * num;
    }
//  [end]
    
    public String toString(){
    	return this.getName();
    }

	public int compareTo(Object arg0) {
       if (arg0 instanceof Role) {
           Role role = (Role) arg0;
           if(this.getName() != null && role.getName() != null){
               return this.getName().compareToIgnoreCase(role.getName());
           }
       } 
       return 0;
    }

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

    public float getNullPercent() {
        return nullPercent;
    }

    public void setNullPercent(float nullPercent) {
        this.nullPercent = nullPercent;
    }	
    
    public static void main(String[] args) throws SQLException, BaseException {
        Role role = new Role();
        role.setName("xxxxx");
        role.setType(Integer.class);
        role.setMin(1000);
        role.setMax(500000);
        role.setPrefix("usr");
        role.setSuffix("@abc.com");
        
        for(int i=0;i < 10000; i++){
            System.out.println(role.getValueByRole());
        }
    }


    public boolean isNullable() {
        return nullable;
    }


    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }


	public RoleFactory getRoleFactory() {
		return roleFactory;
	}


	public void setRoleFactory(RoleFactory roleFactory) {
		this.roleFactory = roleFactory;
	}


	public void setJavaSource(JavaSource javaSource) {
		this.javaSource = javaSource;
		if(this.javaSource != null){
			this.javaSource.setRole(this);
		}
	}



	public JavaSource getJavaSource() {
		return javaSource;
	}
}
