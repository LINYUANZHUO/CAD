package com.vkejun.wjs.activity;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.activity.*;
import com.vkejun.wjs.bmob.*;
import com.vkejun.wjs.utils.*;

import android.support.v7.widget.Toolbar;

public class SettingActivity extends AppCompatActivity
{

	private TextView fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		toolbar.setTitle("发帖");
        //toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
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


		fb = (TextView) findViewById(R.id.ftactivityTextView1);

		//发帖按钮
		fb.setOnClickListener(new OnClickListener()
			{

				private EditText title;

				private EditText message;
				@Override
				public void onClick(View p1)
				{
					title=(EditText)findViewById(R.id.ftactivityEditText1);
					message=(EditText)findViewById(R.id.ftactivityEditText2);

					String nr= title.getText().toString().trim();
					String nr2=message.getText().toString().trim();
					if (nr.isEmpty() || nr2.isEmpty())
					{
						Toast.makeText(SettingActivity.this,"发帖标题或内容不能为空",Toast.LENGTH_SHORT).show();
					}
					else /* if(title.getText().toString().length()>25)
					 {
					 title.setError("亲，你写的标题太长了！"); */
					{
						//Post.setAuthor(BmokUser.getCurrentUser(MyUser.class));
						MyUser bu=BmobUser.getCurrentUser(MyUser.class);

				    	//添加数据
				    	Post vkejun = new Post();
				    	vkejun.setTitle(nr);//标题
					    vkejun.setMessage(nr2);//内容
						vkejun.setAuthors(bu);//发布者
						vkejun.setBk("AIDE");//板块
						vkejun.setSh(true);//审核true
						vkejun.setRz(true);//认证false
					    vkejun.save(new SaveListener<String>()
							{

								@Override
								public void done(String p1, BmobException p2)
								{
									if (p2 == null)
									{
										//Toast.makeText(PostingActivity.this,"发帖成功",Toast.LENGTH_SHORT).show();
										ToastUtils.success(getApplicationContext(), "您刚发的帖子需要审核，审核成功后才会显示！");
										Intent  xh=new Intent( );
										xh.setClass(SettingActivity.this,HomeActivity.class);
										startActivity(xh);//接触当前acticit

									}
									else
									{
										//Toast.makeText(PostingActivity.this,"发帖失败",Toast.LENGTH_SHORT).show();
										ToastUtils.error(getApplicationContext(), "发帖失败，请重试!");

									}
								}

							});
					}

				}
			});



    }
}
