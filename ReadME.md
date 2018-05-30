# BaseFramework

## MVP 架构模式

```text
Model ↔ Presenter ↔ View
```

若对 MVP 架构还不了解，请自行搜索相关文章学习。

## 结构

（在命令提示符中使用 `tree /f` 生成）

``` text
com.mix.framework
    ├─base
    │  │  BaseActivity.java
    │  │  BaseApplication.java
    │  │  BaseFragment.java
    │  │
    │  ├─mvp
    │  │      BaseMVPActivity.java
    │  │      BaseMVPFragment.java
    │  │      BasePresenter.java
    │  │      IBasePresenter.java
    │  │      IBaseView.java
    │  │
    │  └─ui
    │      │  BaseDialogFragment.java
    │      │  BaseListAdapter.java
    │      │  BaseStateActivity.java
    │      │  BaseStateFragment.java
    │      │
    │      └─list
    │              BaseExtendedAdapter.java
    │              HeaderViewHolder.java
    │              LoadingMoreViewHolder.java
    │
    ├─data
    │  ├─model
    │  │      BaseModel.java
    │  │      IModelCallBack.java
    │  │      ModelManager.java
    │  │
    │  └─net
    │      │  HttpClient.java
    │      │  NetClient.java
    │      │  RetrofitClient.java
    │      │
    │      ├─interceptor
    │      │      LogInterceptor.java
    │      │
    │      └─util
    │              ApiException.java
    │              ErrorResolver.java
    │              NetConfig.java
    │
    ├─util
    │      ApplicationUtils.java
    │      Logger.java
    │      QuickClickGuard.java
    │      StringUtils.java
    │      ToastUtils.java
    │
    └─widget
            AATextView.java
            NoScrollViewPager.java
```

### View 层

```text
                                BaseStateActivity
                                       ↑
IBasePresenter → BasePresenter ↔ BaseMVPActivity ← IBaseView
                                       ↑
                                  BaseActivity
                                       ↑
                                AppCompatActivity
```

`BaseActivity` 实现了对 Activity 的简单封装，抽象了提供布局文件的 `getPageView()` 方法和进行初始化的 `init()` 方法。增加了 `onLazyLoad()` 方法实现懒加载功能。懒加载的执行时机为第一次调用 `onResume()` 时。

`BaseMVPActivity` 继承 `BaseActivity` ，提供了 MVP 模式下的 Activity 封装，实现了 `IBaseView` 接口，并持有 Presenter 对象 `mPresenter` 。泛型 `<P extends IBasePresenter>` 为 Presenter 对象的类型。抽象了提供 Presenter 对象的 `createPresenter()` 方法和进行初始化页面的 `initPage()` 方法。在该类中，将视图的生命周期和 Presenter 层的相关方法进行了绑定，可在 Presenter 的具体实现中直接重写方法进行操作。

`BaseStateActivity` 继承 `BaseMVPActivity` ，提供了加载中，加载出错，空白页面，内容视图的默认实现，并可进行自定义设置。抽象了提供内容视图布局的 `getContentView()` 方法和进行初始化内容页面的 `initContent()` 方法。该 Activity 有一个 `FrameLayout` 根视图，背景色为白色，作为不同状态视图的父类，对状态视图的显示进行管理。该类中还集成了 `ButterKnife` 工具，可以直接通过注解获取视图对象。

```text
                                BaseStateFragment
                                       ↑
IBasePresenter → BasePresenter ↔ BaseMVPFragment ← IBaseView
                                       ↑
                                  BaseFragment
                                       ↑
                        android.support.v4.app.Fragment
```

`BaseFragment` 实现了对 Fragment 的简单封装，抽象了提供布局文件的 `getPageView()` 方法和进行初始化的 `init()` 方法。增加了 `onLazyLoad()` 方法实现懒加载功能，懒加载的执行时机为第一次可见时。增加了 `onVisible()` 和 `onInvisible()` 方法，对应可见于不可见的状态。增加了 `findViewById(int)` 方法，直接从视图中寻找对象。

`BaseMVPFragment` 继承 `BaseFragment` ，提供了 MVP 模式下的 Fragment 封装，实现了 `IBaseView` 接口，并持有 Presenter 对象 `mPresenter` 。泛型 `<P extends IBasePresenter>` 为 Presenter 对象的类型。抽象了提供 Presenter 对象的 `createPresenter()` 方法和进行初始化页面的 `initPage()` 方法。在该类中，将视图的生命周期和 Presenter 层的相关方法进行了绑定，可在 Presenter 的具体实现中直接重写方法进行操作。

