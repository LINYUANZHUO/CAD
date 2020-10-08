package com.vkejun.wjs.utils;

import android.content.*;
import es.dmoral.toasty.*;

public class ToastUtils
{
	//成功显示绿色
	public static void success(Context c, String nr)
	{
		Toasty.success(c, nr).show();
	}
	//失败显示红色：error
	public static void error(Context c, String nr)
	{
		Toasty.error(c, nr).show();
	}
	//提示显示黄色
	public static void warning(Context c, String nr)
	{
		Toasty.warning(c, nr).show();
	}
}
