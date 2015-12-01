package kr.co.aura.mtelo.healthcare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

public class LCommonFunction 
{
	private final static String LOG_TAG = "CommonFunction";
	private static BitmapFactory.Options option = null;

	
	
	public static Bitmap getContactPicture(ContentResolver cr, String id, Resources res, int res_id) {
//		final Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI , Long.parseLong(id));		
		Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id)) ;
		InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri );

		if(input == null) 
		{
			return BitmapFactory.decodeResource(res, res_id/*R.drawable.beautyring_icon_default_thumb*/);
		}
		else 
		{
			return BitmapFactory.decodeStream(input);
		}
	}
	
	
	/**
	 * 한글 초성 얻기
	 * @param Hangul
	 * @return
	 */
	public static String HangulToChoseong(String Hangul)
	{
		String  result = null ;
//		result = Hangul; //원본 + 초성형식으로 결과가 출력되므로 주석처리
		
		if(Hangul == null || Hangul.length() == 0 )
		{
			result = "";
		}
		else
		{
			try
			{
				char[] choseong={ 0x3131 , 0x3132 , 0x3134 , 0x3137 , 0x3138 , // ㄱ , ㄲ , ㄴ , ㄷ , ㄸ         
						0x3139 , 0x3141 , 0x3142 , 0x3143 , 0x3145 , // ㄹ , ㅁ , ㅂ  , ㅃ , ㅅ  
						0x3146 , 0x3147 , 0x3148 , 0x3149 , 0x314a ,// ㅆ , ㅇ , ㅈ  , ㅉ , ㅊ
						0x314b , 0x314c , 0x314d , 0x314e  };		   // ㅋ , ㅌ  ,ㅍ , ㅎ
				
				int choseongNum , tempNum;
				char ch;
				
				for(int i = 0 ; i  < Hangul.length() ; i++)
				{
					ch = Hangul.charAt(i);
					if(ch != ' ')
					{
						if(ch >= 0xAC00 && ch <= 0xD7A3 )
						{
							tempNum = ch - 0xAC00;
							choseongNum = tempNum / 588;//(21 * 28);
							result += choseong[choseongNum];
						}
						else
						{
							result +=ch;
						}
					}
				}
				
				result = result.replace("null", "");
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				return result;
			}
		}
		return result;
	}
	
	//국제전화번호에서 국내용으로 변경	
	public static String check_Plus82(String src)
	{
		if ( src == null )
			src = "";
		
		if (src.startsWith("+82"))
		{
			return "0" + src.substring(3);  // +8210XXXXXXXX 의 경우 +82를 0으로 변경
		}
		
		return src;
	}
	
	
	// 폴더와 하위 파일을 삭제한다
	public static void deleteDir(String path) {
		MLog.write("CommonFunction", "|DELETE| deleteDir(" + path + ")");
		File dir = new File(path);

		if (!dir.exists()) {
			dir.mkdirs();
		} else {
			String[] children = dir.list();
			if (children != null) {
				// 파일의 삭제
				for (int i = 0; i < children.length; i++) {
					String filename = children[i];
					File file = new File(path + "/" + filename);
					if (file.exists()) {
						file.delete();
						MLog.write("CommonFunction", "|DELETE| delete file("
								+ file + ")");
					}
				}
				// 디렉토리의 삭제
				File[] files = dir.listFiles();
				for (File file : files) {
					if (file.isDirectory()) {
						deleteDir(file.getAbsolutePath()); // 하위 디렉토리 루프
						MLog.write("CommonFunction", "|DELETE| delete Dir("
								+ file.getAbsolutePath() + ")");
					}
					file.delete();
				}
			}
		}
	}
	
	
	// json 추출
	public static String HtmlToJson(String result)
	{
		int nStart = result.indexOf("<body>");
		int nEnd = result.indexOf("</body>");
		
		if(nStart < nEnd)
		{
			result = result.substring(nStart + 6, nEnd);
			result = result.trim();
		}		
		return result;
	}
	
	public static boolean save_File(Context context, String path, String filename, byte data[])
	{
		boolean result = false;
		
		FileOutputStream os = null;
		
		try
		{
			os = new FileOutputStream( new File(path + filename) );
			os.write(data, 0, data.length);
			result = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if ( os != null )
			{
				try
				{
					os.close();
					os = null;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		MLog.write(LOG_TAG, "save_File("+path+","+filename+", "+data+"), data size = " + data.length+", result= "+result);	
		return result;
	}
	
	public static boolean isFileExists(String filepath)
	{
		boolean return_value = false;
		
		if ( filepath == null )
			return_value = false;
		else		
			return_value = new File(filepath).isFile();
		MLog.write(LOG_TAG, "isFileExists("+filepath+" ), return : " + return_value);
		return return_value;
	}
	

	
	
	
	
//	인자로 받은 다운로드경로를 분해해서 파일이름만 리턴해준다 
	public static String split_FileName(String dn_Str)
	{
		if(dn_Str == null) return null;
		return dn_Str.split("/")[dn_Str.split("/").length -1 ];
	}
	
	public static String add_Ringtone(Context con , String title,  String mp3)
	{
		
		ContentResolver cr = con.getContentResolver();
		File filePath = new File(mp3);
		if(isFileExists(mp3))
		{
			MLog.write(LOG_TAG, "add_Ringtone(title = "+title+", mp3 = "+mp3+")");
			
			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DATA, filePath.getAbsolutePath());
			values.put(MediaStore.MediaColumns.TITLE, title);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");
			values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
			values.put(MediaStore.Audio.Media.IS_ALARM, false);
			values.put(MediaStore.Audio.Media.IS_MUSIC, false);
			Uri newUri = cr.insert(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, values);
			MLog.write(LOG_TAG, "File exist = " + filePath.exists() + ", URI = " + newUri);
			String ringtone_id = newUri.toString().split("/")[newUri.toString().split("/").length -1] ;
			
			Cursor cursor = null;
			String selection = MediaStore.Audio.Media.TITLE+ " = \'"+title+"\'"; //알림음이면 IS_NOTIFICATION으로,,,
			String sortOrder = MediaStore.Audio.Media.DATA+ " ASC";
			cursor = cr.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, selection, null, sortOrder);
			cursor.moveToFirst();
			
			if(cursor.getCount() == 1 )
			{
				do {
					ringtone_id =  cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
					MLog.write(LOG_TAG, "change_Ringtone ringtone_id= "+ringtone_id);
				} while (cursor.moveToNext());
				cursor.close();
			}
			return ringtone_id ;
		}
		MLog.write(LOG_TAG, "add_Ringtone(title = "+title+", mp3 = "+mp3+") is not File, return null!");
		return null;
	}

	
	
	
	public static void change_Ringtone(Context con, String ring_id, ArrayList<String> ids)
	{
		ContentResolver cr = con.getContentResolver();
		
		String customRingtoneValue = "content://media/internal/audio/media/"+ring_id;
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, customRingtoneValue);
        
		 
        Uri uri =  ContactsContract.Contacts.CONTENT_URI;
		for(int i =0 ; i< ids.size(); i++)
		{
			String where = ContactsContract.Contacts._ID+ " = \'"+ids.get(i)+ "\'" ;
			int result = cr.update(uri, values, where, null);
			MLog.write(LOG_TAG, "change_Ringtone count ="+i+", ring= "+customRingtoneValue+", where = "+where +". Result = "+result );
			
		}
	}
	
	
	
	public static String setDeleteHyphen(String strCallNumber) 
	{
	    String strCallphonName = strCallNumber;
	 
	    try 
	    {
	    	strCallphonName = strCallphonName.replace("-", "");
	    	strCallphonName = strCallphonName.replace(" ", "");
		}
	    catch (Exception e) 
		{
	    	 return strCallNumber;
		}
	    
	    try 
	    {
	    	Integer.getInteger(strCallphonName);
		}
	    catch (Exception e) 
		{
	    	return "010000000000"; //전화번호에 문자가 있을 경우
		}
	    return strCallphonName;
	}
	
	
	public static boolean is_Exist_Package(Context con, String packageName)
	{
		boolean result;
		PackageManager pm = con.getPackageManager();
		List<ApplicationInfo> list = pm.getInstalledApplications(0);
		ArrayList<String> items = new ArrayList<String>();
		for(int i =0; i< list.size() ; i++)
		{
			items.add(list.get(i).packageName );
		}
		
		if(items.contains(packageName) )
			result = true;
		else
			result = false;
		
		MLog.write(LOG_TAG,"is_Exist_Package("+ packageName + " is "+result +" )");
		return result;
	}

	public static String remake_PhoneNumber(Context con, String originNum) 
	{
		int len = 0;
        String num = "";
        
        if ( originNum == null )
        	return num;

        String str = getLanguage(con);
        
    	len = originNum.length();
    	originNum = check_Plus82(originNum);
    	if ( len == 0 )
    	{
    	}
    	else
    	{
    		switch (len) 
    		{
    		case 7: // 3,4로 나뉘어 준다.
    			num += originNum.substring(0, 3) + "-";
    			num += originNum.substring(3, 7);
    			break;
    			
    		case 8: // 4,4로 나뉘어 준다.
    			num += originNum.substring(0, 4) + "-";
    			num += originNum.substring(4, 8);
    			break;
    			
    		case 9:  // 2,3,4로 나뉘어 준다.
    			num += originNum.substring(0, 2) + "-";
    			num += originNum.substring(2, 5) + "-";
    			num += originNum.substring(5, 9);
    			break;
    			
    		case 10:  // 2,4,4 그리고 3,3,4로 나뉘어 준다.
    			if ( originNum.startsWith("02") ) // 2,4,4
    			{
    				num += originNum.substring(0, 2) + "-";
    				num += originNum.substring(2, 6) + "-";
    				num += originNum.substring(6, 10);
    			}
    			else	// 3,3,4
    			{
    				num += originNum.substring(0, 3) + "-";
    				num += originNum.substring(3, 6) + "-";
    				num += originNum.substring(6, 10);
    			}
    			break;
    			
    		case 11:  // 4,4,4로 나뉘어 준다.
    			num += originNum.substring(0, 3) + "-";
    			num += originNum.substring(3, 7) + "-";
    			num += originNum.substring(7, 11);
    			break;
    			
    		default: // 고민 없이 뿌린다.
    			num = originNum;
    			break;
    		}
    	}
        return num;
	}
    
    private static boolean isJapanAreaCode(int c, int s, int e)
    {
    	boolean isTrue = false;
    	if ( c >= s && c <= e )
    		isTrue = true;
    	
    	return isTrue;
    }

    public static BitmapFactory.Options get_Bitmap_Option()
	{
    	if ( option == null )
    	{
    		option = new BitmapFactory.Options();
//			option.inSampleSize = 1;S
    		option.inPurgeable = true;
    		option.inDither = true;
    	}
		return option;
	}

    public static String getLanguage(Context con)
    {
        Locale clsLocale =con.getResources( ).getConfiguration( ).locale;
        return clsLocale.getLanguage();
    }
	
	@SuppressWarnings("unused")
	public static String getCountryMoney(Context con, String ko_price, String jp_price, String en_price, String base_price)
	{
		if(ko_price == null || jp_price == null || en_price ==null)
		{
			ko_price = "0";
			jp_price = "0";
			en_price= "0";
		}
		
		String str = getLanguage(con);
		String result = "0";
		if(str.equalsIgnoreCase("ko"))
		{	
			if(ko_price != null)
			{
				result = ko_price + " "+ base_price;
			}
			else
			{
				result = " "+ base_price;
			}
		}
		else if(str.equalsIgnoreCase("ja"))
		{
			result = jp_price+"円";
		}
		else
		{
			result = en_price+"＄";
		}
		
		return result;
	}
	
	
	
	// WiFi에 연결된 상태인지 파악
	public static boolean isWiFi(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfoWiFi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfoWiFi.isConnected();
	}

	
	private static ConnectivityManager cm;
	private static NetworkInfo ni;
	public static boolean IsRoaming(Context context)
	{
//		if ( cm == null )
//			cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//		if ( ni == null )
//			ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//		
		
		// 2013.02.25 ykchoi 로밍일때라도 와이파이에 연결되어 있다면 통신을 허용하도록 수정 
//		boolean result;
//		if(isWiFi(context))  
//			result = false;
//		else
//			result = ni.isRoaming();
//		
//		return result;
		
		// 2013.02.25 ykchoi 로밍이건 아니건 무조건 패스로 수정 
//	 return ni.isRoaming();
		
		return false;
	}
	
	public static boolean isMobile(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfoMoblie = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return networkInfoMoblie.isConnected();
	}	
	
	//수신시 벨소리를 죽이고 mp3로 플레이 가능하도록 추가
	private void changeRingerMode(Context context, boolean beMute)
	{

		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if ( beMute )
		{
			/**
			 * To Enable silent mode.....
			 */
			audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
		else
		{
			/**
			 * To Enable Ringer mode.....
			 */
			audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}
	
	}
	
	 public static boolean copyFile(File file , String save_file){
	        boolean result;
	        if(file!=null&&file.exists()){
	            try {
	                FileInputStream fis = new FileInputStream(file);
	                FileOutputStream newfos = new FileOutputStream(save_file);
	                int readcount=0;
	                byte[] buffer = new byte[1024];
	                while((readcount = fis.read(buffer,0,1024))!= -1){
	                    newfos.write(buffer,0,readcount);
	                }
	                newfos.close();
	                fis.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            result = true;
	        }else{
	            result = false;
	        }
	        MLog.write(Log.ERROR,"",  "copyFile result = "+ result+" file="+ file);
	        return result;
	    }	
	    
	    
	    
	    /**
	     * 앱의 버전 네임을 얻어온다.
	     * @author HYT
	     * @param context
	     * @return 앱의 version name 값.
	     */
	    public static String getAppVersionName(Context context)
	    {
	    	String appVersionName = "";
	    	PackageManager pm = context.getPackageManager();
	    	try 
			{
	    		appVersionName = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).versionName;
			} 
			catch (Exception e) 
			{
				appVersionName = "Get App Version Name Fail.";
				e.getStackTrace();
			}
	    	return appVersionName;
	    }
		
}
