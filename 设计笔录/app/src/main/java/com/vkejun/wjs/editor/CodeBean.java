package com.vkejun.wjs.editor;

import android.content.*;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.io.*;
import org.dom4j.*;
import org.dom4j.io.*;
import android.util.*;

public class CodeBean
{
	public CodeBean(File File) {
		try
		{
			color = new CodeColor(File);
		}
		catch (Exception e)
		{
			Log.e("exception",e.toString());
		}
	}
	public static CodeBean Bean;
	public String BackGround;
	public String PoinBackRound;
	public String Drawable;
	public static CodeColor color;
	public static CodeBean getCodeBean(Context con) throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		Gson gson=new Gson();
		CodeBean bean=gson.fromJson(new FileReader(new File(con.getFilesDir().getAbsolutePath()+"CodeBean")),CodeBean.class);
		return bean;
	}
	public static void Save(Context con,CodeBean code) throws IOException{
		Gson gson=new Gson();
		String Bean=jsonFormatter( gson.toJson(code));
		FileUtils.writeStringToFile(new File(con.getExternalCacheDir().getAbsolutePath()+"/CodeBean"),Bean);
	}

	public static String jsonFormatter(String uglyJSONString){
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(uglyJSONString);
		String prettyJsonString = gson.toJson(je);
		return prettyJsonString;
	}
	public class CodeColor{
		public Map<Pattern,String>map;
		public CodeColor(File File) throws DocumentException,CodeColorException{
			if(!File.exists()){
				throw new CodeColorException("Color is null!!!!!");
			}
			StringBuffer buf;
			map=new HashMap<Pattern,String>();
			Document doc=new SAXReader().read(File);
			Element ele=doc.getRootElement();
			List list= ele.elements("KeyWord");
			for (Object it:list) {   
				Element elm = (Element) it;
				buf=new StringBuffer();
				List value=elm.elements("values");
				for(Object obj:value){
					Element men=(Element) obj;
					buf.append(men.getText()+"|");
				}
				Attribute atr=elm.attribute("color");
				System.out.println(atr.getValue());
				map.put(Pattern.compile(buf.toString()),atr.getValue());
			}

		}
	}
	class CodeColorException extends Exception{
		public CodeColorException(String error){
			super(error);
		}
	}
}

