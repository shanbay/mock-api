package com.shanbay.demo;

import android.os.Bundle;
import android.widget.TextView;

import com.shanbay.demo.api.UserApiService;
import com.trello.rxlifecycle.components.RxActivity;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends RxActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final TextView tvData = (TextView) findViewById(R.id.data);

		UserApiService.getInstance(this).fetchUser()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.compose(this.<User>bindToLifecycle())
				.subscribe(new Action1<User>() {
					@Override
					public void call(User user) {
						tvData.setText(user.username);
					}
				});
	}
}
