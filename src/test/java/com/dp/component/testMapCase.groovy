package com.dp.component

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.util.Map.Entry;

/**
 * @program: Dynamic_Conversion
 *
 * @description: ${description}
 *
 * @author: yhongl
 *
 * @create: 2018-05-02 21:01
 * */
//http://www.yq1012.com/jichu/4136.html



public static String jsonToXml(String json){
    try {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        JSONObject jObj = JSON.parseObject(json);
        jsonToXmlstr(jObj,buffer);
        return buffer.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "";
    }
}



public static String jsonToXmlstr(JSONObject jObj,StringBuffer buffer ){
    Set<Entry<String, Object>>  se = jObj.entrySet();
    for( Iterator<Entry<String, Object>>   it = se.iterator();  it.hasNext(); )
    {
        Entry<String, Object> en = it.next();
        if(en.getValue().getClass().getName().equals("com.alibaba.fastjson.JSONObject")){
            buffer.append("<"+en.getKey()+">");
            JSONObject jo = jObj.getJSONObject(en.getKey());
            jsonToXmlstr(jo,buffer);
            buffer.append("</"+en.getKey()+">");
        }else if(en.getValue().getClass().getName().equals("com.alibaba.fastjson.JSONArray")){
            JSONArray jarray = jObj.getJSONArray(en.getKey());
            for (int i = 0; i < jarray.size(); i++) {
                buffer.append("<"+en.getKey()+">");
                JSONObject jsonobject =  jarray.getJSONObject(i);
                jsonToXmlstr(jsonobject,buffer);
                buffer.append("</"+en.getKey()+">");
            }
        }else if(en.getValue().getClass().getName().equals("java.lang.String")){
            buffer.append("<"+en.getKey()+">"+en.getValue());
            buffer.append("</"+en.getKey()+">");
        }

    }
    return buffer.toString();
}



def mapInfo =[:]

mapInfo << [("name"): (new Date())]
mapInfo << [("gender"):("1")]



def mapChildInfo = [:]
mapChildInfo <<[("id"):"3455"]
mapChildInfo <<[("info"):"3455"]
mapChildInfo <<[("ids"):"3455"]
mapChildInfo <<[("ide"):"3455"]
mapChildInfo <<[("idee"):"3455"]
mapInfo << [("item"):(mapChildInfo)]

print(jsonToXml(JSON.toJSONString(mapInfo)))