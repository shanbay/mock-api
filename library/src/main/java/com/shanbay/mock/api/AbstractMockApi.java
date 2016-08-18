package com.shanbay.mock.api;

import com.shanbay.mock.constant.MockHttpMethod;

import java.util.regex.Pattern;

public abstract class AbstractMockApi implements IMockApi {

	// mock url（支持正则）
	private String regExUrl;
	private Pattern urlPattern;

	// mock http method
	private String method;

	// mock api 请求时间
	private long requestTime = -1L;

	public AbstractMockApi(MockHttpMethod method, String url) {
		this.regExUrl = url;
		this.method = method.name();
		this.urlPattern = Pattern.compile(url);
	}

	@Override
	public boolean isMock(String method, String url) {
		return this.method.equalsIgnoreCase(method) && urlPattern.matcher(url).find();
	}

	public long getRequestTime() {
		return requestTime;
	}

	public AbstractMockApi setRequestTime(long requestTime) {
		this.requestTime = requestTime;
		return this;
	}
}
