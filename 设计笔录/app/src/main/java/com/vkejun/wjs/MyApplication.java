package com.vkejun.wjs;

import android.app.*;
import cn.bmob.v3.*;

public class MyApplication extends Application 
{
	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		Bmob.initialize(this, "bf5fd690ed270e70f2ad18f331a3f2e0");//进度管理

	}

}
