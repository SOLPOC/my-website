package ind.xyz.mywebsite.util;

import com.google.gson.Gson;
import ind.xyz.mywebsite.dto.Result;

import java.lang.reflect.Type;

public class JsonUtil<T> {
    public static String toJson(Object object){
        Gson gson=new Gson();
        return gson.toJson(object);
    }

    public static String toJson(Object object,Gson gson){
        return gson.toJson(object);
    }

    public static <T> T fromJson(String s,Class<T> t){
        Gson gson=new Gson();
        return gson.fromJson(s,t);
    }

    public static <T> T fromJson(String s, Type t){
        Gson gson=new Gson();
        return gson.fromJson(s,t);
    }

    public static Result parseResult(String s){
        Gson gson=new Gson();
        return gson.fromJson(s,Result.class);
    }


}

