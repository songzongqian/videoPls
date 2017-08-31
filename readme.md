Video++互动层Android SDK对接指南

Video++ 互动层目前支持直播和点播功能的同时集成。其中具体集成直播、点播、或者直播点播融合的版本请先和商务同学确认。

引入SDK到项目的方式非常简单：

  1. 在项目 build.gradle 中complie 'com.videoli:video_pub:1.3.2.0' (版本号只是举例，具体版本请咨询对接技术同学)
  2. 如果集成点播功能，请参考Demo项目中MainActivity.java 中的调用。
  3. 如果集成直播功能，请参考Demo项目中LiveActivity.java 中的调用。

注意点：

  1. 配置相关的权限(如网络权限),互动SDK所需权限详见Demo中AndroidManifest.xml的权限配置

  2. 请在Application中调用Videopls.appCreate()方法，可参考MyApp.java的实例化设置（重要，V1.3.4.x以上版本增加）

  3. 直播和点播均会需要构造一个VideoPlusAdapter 设置给对应的View。直播和点播目前根据对接平台的不一致可能在构造Adapter的时候可能会有略微的差异性。请参考Demo中的Adapter构造。

  4. 直播互动竖屏有两种展示模式：即竖屏全屏和竖屏小屏。两种展示模式的切换可以调用VideoPlusAdapter的notifyLiveVerticalScreen方法，详见Demo中LiveActivity.java

  5. 直播互动中互动层的默认点击事件为:竖屏全屏和横屏全屏两种模式下由SDK打开自己的WebView,在竖屏小屏幕模式下由调用方监听点击事件，此时需要重写Adapter的OnViewClickListener方法(详见Demo中LiveActivity.java)。

  6. 直播互动中如果在横竖屏全屏以及横竖屏小屏模式下全部使用自己的WebView，可调用Adapter的setIsPear()方法。同时重写Adapter的OnViewClickListener方法(详见Demo中LiveActivity.java)。

  7. 在页面的生命周期方法中请对应调用adapter中提供的对应生命周期方法。例如：adapter.onDestroy()。详见Demo中的MainActivity.java

  8. 在点播构造Adapter的时候buildMediaController（）方法必须重载，此方法返回接口为控制播放器行为。其中getCurrentPosition方法为获取当前播放器时间，单位为毫秒级。

  9. 在直播中可能有相关的配置信息，如是否为芒果的配置 setIsMango(). 各平台接入是否需要做相应平台配置请咨询对接人员。

  10. 在需要对Provider动态设置属性的时候推荐奖Builder对象作为全局对象，每次利用Builder对象来构建provider对象后调用Adapter的updateProvider()方法。


  ——————————————————

  V1.2.0.14 版本升级内容
  1. android支持远程依赖接入
  2. android直播优化内存占用
  3. android点播优化内存占用
  4. android点播横竖屏切换曝光投递优化
  5. 增加中间层Provider动态更新功能
  6. 增加图片下载成功监控
  7. 将SDK编译版本下调，同时将依赖项目暴露给调用方

  v1.3.2.0版本升级内容
  1. 对于V1.2.0.x版本接入方，外部三方引入依赖方式有稍微变化，不需要再build.gradle中写除了vido++依赖以外的三方complie依赖.
  2. 增强android广告缓存，加载速度优化

  v1.3.4.20 版本升级内容（2017.07.06）
  1. 将默认图片框架改为Glide，如果对接上需要Fresco 作为图片框架，请联系技术同学。
  2. 如果对接方的glide版本低于3.8.0，gif投递的时候gif的素材不能设置为无限循环。可以设置循环次数为一个非常大的值来实现无限循环
  3. 请在项目application里调用VideoPlus.appCreate()方法。（非常重要，V1.3.4.x及以上版本必须需要调用)
  4. fix 曝光成功率问题
  5. 修复中插bug

  v1.3.6.0 版本升级内容（2017.07.14）
  1. 增加热点点击事件回调
  2. fix bug

  v1.5.0.6 版本升级内容（2017.08.04）


  1. 新增热点展示、点击、关闭监听。
  2. 直播新版投票样式。
  3. 直播商品列表
  4. fix bug

 v1.6.0.16版本升级内容(2017.08.13)

  1.修改返回首页后中插倒计时从头开始的bug
  2.直播点播互动层添加“广告”和“关闭”按钮标识
  3.解决部分机型卡顿问题
  4.广告展示，点击，关闭统一接口抛给接入方，接入方可根据不同的广告类型事件来做不同的逻辑。具体逻辑参考VideoOSActivity.java 的Adapter构建注释说明
  5.为了防止与平台MQTT冲突，现在MQTT为provided引入。接入方需要手动依赖下MQTT。
