## Getting Started

<h1 align="center">fluocean</h1>
<p>
<a href="https://www.npmjs.com/package/drone"><img src=https://img.shields.io/badge/license-MIT-brightgreen></a>
<a href="https://www.apple.com/lae/ios/ios-13/"><img src=https://img.shields.io/badge/platform-ios-lightgrey></a>
<a href="https://www.Android.com/package/drone"><img src=https://img.shields.io/badge/platform-Android-lightgrey></a>
<a href="https://www.dart.dev"><img src=https://img.shields.io/badge/Language-Dart-orange></a>
</p>

## 前言
️在使用本插件前请认真，仔细阅读[穿山甲官方文档](http://partner.toutiao.com/doc?id=5dd0fe756b181e00112e3ec5)。

## 简介
fluocean是一款Flutter插件，集成了字节跳动旗下的广告平台——穿山甲的Android和iOS的SDK，方便开发者直接在Flutter层面调用相关方法。

## 插件开发环境相关
### Flutter
```
Flutter (Channel beta, 1.18.0-11.1.pre, on Mac OS X 10.14.5, locale zh-Hans-CN)
```
### Dart
```
Dart VM version: 2.9.0 on "macos_x64"
```
### Platform
```
Xcode - develop for iOS and macOS (Xcode 11.3)
Android Studio (version 4.0)
```
### 穿山甲
```
iOS - 2.9.5.6(cocoapods lastest version)
Android - 2.9.5.0
```

## 安装
```yaml
# add this line to your dependencies
dependencies:
  fluocean: ^0.1.0
```

## 环境配置
使用前请确认您以根据穿山甲的官方文档中的步骤进行了相应的依赖添加，权限获取以及参数配置
### Android
[穿山甲Android SDK 接入基础配置](https://partner.oceanengine.com/union/media/union/download/detail?id=4&docId=5de8d9b425b16b00113af0da&osType=android)

#### iOS
[穿山甲iOS SDK 接入基础配置](https://partner.oceanengine.com/union/media/union/download/detail?id=16&docId=5de8d570b1afac00129330c5&osType=ios)

## flocean集成
### Android
 [下载open_ad_sdk.aar](https://github.com/qyxing/fluocean/blob/master/example/android/open_ad_sdk/open_ad_sdk.aar)。或者从官方SDK下载。
将open_ad_sdk.arr导入工程

### iOS
```
pod install
```
## 穿山甲平台
在使用之前必须确认您在穿山甲平台的[控制台](https://partner.oceanengine.com/union/media/union/site)已经注册了自己app所对应的应用以及对应广告类型的代码位，由于穿山甲包含多种类型的广告和功能请务必确认你在插件中注册的和你在平台注册的一一对应。
## 开始使用
### 初始化（register）
调用穿山甲SDK的第一步是对SDK的初始化

```dart
import 'package:fluocean/fluocean.dart' as fluocean;

fluocean.OceanResponse r = await fluocean.registerPangolin(
        appId: "appId", // Your appID
        useTextureView: true,
        appName: "appName", // Your appName
        allowShowNotify: true,
        canUseLocation: false,
        allowShowPageWhenScreenLock: true,
        debug: true,
        supportMultiProcess: true);
        
print('success: ${r.isSuccessful}')
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| appId      | 在穿山甲平台注册的自己的AppId | null |
| useTextureView       | 使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView       |   false |
| appName  | 自己的应用名称       |    null |
| allowShowNotify   | 是否允许sdk展示通知栏提示       |    true |
| allowShowPageWhenScreenLock  | 是否在锁屏场景支持展示广告落地页       |    true |
| debug  | 测试阶段打开，可以通过日志排查问题，上线时去除该调用       |    true |
| supportMultiProcess  | 是否支持多进程      |    false |

* 注意以上参数大部分针对Android端，iOS端由于穿山甲SDK本身的原因并没有过多的参数配置，有用的参数仅为appId，appName。

#### 接入成功debug信息
* Android
```
E/TTAdSdk-InitChecker( 7673): ==穿山甲sdk接入，环境为debug，初始化配置检测开始==
E/TTAdSdk-InitChecker( 7673): AndroidManifest.xml中TTFileProvider配置正常
E/TTAdSdk-InitChecker( 7673): AndroidManifest.xml中TTMultiProvider配置正常
E/TTAdSdk-InitChecker( 7673): AndroidManifest.xml中权限配置正常
E/TTAdSdk-InitChecker( 7673): 动态权限没有获取，可能影响转化：android.permission.READ_PHONE_STATE
E/TTAdSdk-InitChecker( 7673): 动态权限没有获取，可能影响转化：android.permission.ACCESS_COARSE_LOCATION
E/TTAdSdk-InitChecker( 7673): 动态权限没有获取，可能影响转化：android.permission.ACCESS_FINE_LOCATION
E/TTAdSdk-InitChecker( 7673): 动态权限没有获取，可能影响转化：android.permission.WRITE_EXTERNAL_STORAGE
E/TTAdSdk-InitChecker( 7673): ==穿山甲sdk初始化配置检测结束==
```

* iOS
仅提示穿山甲接入成功


### 加载开屏广告
* 在初始化成功后执行

```dart
fluocean.OceanResponse r =
        await fluocean.loadSplashAd(codeId: '广告位ID', debug: true);
print('loadSplashAd load success: ${r.isSuccessful}');
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| codeId      | 在穿山甲平台注册的自己的广告位id | null |
| debug  | 测试阶段打开，可以通过日志排查问题，上线时去除该调用       |    true |

### 加载激励视频
使用前请确认您已在穿山甲平台的[控制台](https://partner.oceanengine.com/union/media/union/site)建立了你的激励视频广告id。<br/>

```dart
fluocean.OceanResponse r = await fluocean.loadRewardAd(
    isHorizontal: true,
    debug: true,
    codeId: "90000000", // 替换为广告位ID
    supportDeepLink: true,
    rewardName: "答题奖励", // 替换为name
    rewardAmount: 1,
    isExpress: true,
    expressViewAcceptedSizeH: 750,
    expressViewAcceptedSizeW: 1334,
    userID: "user",
    mediaExtra: "media_extra");
print('loadRewardAd load success: ${r.isSuccessful}');
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| isHorizontal  | 是否横屏      |    false |
| codeId      | 在穿山甲平台注册的自己的广告位id | null |
| debug  | 此处debug为true的情况下 我会给你显示整体进程的一个Toast 方便你调试      |    true |
| supportDeepLink  | 是否横屏      |    false |
| rewardName  | 奖励的名称      |    null |
| rewardAmount  | 奖励数量      |    null |
| isExpress  |是否进行自渲染（传入后设置激励视频尺寸）      |    true |
| expressViewAcceptedSizeH  | 渲染视频高度      |    500 |
| expressViewAcceptedSizeW  | 渲染视频宽度      |    500 |
| userID  | 必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传      |    null |
| mediaExtra  | 用户透传的信息，可不传      |    media_extra |


### 激励视频回调监听
在合适的位置注册你的监听，保证用户看完广告时接收到我给你的回调信息，并做下一步处理

```dart
fluocean.eventHandler.listen((value) {
  if (value is fluocean.RewardResponse) {
    // 激励视频回调
    if (value.code != 0) {
      print('播放错误: ${value.message}');
      return;
    }
    if (value.message == 'rendered') {
      print('广告开始播放');
      return;
    }
    print(
        'verify: ${value.verify}  amount: ${value.amount} + name: ${value.name}');
  }
});
```
#### 参数说明
| 参数  | 描述  | 默认值 |
| :------------ |:---------------:| -----:|
| verify      | 验证奖励有效性，即用户是否完成观看 |   |
| name  | 你在穿山甲填写的奖励名称      |     |
| amount  | 你在穿山甲填写的奖励数量     |     |

激励视频的具体使用参见项目目录下Example

## 版本信息
| 版本  | 更新信息  |
| :------------ |:---------------:|
| 0.0.1  | 项目初始化 |
| 0.1.0  | 集成穿山甲开屏广告与激励广告(支持Android与IOS) |

## 联系方式
* 欢迎小伙伴加入，一起完善。
* QQ群: 17313538<br/>


