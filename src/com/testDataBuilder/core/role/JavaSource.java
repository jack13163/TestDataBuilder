package com.testDataBuilder.core.role;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.FlyweightCDATA;

import com.testDataBuilder.core.XmlUtil;
import com.testDataBuilder.dynamicCompile.DynamicCompiler;
import com.testDataBuilder.exception.BaseException;
import com.testDataBuilder.exception.JavaCodeRuntimeException;
import com.testDataBuilder.resources.RM;
import com.testDataBuilder.util.StringUtil;
import com.testDataBuilder.util.XmlFileUtil;
/**
 * Java代码，高级规则。
 * @author Administrator
 *
 */
public class JavaSource {

	static final Logger logger = Logger.getLogger(JavaSource.class);
	
    public static final String TAG_JAVA_SOURCE = "javaCode";
    public static final String TAG_RETURN_TYPE = "returnType";
    public static final String RETURN_TYPE_SIMPLE_OBJ = "simpleObj";
    public static final String RETURN_TYPE_COMPLEX_OBJ = "complexObj";
    
    public static final String TAG_RETURN_FIELDS = "returnFields";   
    
    
    private IJavaRole javaRoleImpl = null;
    
    private Role role = null;
    
    private String returnType = null;
    
    private String returnFields = null;
    
    private String javaCode = null;
    
    public IJavaRole getJavaRoleImpl() throws BaseException{
    	String javaSource = this.getJavaCode();
    	if(javaRoleImpl == null && StringUtils.isNotEmpty(javaSource)){
    		DynamicCompiler dynamicCompiler = new DynamicCompiler();
    		javaRoleImpl = dynamicCompiler.getJavaRoleImpl(javaSource, this.getRole().getRoleFactory().getTableConfig());
    	}
    	return javaRoleImpl;
    }
    
    public boolean isSimpleObjType(){
    	return RETURN_TYPE_SIMPLE_OBJ.equalsIgnoreCase(returnType);
    }
    
    public boolean isComplexObjType(){
    	return RETURN_TYPE_COMPLEX_OBJ.equalsIgnoreCase(returnType);
    }
    
    public static JavaSource configure(Element javaSourceEle) throws BaseException{
    	JavaSource javaSource = new JavaSource();
    	String strReturnType = XmlUtil.getStringAttributeValue(javaSourceEle,TAG_RETURN_TYPE,true);
    	
    	if(RETURN_TYPE_SIMPLE_OBJ.equalsIgnoreCase(strReturnType)){
    		javaSource.setReturnType(RETURN_TYPE_SIMPLE_OBJ);
    	}else if(RETURN_TYPE_COMPLEX_OBJ.equalsIgnoreCase(strReturnType)){
    		javaSource.setReturnType(RETURN_TYPE_COMPLEX_OBJ);
    		String returnFields = XmlUtil.getStringAttributeValue(javaSourceEle,TAG_RETURN_FIELDS,true);
    		javaSource.setReturnFields(returnFields);
    	}else{
    		throw new BaseException(TAG_JAVA_SOURCE , "invalid returnType value[" + strReturnType + "]");
    	}
    	
    	String javaCode = XmlUtil.getContentValue(javaSourceEle, true);
    	javaSource.javaCode = XmlFileUtil.xmlToString(javaCode);
    	
    	return javaSource;
    }

    public Object getObjectByJava() throws BaseException, SQLException{
    	try{
    		return this.getJavaRoleImpl().getValueByJava();
    	}catch(RuntimeException ex){
    		logger.error(String.format(RM.R("label.JavaSource.error.runtimeException"), this.getRole().getName()), ex);
    		throw new JavaCodeRuntimeException(String.format(RM.R("label.JavaSource.error.runtimeException"), this.getRole().getName()), ex);
    		//throw ex;
    	}
    }
    
    public Element toElement(){
    	Element javaSourceEle = DocumentHelper.createElement(TAG_JAVA_SOURCE);
    	javaSourceEle.addAttribute(TAG_RETURN_TYPE, this.getReturnType());
    	javaSourceEle.addAttribute(TAG_RETURN_FIELDS, this.getReturnFields());
    	
    	javaSourceEle.add(new FlyweightCDATA(getJavaCode()));
    	
    	return javaSourceEle;
    }
    
	public String getJavaCode() {
		return javaCode;
	}

	public void setJavaCode(String javaCode) throws BaseException {
		String oldJavaCode = this.getJavaCode();
    	if(StringUtil.equals(oldJavaCode, javaCode)){
    		return;
    	}
    	this.javaRoleImpl = null;
    	this.javaCode = javaCode;

		this.getJavaRoleImpl();
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getReturnFields() {
		return returnFields;
	}

	public void setReturnFields(String returnFields) {
		this.returnFields = returnFields;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
}
