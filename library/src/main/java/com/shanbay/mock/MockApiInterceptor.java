package com.shanbay.mock;

import android.content.Context;
import android.os.Environment;

import com.shanbay.mock.api.AbstractMockApi;
import com.shanbay.mock.api.PageMockApi;
import com.shanbay.mock.api.StandardMockApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockApiInterceptor implements Interceptor {

	private Context appContext;
	private boolean supportSDCard;
	private List<MockApiSuite> mockApiSuiteList = new ArrayList<>();

	public MockApiInterceptor(Context appContext) {
		this(appContext, false);
	}

	public MockApiInterceptor(Context appContext, boolean supportSDCard) {
		this.appContext = appContext;
		this.supportSDCard = supportSDCard;
	}

	public MockApiInterceptor addMockApiSuite(MockApiSuite suite) {
		mockApiSuiteList.add(suite);
		return this;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		HttpUrl httpUrl = request.url();
		String path = httpUrl.url().getPath();

		for (MockApiSuite suite : mockApiSuiteList) {
			List<AbstractMockApi> mockApiList = suite.getMockApiList();
			for (AbstractMockApi api : mockApiList) {
				if (api.isMock(request.method(), path)) {

					// 模拟请求消耗时间
					sleep(api.getRequestTime());

					// 获取结果数据文件
					String dataFile;
					if (api instanceof PageMockApi) {
						int page = queryPage(httpUrl.queryParameter("page"));
						dataFile = ((PageMockApi) api).getDataFile(page);

					} else {
						dataFile = ((StandardMockApi) api).getDataFile();
					}

					// 返回模拟数据
					String jsonData = readMockData(suite.getSuiteName(), dataFile);
					return new Response.Builder()
							.code(200)
							.message(jsonData)
							.request(chain.request())
							.protocol(Protocol.HTTP_1_0)
							.body(ResponseBody.create(MediaType.parse("application/json"), jsonData.getBytes("UTF-8")))
							.addHeader("content-type", "application/json")
							.build();
				}
			}
		}

		return chain.proceed(request);
	}

	private String readMockData(String suiteName, String dataFileName) throws IOException {

		StringBuilder sbJson = new StringBuilder();
		// 约定 mock 数据存放要求！！！
		String relativePath = "mockdata/" + suiteName + "/" + dataFileName;

		InputStream inputStream = openMockFile(relativePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sbJson.append(line);
			}
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sbJson.toString();
	}

	private InputStream openMockFile(String relativePath) throws IOException {
		InputStream inputStream;
		if (supportSDCard) {
			File file = new File(Environment.getExternalStorageDirectory(), relativePath);
			inputStream = new FileInputStream(file);
		} else {
			inputStream = appContext.getAssets().open(relativePath);
		}

		return inputStream;
	}

	private void sleep(long time) {
		try {
			if (time > 0) {
				TimeUnit.MILLISECONDS.sleep(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int queryPage(String page) {
		return (page == null || page.trim().length() <= 0) ? 1 : Integer.valueOf(page);
	}
}