`BaseStateFragment` 继承 `BaseMVPFragment` ，提供了加载中，加载出错，空白页面，内容视图的默认实现，并可进行自定义设置。抽象了提供内容视图布局的 `getContentView()` 方法和进行初始化内容页面的 `initContent()` 方法。该 Fragment 有一个 `FrameLayout` 根视图，背景色为白色，作为不同状态视图的父类，对状态视图的显示进行管理。该类中还集成了 `ButterKnife` 工具，可以直接通过注解获取视图对象。

### Presenter 层

```text
                *PresenterImpl* → *ModelManager*
                       ↑  ↑                │
                       │  └ IModelCallback ┘
                       │
IBasePresenter → BasePresenter ↔ *View*
```

Presenter 层的基类 `BasePresenter` 实现了 View 层生命周期对应的方法，子类可以直接进行重写。在进行网络请求时，需要调用 `makeRequest(ResourceSubscriber<T>)` 方法，该方法会将 Model 返回的订阅加入 `CompositeDisposable` 进行统一管理。 Presenter 生命周期方法与 View 生命周期对应如下：

|Presenter   |Activity        |Fragment                                       |
|------------|----------------|-----------------------------------------------|
|onAttach()  |onCreate(Bundle)|onCreate(Bundle)                               |
|onStart()   |onCreate(Bundle)|onCreateView(LayoutInflater, ViewGroup, Bundle)|
|onLazyLoad()|onResume()      |onResume() 或 setUserVisibleHint(boolean)      |
|onResume()  |onResume()      |onResume()                                     |
|onPause()   |onPause()       |onPause()                                      |
|onStop()    |onStop()        |onStop()                                       |
|onDetach()  |onDestroy()     |onDestroyView()                                |

### Model 层

```text
               *Service* → *ModelImpl* ┐
NetConfig ┐                     ↑      ├→ ModelManager ─ IModelCallback → *Presenter*
      NetClient ──────────→ BaseModel  ┘       ↑                              │
       │     │                                 └──────────────────────────────┘
HttpClient RetrofitClient
```

整体 Model 层分为两个部分，一部分是针对网络框架的封装，另一部分是面向 Presenter 层的调用方法与回调方法。

网络框架部分，目前支持 http 方式，核心为 `NetClient` 类。该类是单例的，需要通过 `getInstance()` 方法获取实例。在使用前需要进行且只能进行一次初始化，即调用 `init(NetConfig)` 静态方法，传入参数为 `NetConfig` 类对象，可以设置网络超时时间，服务器地址，网络拦截器等。通过该类，可以获取到全局的 `OkHttpClient` 以及 `Retorfit` 实例。如果需要改变网络设置，可以调用 `updateConfig(NetConfig)` 方法，该方法会通知各模块进行更新。

面向 Presenter 层部分，核心为 `ModelManager` 类。该类用一个 `HashMap<String, BaseModel>` 管理所有已实例化的 Model 对象，所有 Model 对象的实现都应继承 `BaseModel` 并实现抽象方法 `getService()` ，在其中返回 Retrofit 的 Service 接口。并且在使用中需要通过 `<T extends BaseModel>ModelManager.get(Class<T>)` 静态方法取得相应的 Model 对象，保证 Model 在框架范围内是单例的。该方法首先在 `ModelManager` 中查询相应的 Model 对象是否已经注册，如果已经注册则返回该 Model 的实例，未注册则调用该 Model 的构造方法。由于 Model 均继承自 `BaseModel` ，所以先调用 `BaseModel` 的构造方法。 `BaseModel` 的构造方法声明为 `protected` ，仅子类可以调用。其中逻辑为首先在 `ModelManager` 中查询是否已经注册，如果已经注册则抛出异常，未注册则将实例注册到 `ModelManager` 中。最后再由 `ModelManager` 返回该 Model 的实例。

`BaseModel` 中网络请求方法声明如下：

