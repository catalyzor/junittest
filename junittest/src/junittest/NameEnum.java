package junittest;

public interface NameEnum {
	public static final String TAG_DEFAULT="DEFAULT";
	public static final String TAG_RUNSTEP = "RUNSTEP";
	public static final String TAG_PARAMETER = "PARAMETER";
	public static final String TAG_PARAMETER_VALUE = "PARAMETER_VALUE";
	
	public static final String TAG_C_APDU = "C_APDU";
	public static final String TAG_R_APDU = "R_APDU";
	public static final String TAG_EXPECTED_R_APDU = "EXPECTED_R_APDU";	
	public static final String TAG_EVALUATED_R_APDU = "EVALUATED_R_APDU";	
	
	public static final String[] TAGS = {TAG_DEFAULT, TAG_RUNSTEP, TAG_PARAMETER, TAG_PARAMETER_VALUE, 
		TAG_C_APDU, TAG_R_APDU, TAG_EXPECTED_R_APDU, TAG_EVALUATED_R_APDU};
}
