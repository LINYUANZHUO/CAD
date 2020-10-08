package com.vkejun.wjs.fragment;

import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.activity.*;
import com.vkejun.wjs.bmob.*;
import java.util.*;
import java.util.concurrent.*;

public class Fragment_a extends Fragment
{
	private ListView list;
	private SwipeRefreshLayout sw;
	List<Post> mPostlist = new ArrayList<Post>();

	private FloatingActionButton fab;

	private ImageView iv1;
	@Override
	public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = paramLayoutInflater.inflate(R.layout.fragment_1, paramViewGroup, false);

		//进入时加载数据
		getdata();
		this.list = (ListView) view.findViewById(R.id.listpost);
		sw = (SwipeRefreshLayout) view.findViewById(R.id.mainSwipeRefreshLayout1);
		sw.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light);

		this.fab = (FloatingActionButton) view.findViewById(R.id.FloatingActionButton1);
		this.iv1 = (ImageView) view.findViewById(R.id.activitylistImageView1);

		sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
			{
				@Override
				public void onRefresh()
				{
					//text.setText("正在刷新");
					getdata();
					new Handler().postDelayed(new Runnable()
						{
							@Override
							public void run()
							{
								//text.setText("刷新完成");
								sw.setRefreshing(false);
								//Toast.makeText(getActivity(),"刷新完成", Toast.LENGTH_SHORT).show();
							}
						}, 3000);
				}
			});

		list.setOnScrollListener(new OnScrollListener()
			{
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState)
				{
				}
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
				{
					boolean enable = false;
					if (list != null && list.getChildCount() > 0)
					{
						// 检查listView第一个item是否可见
						boolean firstItemVisible = list.getFirstVisiblePosition() == 0;
						// 检查第一个item的顶部是否可见
						boolean topOfFirstItemVisible = list.getChildAt(0).getTop() == 0;
						// 启用或者禁用SwipeRefreshLayout刷新标识
						enable = firstItemVisible && topOfFirstItemVisible;
					}
					else if (list != null && list.getChildCount() == 0)
					{
						// 没有数据的时候允许刷新
						enable = true;
					}
					// 把标识传给swipeRefreshLayout
					sw.setEnabled(enable);

				}
			});
		clickEvents();
		return view;
	}


	public void clickEvents() {
        fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent(getActivity(), SettingActivity.class);
					startActivity(it);
				}
			});
	}

	class ItemListAdapter extends BaseAdapter
	{
		private Intent intent;
		//private ViewHolder viewHolder;
		private Post fenxiang;
		//适配器
		@Override
		public int getCount()
		{
			if (mPostlist.size() > 0)
			{
				return mPostlist.size();
			}
			return 0;
		}
		@Override
		public Object getItem(int position)
		{
			return mPostlist.get(position);
		}
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		@Override 
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(R.layout.activity_list, null);

				viewHolder.yonghuming =convertView.findViewById(R.id.activitylistTextView1);//昵称
				viewHolder.shijian = (TextView) convertView.findViewById(R.id.activitylistTextView2);//时间
				viewHolder.yonghuzu = (TextView) convertView.findViewById(R.id.activitylistTextView3);//用户组
				//viewHolder.shijian = (TextView) convertView.findViewById(R.id.activitylistTextView4);//
				//viewHolder.shijian = (TextView) convertView.findViewById(R.id.activitylistTextView5);//
				viewHolder.biaoti = (TextView) convertView.findViewById(R.id.activitylistTextView6);//标题
				viewHolder.neirong = (TextView) convertView.findViewById(R.id.activitylistTextView7);//内容
				viewHolder.l = (LinearLayout) convertView.findViewById(R.id.list_fenxiangLinearLayout);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}

			fenxiang = mPostlist.get(position);
			/*
			 MyUser MyUse = BmobUser.getCurrentUser(MyUser.class); 
			 String Url = MyUse.getHeadportraiturl();
			 //viewHolder.tubiao.setText("这是标题");
			 //viewHolder.nameandtime.setText("作者："+fenxiang.getAuthors()+"   时间："+fenxiang.getCreatedAt());
			 //头像
			 /*
			 Glide.with(Fragment_a.this)
			 .load(Url)
			 .bitmapTransform(new Transformation[] { (Transformation)new CropCircleTransformation((Context)getActivity()) })
			 .into(iv1);
			 */
			//viewHolder.yonghuming.setText(MyUse.getname());//作者
			//viewHolder.yonghuzu.setText(MyUse.getYhzss());//用户组
			viewHolder.yonghuming.setText("作者："+fenxiang.getAuthors());

			viewHolder.shijian.setText(fenxiang.getCreatedAt());//时间

			viewHolder.biaoti.setText(fenxiang.getTitle());//标题
			viewHolder.neirong.setText(fenxiang.getMessage());//内容
			// TextView设置文本
			viewHolder.l.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view)
					{
						String biaoti=mPostlist.get(position).getTitle(); //这里就获取到了内容。操作在下面写也行
						String neirong=mPostlist.get(position).getMessage();
						String id=mPostlist.get(position).getObjectId();
						intent = new Intent(Fragment_a.this.getActivity(), EditCodeActivity.class);
						intent.putExtra("biaoti", biaoti);
						intent.putExtra("neirong", neirong);
						intent.putExtra("id", id);
						startActivity(intent);
					}
				});
			return convertView;
		}
		public class ViewHolder
		{
			public TextView yonghuming,yonghuzu,shijian,biaoti,neirong;
			public LinearLayout l;
		}
	}

	public void getdata()
	{
		//Bmob.initialize(this.getActivity(),"99a26b3b9e330afec9b6");
		BmobQuery<Post> query=new BmobQuery<Post>();
		//query.addWhereEqualTo("home", "true");//置顶🔝
		query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));//此表示缓存一天
		query.order("-createdAt");//依照数据排序时间排序
		//返回500条maps，如果不加上这条语句，默认返回10条maps
		query.setLimit(500);
		query.include("authors");//可能会报错
		query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
		query.findObjects(new FindListener<Post>()
			{

				@Override
				public void done(List<Post> p1, BmobException p2)
				{
					if(p2 == null)
					{
						mPostlist.clear();
						for (int i = 0; i < p1.size(); i++)
						{
							mPostlist.add(p1.get(i));
						}
						list.setAdapter(new ItemListAdapter());
						sw.setRefreshing(false);
						//此时列表加载完成
					}
					else
					{
						sw.setRefreshing(false);
						Toast.makeText(getActivity(),"数据加载失败" + "错误代号：" +p1 + "错误问题：" +p2, Toast.LENGTH_SHORT).show();
					}
				}
			});
	}
}
