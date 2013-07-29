package junittest.userlog;

public interface NameEnum {
	public static final String TAG_DEFAULT="DEFAULT";
	
	public static final String TAG_RUNSUCCESS = "RUNSUCCESS";
	public static final String TAG_RUNFAIL = "RUNFAIL";
	
	public static final String TAG_PARAMETER = "PARAMETER";
	public static final String TAG_PARAMETER_VALUE = "PARAMETER_VALUE";
	
	public static final String TAG_SEND = "SEND";
	public static final String TAG_RECEIVE = "RECEIVE";
	
	public static final String TAG_SUCCESS_EXPECTED = "SUCCESS_EXPECTED";	
	public static final String TAG_SUCCESS_EVALUATED = "SUCCESS_EVALUATED";
	
	public static final String TAG_FAIL_EXPECTED = "FAIL_EXPECTED";	
	public static final String TAG_FAIL_EVALUATED = "FAIL_EVALUATED";	
	
	public static final String[] TAGS = {TAG_DEFAULT, TAG_RUNSUCCESS, TAG_RUNFAIL, TAG_PARAMETER, 
		TAG_PARAMETER_VALUE, TAG_SEND, TAG_RECEIVE, TAG_SUCCESS_EXPECTED,
		TAG_SUCCESS_EVALUATED,TAG_FAIL_EXPECTED,TAG_FAIL_EVALUATED};
}
