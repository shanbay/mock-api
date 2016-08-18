package com.shanbay.demo;

import android.content.Context;

import com.shanbay.mock.MockApiInterceptor;
import com.shanbay.mock.MockApiSuite;
import com.shanbay.mock.api.StandardMockApi;
import com.shanbay.mock.constant.MockHttpMethod;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create kang.zhou@shanbay.com on 16/8/18.
 */
public class HttpClient {

	public static String BASE_API_URL = "http://www.shanbay.com/";

	private static HttpClient sInstance;

	private Retrofit retrofit;

	public static synchronized HttpClient getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new HttpClient();

			MockApiSuite suite = new MockApiSuite("account"); // account为suite name
			suite.addMockApi(new StandardMockApi(MockHttpMethod.GET, "/api/user").setSuccessDataFile("user.json"));

			// mock 数据默认存放在assets目录中，如果需要放到sdcard上，使用new MockApiInterceptor(context, true)
			MockApiInterceptor mockApiInterceptor = new MockApiInterceptor(context);
			mockApiInterceptor.addMockApiSuite(suite);

			// define client
			OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(mockApiInterceptor);

			sInstance.retrofit = new Retrofit.Builder()
					.baseUrl(BASE_API_URL)
					.client(clientBuilder.build())
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
					.build();
		}
		return sInstance;
	}

	public Retrofit getClient() {
		return retrofit;
	}
}
