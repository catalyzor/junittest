package junittest.resource;

public interface TestResultConstants {


	public static final String RESULT_NAME_OK = "成功";
	public static final String RESULT_NAME_FAIL = "失败";
	public static final String RESULT_NAME_IGNORE = "未测试项";
	public static final String RESULT_NAME_ERROR = "预置条件执行失败";
	
	public static final String[] RESULT_NAMES= new String[]{RESULT_NAME_IGNORE, RESULT_NAME_OK, RESULT_NAME_FAIL, RESULT_NAME_ERROR};
}
