Video++互动层Android SDK对接指南

Video++ 互动层目前支持直播和点播功能的同时集成。其中具体集成直播、点播、或者直播点播融合的版本请先和商务同学确认。

引入SDK到项目的方式非常简单：

  1. 在项目 build.gradle 中complie 'com.videoli:video_pub:1.0.2' (版本号只是举例，具体版本请咨询对接技术同学)
  2. 如果集成点播功能，请参考Demo项目中MainActivity.java 中的调用。
  3. 如果集成直播功能，请参考Demo项目中LiveActivity.java 中的调用。

注意点：

  1. 配置相关的权限(如网络权限),互动SDK所需权限详见Demo中AndroidManifest.xml的权限配置

  2. Video++互动层对于三方的引入，为了避免冲突和重复引入，导入三方功能开放给最外层调用，如项目中已配置，可按照项目中原有配置来，如没配置相关依赖，需要在项目中添加。具体依赖可看build.gradle文件注释说明.

  3. 请在Application中实例化Fresco。 至少应该在调用Video ++ 逻辑前实例化。可参考MyApp.java的实例化设置

  4. 直播和点播均会需要构造一个VideoPlusAdapter 设置给对应的View。直播和点播目前根据对接平台的不一致可能在构造Adapter的时候可能会有略微的差异性。请参考Demo中的Adapter构造。

  5. 直播互动竖屏有两种展示模式：即竖屏全屏和竖屏小屏。两种展示模式的切换可以调用VideoPlusAdapter的notifyLiveVerticalScreen方法，详见Demo中LiveActivity.java

  6. 直播互动中互动层的默认点击事件为:竖屏全屏和横屏全屏两种模式下由SDK打开自己的WebView,在竖屏小屏幕模式下由调用方监听点击事件，此时需要重写Adapter的OnViewClickListener方法(详见Demo中LiveActivity.java)。

  7. 直播互动中如果在横竖屏全屏以及横竖屏小屏模式下全部使用自己的WebView，可调用Adapter的setIsPear()方法。同时重写Adapter的OnViewClickListener方法(详见Demo中LiveActivity.java)。

  8. 在页面的生命周期方法中请对应调用adapter中提供的对应生命周期方法。例如：adapter.onDestroy()。详见Demo中的MainActivity.java

  9. 在点播构造Adapter的时候buildMediaController（）方法必须重载，此方法返回接口为控制播放器行为。其中getCurrentPosition方法为获取当前播放器时间，单位为毫秒级。

  10. 在直播中可能有相关的配置信息，如是否为芒果的配置 setIsMango(). 各平台接入是否需要做相应平台配置请咨询对接人员。

  11. 在需要对Provider动态设置属性的时候推荐奖Builder对象作为全局对象，每次利用Builder对象来构建provider对象后调用Adapter的updateProvider()方法。


  ——————————————————

  V1.2.0 版本升级内容

  1. android支持远程依赖接入
  2. android直播优化内存占用
  3. android点播优化内存占用
  4. android点播横竖屏切换曝光投递优化


  V1.2.0.2 版本升级内容

  1. 增加中间层Provider动态更新功能
  2. 增加图片下载成功监控

  v1.2.0.4 版本升级内容

  1. 将SDK编译版本下调，同时将依赖项目暴露给调用方
  2. Fix 点播读取文件BUG

  V1.2.0.6 版本升级内容

  1. fix 部分机型上闪退的bug
 
  v1.2.0.8版本内容
  
  1. 修复config接口中返回的对外链处理
