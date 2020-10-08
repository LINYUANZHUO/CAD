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

public class aa extends Fragment implements OnClickListener
{

	private Toolbar toolbar;

	private CardView cv1;

	private TextView txv1,txv2,txv3,txv4,txv5;

	private BmobUser.BmobThirdUserAuth authInfo;
	private static final String APP_ID = "1107700107";//官方获取的APPID
	private String openID,accessToken,expires,name,sex,province,city,year,figureurl_qq_2;
	private Tencent mTencent;

	private ImageView iv1;

	private TextView vip;

	//private BaseUiListener mIUiListener;
	private UserInfo mUserInfo;

	private File imageUrl;

	private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v=inflater.inflate(R.layout.aa, container, false);
		this.toolbar = v.findViewById(R.id.toolbar);
		cv1 = (CardView) v.findViewById(R.id.f3CardView1);
		iv1 = (ImageView)v.findViewById(R.id.contentmainImageView1);
		txv1 = (TextView)v.findViewById(R.id.contentmainTextView1);
		txv2 = (TextView)v.findViewById(R.id.contentmainTextView2);
		txv3 = (TextView)v.findViewById(R.id.contentmainTextView3);
		txv4 = (TextView)v.findViewById(R.id.contentmainTextView4);
		txv5 = (TextView)v.findViewById(R.id.contentmainTextView5);




		//getuser();

		initData();
		initItem();
		initToolbar();
		return v;
	}
	private void initItem()
	{
		this.cv1.setOnClickListener(this);



	}
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch (p1.getId())
		{
			case R.id.f3CardView1:
				startActivity(new Intent(getContext(), LoginqqActivity.class));
				//ToastUtils.error(getContext(), "此版本暂不支持");
				break;
		}

	}


	private void initToolbar()
	{
		setHasOptionsMenu(true);
        this.toolbar.setTitle(R.string.mydata);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(this.toolbar);
	}

	private void initData()
	{
		//MyUser userInfo = MyUser.getCurrentUser(MyUser.class);
		//是否缓存有登陆数据,缓存时间为一年

		//MyUser icon = BmobFile.getDynamicPicture(MyUser.class);

		//获取本地用户对象        
		MyUser MyUse = BmobUser.getCurrentUser(MyUser.class); 
		//判断用户是否不等于无效



		//all表示获取所有权限
		//mTencent.login(Fragment_c.this, "all", mIUiListener);
		//Toast("已安装");



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
			/*
			Glide.with(Fragment_c.this)
				.load(Url)
				.override(800, 800)
				.bitmapTransform(new Transformation[] { (Transformation)new CropCircleTransformation((Context)getActivity()) })
				.into(iv1);
				*/
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
			txv1.setText(MyUse.getname());
			txv2.setText(MyUse.getsex());
			txv3.setText(MyUse.getaddress());
			txv4.setText(MyUse.getYhzss());

			if ( MyUse.getVip()==null  ){
				txv5.setText("否");
			}else  if ( MyUse.getVip()  ){
				txv5.setText("是");
			}else {
				txv5.setText("否");
			}


			//mIUiListener = new BaseUiListener();

		}else 
		{            
			//缓存用户对象为空时， 可打开用户注册界面… 



		}
	}
	/*
	 private class BaseUiListener implements IUiListener
	 {

	 @Override
	 public void onComplete(Object response)
	 {
	 // 登陆成功后解析json数据
	 //Toast.makeText(Fragment_c.this, "授权成功", Toast.LENGTH_SHORT).show();
	 JSONObject obj = (JSONObject) response;
	 try
	 {
	 openID = obj.getString("openid");
	 accessToken = obj.getString("access_token");
	 expires = obj.getString("expires_in");
	 mTencent.setOpenId(openID);
	 mTencent.setAccessToken(accessToken, expires);
	 QQToken qqToken = mTencent.getQQToken();
	 //mUserInfo = new UserInfo(getApplicationContext(), qqToken);
	 mUserInfo.getUserInfo(new IUiListener()
	 {

	 @Override
	 public void onComplete(final Object response)
	 {
	 // TODO: Implement this method
	 try
	 {
	 JSONObject jsonObject = new JSONObject(response.toString());
	 figureurl_qq_2 = jsonObject.getString("figureurl_qq_2");
	 // 获取信息 json格式
	 Log.i("json", jsonObject.toString());

	 // 显示头像
	 Glide.with(Fragment_c.this).load(figureurl_qq_2).crossFade(800).into(iv1);
	 // 显示信息 需要更多信息自己加



	 }
	 catch (JSONException e)
	 {
	 Toast("解析错误" + e);
	 }

	 }

	 @Override
	 public void onError(UiError uiError)
	 {
	 Toast("登录失败" + uiError.toString());
	 }

	 @Override
	 public void onCancel()
	 {
	 Toast("登录取消");

	 }
	 });
	 }
	 catch (JSONException e)
	 {
	 e.printStackTrace();
	 }

	 // TODO: Implement this method
	 }

	 @Override
	 public void onError(UiError uiError)
	 {
	 Toast("授权失败");
	 }

	 @Override
	 public void onCancel()
	 {
	 Toast("授权取消");
	 }

	 private void Toast(String p0)
	 {
	 // TODO: Implement this method
	 }

	 */




}