```java
/**
 * 进行 http 网络请求的方法。
 *
 * @param request       由 retrofit 返回的 Flowable 对象
 * @param dataGetter    从 Response 中提取数据的方法
 * @param dataConverter 将数据进行转换的方法。运行在 IO 线程
 * @param callback      结果的回调。运行在主线程
 * @param resolver      错误异常的解析类。如果为空，则返回 Throwable#getMessage()
 * @param <P>           通用服务器返回类型的泛型
 * @param <D>           从服务器返回中提取数据的泛型
 * @param <R>           最终结果的泛型
 * @return 网络请求的订阅
 */
protected <P, D, R> ResourceSubscriber<R> request(@NonNull Flowable<P> request,
                                                    @NonNull FlowableTransformer<P, D> dataGetter,
                                                    @NonNull Function<D, R> dataConverter,
                                                    @NonNull IModelCallback<R> callback,
                                                    @Nullable ErrorResolver resolver)
```

其中 `Flowable<P>` 参数为 Retrofit 中返回的 Flowable 对象。 `FlowableTransformer<P, D>` 参数为从服务器响应中提取需要数据的 FlatMap ，此处一般使用统一处理即可。 `Function<D, R>` 参数为处理数据的 map 操作，在 IO 线程，可以按需求进行。 `IModelCallback<R>` 为回调方法，执行在主线程。 `ErrorResolver` 参数为自定义错误解析器，作用是根据 `Throwable` 解析出错误信息并传给 `IModelCallback#onError(String)` 。如果不需要则传入 `null` ，此时错误信息为 `Throwable#getMessage()` 。在具体实现 Model 时，可实现一个 `ModelImpl` 类，将 `FlowableTransformer<P, D>` 和 `ErrorResolver` 做应用级的统一处理。

### Application

可以直接将 `BaseApplication` 指定为应用的 Application ，也可以自定义并继承 `BaseApplication` 。 `BaseApplication` 提供了静态方法 `getContext()` 以全局获取 Context 。

### Dialog

Dialog 统一使用 `DialogFragment` ，将自定义 Dialog 继承 `BaseDialogFragment` ，并实现其中的 `getContentView()` 和 `initView(View)` 方法提供布局视图及初始化视图。要指定对话框的大小，可以调用 `setSize(int, int)` 方法传入宽高。最后在需要调用 `show()` 方法显示。

### ListAdapter

```text
BaseExtendedAdapter
        ↑
  BaseListAdapter
```

所有列表均采用 `RecyclerView` 实现。 `BaseListAdapter` 实现了对 Adapter 的简单封装，使用泛型规定数据格式，提供 `setNewData(List<T>)` 方法设置数据。

`BaseExtendedAdapter` 提供了添加头部，添加尾部，下拉刷新和上拉加载更多的实现。可以通过 `addHeader(View)` 方法添加头部视图，也可添加多个，新添加的头部会在列表的最上端，但在下拉刷新条目之后。通过 `addFooter(View)` 方法添加尾部视图，可添加多个，新添加的尾部会在列表的最下端，但在上拉加载条目之前。下拉刷新默认关闭，使用 `setRefreshEnable(boolean)` 方法开启，使用 `setOnRefreshListener(OnRefreshListener)` 方法添加加载更多下拉刷新的监听。刷新完成时，需要调用 `setNewData(List<T>）` 方法替换新数据并调用 `refreshEnd()` 方法更新状态；上拉加载更多默认关闭，使用 `setLoadMoreEnable(true)` 方法开启，使用 `setOnLoadMoreListener(OnLoadMoreListener)` 方法添加加载更多的监听。一次加载完成时，需要调用 `addData(List<T>）` 方法添加新数据并调用 `loadMoreEnd()` 方法更新状态；加载出错时，调用 `loadMoreError()` 方法更新状态；没有更多数据时，调用 `noMore()` 方法更新状态。在实现 Adapter 时，需要实现 `onCreateViewHolderCompat(ViewGroup, int)` 和 `onBindViewHolderCompat(ViewHolder, int)` 方法，对应原始 Adapter 的 `onCreateViewHolder(ViewGroup, int)` 和 `onBindViewHolder(ViewHolder, int)` 。如果需要多条目类型，则重写 `getItemViewTypeCompat(int)` 方法。 所有 compat 方法的 `int` 参数均为 dataPosition ，即条目数据在数据集中的位置，不一定为条目在整个列表中的位置。

## 使用方法

参照工程中 `app` 模块的 Demo 。

## 更改记录

- 2018/05/30

    列表添加下拉刷新

- 2018/05/22

    初始版本
