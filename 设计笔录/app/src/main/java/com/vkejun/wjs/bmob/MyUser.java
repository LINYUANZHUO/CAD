package com.vkejun.wjs.bmob;

import cn.bmob.v3.*;

public class MyUser extends BmobUser
{
	private String headportraiturl, name,sex,provinceandcity,alldata;

	private String id;
	
	private String yhzss;
	
	private Boolean vip;
	
	private boolean loginQQ;

	public boolean isloginQQ() {
        return this.loginQQ;
    }

    public void setloginQQ(boolean paramBoolean) {
        this.loginQQ = paramBoolean;
    }

	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public void setVip(Boolean vip)
	{
		this.vip = vip;
	}

	public Boolean getVip()
	{
		return vip;
	}

	public void setYhzss(String yhzss)
	{
		this.yhzss = yhzss;
	}

	public String getYhzss()
	{
		return yhzss;
	}


	// 头像链接
	public String getHeadportraiturl()
	{
		return headportraiturl;
	}

	public void setHeadportraiturl(String s)
	{
		this.headportraiturl = s;
	}

	// Q名作用户名
	public String getname()
	{
		return name;
	}


	public void setname(String s)
	{
		this.name = s;
	}

	// 性别
	public String getsex()
	{
		return sex;
	}

	public void setsex(String s)
	{
		this.sex = s;
	}

	// 住址
	public String getaddress()
	{
		return provinceandcity;
	}

	public void setaddress(String s)
	{
		this.provinceandcity = s;
	}

	// QQ所有jaon数据
	public String getAlldata()
	{
		return alldata;
	}

	public void setAlldata(String s)
	{
		this.alldata = s;
	}

}
