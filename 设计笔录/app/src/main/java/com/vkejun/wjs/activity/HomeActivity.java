package com.vkejun.wjs.activity;

import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.view.*;
import com.ashokvarma.bottomnavigation.*;
import com.vkejun.wjs.*;
import com.vkejun.wjs.fragment.*;
import com.vkejun.wjs.utils.*;

import com.vkejun.wjs.R;

public class HomeActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener
{
    private BottomNavigationBar bottomNavigationBar;
	
	private long firstTime;
    private Fragment_a f1q;
    private Fragment_b f2q;
    private Fragment_c f3q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initBottomNavBar();
        initTab();
        bottomNavigationBar.setTabSelectedListener(this);
		
		StatusBarUtil.setStatusBarTransparent(this);
		
		//调用设置//栏颜色
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimaryDark));
        //图片置顶
        //StatusBarUtils.setTransparent(this);
		
    }
    private void initBottomNavBar(){
        /*1.首先进行fvb*/
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_nav_bar);
        /*2.进行必要的设置*/
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);//适应大小
        bottomNavigationBar.setBarBackgroundColor(R.color.bj)//背景色
			.setInActiveColor(R.color.wx)//未选中
			.setActiveColor(R.color.xz);//已选中
        /*3.添加Tab*/
        bottomNavigationBar
            .addItem(new BottomNavigationItem(R.drawable.ic_account_circle,R.string.f1))
            .addItem(new BottomNavigationItem( R.drawable.ic_adb,R.string.f2))
            .addItem(new BottomNavigationItem( R.drawable.ic_assignment,R.string.f3))
            .setFirstSelectedPosition(0)//默认显示面板
            .initialise();
    }
    private void initTab(){
        f1q = new Fragment_a();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content,f1q);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position){
            case 0:
                if(f1q== null){
                    f1q= new Fragment_a();
                }
                transaction.replace(R.id.frame_content,f1q);
                break;
            case 1:
                if(f2q == null){
                    f2q = new Fragment_b();
                }
                transaction.replace(R.id.frame_content,f2q);
                break;
            case 2:
                if(f3q == null){
                    f3q = new Fragment_c();
                }
                transaction.replace(R.id.frame_content,f3q);
                break;
        }
        transaction.commit();
    }
	
	
	
	
	
    @Override
    public void onTabUnselected(int position) {}
    @Override
    public void onTabReselected(int position) {}
	
	
	
	
	//双击退出实现
	@Override 
    public boolean onKeyUp(int keyCode, KeyEvent event)
	{ 
        if (keyCode == KeyEvent.KEYCODE_BACK)
		{ 
            long secondTime = System.currentTimeMillis(); 
            if (secondTime - firstTime > 2000)
			{//如果两次按键时间间隔大于800毫秒，则不退出 
				ToastUtils.warning(getApplicationContext(), "再按一次退出程序");
                firstTime = secondTime;//更新firstTime 
                return true; 
            }
			else
			{ 
                System.exit(0);//否则退出程序 
            } 
        } 
        return super.onKeyUp(keyCode, event); 
	}
	
}
