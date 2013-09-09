package junittest.userlog;

public interface NameEnum {
	public static final String TAG_DEFAULT=Messages.NameEnum_0;
	
	public static final String TAG_RUNSUCCESS = Messages.NameEnum_1;
	public static final String TAG_RUNFAIL = Messages.NameEnum_2;
	
	public static final String TAG_PARAMETER = Messages.NameEnum_3;
	public static final String TAG_PARAMETER_VALUE = Messages.NameEnum_4;
	
	public static final String TAG_SEND = Messages.NameEnum_5;
	public static final String TAG_RECEIVE = Messages.NameEnum_6;
	
	public static final String TAG_SUCCESS_EXPECTED = Messages.NameEnum_7;	
	public static final String TAG_SUCCESS_EVALUATED = Messages.NameEnum_8;
	
	public static final String TAG_FAIL_EXPECTED = Messages.NameEnum_9;	
	public static final String TAG_FAIL_EVALUATED = Messages.NameEnum_10;	
	
	public static final String[] TAGS = {TAG_DEFAULT, TAG_RUNSUCCESS, TAG_RUNFAIL, TAG_PARAMETER, 
		TAG_PARAMETER_VALUE, TAG_SEND, TAG_RECEIVE, TAG_SUCCESS_EXPECTED,
		TAG_SUCCESS_EVALUATED,TAG_FAIL_EXPECTED,TAG_FAIL_EVALUATED};
}
