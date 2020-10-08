package com.vkejun.wjs.utils;

import android.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.support.v7.app.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import com.vkejun.wjs.activity.*;

public class Welcome  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        }

		//建文件夹
		File a=new File("/sdcard/源社区/apks");
		if(a.isDirectory()==false)
			if(!a.mkdirs())
			{
				print("创建文件夹失败,请检查Sd卡");
			}
		a=new File("/sdcard/源社区/logs");
		if(a.isDirectory()==false)
			if(!a.mkdirs())
			{
				print("创建文件夹失败,请检查Sd卡");
			}
		a=new File("/sdcard/源社区/file");
		if(a.isDirectory()==false)
			if(!a.mkdirs())
			{
				print("创建文件夹失败,请检查Sd卡");
			}
	}
	private	void print(String str)
	{
		//Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
    }
	String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
		Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.READ_PHONE_STATE
    };
	//2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
	List<String> mPermissionList = new ArrayList<>();

	private final int mRequestCode = 100;//权限请求码
	//权限判断和申请
	private void initPermission() {

		mPermissionList.clear();//清空没有通过的权限
		//逐个判断你要的权限是否已经通过
		for (int i = 0; i < permissions.length; i++) {
			if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
				mPermissionList.add(permissions[i]);//添加还未授予的权限
			}
		}
		//申请权限
		if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
			ActivityCompat.requestPermissions(this, permissions, mRequestCode);

		}else{

			startActivity(new Intent(this,HomeActivity.class));
			finish();

			//说明权限都已经通过，可以做你想做的事情去
		}
	}
	//请求权限后回调的方法
	//参数： requestCode  是我们自己定义的权限请求码
	//参数： permissions  是我们请求的权限名称数组
	//参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		boolean hasPermissionDismiss = false;//有权限没有通过
		if (mRequestCode == requestCode) {
			for (int i = 0; i < grantResults.length; i++) {
				if (grantResults[i] == -1) {
					hasPermissionDismiss = true;
				}
			}
			//如果有权限没有被允许
			if (hasPermissionDismiss) {

				finish();

				showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
			}else{
				//全部权限通过，可以进行下一步操作。。。

                startActivity(new Intent(this,HomeActivity.class));
				finish();
			}
		}
	}
	/**
	 * 不再提示权限时的展示对话框
	 */
	AlertDialog mPermissionDialog;
	String mPackName = "com.vkejun.myapp.weixinasr";

	private void showPermissionDialog() {
		if (mPermissionDialog == null) {
			mPermissionDialog = new AlertDialog.Builder(this)
				.setMessage("已禁用权限，请手动授予")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelPermissionDialog();

						Uri packageURI = Uri.parse("package:" + mPackName);
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
						startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//关闭页面或者做其他操作
						cancelPermissionDialog();
					}
				})
				.create();
		}
		mPermissionDialog.show();
	}
	//关闭对话框
	private void cancelPermissionDialog() {
		mPermissionDialog.cancel();
	}

}

