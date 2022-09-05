import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.testDataBuilder.resources.ResourceManager;
import com.testDataBuilder.ui.main.MainFrame;
import com.testDataBuilder.ui.main.others.AppPropertyDialog;
import com.testDataBuilder.util.AppProperty;

public class TestDataBuilder {
    
    
	/**
     * <p>
     * <code>main</code>
     * </p>
     * 
     * @param args
     * @author LiuXiaojie 2007-1-25
     */
    public static void main(String[] args) {    	
        System.out.println("WorkDir:" +System.getProperty("user.dir"));
        SwingUtilities.invokeLater(new TestDataBuilderRunner());
    }
}

class TestDataBuilderRunner implements Runnable {
	static Logger logger = Logger.getLogger(TestDataBuilder.class);
    
	static FileLock lock = null;
    static String dir = System.getProperty("java.home") + "\\bin";
    
	public static boolean tdbIsRunning() {
		try {
			
			// 获得实例标志文件 
			File flagFile = new File(dir, "tdbInstance");
			// 如果不存在就新建一个   
			if (!flagFile.exists())
				flagFile.createNewFile();
			// 获得文件锁

			lock = new FileOutputStream(flagFile).getChannel().tryLock();
			// 返回空表示文件已被运行的实例锁定
			if (lock == null) {
				return true;
			}
		} catch (Exception ex) {
			logger.error("tdbIsRunning", ex);
		}
		return false;
	}
    public void run() {
    	if(tdbIsRunning()){
    		System.out.println("已经有一个事例已经在运行了。");
    	}else{
	        AppProperty appProperty = AppProperty.getInstance();
	        try {            
	            appProperty.loadFromDefXmlFile();
	            if(StringUtils.isNotEmpty(appProperty.getLookAndFeel())){
	                UIManager.setLookAndFeel(appProperty.getLookAndFeel());
	            }
	            if(StringUtils.isNotEmpty(appProperty.getLocale())){
	                ResourceManager.setLocale(new Locale(appProperty.getLocale()));
	            }
	            
	            if (appProperty.getShowConfigDialog()) {
	                AppPropertyDialog appPropertyDialog = new AppPropertyDialog(appProperty);
	                appPropertyDialog.setModal(true);
	                appPropertyDialog.setVisible(true);
	                if(appPropertyDialog.isOK()){
	                    if(StringUtils.isNotEmpty(appProperty.getLookAndFeel())){
	                        UIManager.setLookAndFeel(appProperty.getLookAndFeel());
	                    }
	                    if(StringUtils.isNotEmpty(appProperty.getLocale())){
	                        ResourceManager.setLocale(new Locale(appProperty.getLocale()));
	                    }
	                }
	            }
	        } catch (Exception e) {
                logger.error("TestDataBuilderRunner", e);
	        }
	        
	        MainFrame thisClass = MainFrame.getInstance();
	        thisClass.initWorkspace();
	    
	        thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        thisClass.setVisible(true);
    	}
    }

}