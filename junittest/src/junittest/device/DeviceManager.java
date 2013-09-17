package junittest.device;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import junittest.resource.ResourceManager;
import junittest.view.AdditionLogView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.android.ddmlib.DdmPreferences;
import com.broadthinking.btt.smartcard.SPISmartCard;
import com.broadthinking.btt.smartcard.SampleCard;
import com.broadthinking.btt.device.ExtDeviceException;
import com.broadthinking.btt.device.ExtDeviceManager;
import com.broadthinking.btt.device.IExtDevice;
import com.broadthinking.btt.device.ILogReceiver;
import com.broadthinking.btt.phone.AdbSocketPhone;
import com.broadthinking.btt.phone.SamplePhone;

public class DeviceManager {
	
	public static final String TYPE_PHONE = Messages.DeviceManager_0;
	public static final String TYPE_SPISMARTCARD = Messages.DeviceManager_1;	
	public static final String TYPE_SAMPLE_PHONE = Messages.DeviceManager_2;
	public static final String TYPE_SAMPLE_CARD = Messages.DeviceManager_3;

	public static final String CLASSNAME_PHONE = AdbSocketPhone.class.getName();
	public static final String CLASSNAME_SPISMARTCARD = SPISmartCard.class.getName();	
	public static final String CLASSNAME_SAMPLE_PHONE = SamplePhone.class.getName();
	public static final String CLASSNAME_SAMPLE_CARD = SampleCard.class.getName();
	
//	public static Map<String, Phone> mapPhone = new HashMap<>();
	public static String[] getDeviceTypes(){
		return new String[]{TYPE_PHONE, TYPE_SPISMARTCARD, TYPE_SAMPLE_PHONE, TYPE_SAMPLE_CARD};
	}
	public static String[] getDeviceClassname(){
		return new String[]{CLASSNAME_PHONE, CLASSNAME_SPISMARTCARD, CLASSNAME_SAMPLE_PHONE, CLASSNAME_SAMPLE_CARD};
	}
	public Map<String, Device> mapDevice = new HashMap<>();
	private String name;
	private IProject project;
	private List<String> lstProjectDeviceType;
	
	public List<String> getProjectDeviceTypes(){
		if(lstProjectDeviceType == null) lstProjectDeviceType = new ArrayList<>();
		return lstProjectDeviceType;
	}
	
	public List<Device> getConnectedDevices(String classnameString){
		List<Device> lstDevice = new ArrayList<Device>();
		
		Iterator<Entry<String, Device>> itr = mapDevice.entrySet().iterator();
		
		while(itr.hasNext()){
			Device device = itr.next().getValue();
			
			if(device.getDevice().getClass().getName().equals(classnameString) && device.isConncted()){
				lstDevice.add(device);
			}
		}
		
		return lstDevice;
	}
	
	public List<Device> getDevices(String type) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ExtDeviceException{
//		init();
		List<Device> lstDevice = new ArrayList<>();
//		if(type.equals(TYPE_PHONE)){
			Iterator<Entry<String, Device>> itr = mapDevice.entrySet().iterator();
			while(itr.hasNext()){
				Device device = itr.next().getValue();
				if(device.getType().equals(type)){
					lstDevice.add(device);
				}
			}
//		}
		return lstDevice;
	}
	public Map<String, Device> getAllDevices() {
		return mapDevice;
	}

	public void setDevices(Map<String, Device> mapDevice) {
		this.mapDevice = mapDevice;
	}
	
