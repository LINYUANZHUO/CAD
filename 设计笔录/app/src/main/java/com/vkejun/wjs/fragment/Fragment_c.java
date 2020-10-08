package com.vkejun.wjs.fragment;

import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import cn.bmob.v3.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.bumptech.glide.*;
import com.tencent.connect.*;
import com.tencent.tauth.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.activity.*;
import com.vkejun.wjs.bmob.*;
import java.io.*;
import java.util.*;

import android.support.v7.widget.Toolbar;
import com.bumptech.glide.load.*;
import jp.wasabeef.glide.transformations.*;

public class Fragment_c extends Fragment implements OnClickListener
{

	private Toolbar toolbar;

	private LinearLayout ll1;

	private ImageView iv1;

	private TextView txv1,txv2,txv3;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.fragment_3, container, false);
		
		this.toolbar = v.findViewById(R.id.toolbar);
		ll1 = (LinearLayout) v.findViewById(R.id.fragment_3LinearLayout1);
		iv1 = (ImageView)v.findViewById(R.id.fragment_3ImageView1);
		txv1 = (TextView)v.findViewById(R.id.fragment_3TextView1);
		txv2 = (TextView)v.findViewById(R.id.fragment_3TextView2);
		txv3 = (TextView)v.findViewById(R.id.fragment_3TextView3);
		
		
		
		
		//getuser();
		
		initData();
		initItem();
		initToolbar();
		return v;
	}
	private void initItem()
	{
		
		this.ll1.setOnClickListener(this);
		
		
		
	}
	@Override
	public void onClick(View p1)
	{
		
		// TODO: Implement this method
		switch (p1.getId())
		{
			case R.id.fragment_3LinearLayout1:
				startActivity(new Intent(getContext(), LoginqqActivity.class));
				//ToastUtils.error(getContext(), "此版本暂不支持");
				break;
		}
		
		
	}
	
	
	private void initToolbar()
	{
		setHasOptionsMenu(true);
        this.toolbar.setTitle("我的");
		((AppCompatActivity)getActivity()).setSupportActionBar(this.toolbar);
        //((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(this.toolbar);
	}
	/*
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_name, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.name1:
                break;
            case R.id.name2:
                break;
        }
        return true;
    }
	*/
	private void initData()
	{
		
		//MyUser userInfo = MyUser.getCurrentUser(MyUser.class);
		//是否缓存有登陆数据,缓存时间为一年
		
		//MyUser icon = BmobFile.getDynamicPicture(MyUser.class);
		
		//获取本地用户对象        
		MyUser MyUse = BmobUser.getCurrentUser(MyUser.class); 
		//判断用户是否不等于无效
		
		if(MyUse != null)
			{
				
				//String urll=data.getStringExtra("URL");
				
				//获得Name的信息
                //object.getName();
                //获得数据的objectId信息
                //object.getObjectId();
                //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
                //object.getCreatedAt();
				
				// 允许用户使用应用
				//Glide.with(Fragment_c.this).load(object.getHeadportraiturl()).transform(new GlideCircleTransform(Fragment_c.this)).into(iv1);
				//img_url = dynamic.getDynamicPicture().getUrl();
				
				String Url = MyUse.getHeadportraiturl();
				
				//Glide.with(context).load(Url).into(iv1);
				
				//radius取值1-25，值越大图片越模糊
				//Glide.with(context).load(Url).bitmapTransform(new BlurTransformation(context, radius)).into(iv1);
				
				//Transformation cropCircleTransformation = null;
				Glide.with(Fragment_c.this)
				    .load(Url)
					.override(800, 800)
					.bitmapTransform(new Transformation[] { (Transformation)new CropCircleTransformation((Context)getActivity()) })
					.into(iv1);
					
				//加载唯一ID
				//txv1.setText(BmobUser.getCurrentUser(MyUser.class).getUsername());
				//加载作者头像
				/*
				ImageRequest imageRequest=new ImageRequest(Url, new Response.Listener<Bitmap>() 
					{
						@Override
						public void onResponse(Bitmap response)
						{
							iv1.setImageBitmap(response);
						}
					}, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error)
						{
							iv1.setImageResource(R.drawable.ic_account_circle);//失败用这张图片
						}
					});
					
				RequestQueue mQueue = Volley.newRequestQueue(getContext());//创建一个volley队列
				mQueue.add(imageRequest);//加入队列 开始下载
				*/
				//Glide.with(LoginqqActivity.this).load(figureurl_qq_2).crossFade(800).transform(new GlideCircleTransform(LoginqqActivity.this)).into(iv1);
				//Glide.with(Fragment_c.this).load(MyUse.getHeadportraiturl());
				txv1.setText(MyUse.getname());//昵称
				txv2.setText(MyUse.getUsername());//社区号
				txv3.setText(MyUse.getYhzss());//用户组
				/*
				if ( MyUse.getVip()==null  ){
					txv5.setText("否");
				}else  if ( MyUse.getVip()  ){
					txv5.setText("是");
				}else {
					txv5.setText("否");
				}
				*/
				
				//mIUiListener = new BaseUiListener();
				
			}else 
			{            
			//缓存用户对象为空时， 可打开用户注册界面… 
			
				
			
			}
			
	}
	
	
	
	
}
