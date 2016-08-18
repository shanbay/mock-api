package com.shanbay.mock.api;

import com.shanbay.mock.constant.MockHttpMethod;
import com.shanbay.mock.constant.PageMockState;

import java.util.ArrayList;
import java.util.List;

public class PageMockApi extends AbstractMockApi {

	// 默认 mock 成功状态
	private PageMockState state = PageMockState.SUCCESS;

	// mock api 请求成功返回的分页数据
	private List<String> dataFileList = new ArrayList<>();

	// mock api 请求成功返回的空数据
	private String emptyDataFile;

	// mock api 请求失败返回的数据
	private String errorDataFile;

	public PageMockApi(String url) {
		super(MockHttpMethod.GET, url);
	}

	public String getDataFile(int page) {
		if (state == PageMockState.SUCCESS) {
			return dataFileList.get(page - 1);
		} else if (state == PageMockState.EMPTY) {
			return emptyDataFile;
		} else {
			return errorDataFile;
		}
	}

	public void addDataFile(String dataFile) {
		dataFileList.add(dataFile);
	}

	public void setEmptyDataFile(String emptyDataFile) {
		this.emptyDataFile = emptyDataFile;
	}

	public void setErrorDataFile(String errorDataFile) {
		this.errorDataFile = errorDataFile;
	}
}
