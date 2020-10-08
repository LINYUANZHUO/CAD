package com.vkejun.wjs.activity;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.bmob.*;
import com.vkejun.wjs.utils.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import android.support.v7.widget.Toolbar;

public class EditCodeActivity extends AppCompatActivity
{
	private String getbiaoti,getneirong;
	private AlertDialog.Builder a;
	private ClipboardManager manager;

	private String ObjectId;

	private String objectid;

	private String getid;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittexthanghao);

		Intent intent=getIntent();
		getbiaoti = intent.getStringExtra("biaoti");
		getneirong = intent.getStringExtra("neirong");
		getid = intent.getStringExtra("id");


		EditText edittext1=(EditText)findViewById(R.id.ftactivityEditText1);
		EditText edittext2=(EditText)findViewById(R.id.edittexthanghao);
		edittext1.setText(getbiaoti);
		edittext2.setText(getneirong);



		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//设置toolbar颜色
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		//设置标题
		toolbar.setTitle("帖子详情");
		//toolbar.setTitle(getbiaoti);//toolbar呈现标题栏
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
			case R.id.gx:
				updatePosting();
				break;
			case R.id.menulist1:
				//复制
				a=new AlertDialog.Builder(EditCodeActivity.this);
				a.setTitle("更新");
				//a.setIcon(R.drawable.fuzhi);//图标
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
							Toast.makeText(EditCodeActivity.this,"复制代码成功！",Toast.LENGTH_LONG).show();
						}
					});

				a.setNegativeButton("复制标题",new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							//第二个按钮的点击事件
							manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
							manager.setText(getbiaoti);
							Toast.makeText(EditCodeActivity.this,"复制标题成功！",Toast.LENGTH_LONG).show();
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
			case R.id.delete:
				deletePosting();
				break;



		}
		return super.onOptionsItemSelected(item);
	}

	private void updatePosting()
	{

		EditText edittext1=(EditText)findViewById(R.id.ftactivityEditText1);
		EditText edittext2=(EditText)findViewById(R.id.edittexthanghao);

		String nr= edittext1.getText().toString().trim();
		String nr2=edittext2.getText().toString().trim();


		MyUser user=BmobUser.getCurrentUser(MyUser.class);
		
		if(user == null)
		{
			
		}


		//添加数据
		Post vkejun = new Post();
		vkejun.setValue("user",BmobUser.getCurrentUser(MyUser.class));
		vkejun.setTitle(nr);//标题
		vkejun.setMessage(nr2);//内容
		//vkejun.setObjectId(getid);
		vkejun.update(getid, new UpdateListener()
			{
				@Override
				public void done(BmobException e)
				{
					// TODO: Implement this method
					if(e == null)
					{
						ToastUtils.success(getApplicationContext(), "更新成功");

						Intent  xh=new Intent( );
						xh.setClass(EditCodeActivity.this,HomeActivity.class);
						startActivity(xh);//接触当前acticit
					}else{
						Log.i("bmob","更新失败：\n"+e.getMessage()+","+e.getErrorCode());

					}

				}


			});

	}

	private void deletePosting()
	{
		Post vkejun = new Post();
		vkejun.setObjectId(getid);//获取帖子唯一 objectid
		vkejun.delete(new UpdateListener()
			{

				@Override
				public void done(BmobException e)
				{
					// TODO: Implement this method
					if(e == null)
					{
						ToastUtils.success(getApplicationContext(), "删除成功");
						Intent  xh=new Intent( );
						xh.setClass(EditCodeActivity.this,HomeActivity.class);
						startActivity(xh);//接触当前acticit
					}
					else
					{
						ToastUtils.error(getApplicationContext(), "删除失败: \n" +  e.getMessage());
					}
				}


			});

	}





}
