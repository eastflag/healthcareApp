/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package kr.co.aura.mtelo.healthcare;

import java.io.File;

import kr.co.aura.mtelo.healthcare.Define.Config;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

@ReportsCrashes(
		formKey = "", // This is required for backward compatibility but not used
		mailTo = "aura.helth@gmail.com",
		mode = ReportingInteractionMode.DIALOG,
		customReportContent = { ReportField.DEVICE_ID , ReportField.DISPLAY , ReportField.USER_CRASH_DATE ,ReportField.USER_EMAIL, ReportField.USER_IP
				, ReportField.ANDROID_VERSION, ReportField.APP_VERSION_NAME , ReportField.BRAND
				,ReportField.PHONE_MODEL, ReportField.STACK_TRACE ,ReportField.USER_COMMENT},
				resDialogTitle  = R.string.dialog_title , 
				resDialogText = R.string.dialog_msg
//				resDialogCommentPrompt = R.string.dialog_prompt
//				resDialogOkToast = R.string.dialog_ok_toast
//				resDialogEmailPrompt = R.string.dialog_email
		)
public class ApplicationSetting extends Application {
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		super.onCreate();
		if (Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}
		initImageLoader(getApplicationContext());
		ACRA.init(this);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(30)
				.threadPriority(Thread.NORM_PRIORITY - 2)
//				.memoryCache( new LruMemoryCache(2*688*286))  //����Ʈ�� �̹��� ũ��
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCache(new UnlimitedDiscCache(new File(Define.DISK_CACHE_PATH) ) )
				.discCacheFileCount(100)   //��ũ ĳ�������� �ִ���� ����
				.tasksProcessingOrder(QueueProcessingType.LIFO)
					//	.writeDebugLogs()          // ����׿� �α׾���  
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}