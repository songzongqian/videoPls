Video++互动层Android SDK对接指南

Video++ 互动层目前支持直播、点播、Video++商城、互动娱乐以及OTT业务的分别以及全部的集成。 

引入SDK到项目的方式非常简单：

 1. 在项目 build.gradle 中添加配置aar（注：完整请参考demo）
    
    compile(name: 'venvy_pub-release-2.0.0', ext: 'aar')
    compile  "com.github.bumptech.glide:glide:YOU_VERSION" // glide
    compile "com.squareup.okhttp3:okhttp:YOU_VERSION" // okhttp
    compile 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.0.2' // mqtt 长链接
    //接入互娱以及子商城时需要另外添加依赖 
    compile 'com.videoli:venvy_lua:1.0.0'
    //接入子商城（横屏）时需要另外添加依赖 
    compile 'com.android.support:recyclerview-v7:25+'
    (版本号只是举例，具体版本请咨询对接技术同学)
    
2. 如果集成点播功能，请参考Demo项目中VideoOsActivity.java 中的调用。
3. 如果集成直播功能，请参考Demo项目中LiveOsActivity.java 中的调用。
4. 如果集成Video++商城功能，请参考Demo项目中MallOsActivity.java 中的调用。
5. 如果集成互动娱乐功能，请参考Demo项目中HuyuOsActivity.java 中的调用。
6. 如果集成OTT功能，请参考Demo项目中VideoOTTActivity.java 中的调用。
  

注意点：

  1. 配置相关的权限(如网络权限),互动SDK所需权限详见Demo中AndroidManifest.xml的权限配置

  2. 请在Application中调用Videopls.appCreate()方法，可参考MyApp.java的实例化设置（重要，V1.3.4.x以上版本增加）

  3. 直播和点播均会需要构造一个VideoPlusAdapter 设置给对应的View。直播和点播目前根据对接平台的不一致可能在构造Adapter的时候可能会有略微的差异性。请参考Demo中的Adapter构造。

  4. 直播互动竖屏有两种展示模式：即竖屏全屏和竖屏小屏。两种展示模式的切换可以调用VideoPlusAdapter的notifyLiveVerticalScreen方法，详见Demo中LiveOsActivity.java

  5. 直播互动中互动层的默认点击事件为:竖屏全屏和横屏全屏两种模式下由SDK打开自己的WebView,在竖屏小屏幕模式下由调用方监听点击事件，此时需要重写Adapter的IWidgetClickListener方法(详见Demo中LiveOsActivity.java)。
  6. 在页面的生命周期方法中请对应调用adapter中提供的对应生命周期方法。例如：adapter.onDestroy()。详见Demo中的BasePlayerActivity.java

  7. 在点播构造Adapter的时候buildMediaController（）方法必须重载，此方法返回接口为控制播放器行为。其中getCurrentPosition方法为获取当前播放器时间，单位为毫秒级。

  8. 在需要对Provider动态设置属性的时候推荐奖Builder对象作为全局对象，每次利用Builder对象来构建provider对象后调用Adapter的updateProvider()方法。
  9. 因为sdk的webView用的是腾讯的x5webView, 所以需要libs文件配置x5WebView的jar，同时需要在src/jniLibs文件放x5的so库,jar包和so库可以在该地址中下载到https://x5.tencent.com/tbs/sdk.html

