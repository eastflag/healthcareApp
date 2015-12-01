package kr.co.aura.mtelo.healthcare.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import kr.co.aura.mtelo.healthcare.network.LTimeOut_Kepper;
import kr.co.aura.mtelo.healthcare.network.LTimeOut_Kepper.Kepper_CallBack;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager.OneButton_Handle;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class Content_Download {

	private final String TAG = "Content_Download";
	private Context mContext; 
	private String mMp3;
//	private ContentValues mvalues; 
	private ProgressDialog mPro;
	private boolean isShowDialog;
	
//	public Content_Download( Context con, String down_file , String save_file, ContentValues values)
//	{
//		 this(null, con , down_file, save_file, values, true);
//	}
	HttpURLConnection  connection = null ;
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public Content_Download( Context con , String down_url, final String save_path, boolean showDialog)
	{
		
		MLog.write(TAG, "Content_Download Working Start");
		mContext = con;
		isShowDialog = showDialog;
		
		String file_name =down_url.split("/")[down_url.split("/").length-1];
		MLog.write(TAG, "Content_Download downfiile = "+down_url + ", save_file = "+save_path+", file_name= "+file_name);


		//13.11.05 서버의 파일용량을 확인하여 틀리면 재 다운로드
//		 if(LCommonFunction.isFileExists(save_path+file_name) )
//		 {
//			 isShowDialog = false;
//			 final String url = down_file.replace(file_name, "");
//				  new Thread(new Runnable() {
//				 @Override
//				 public void run() {
//					 try {
//						 final URL  u = new URL( url + URL_Encoder(file_name));
//						 connection = (HttpURLConnection)u.openConnection();
//						 connection.connect();
//						 
//						 if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
//						 {
//							 int fileLength = connection.getContentLength();
//							 File f= new File(save_path + file_name);
//							 if(f.length() != fileLength )
//							 {
//								 f.delete();
//								 isShowDialog = true;
//							 }
//							 Log.e(" ", "파일 = "+ f.length() +", 서버 = "+fileLength);
//						 }
//					 } catch (MalformedURLException e) {
//						 e.printStackTrace();
//					 } catch (IOException e) {
//						 e.printStackTrace();
//					 }finally{
//						 connection.disconnect();
//						 connection = null;
//					 }
//				 }
//			 }).start();
//		 }

		 
		if(isShowDialog)
		{
			mPro = new ProgressDialog(mContext);
			mPro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mPro.setCancelable(false);
			mPro.setMessage( "컨텐츠를 다운로드중 입니다.");
//			mPro.setProgressNumberFormat ("%1d kb of %2d kb");
			mPro.setProgressNumberFormat ("");
			mPro.show();
		}
		
		Dwonload_Working download = new Dwonload_Working();
		download.execute( down_url.replace(file_name, "") , save_path, file_name );
		
		
	}
	
	public String getMp3()
	{
		return mMp3;
	}
	
	private String URL_Encoder(String url)
	{
		String encodedString = null; 
		try {
			 encodedString = URLEncoder.encode(url, "EUC-KR");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		Log.e("" , "URL_Encoder = "+ encodedString);
		return encodedString;
	}
	
	
	private class Dwonload_Working extends AsyncTask<String, String, String >{

		@Override
		protected String doInBackground(String... params) {// [0]:다운로드 경로, [1]:패키지명, [2]: 다운받는 파일이름 
			 int count;
//			 String file_save = params[1] + params[2];
			 String file_save = params[1] + "/temp.mp4";
//			 file_save = Define.PATH_ROOT_SDCARD+"/"+params[2];
			 MLog.write(TAG, "File down start :"+params[0]);
			 
			 
			 LTimeOut_Kepper time = new LTimeOut_Kepper();
				time.Start_Kepper( 600000, new Kepper_CallBack() {
					
					@Override
					public void onTimeOut() {
						Log.e("Content_Download" ,  "캔슬");
						mPro.dismiss();
						mPro.cancel();
						onCancelled();
					}
				});
				
				
				//13.11.05 같은파일로 다운로드하는것으로 변경
//			 if(LCommonFunction.isFileExists(file_save) )
//			 {
//				 return file_save;
//			 }

			 try {
		            InputStream input = null;
		            OutputStream output = null;
		            HttpURLConnection connection = null;
		            try {
		            	URL url = new URL(params[0]+URL_Encoder(params[2]));
		                connection = (HttpURLConnection) url.openConnection();
		                connection.connect();

		                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
		                     return "Server returned HTTP " + connection.getResponseCode() 
		                         + " " + connection.getResponseMessage();
		                
		                int fileLength = connection.getContentLength();

		                // download the file
		                input = connection.getInputStream();
		                output = new FileOutputStream(file_save);

		                byte data[] = new byte[4096];
		                long total = 0;
		                while ((count = input.read(data)) != -1) {
		                    if (isCancelled())  return null;
		                    total += count;
		                    if (fileLength > 0) // only if total length is known
		                    publishProgress(""+(int)((total*100)/fileLength)) ; //	onProgressUpdate()를 업데이트 한다 
		                    														  //	lenghtOfFile을 사용해서 파일의 용량을 100으로 환산하여 업데이트
		                    output.write(data, 0, count);
		                }
		            } catch (Exception e) {
		                return e.toString();
		            } finally {
		                try {
		                    if (output != null)
		                        output.close();
		                    if (input != null)
		                        input.close();
		                } 
		                catch (IOException ignored) { }

		                if (connection != null)
		                    connection.disconnect();
		            }
		        } finally {
		        }			 
			 
			 return file_save;
		}

		@Override
		protected void onPostExecute(String result) {
			if(result == null )
			{
				return;
			}
			
//			if(mPro != null) mPro.dismiss();
			if(isShowDialog)
			{
				mPro.cancel();
			}
			
			if(LCommonFunction.isFileExists(result) )
			{
				
				Uri uri = Uri.parse(result);
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.setDataAndType(uri, "video/mp4");
				mContext.startActivity(it);
			}
			 Log.e("Content_Download", " 파일 다운로드 앤드! "+ result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
//			super.onProgressUpdate(values);
//			 MLog.write(TAG, "onProgressUpdate: "+ values[0]);
			  if(mPro != null)mPro.setProgress(Integer.parseInt(values[0]));
		}
		
		@Override
		protected void onCancelled(String result) {
			super.onCancelled(result);
		
		}
	}
}
