package junittest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import junittest.Activator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.eclipse.core.resources.IResource;

public class ReportUtils {

	public static final String REPORT_TYPE_HTML = "HTML";
	public static final String REPORT_TYPE_PDF = "PDF";
	
	public static boolean generateReport(IResource logFile, String jasperfile, String type, String destPath){
		boolean bool = false;
		if(logFile.exists()){
			try {
				JRXmlDataSource dataSource = new JRXmlDataSource(logFile.getLocation().toOSString());
				InputStream is = Activator.getDefault().getBundle().getEntry(jasperfile).openStream();
				JasperPrint print = JasperFillManager.fillReport(is, new HashMap<String, Object>(), dataSource);
				is.close();
				switch(type){
				case REPORT_TYPE_HTML:
					JasperExportManager.exportReportToHtmlFile(print, destPath);
					break;
				case REPORT_TYPE_PDF:
					JasperExportManager.exportReportToPdfFile(print, destPath);
					break;
				}
				bool = true;
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bool;
	}
}
