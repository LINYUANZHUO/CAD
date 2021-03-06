package com.vkejun.wjs.fragment;

import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import android.widget.AbsListView.*;
import cn.bmob.v3.*;
import cn.bmob.v3.exception.*;
import cn.bmob.v3.listener.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.bmob.*;
import com.vkejun.wjs.editor.*;
import java.util.*;

import android.support.v7.widget.Toolbar;
import com.vkejun.wjs.editor.*;
import com.vkejun.wjs.editor.as.*;

public class Fragment_b extends Fragment
{
	private ListView list;
	private SwipeRefreshLayout sw;
	List<Post> mPostlist = new ArrayList<Post>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_2, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		//进入时加载数据
		getdata();
		list = (ListView) getActivity().findViewById(R.id.listpost);
		sw = (SwipeRefreshLayout) getActivity().findViewById(R.id.mainSwipeRefreshLayout1);
		sw.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
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

		FloatingActionButton fab=(FloatingActionButton) getActivity().findViewById(R.id.fragment_2_FAB);
		fab.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(Fragment_b.this.getActivity(),EditorCodeActivity.class);
					startActivity(intent);
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.activity_listss, null);

				viewHolder.tubiao =convertView.findViewById(R.id.listfenxiangTextView1);
				viewHolder.nameandtime = (TextView) convertView.findViewById(R.id.listfenxiangTextView2);
				viewHolder.biaoti = (TextView) convertView.findViewById(R.id.photoname);
				viewHolder.neirong = (TextView) convertView.findViewById(R.id.itempostTextView1);
				viewHolder.l = (LinearLayout) convertView.findViewById(R.id.list_fenxiangLinearLayout);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder)convertView.getTag();
			}

			fenxiang = mPostlist.get(position);

			viewHolder.tubiao.setText("这是标题");
			viewHolder.nameandtime.setText("作者："+fenxiang.getAuthors()+"   时间："+fenxiang.getCreatedAt());
			viewHolder.biaoti.setText(fenxiang.getTitle());
			viewHolder.neirong.setText(fenxiang.getMessage());
			// TextView设置文本
			viewHolder.l.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view)
					{
						String biaoti=mPostlist.get(position).getTitle(); //这里就获取到了内容。操作在下面写也行
						String neirong=mPostlist.get(position).getMessage();
						intent = new Intent(Fragment_b.this.getActivity(), Jieshouqi.class);
						intent.putExtra("biaoti", biaoti);
						intent.putExtra("neirong", neirong);
						startActivity(intent);
					}
				});
			return convertView;
		}
		public class ViewHolder
		{
			public TextView tubiao,nameandtime,biaoti,neirong;
			public LinearLayout l;
		}
	}

	public void getdata()
	{
		//Bmob.initialize(this.getActivity(),"99a26b3b9e330afec9b6");
		BmobQuery<Post> query=new BmobQuery<Post>();
		query.order("-createdAt");//依照数据排序时间排序
		query.setLimit(500);
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
