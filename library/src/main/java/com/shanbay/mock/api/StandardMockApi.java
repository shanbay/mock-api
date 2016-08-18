package com.shanbay.mock.api;


import com.shanbay.mock.constant.MockHttpMethod;
import com.shanbay.mock.constant.MockState;

public class StandardMockApi extends AbstractMockApi {

	// mock api 请求成功返回的数据
	private String successDataFile;

	// mock api 请求失败返回的数据
	private String errorDataFile;

	// 默认 mock 成功状态
	private MockState state = MockState.SUCCESS;

	public StandardMockApi(MockHttpMethod method, String url) {
		super(method, url);
	}

	public String getDataFile() {
		if (state == MockState.SUCCESS) {
			return successDataFile;
		} else {
			return errorDataFile;
		}
	}

	public StandardMockApi setState(MockState state) {
		this.state = state;
		return this;
	}

	public StandardMockApi setErrorDataFile(String errorDataFile) {
		this.errorDataFile = errorDataFile;
		return this;
	}

	public StandardMockApi setSuccessDataFile(String successDataFile) {
		this.successDataFile = successDataFile;
		return this;
	}
}
