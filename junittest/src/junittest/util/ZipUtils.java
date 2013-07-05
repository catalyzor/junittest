package junittest.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

//import org.apache.commons.io.IOUtils;
//import org.apache.log4j.Logger;

public class ZipUtils {
	
	


//	static Logger logger = Logger.getLogger(ZipUtils.class);
	
	static final long timeUpdate = 2000;

//	public static void zip(String outFileName, String dirName) throws Exception {
//		ZipOutputStream out = null;
//		try {
//			out = new ZipOutputStream(new FileOutputStream(outFileName));
//			zip(out, new File(dirName), "");
//		} finally {
//			if (null != out) {
//				out.flush();
//				out.close();
//			}
//		}
//	}

//	public static void zip(ZipOutputStream out, File f, String base) throws Exception {
//		if (f.isDirectory()) {
//			File[] fl = f.listFiles();
//			ZipEntry zipEntry = new ZipEntry(base + "/");
//			zipEntry.setTime(timeUpdate);
//			out.putNextEntry(zipEntry);
//			base = base.length() == 0 ? "" : base + "/";
//			for (int i = 0; i < fl.length; i++) {
//				zip(out, fl[i], base + fl[i].getName());
//			}
//		} else {
//			// 瀵瑰凡缁忓帇缂╄繃鐨勬枃浠舵閲囧彇鎵撳寘鏂瑰紡锛屽姞蹇�搴�
//			if(isAlreadyCompressed(f.getName())) {
//				out.setLevel(ZipOutputStream.STORED);
//			}else {
//				out.setLevel(ZipOutputStream.DEFLATED);
//			}
//
//			FileInputStream in = null;
//			try {
//				ZipEntry zipEntry = new ZipEntry(base);
//				zipEntry.setTime(timeUpdate);
//				out.putNextEntry(zipEntry);
//				in = new FileInputStream(f);
//
//				// TODO optimize via buffer size
//				IOUtils.copy(in, out);
//			} finally {
//				if (in != null) {
//					in.close();
//				}
//			}
//		}
//	}

//	private static boolean isAlreadyCompressed(String fileName) {
//		// avoid compress an already compressed file
//		boolean res = false;
//
//		// 宸茬粡鍘嬬缉鍥犳涓嶉渶鍐嶅帇缂╃殑鏂囦欢鍚庣紑鍚嶅垪琛�
//		String[] exts = new String[] {
//				".zip",
//				".jar",
//				".jpg",
//				".png",
//				".war",
//		};
//
//		for(String ext : exts) {
//			if(fileName.endsWith(ext)) {
//				res = true;
//				break;
//			}
//		}
//
//		return res;
//	}

	public static void unZip(String zipfile, String destDir) throws Exception {
		if (!destDir.endsWith("/")) {
			destDir = destDir + "/";
		}

		byte b[] = new byte [1024]; 
		int length; 
		ZipFile zipFile = new ZipFile( new File(zipfile)); 
		Enumeration<?> enumeration = zipFile.entries(); 
		ZipEntry zipEntry = null ; 
		while (enumeration.hasMoreElements()) { 
			zipEntry = (ZipEntry) enumeration.nextElement(); 
			File loadFile = new File(destDir + zipEntry.getName()); 
			if (zipEntry.isDirectory()) { 
				loadFile.mkdirs(); 
			} else { 
				if (!loadFile.getParentFile().exists()) 
					loadFile.getParentFile().mkdirs(); 

				OutputStream outputStream = null;
				InputStream inputStream = null;
				try {
					outputStream = new FileOutputStream(loadFile); 
					inputStream = zipFile.getInputStream(zipEntry); 
					while ((length = inputStream.read(b)) > 0) 
						outputStream.write(b, 0, length);
				} finally {
					if (outputStream != null) {
						outputStream.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				}
			} 
		} 
		zipFile.close();
	} 

	
}
