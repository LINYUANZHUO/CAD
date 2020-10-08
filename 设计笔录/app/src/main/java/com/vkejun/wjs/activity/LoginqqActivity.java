package com.vkejun.wjs.activity;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import cn.bmob.v3.*;
import cn.bmob.v3.BmobUser.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.bumptech.glide.*;
import com.tencent.connect.*;
import com.tencent.connect.auth.*;
import com.tencent.connect.common.*;
import com.tencent.tauth.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.bmob.*;
import com.vkejun.wjs.utils.*;
import org.json.*;

import android.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import com.vkejun.wjs.fragment.*;

public class LoginqqActivity extends AppCompatActivity
{
	// 这id是我的你们可以用 别干坏事就得了
	private static final String APP_ID = "1107700107";//官方获取的APPID
	private String openID,accessToken,expires,name,sex,province,city,year,figureurl_qq_2;
	private Tencent mTencent;
	private BaseUiListener mIUiListener;
	private UserInfo mUserInfo;
	private CardView cv1;
	private ImageView iv1;
	private TextView txv1,txv2,txv3;

	private BmobUser.BmobThirdUserAuth authInfo;

	private TextView vip;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginqq);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//传入参数APPID和全局Context上下文
		mTencent = Tencent.createInstance(APP_ID, LoginqqActivity.this.getApplicationContext());

		iv1 = (ImageView)findViewById(R.id.contentmainImageView1);
		txv1 = (TextView)findViewById(R.id.contentmainTextView1);
		txv2 = (TextView)findViewById(R.id.contentmainTextView2);
		txv3 = (TextView)findViewById(R.id.contentmainTextView3);
		
		
		
		

		cv1 = (CardView)findViewById(R.id.contentmainCardView1);
		// 设置图片圆角的半径大小
		cv1.setRadius(12);
		// 设置阴影部分大小
		cv1.setCardElevation(5);

		MyUser userInfo = MyUser.getCurrentUser(MyUser.class);
		//是否缓存有登陆数据,缓存时间为一年
		if (userInfo != null)
		{
			// token拉起登陆
			JSONObject jsonObject = null;
			boolean isValid = mTencent.checkSessionValid(APP_ID);
			if (!isValid)
			{
				//Toast("token过期，请重新登陆");
				return;
			}
			else
			{
				jsonObject = mTencent.loadSession(APP_ID);
				mTencent.initSessionCache(jsonObject);

				try
				{
					BmobThirdUserAuth authInfo = new BmobThirdUserAuth("qq", jsonObject.getString("access_token"), jsonObject.getString("expires_in"), jsonObject.getString("openid"));
					BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>()
						{
							@Override
							public void done(JSONObject userAuth, BmobException e)
							{
								if (e == null)
								{
									BmobQuery<MyUser> bq1 = new BmobQuery<MyUser>();
									bq1.getObject((String) BmobUser.getObjectByKey("objectId"), new QueryListener<MyUser>() 
										{
											@Override
											public void done(MyUser object, BmobException e) 
											{
												if (e == null)
												{
													Glide.with(LoginqqActivity.this).load(object.getHeadportraiturl()).transform(new GlideCircleTransform(LoginqqActivity.this)).into(iv1);
													txv1.setText(object.getname());
													txv2.setText(object.getsex());
													txv3.setText(object.getaddress());
													//Toast("Bmob登陆成功");
												}
												else
												{
													Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
												}
											}

										});

								}
							}
						});

				}
				catch (JSONException e)
				{

				}

			}
		}
		else 
		{
			// Bmob缓存用户对象为空时，要求用户重新拉起QQ登陆
		}

	}

	// 拉起QQ登陆
	public void buttonLogin(View v)
	{
		// 判断是否安装了QQ
		if (mTencent.isQQInstalled(LoginqqActivity.this) == false)
		{
			AlertDialog.Builder ad1=new AlertDialog.Builder(this);
			ad1.setTitle("提示");
			ad1.setMessage("你的手机尚未安装QQ，无法拉起登陆");
			ad1.setPositiveButton("下载QQ", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						// TODO: Implement this method
						Uri uri = Uri.parse("https://im.qq.com/mobileqq/touch/android/");
						Intent intent=new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					}
				});
			ad1.setNegativeButton("取消", null).show();
		}
		else 
		{
			mIUiListener = new BaseUiListener();
			//all表示获取所有权限
			mTencent.login(LoginqqActivity.this, "all", mIUiListener);
			//Toast("已安装");
		}

	}

	private class BaseUiListener implements IUiListener
	{
		@Override
		public void onComplete(Object response)
		{
			// 登陆成功后解析json数据
			Toast.makeText(LoginqqActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
			JSONObject obj = (JSONObject) response;
			try
			{
				openID = obj.getString("openid");
				accessToken = obj.getString("access_token");
				expires = obj.getString("expires_in");
				mTencent.setOpenId(openID);
				mTencent.setAccessToken(accessToken, expires);
				QQToken qqToken = mTencent.getQQToken();
				mUserInfo = new UserInfo(getApplicationContext(), qqToken);
				mUserInfo.getUserInfo(new IUiListener()
					{
						@Override
						public void onComplete(final Object response)
						{
							try
							{

								JSONObject jsonObject = new JSONObject(response.toString());
								// Q名
								name = jsonObject.getString("nickname");
								// 性别
								sex = jsonObject.getString("gender");
								// 所在省
								province = jsonObject.getString("province");
								// 所在市
								city = jsonObject.getString("city");
								// 年龄 (返回的只是出生年，故以今年减去出生年得出年龄)
								year = jsonObject.getString("year");
								int i = Integer.parseInt(year);
								i = 2018 - i;

								// 经测试这貌似是空间头像头像 30x30
								// String figureurl=jsonObject.getString("figureurl");
								// 经测试这貌似是空间头像头像 100x100
								// String figureurl_2=jsonObject.getString("figureurl_2");

								// 这才是QQ头像 100x100
								figureurl_qq_2 = jsonObject.getString("figureurl_qq_2");
								// 获取信息 json格式
								Log.i("json", jsonObject.toString());

								// 显示头像
								Glide.with(LoginqqActivity.this).load(figureurl_qq_2).crossFade(800).transform(new GlideCircleTransform(LoginqqActivity.this)).into(iv1);
								// 显示信息 需要更多信息自己加
								txv1.setText("名称：" + name);
								txv2.setText("性别：" + sex);
								txv3.setText("地址：" + province + "省" + city + "市");

							}
							catch (JSONException e)
							{
								Toast("解析错误" + e);
							}

							// Bmob一键注册
							BmobThirdUserAuth authInfo = new BmobThirdUserAuth("qq", accessToken, expires, openID);
							BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>()
								{
									@Override
									public void done(JSONObject userAuth, BmobException e)
									{
										if (e == null)
										{
											MyUser bu1 = new MyUser();
											bu1.setHeadportraiturl(figureurl_qq_2);
											bu1.setname(name);
											bu1.setsex(sex);
											bu1.setaddress(province + "省" + city + "市");
											
											bu1.setVip(false);
											
											bu1.setYhzss("普通用户");
											
											bu1.setAlldata(response.toString());
											bu1.update((String) BmobUser.getObjectByKey("objectId"), new UpdateListener()
												{
													@Override
													public void done(BmobException e)
													{
														if (e == null)
														{
															//startActivity(new Intent(this, Fragment_c.class));//转跳到注册界面
															startActivity(new Intent(LoginqqActivity.this, HomeActivity.class));
															/*
															Intent intent = new Intent();
															intent.setClass(LoginqqActivity.this, Fragment_c.class);
															startActivity(intent);
															LoginqqActivity.this.finish();
															*/
															//结束当前activity
															finish();
															Toast("登陆注册成功");
														}
														else
														{
															startActivity(new Intent(LoginqqActivity.this, HomeActivity.class));
															
															Toast("登陆注册失败" + e.getMessage() + "," + e.getErrorCode());
														}
													}
												});
										}
									}
								});
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


	}

	// 返回登陆数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Constants.REQUEST_LOGIN)
		{
			Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	// 退出登陆
	public void exit(View view)
	{
		if (mTencent.isSessionValid())
		{
			// 退出QQ登陆
			mTencent.logout(this);
			// 退出Bmob并清空缓存，清除缓存用户对象
			BmobUser.logOut();
			BmobUser currentUser = BmobUser.getCurrentUser();
			if (currentUser == null)
			{
				// 修改UI
				iv1.setImageResource(R.drawable.ic_launcher);
				txv1.setText("Text");
				txv2.setText("Text");
				txv3.setText("Text");
				Toast("退出成功");
			}
        }
	}



	// Toast
	public void Toast(String s)
	{
		Toast.makeText(LoginqqActivity.this, s, Toast.LENGTH_SHORT).show();
	}


}
