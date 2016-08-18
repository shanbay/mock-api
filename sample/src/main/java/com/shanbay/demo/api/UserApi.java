package com.shanbay.demo.api;

import com.shanbay.demo.User;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Create kang.zhou@shanbay.com on 16/8/18.
 */
public interface UserApi {

	@GET("/api/user")
	Observable<User> fetchUser();

}
