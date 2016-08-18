# mock-api

### 原理

基于 [Okhttp](https://github.com/square/okhttp) 的 [Interceptor](https://github.com/square/okhttp/wiki/Interceptors) ：**如果目标url需要mock，则从本地（assets目录或sdcard）读取数据，否则继续向外发送请求。**


### 使用

在`build.gradle`中添加以下依赖：

TODO

准备mock数据，比如对于请求：`/api/user/` 我们期望返回：

```
{
    username: "shanbay"
}
```

我们将上述数据保存为`user.json`，然后放在Assets根目录下的`mockdata/account/`目录中，接着在创建Client的时候，增加以下代码：

```java

MockApiSuite suite = new MockApiSuite("account"); // account 表示 suite name
suite.addMockApi(new StandardMockApi(MockHttpMethod.GET, "/api/user/").setSuccessDataFile("user.json"))

MockApiInterceptor mockApiInterceptor = new MockApiInterceptor(context);
mockApiInterceptor.addMockApiSuite(suite);

OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
    .addInterceptor(mockApiInterceptor)   // 注入mock api interceptor
    .addInterceptor(new UserAgentInterceptor(context));
    ....

```

这时候请求`/api/user/`，你会发现返回了我们刚才mock的数据 ;-)

### 进阶
**MockApiSuite**

当我们mock很多api的时候，如果只是简单聚合这些api将会非常的凌乱&难以管理，所以我们建立了Suite的概念，把相同业务场景的api放到一个suite中（比如分为account模块、purchase模块），这样对于之后的更新、维护都非常方便。

**StandardMockApi**

StandardMockApi用来声明一个需要mock的api，其中第一个参数表示需要mock的 HTTP Method（GET、POST、PUT、DELETE），第二个参数表示需要mock的url**（支持正则）**，通过这两个构造参数，我们基本上就定义出一个mock api的大体框架，之后我们可以通过：
- `setSuccessDataFile` 设置api调用成功返回的数据结果；
- `setErrorDataFile` 设置api调用失败返回的数据结果，
- `setState` 设置这次是模拟api调用成功还是失败（默认模拟调用成功）；
- `setRequestTime` 设置api请求时间（可以用来模拟请求慢的情况）；

**Mock数据**

- 约定mock数据存放在： mockdata/[suite_name]/中，
- 通过MockApiInterceptor的第二个构造参数，指定mock数据是放在sdcard根目录，还是assets根目录（默认）；

