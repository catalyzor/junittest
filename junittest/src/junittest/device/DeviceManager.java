package junittest.device;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import junittest.resource.ResourceManager;
import junittest.view.AdditionLogView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.android.ddmlib.DdmPreferences;
import com.broadthinking.btt.card.SampleCard;
import com.broadthinking.btt.device.ExtDeviceException;
import com.broadthinking.btt.device.ExtDeviceManager;
import com.broadthinking.btt.device.IExtDevice;
import com.broadthinking.btt.device.ILogReceiver;
import com.broadthinking.btt.phone.AdbSocketPhone;
import com.broadthinking.btt.phone.SamplePhone;

public class DeviceManager {

	public static final String TYPE_PHONE = "Phone";
	public static final String TYPE_SAMPLE_PHONE = "SamplePhone";
	public static final String TYPE_SAMPLE_CARD = "SampleCard";
//	public static Map<String, Phone> mapPhone = new HashMap<>();
	public static String[] getDeviceTypes(){
		return new String[]{TYPE_PHONE, TYPE_SAMPLE_PHONE, TYPE_SAMPLE_CARD};
	}
	public Map<String, Device> mapDevice = new HashMap<>();
	private String name;
	private IProject project;
	
	public List<Device> getDevices(String type) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ExtDeviceException{
		init();
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
	
	private void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException, ExtDeviceException {
		// TODO Auto-generated method stub
//		updateUI(project);
		List<DeviceConfig> lstConfig = getProjectDeviceConfigs(project);
		for(DeviceConfig config: lstConfig){
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
		}
	}

	public static void addDeviceConfig(IProject project, String type, int num, boolean log){
		Properties properties = new Properties();
		properties.put("device0",type + ";" + num + ";" + log);
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
				properties.put("device" + i + "", deviceConfigs[i].getType() + ";" + deviceConfigs[i].getNum() + ";" + deviceConfigs[i].isLog());
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
				if(entry.getKey().toString().startsWith("device")){
					String value = (String) entry.getValue();
					if(value != null){
						String[] strs = value.split(";");
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
		private boolean conncted;
		private IExtDevice device;
		private String name;
		private String type;
		private int num;
		private Thread t1;
		private junittest.device.DeviceManager.Device.Devicelogreceiver adDevicelogreceiver;
		
		public boolean isConncted() {
			return conncted;
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
			conncted = device.connectDevice(name);
			log(conncted);
		}
		
		public void disconnnect() throws ExtDeviceException{
			conncted = false;
			log(conncted);
			device.disconnectDevice();
		}
		
		public void restart() throws ExtDeviceException{
			device.resetDevice();
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
			public void addOutput(final byte[] data, final int offset, final int length) {
				
				if (!bisCancel){
//				System.out.println(new String(data,offset,length));
					Display.getDefault().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								AdditionLogView view = (AdditionLogView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AdditionLogView.ID, Device.this.type + Device.this.name, IWorkbenchPage.VIEW_CREATE);
								if(view != null){
									view.updateTitle(Device.this.type + ":" + Device.this.name);
									view.appendContent(new String(data,offset,length));
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
				bisCancel = false;
			}


		}
	}
}