	public void disconnectAllDevices() throws ExtDeviceException{
		Iterator<Device> itr = getAllDevices().values().iterator();
		while(itr.hasNext()){
			Device device = itr.next();
			if(device.isConncted()){
				device.disconnnect();
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	public DeviceManager(String name, IProject project) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ExtDeviceException{
		this.name = name;
		this.project = project;
		init();
	}
	static IExtDevice[] sIExtDevices = null;
	
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ExtDeviceException {
		// TODO Auto-generated method stub
//		updateUI(project);
		//release all devices
		disconnectAllDevices();
		getAllDevices().clear();
		
		List<DeviceConfig> lstConfig = getProjectDeviceConfigs(project);
		String[] typeStrings = getDeviceTypes();
		String[] classnameStrings = getDeviceClassname();
		
		if (sIExtDevices == null){
			sIExtDevices = new IExtDevice[typeStrings.length];
			
			//init devices list 
			for (int i =0; i < typeStrings.length; i++){
				sIExtDevices[i] = ExtDeviceManager.getInstance(classnameStrings[i]);				
			}
		}
		for(DeviceConfig config: lstConfig){
			if(!getProjectDeviceTypes().contains(config.getType())){
				getProjectDeviceTypes().add(config.getType());
			}
			for (int i =0; i < typeStrings.length; i++){
				if(config.getType().equals(typeStrings[i])){
					String[] devices = sIExtDevices[i].listDevices();
					if(devices != null){
						for(String name: devices){
							Device dev = new Device(typeStrings[i], name, config.num, config.log, classnameStrings[i]);
							if(!mapDevice.containsKey(name)){
								mapDevice.put(name, dev);
							}else{
								Device dd = mapDevice.get(name);
								dd.setLog(config.log);
								dd.setNum(config.num);
							}
						}
					}
					break;
				}
			}
			/**
			if(config.getType().equals(TYPE_PHONE)){
				IExtDevice device = ExtDeviceManager.getInstance("com.broadthinking.btt.phone.AdbSocketPhone");
				String[] devices = device.listDevices();
				if(devices != null){
					for(String name: devices){
						Device dev = new Device(TYPE_PHONE, name, config.num, config.log, AdbSocketPhone.class.getName());
						if(!mapDevice.containsKey(name)){
							mapDevice.put(name, dev);
						}else{
							Device dd = mapDevice.get(name);
							dd.setLog(config.log);
							dd.setNum(config.num);
						}
					}
				}
			}else if(config.getType().equals(TYPE_SAMPLE_PHONE)){
				IExtDevice device = ExtDeviceManager.getInstance(SamplePhone.class.getName());
				String[] devices = device.listDevices();
				if(devices != null){
					for(String name: devices){
						Device dev = new Device(TYPE_SAMPLE_PHONE, name, config.num, config.log, SamplePhone.class.getName());
						if(!mapDevice.containsKey(name)){
							mapDevice.put(name, dev);
						}else{
							Device dd = mapDevice.get(name);
							dd.setLog(config.log);
							dd.setNum(config.num);
						}
					}
				}
			}else if(config.getType().equals(TYPE_SAMPLE_CARD)){
				IExtDevice device = ExtDeviceManager.getInstance(SampleCard.class.getName());
				String[] devices = device.listDevices();
				if(devices != null){
					for(String name: devices){
						Device dev = new Device(TYPE_SAMPLE_CARD, name, config.num, config.log, SampleCard.class.getName());
						if(!mapDevice.containsKey(name)){
							mapDevice.put(name, dev);
						}else{
							Device dd = mapDevice.get(name);
							dd.setLog(config.log);
							dd.setNum(config.num);
						}
					}
				}
			}
			*/
		}
	}

	public static void addDeviceConfig(IProject project, String type, int num, boolean log){
		Properties properties = new Properties();
		properties.put(Messages.DeviceManager_4,type + Messages.DeviceManager_5 + num + Messages.DeviceManager_6 + log);
		IFile file = project.getFile(ResourceManager.FILE_CONFIG);
		if(file.exists()){
			try {
				OutputStream out = new FileOutputStream(file.getLocation().toFile());
				properties.store(out, null);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void addDeviceConfig(IProject project, DeviceConfig[] deviceConfigs){
		Properties properties = new Properties();
		for(int i = 0;i < deviceConfigs.length;i ++){
			if(deviceConfigs[i] != null){
				properties.put(Messages.DeviceManager_7 + i + Messages.DeviceManager_8, deviceConfigs[i].getType() + Messages.DeviceManager_9 + deviceConfigs[i].getNum() + Messages.DeviceManager_10 + deviceConfigs[i].isLog());
			}
//			properties.put("device" + i + ".num", deviceConfigs[i].getNum());
//			properties.put("device" + i + ".log", deviceConfigs[i].isLog());
		}
		IFile file = project.getFile(ResourceManager.FILE_CONFIG);
		if(file.exists()){
			try {
				OutputStream out = new FileOutputStream(file.getLocation().toFile());
				properties.store(out, null);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<DeviceConfig> getProjectDeviceConfigs(IProject project){
		List<DeviceConfig> lstConfig = new ArrayList<>();
		Properties properties = new Properties();
		try {
			InputStream input = project.getFile(ResourceManager.FILE_CONFIG).getContents();
			properties.load(input);
			input.close();
			Iterator<Entry<Object, Object>> itr = properties.entrySet().iterator();
			while(itr.hasNext()){
				Entry<Object, Object> entry = itr.next();
				if(entry.getKey().toString().startsWith(Messages.DeviceManager_11)){
					String value = (String) entry.getValue();
					if(value != null){
						String[] strs = value.split(Messages.DeviceManager_12);
						DeviceConfig config = new DeviceConfig(strs[0].trim(), Integer.parseInt(strs[1]), Boolean.parseBoolean(strs[2]));
						lstConfig.add(config);
					}
				}
			}
		} catch (IOException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lstConfig;
	}
//	public static void updateUI(IProject project){
//		Properties properties = new Properties();
//		try {
//			InputStream input = project.getFile(ResourceManager.FILE_CONFIG).getContents();
//			properties.load(input);
//			input.close();
//			Iterator<Entry<Object, Object>> itr = properties.entrySet().iterator();
//			while(itr.hasNext()){
//				Entry<Object, Object> entry = itr.next();
//				if(entry.getKey().toString().startsWith("device")){
//					String value = (String) entry.getValue();
//					if(value != null){
//						String[] strs = value.split(";");
//						DeviceConfig config = new DeviceConfig(strs[0].trim(), Integer.parseInt(strs[1]), Boolean.parseBoolean(strs[2]));
//						if(config.isLog()){
////							for(int i = 0;i < config.num;i ++){
//								PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AdditionLogView.ID, config.type, IWorkbenchPage.VIEW_CREATE);
////							}
//						}
//					}
//				}
//			}
//		} catch (IOException | CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
//	public static Phone[] getPhones(){
//		Phone[] phones = null;
//		try {
//			IExtDevice device = ExtDeviceManager.getInstance("com.broadthinking.btt.phone.AdbSocketPhone");
//			String[] listpStrings = device.listDevices();
//			phones = new Phone[listpStrings.length];
//			for(int i = 0; i < listpStrings.length; i ++){
//				if(!mapPhone.containsKey(listpStrings[i])){
//					phones[i] = new Phone(listpStrings[i], device);
//					mapPhone.put(listpStrings[i], phones[i]);
//				}
//			}
//		} catch (ClassNotFoundException | InstantiationException
//				| IllegalAccessException | ExtDeviceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return mapPhone.values().toArray(phones);
//	}
	public static class DeviceConfig{
		private String type;
		private int num;
		private boolean log;
		
		public DeviceConfig(){}
		
		public DeviceConfig(String type, int num, boolean log){
			this.type = type;
			this.num = num;
			this.log = log;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public boolean isLog() {
			return log;
		}
		public void setLog(boolean log) {
			this.log = log;
		}
		
	}
	
//	public static class Phone{
//		private String name;
//		private IExtDevice device;
//		
//		public Phone(String name, IExtDevice device){
//			this.name = name;
//			this.device = device;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public IExtDevice getDevice() {
//			return device;
//		}
//
//		public void setDevice(IExtDevice device) {
//			this.device = device;
//		}
//
//		
//		public boolean connect() throws ExtDeviceException{
//			
//			return this.device.connectDevice(this.name);
//		}
//		
//		public void disconnect() throws ExtDeviceException{
//			
//			this.device.disconnectDevice();
//		}
//		
//		public boolean restart(){
//			
//			return false;
//		}
//	}
	
	public static class Device{
		private boolean log;
		//private boolean conncted;
		private IExtDevice device;
		private String name;
		private String type;
		private int num;
		private Thread t1;
		private junittest.device.DeviceManager.Device.Devicelogreceiver adDevicelogreceiver;
		
		public boolean isConncted() {
			//return conncted;
			return device.isConnect();
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public boolean isLog() {
			return log;
		}
		public void setLog(boolean log) {
			this.log = log;
		}
		public IExtDevice getDevice() {
			return device;
		}
		public void setDevice(IExtDevice device) {
			this.device = device;
		}
		
		public Device(String type, String name, int num, boolean log, String classname) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
			this.type = type;
			this.name = name;
			this.num = num;
			this.log = log;
			this.device = ExtDeviceManager.getInstance(classname);
//			if(log) log();
		}
//		public Device(IExtDevice device, boolean log){
//			this.device = device;
//			this.log = log;
//		}
		
		public String[] getDevices() throws ExtDeviceException{
			return device.listDevices();
		}
//		public void connect(String devicename) throws ExtDeviceException{
//			conncted = device.connectDevice(devicename);
//		}
		public void connect() throws ExtDeviceException{
			device.connectDevice(name);
			//conncted = true;
			log(true);
		}
		
		public void disconnnect() throws ExtDeviceException{
			//conncted = false;
			log(false);
			device.disconnectDevice();
		}
		
		public void restart() throws ExtDeviceException{
			device.resetDevice(0);
		}
		
		public void log(boolean bool){

			if(isLog()){
				if(bool){
					adDevicelogreceiver  = new Devicelogreceiver();
//					if(t1 == null){
						t1 = new Thread(new runlog(device, adDevicelogreceiver));
						t1.start();
//					}
				}else{
					adDevicelogreceiver.cancel();
				}
			}
		}
		public class runlog implements java.lang.Runnable{
			IExtDevice mDevice;
			Devicelogreceiver mDevicelogreceiver;
			public runlog(IExtDevice device, Devicelogreceiver aDevicelogreceiver) {
				mDevice = device; 
				mDevicelogreceiver = aDevicelogreceiver;
			}
			@Override
			public void run() {

				try {
					DdmPreferences.setTimeOut(300000);
					mDevice.runLogServer(mDevicelogreceiver);
				} catch (ExtDeviceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		public class Devicelogreceiver implements ILogReceiver{
			boolean bisCancel = false;
			@Override
			public void addOutput(byte[] data, int offset, int length) {
				final String log = new String(data,offset,length);
				IProject project = ResourceManager.getInstance().getProject();
				if(project == null) return;
				IFolder folder = project.getFolder(ResourceManager.FOLDER_LOG);
				if(folder.exists()){
					String filename = Device.this.type + Messages.DeviceManager_13 + Device.this.name + Messages.DeviceManager_14 + ResourceManager.SUFFIX_ADDITIONAL_LOG;
					IFile logfile = folder.getFile(filename);
					if(!logfile.exists()){
						try {
							File file = logfile.getLocation().toFile();
							if(file.createNewFile()){
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					File file = logfile.getLocation().toFile();
					
					try {
						BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
						writer.write(log);
						writer.newLine();
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(file.length()/(1024 * 1024) >= 3){
						String str = Device.this.type + Messages.DeviceManager_15 + Device.this.name + Messages.DeviceManager_16 + new SimpleDateFormat(Messages.DeviceManager_17).format(Calendar.getInstance().getTime());
						int i = 0;
						if(!file.renameTo(new File(file.getParentFile(), str + Messages.DeviceManager_18 + ResourceManager.SUFFIX_ADDITIONAL_LOG))){
							while(!file.renameTo(new File(file.getParentFile(), str + (++i) + Messages.DeviceManager_19 + ResourceManager.SUFFIX_ADDITIONAL_LOG))){
								if(i >= 100){
									break;
								}
							}
						}
					}
				}
				if (!bisCancel){
//				System.out.println(new String(data,offset,length));
					Display.getDefault().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								AdditionLogView view = (AdditionLogView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AdditionLogView.ID, Device.this.type + Device.this.name, IWorkbenchPage.VIEW_CREATE);
								if(view != null){
									view.updateTitle(Device.this.type + Messages.DeviceManager_20 + Device.this.name);
									view.appendContent(log);
								}
							} catch (PartInitException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				}
			}

			@Override
			public boolean isCancelled() {
				// TODO Auto-generated method stub
				return bisCancel;
			}

			public void cancel() {
				// TODO Auto-generated method stub
				bisCancel = true;
			}


		}
	}
}
