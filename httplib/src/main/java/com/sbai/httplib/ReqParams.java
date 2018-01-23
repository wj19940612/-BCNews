package com.sbai.httplib;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Modified by john on 18/01/2018
 * <p>
 * Description: 请求参数，支持链式 put & 对象解析
 *
 */
public class ReqParams {

    private HashMap<String, String> mParams;

    public ReqParams() {
    }

    public ReqParams(Class<?> clazz, Object object) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public ReqParams put(String key, Object value) {
        if (mParams == null) {
            mParams = new HashMap<>();
        }

        if (value != null) {
            mParams.put(key, value.toString());
        }

        return this;
    }

    public HashMap<String, String> get() {
        return mParams;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (mParams != null && !mParams.isEmpty()) {
            builder.append("?");
            for (Object key : mParams.keySet()) {
                builder.append(key).append("=").append(mParams.get(key)).append("&");
            }
            if (builder.toString().endsWith("&")) {
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        return builder.toString();
    }
}
