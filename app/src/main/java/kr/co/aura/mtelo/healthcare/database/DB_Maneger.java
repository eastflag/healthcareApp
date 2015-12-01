package kr.co.aura.mtelo.healthcare.database;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.Define;
import kr.co.aura.mtelo.healthcare.util.MLog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DB_Maneger  implements DB_Define{

	private Context mContext;
	private ContentResolver mContentResolver;

	public DB_Maneger(Context context)
	{
		mContext = context;
		mContentResolver = mContext.getContentResolver();
	 }
	 
	 
	 //보관함에 입력
	 public void  insert_Child_Data(ContentValues values )
	 {
		 Uri u = Uri.parse(Define.URI +URI_CHILD_DATA +INSERT);
		 Uri insertItemUri = mContentResolver.insert( u , values);
		 MLog.write(this, "insert_Child_Data("+values+"), return = "+insertItemUri);
	 }
	 
	 
	//자녀데이터 고속입력DB
	 public void  fast_insert_Child_Data(ArrayList<ContentValues>  arrlist)
	 {
		 ContentValues[] values = new ContentValues[arrlist.size()];
		 values = (ContentValues[]) arrlist.toArray(new ContentValues[arrlist.size()] );
		 Uri u = Uri.parse(Define.URI +URI_CHILD_DATA +INSERT);
		 int insertItemUri=  mContentResolver.bulkInsert(u, values);
		 mContext.getContentResolver().notifyChange(u, null);
		 MLog.write(this, "fast_insert_Child_Data("+values+"), return = "+insertItemUri);
	 }
	 
	 

	//자녀데이터 쿼리
	 public Cursor get_Child_Data(String where, String order_by)
	 {
		 MLog.write(this, "get_Child_Data(" + where + ","+order_by+")");
		 Uri u = Uri.parse(Define.URI+URI_CHILD_DATA+SEARCH);
		 Cursor searchResult = mContentResolver.query(u, null, where, null, order_by);
		 MLog.write(this, "Query " + "query("+u+")" );
		 return searchResult;
	 }
	 
	 public int[] delete_All_Table()
	 {
		 int Result[] = new int[6];
		 Result[0] = mContentResolver.delete(Uri.parse(Define.URI +URI_CHILD_DATA+ DELETE), null, null);
//		 MLog.write(this, "delete_All_Table(), return = " +Result[0]+", "+Result[1]+", "+Result[2]+", "+Result[3]+", "+Result[4]+", "+Result[5]);
		 return Result;
	 }
	 
	 
	 
	 public int delete_Child_Data(String where)
	 {
		 int deleteResult = mContentResolver.delete(Uri.parse(Define.URI +URI_CHILD_DATA+ DELETE), where, null);
		 MLog.write(this, "delete_Child_Data(" + where + "), return = " + deleteResult);
		 return deleteResult;
	 }
	 
	 
	 public void update_Child_Data(ContentValues values, String where)
	 {
		 int updateResult = mContentResolver.update(Uri.parse(Define.URI +URI_CHILD_DATA + UPDATE), values, where , null);
		 MLog.write(this, "update_Child_Data updateResult = " + updateResult);
	 }
	 
	//자녀데이터 고속업데이트DB
	 public void  fast_update_Child_Data(ArrayList<ContentValues>  arrlist)
	 {
		 ContentValues[] values = new ContentValues[arrlist.size()];
		 values = (ContentValues[]) arrlist.toArray(new ContentValues[arrlist.size()] );
		 Uri u = Uri.parse(Define.URI +URI_CHILD_DATA +UPDATE);
		 int insertItemUri=  mContentResolver.bulkInsert(u, values);
		 mContext.getContentResolver().notifyChange(u, null);
		 MLog.write(this, "fast_update_Child_Data("+values+"), return = "+insertItemUri);
	 }
}
