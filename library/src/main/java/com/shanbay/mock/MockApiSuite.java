package com.shanbay.mock;

import com.shanbay.mock.api.AbstractMockApi;

import java.util.ArrayList;
import java.util.List;

public class MockApiSuite {

	private String name;
	private List<AbstractMockApi> mockApiList = new ArrayList<>();

	public MockApiSuite(String name) {
		if (name == null || name.trim().length() <= 0) {
			throw new IllegalArgumentException("suite name can't be blank!");
		}
		this.name = name;
	}

	public MockApiSuite addMockApi(AbstractMockApi mockApi) {
		this.mockApiList.add(mockApi);
		return this;
	}

	public String getSuiteName() {
		return this.name;
	}

	public List<AbstractMockApi> getMockApiList() {
		return this.mockApiList;
	}
}
