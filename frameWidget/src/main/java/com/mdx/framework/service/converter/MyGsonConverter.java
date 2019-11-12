package com.mdx.framework.service.converter;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mdx.framework.service.entity.HttpErrorResponseEntity;
import com.mdx.framework.service.entity.HttpResultEntity;
import com.mdx.framework.service.exception.HttpResultException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import timber.log.Timber;

public class MyGsonConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    public MyGsonConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String responseStr = responseBody.string();
        Timber.d(responseStr);
        if (TextUtils.isEmpty(responseStr)) {
            HttpErrorResponseEntity errorResponse = new HttpErrorResponseEntity(-200, "The result of the request is returned as null");
            HttpResultException resultException = new HttpResultException(errorResponse.getErrorCode(), errorResponse.getErrorMsg());
            Log.e("tag","this ResultException msg is request result is null");
            Log.d("tag", "The responseStr is  " + responseStr);
            throw resultException;
        }

        HttpResultEntity httpResult;
        try {
            httpResult = gson.fromJson(responseStr, HttpResultEntity.class);
        } catch (JsonSyntaxException e) {
            HttpResultException resultException = new HttpResultException(-201, "Json conversion exception");
            Log.e("tag","this JsonSyntaxException msg is"+e.getLocalizedMessage());
            Log.d("tag", "The responseStr is  " + responseStr);
            throw resultException;
        }

        if (httpResult.getErrorCode() == 0) {
            try {
                return gson.fromJson(responseStr, type);
            } catch (JsonSyntaxException e) {
                Log.e("tag","this JsonSyntaxException msg is "+e.getMessage());
                Log.d("tag", "The responseStr is  " + responseStr);
                HttpErrorResponseEntity errorResponse = new HttpErrorResponseEntity(-202, "Json conversion exception");
                throw new HttpResultException(errorResponse.getErrorCode(), errorResponse.getErrorMsg());
            }
        } else {
            try {
                Log.e("tag","this ResultException msg is json convert fail" );
                Log.d("tag", "The responseStr is  " + responseStr);
                HttpErrorResponseEntity errorResponse = gson.fromJson(responseStr, HttpErrorResponseEntity.class);
                throw new HttpResultException(errorResponse.getErrorCode(), errorResponse.getErrorMsg());
            } catch (JsonSyntaxException e) {
                Log.e("tag","this JsonSyntaxException msg is "+e.getMessage() );
                Log.d("tag", "The responseStr is  " + responseStr);
                HttpErrorResponseEntity errorResponse = new HttpErrorResponseEntity(-203, "Json conversion exception");
                throw new HttpResultException(errorResponse.getErrorCode(), errorResponse.getErrorMsg());
            }
        }
    }
}
