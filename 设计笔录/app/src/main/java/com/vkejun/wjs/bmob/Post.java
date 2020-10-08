package com.vkejun.wjs.bmob;

import cn.bmob.v3.*;
import cn.bmob.v3.datatype.*;

public class Post extends BmobObject
{

	//贴子标题
	private String title;
	//贴子内容
	private String Message;
	//帖子作者名
	private MyUser authors;
	//图片
	private BmobFile image;//图片
	//贴子板块
	private String bk;
	//审核贴纸
	private Boolean sh;
	//版主认证
	private Boolean rz;
	//首页帖子
	private String home;
	//收藏帖子
	private BmobRelation likes;

	public void setLikes(BmobRelation likes)
	{
		this.likes = likes;
	}

	public BmobRelation getLikes()
	{
		return likes;
	}

	public void setHome(String home)
	{
		this.home = home;
	}

	public String getHome()
	{
		return home;
	}

	public void setAuthors(MyUser authors)
	{
		this.authors = authors;
	}

	public MyUser getAuthors()
	{
		return authors;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setMessage(String message)
	{
		Message = message;
	}

	public String getMessage()
	{
		return Message;
	}

	public void setBk(String bk)
	{
		this.bk = bk;
	}

	public String getBk()
	{
		return bk;
	}

	public void setSh(Boolean sh)
	{
		this.sh = sh;
	}

	public Boolean getSh()
	{
		return sh;
	}

	public void setRz(Boolean rz)
	{
		this.rz = rz;
	}

	public Boolean getRz()
	{
		return rz;
	}
    public BmobFile getImage() {
        return image;
    }

    public Post setImage(BmobFile image) {
        this.image = image;
        return this;
    }
}

    
