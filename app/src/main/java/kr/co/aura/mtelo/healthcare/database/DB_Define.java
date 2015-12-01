package kr.co.aura.mtelo.healthcare.database;

public interface DB_Define 
{

	
	public static final String URI_CHILD_DATA = "child_database";					//자녀 DB

	
	public static final String KEY_ID 						= 	"_id";					//_id
	public static final String KEY_CHILD_ID 				=	"child_id";				//자녀 ID
	public static final String KEY_CHILD_NAME			=	"child_name";		//자녀 이름
	public static final String KEY_CHILD_AGE 				=	"child_age";			//자녀 성별
	public static final String KEY_CHILD_GENDER 			= 	"child_gender";		//자녀 최근측정일
	public static final String KEY_CHILD_LASTDATE 		= 	"child_lastdate";		//자녀 측정회수
	public static final String KEY_CHILD_COUNT			= 	"child_count";		//자녀 측정회수
	public static final String KEY_CHILD_BMI				=	"child_bmi";			//자녀 BMI
	public static final String KEY_CHILD_CM				=	"child_cm";			//자녀 키
	public static final String KEY_CHILD_KG 				=	"child_kg";			//자녀 몸무게
	public static final String KEY_CHILD_G_POINT 	 	=  "child_g_point";		//자녀 성장점수
	public static final String KEY_CHILD_PHOTO_URI 	 	=	"child_photo";		//자녀 사진uri
	public static final String KEY_CHILD_BIRTHDAY 		=	"birth_day";			//자녀 생일
	public static final String KEY_CHILD_PPM  			= 	"child_ppm";			//자녀 ppm
	public static final String KEY_CHILD_COHD 			=	"child_cohd";			//자녀 chod
	public static final String KEY_CHILD_CM_STATUS 	= 	"child_cm_status";	//자녀 키의 스테이터스
	public static final String KEY_CHILD_KG_STATUS	 	= 	"child_kg_status";	//자녀 체중의 스테이터스
	public static final String KEY_CHILD_BMI_STATUS 	=	"child_bmi_status";	//자녀 BMI의 스테이터스
	public static final String KEY_CHILD_PPM_STATUS 	=	"child_ppm_status";	//자녀 흡연의 스테이터스
	public static final String KEY_CHILD_SCHOOL_ID  	= 	"child_school_id";	//13.10.10  schoolGradeId 로 사용 
	public static final String KEY_CHILD_MASTER_ID 		=  "child_master_id";	//마스터ID
	public static final String KEY_CHILD_ETC1				=	"child_etc1";			//자녀 예비1 
	public static final String KEY_CHILD_ETC2				= 	"child_etc2";			//자녀 예비2
	public static final String KEY_CHILD_ETC3			 	= 	"child_etc3";			//자녀 예비3
	public static final String KEY_CHILD_ETC4 				= 	"child_etc4";			//자녀 예비4
	
	
	public static final int COLUMN_ID = 0;
	public static final int COLUMN_CHILD_ID 					= COLUMN_ID +1;
	public static final int COLUMN_CHILD_NAME 			= COLUMN_ID +2;
	public static final int COLUMN_CHILD_ANG 				= COLUMN_ID +3;
	public static final int COLUMN_CHILD_GENDER 			= COLUMN_ID +4;
	public static final int COLUMN_CHILD_LASTDATE 		= COLUMN_ID +5;
	public static final int COLUMN_CHILD_COUNT 			= COLUMN_ID +6;
	public static final int COLUMN_CHILD_CM 				= COLUMN_ID +7;
	public static final int COLUMN_CHILD_KG 				= COLUMN_ID +8;
	public static final int COLUMN_CHILD_BMI 				= COLUMN_ID +9;
	public static final int COLUMN_CHILD_G_POINT 			= COLUMN_ID +10;
	public static final int COLUMN_CHILD_PHOTH_URI 		= COLUMN_ID +11;
	public static final int COLUMN_CHILD_BIRTHDAY  		= COLUMN_ID +12;
	public static final int COLUMN_CHILD_PPM				= COLUMN_ID +13;
	public static final int COLUMN_CHILD_COHD	 			= COLUMN_ID +14;
	public static final int COLUMN_CHILD_CM_STATUS 		= COLUMN_ID +15;
	public static final int COLUMN_CHILD_KG_STATUS 		= COLUMN_ID +16;
	public static final int COLUMN_CHILD_BMI_STATUS	 	= COLUMN_ID +17;
	public static final int COLUMN_CHILD_PPM_STATUS	 	= COLUMN_ID +18;
	public static final int COLUMN_CHILD_SCHOOL_ID 		= COLUMN_ID +19;
	public static final int COLUMN_CHILD_MASTER_ID 		= COLUMN_ID +20;
	public static final int COLUMN_CHILD_ETC1				= COLUMN_ID +21;
	public static final int COLUMN_CHILD_ETC2				= COLUMN_ID +22;
	public static final int COLUMN_CHILD_ETC3 				= COLUMN_ID +23;
	public static final int COLUMN_CHILD_ETC4 				= COLUMN_ID +24;
	
	
	public static final String 
	SEARCH					= "_search",
	INSERT					= "_insert",
	UPDATE					= "_update",
	DELETE					= "_delete",
	DELETE_TABLE			= "_delete_table";
}
