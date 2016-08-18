package com.shanbay.demo.api;

import android.content.Context;

import com.shanbay.demo.HttpClient;
import com.shanbay.demo.User;

import rx.Observable;

/**
 * Create kang.zhou@shanbay.com on 16/8/18.
 */
public class UserApiService {

	private static UserApiService instance;

	private UserApi api;

	public static synchronized UserApiService getInstance(Context context) {
		if (instance == null) {
			instance = new UserApiService(HttpClient.getInstance(context).getClient().create(UserApi.class));
		}
		return instance;
	}

	public UserApiService(UserApi api) {
		this.api = api;
	}

	public Observable<User> fetchUser() {
		return api.fetchUser();
	}
}
