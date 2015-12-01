package kr.co.aura.mtelo.healthcare.database;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.util.MLog;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class HealthCare_Provider extends ContentProvider implements DB_Define {
	private final String TAG = "HealthCare_Providrer";
	private Context mContext;
	private static SQLiteDatabase micContentDB = null;
	private static final String DATABASE_NAME = "content.db";
	private static final int DATABASE_VERSION = 1;
//	private static final UriMatcher uriMatcher;
	private static UriMatcher uriMatcher;
	static 
	{
//		MLog.set_LogEnable(Define.ENABLE_LOG, Define.LOG_FILTER);
		MLog.set_LogEnable(Define.LOG, "starring");
		
	}
	
	
	private static final int 
//			MIC_ITEMS 			= 1,
//			GET_TOTALSIZE 		= 2,
//			DELETE_ITEM 		= 3,
//			INSERT_ITEM 		= 4,
//			UPDATE_ITEM 		= 5,
//			UPDATE_TOTALSIZE 	= 6,
//			INSERT_TOTALSIZE 	= 7,
			DELETE_All_ITEM  	= 8,

			//그룹리스트 UR
			CHILD_LIST_SEARCH		= 20,
			CHILD_LIST_INSERT		= CHILD_LIST_SEARCH + 1,
			CHILD_LIST_UPDATE		= CHILD_LIST_SEARCH + 2,
			CHILD_LIST_DELETE		= CHILD_LIST_SEARCH + 3,
			CHILD_LIST_DELETE_TABLE = CHILD_LIST_SEARCH + 4;
			//그룹아이템 URI
	
//		init_uriMatcher();
	static 
	{
		MLog.set_LogEnable(Define.LOG, Define.LOG_FILTER);
	}
	
	private static void init_uriMatcher() {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//�׷� ����Ʈ
		uriMatcher.addURI(Define.URI_AUTHORITY, URI_CHILD_DATA + SEARCH, CHILD_LIST_SEARCH );
		uriMatcher.addURI(Define.URI_AUTHORITY, URI_CHILD_DATA + INSERT, CHILD_LIST_INSERT );
		uriMatcher.addURI(Define.URI_AUTHORITY, URI_CHILD_DATA + UPDATE, CHILD_LIST_UPDATE );
		uriMatcher.addURI(Define.URI_AUTHORITY, URI_CHILD_DATA + DELETE, CHILD_LIST_DELETE );
		uriMatcher.addURI(Define.URI_AUTHORITY, URI_CHILD_DATA + DELETE_All_ITEM, CHILD_LIST_DELETE_TABLE );
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count = 0;
		if ( micContentDB.isOpen() == false )
	    {
	    	micContentDB = new HealthCare_DBHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
	    }
		
	    switch (uriMatcher.match(uri)) 
	    {
	        case CHILD_LIST_DELETE:
	            count = micContentDB.delete(URI_CHILD_DATA, where, whereArgs);
	            break;
	        default: throw new IllegalArgumentException("Unsupported URI: " + uri);
	    }

	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}

	@Override
	public String getType(Uri uri) {
		MLog.write(this, "# DataBase_Provider - getType(" + uri + ") start.");
		
//	    switch (uriMatcher.match(uri)) 
//	    {
//	    	case CHILD:     	return "mic: select all items";
//	        case GET_TOTALSIZE:   	return "mic: get total saved file size";
//	        case DELETE_ITEM:	    return "mic: delete one item file";
//	        case INSERT_ITEM:	    return "mic: insert one item";
//	        case UPDATE_ITEM:		return "mic: update some item info";
//	        default: 				throw new IllegalArgumentException("Unsupported URI: " + uri);
//	    }
		return null;
	}

	
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if ( micContentDB.isOpen() == false )
	    {
	    	micContentDB = new HealthCare_DBHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
	    }
		
		if(values != null)
		{
			long rowID =0 ;
			
			switch (uriMatcher.match(uri)) {
			case CHILD_LIST_INSERT:
//				if(check_Insert(uri, values) ==0)
				rowID = micContentDB.insert(URI_CHILD_DATA, null, values);
				break;
			}
			
			if (rowID > 0) 
			{
				Uri _uri = ContentUris.withAppendedId(uri, rowID);
//				getContext().getContentResolver().notifyChange(_uri, null);
				return _uri;
			}
			throw new SQLException("Failed to insert row into " + uri);
		}
		
		return null;
	}

	
	@Override
	public boolean onCreate() 
	{
		init_uriMatcher();
		mContext = getContext();
		
		HealthCare_DBHelper dbHelper = new HealthCare_DBHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
	    micContentDB = dbHelper.getWritableDatabase();
	    
		return (micContentDB == null) ? false: true;
	}
	
	public void init()
	{
		
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String where, String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
	    switch (uriMatcher.match(uri)) 
	    {
	    	case CHILD_LIST_SEARCH:  // 				
	    		qb.setTables(URI_CHILD_DATA);
	    		if ( where != null )
	    			qb.appendWhere(where);
	    		break;
	        default: 					        	
	        	break;
	    }
	    
	    if ( micContentDB.isOpen() == false )
	    {
	    	micContentDB = new HealthCare_DBHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
	    }
	    Cursor c = null;
	    
	    try 
	    {
	    	c = qb.query(micContentDB, projection, where, selectionArgs, null, null, sortOrder);
	    	c.setNotificationUri(getContext().getContentResolver(), uri);
	    	MLog.write(Log.ERROR,  "Query " + "query("+uri+","+ projection+","+ where+","+ selectionArgs+","+ sortOrder+")");
	    }
	    catch (SQLiteException se) 
	    {
	    	MLog.write(this, "SQLiteException!! " + where);
	    	se.getStackTrace();
	    }
	    return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] selectionArgs) 
	{
		int count = 0;
	    if ( micContentDB.isOpen() == false )
	    {
	    	micContentDB = new HealthCare_DBHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
	    }
	    
	    MLog.write(Log.ERROR, TAG, uri +" "+where+ " match = "+uriMatcher.match(uri));
	    switch ( uriMatcher.match(uri) )
	    {
		    case CHILD_LIST_UPDATE:
		    	count = micContentDB.update(URI_CHILD_DATA, values, where, selectionArgs);
		    	break;
		    	
	    	default:
	    		throw new IllegalArgumentException("Unknown URI " + uri);
	    }
	    getContext().getContentResolver().notifyChange(uri, null);
	    return count;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int Result = 0;
		
		if ( micContentDB.isOpen() == false )
	    {
	    	micContentDB = new HealthCare_DBHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
	    }
		
		try
		{
			if(micContentDB.inTransaction() ) 
			{
				micContentDB.endTransaction();
			}
			
			MLog.write(TAG, "bulkInsert start begin transaction");
			micContentDB.beginTransaction();
			for(int i = 0; i< values.length ; i++ )
			{
				insert(uri, values[i]);
			}
			micContentDB.setTransactionSuccessful();
		}
		
		catch(Exception e){
			Result =  1;
		}
		finally{
			MLog.write(TAG, "bulkInsert end transaction");
			micContentDB.endTransaction();
		}
		return Result;
	}
	
	
	public static class HealthCare_DBHelper extends SQLiteOpenHelper
	{
		//그룹리스트 테이블 생성
		public  static  final String CREATE_CHILD_LIST_TABLE =
				"create table IF NOT EXISTS "+ URI_CHILD_DATA  + " ("
				+ KEY_ID + " integer primary key autoincrement, "
				+ KEY_CHILD_ID 					+ " TEXT, "
				+ KEY_CHILD_NAME				+ " TEXT, "
				+ KEY_CHILD_AGE					+ " TEXT, "
				+ KEY_CHILD_GENDER				+ " TEXT, "
				+ KEY_CHILD_LASTDATE			+ " TEXT, "
				+ KEY_CHILD_COUNT				+ " TEXT, "
				+ KEY_CHILD_CM					+ " TEXT, "
				+ KEY_CHILD_KG					+ " TEXT, "
				+ KEY_CHILD_BMI					+ " TEXT, "
				+ KEY_CHILD_G_POINT				+ " TEXT, "
				+ KEY_CHILD_PHOTO_URI			+ " TEXT, "
				+ KEY_CHILD_BIRTHDAY			+ " TEXT, "
				+ KEY_CHILD_PPM					+ " TEXT, "
				+ KEY_CHILD_COHD				+ " TEXT, "
				+ KEY_CHILD_CM_STATUS			+ " TEXT, "
				+ KEY_CHILD_KG_STATUS			+ " TEXT, "
				+ KEY_CHILD_BMI_STATUS			+ " TEXT, "
				+ KEY_CHILD_PPM_STATUS			+ " TEXT, "
				+ KEY_CHILD_SCHOOL_ID			+ " TEXT, "
				+ KEY_CHILD_MASTER_ID			+ " TEXT, "
				+ KEY_CHILD_ETC1				+ " TEXT, "
				+ KEY_CHILD_ETC2				+ " TEXT, "
				+ KEY_CHILD_ETC3				+ " TEXT, "
				+ KEY_CHILD_ETC4				+ " TEXT );";
		
		public HealthCare_DBHelper(Context context, String name, CursorFactory factory, int version) 
		{
			super(context, name, factory, version);
			MLog.write(this, "HealthCare_DBHelper(" + context + ", " + name + ", " + factory + ", " + version + ")");
		}


		@Override
		public void onCreate(SQLiteDatabase db) {
			MLog.write(this, "HealthCare_DBHelper.onCreate(" + db + ") start");
			db.execSQL(CREATE_CHILD_LIST_TABLE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			MLog.write(this, "HealthCare_DBHelper.onUpgrade(" + db + ", " + oldVersion + ", " + newVersion + ") start");
//			db.execSQL("DROP TABLE IF EXISTS " + URI_CHILD_DATA);
			onCreate(db);
		}
	}
	

}
