package junittest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.eclipse.core.resources.IResource;
import org.w3c.dom.Document;

public class ReportUtils {

	public static final String REPORT_TYPE_HTML = "HTML";
	public static final String REPORT_TYPE_PDF = "PDF";
	
	public static boolean generateReport(IResource logFile, String jasperfile, String type, String destPath) throws JRException, IOException{
		boolean bool = false;
		if(logFile.exists()){
//			try {
//				JRXmlDataSource dataSource = new JRXmlDataSource(logFile.getLocation().toOSString());
//				InputStream is = Activator.getDefault().getBundle().getEntry(jasperfile).openStream();
				Map<String, Object> params = new HashMap<>();
				Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream(logFile.getLocation().toOSString()));
			    params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
			    params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
			    params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
			    params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
			    params.put(JRParameter.REPORT_LOCALE, Locale.US);

				JasperPrint print = JasperFillManager.fillReport(jasperfile, params);
//				JasperPrint print = JasperFillManager.fillReport(FileLocator.resolve(Activator.getDefault().getBundle().getEntry(jasperfile)).getFile(), new HashMap<String, Object>(), dataSource);
//				is.close();
				switch(type){
				case REPORT_TYPE_HTML:
					JasperExportManager.exportReportToHtmlFile(print, destPath);
					break;
				case REPORT_TYPE_PDF:
					JasperExportManager.exportReportToPdfFile(print, destPath);
					break;
				}
				bool = true;
//			} catch (JRException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return bool;
	}
}
