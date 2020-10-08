package com.vkejun.wjs.editor.as;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.b.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.editor.as.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import android.support.v7.widget.Toolbar;

public class Jieshouqi extends AppCompatActivity
{
	private String getbiaoti,getneirong;
	private AlertDialog.Builder a;
	private ClipboardManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jieshouqi_edittexthanghao);

		Intent intent=getIntent();
		getbiaoti = intent.getStringExtra("biaoti");
		getneirong = intent.getStringExtra("neirong");

		EditText edittext=(EditText)findViewById(R.id.jieshouqiedittexthanghaoJiesouqi_edittexthanghao1);
		edittext.setText(getneirong);


		Toolbar toolbar = (Toolbar) findViewById(R.id.jieshouqiedittexthanghaoToolbar1);
		//设置toolbar颜色
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		//设置标题
		toolbar.setTitle(getbiaoti);
		//添加toolbar
		setSupportActionBar(toolbar);
		//toolbar添加返回按钮
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//返回按钮点击事件
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					//退出
					finish();
				}
			});
	}
	// 让菜单同时显示图标和文字
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (menu != null) {
			if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
				try {
					Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
					method.setAccessible(true);
					method.invoke(menu, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.jieshouqi, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menulist1:
				//复制
				a=new AlertDialog.Builder(Jieshouqi.this);
				a.setIcon(R.drawable.fuzhi);//图标
				//a.setCancelable(false);//点击界面其他它地方弹窗不会消失
				a.setTitle("复制");//标题
				a.setMessage("请选择要复制的内容。");//弹窗内容
				a.setPositiveButton("复制代码", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							//第一个按钮按钮的点击事件
							//获取当前时间
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
							Date curDate = new Date(System.currentTimeMillis());
							manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							manager.setText(getneirong+"\n\n/******************************************\n*此代码源于AIDE布局助手\n*下载地址：https://pan.baidu.com/s/1NaMMm89gZpJ7zjdq3GVijg\n*作者：小亨\n*"+formatter.format(curDate)+"\n******************************************/");
							Toast.makeText(Jieshouqi.this,"复制代码成功！",Toast.LENGTH_LONG).show();
						}
					});

				a.setNegativeButton("复制标题",new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							//第二个按钮的点击事件
							manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							manager.setText(getbiaoti);
							Toast.makeText(Jieshouqi.this,"复制标题成功！",Toast.LENGTH_LONG).show();
						}
					});
				a.show();
				break;
			case R.id.menulist2:
				//分享
				//获取当前时间
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());
				manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				Intent shareIntent = new Intent(); shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT,getbiaoti+"\n\n"+getneirong+"\n\n/******************************************\n*此代码源于AIDE布局助手\n*下载地址：https://pan.baidu.com/s/1NaMMm89gZpJ7zjdq3GVijg\n*作者：小亨\n*"+formatter.format(curDate)+"\n******************************************/");
				shareIntent.setType("text/plain");
				startActivity(Intent.createChooser(shareIntent, "分享到"));

				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
