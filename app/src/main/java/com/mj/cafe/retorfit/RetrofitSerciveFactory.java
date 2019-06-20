package com.mj.cafe.retorfit;


import com.mj.cafe.BizcContant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2017/11/1.
 */

public class RetrofitSerciveFactory {
    private static ConcurrentMap<Class, Object> mService = new ConcurrentHashMap();

    /**
     * 提供ComService服务
     * @return
     */
    public static ComService provideComService(){
        return provideService(BizcContant.API,ComService.class);
    }

    /**
     *
     * @param baseUrl
     * @param clz
     * @param <T>
     * @return
     */
    private static  <T>T provideService(String baseUrl, Class clz){
        Object object = mService.get(clz);
        if(object == null){
            object = mService.get(clz);
            if(object==null){
                if(baseUrl.equals(BizcContant.API)){
                    object = RetrofitComClient.getInstance(baseUrl).createService(ComService.class);
                }
            }
            mService.put(clz,object);
        }
        return (T)object;
    }
}
