import 'package:flutter/material.dart';
import 'dart:async';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import 'package:fluocean/fluocean.dart' as fluocean;
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
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

    initPlatformState();

    initFluocean();
  }

  void initFluocean() async {
    fluocean.OceanResponse r = await fluocean.registerPangolin(
        appId: "appId", // 替换为申请的appID
        useTextureView: true,
        appName: "appName", // 替换为申请的appName
        allowShowNotify: true,
        canUseLocation: false,
        allowShowPageWhenScreenLock: true,
        debug: true,
        supportMultiProcess: true);

    if (r.isSuccessful) {
      // 根据实际项目情况，设置是否直接显示开屏广告
      await loadSplashAd();
    }
  }

  void loadSplashAd() async {
    // codeId 替换为在穿山甲申请的广告位ID
    fluocean.OceanResponse r =
        await fluocean.loadSplashAd(codeId: '8000000', debug: true);
    print('loadSplashAd load success: ${r.isSuccessful}');
  }

  void loadRewardAd() async {
    fluocean.OceanResponse r = await fluocean.loadRewardAd(
        isHorizontal: true,
        debug: true,
        codeId: "90000000", // 替换为广告位ID
        supportDeepLink: true,
        rewardName: "答题奖励", // 替换为name
        rewardAmount: 1,
        isExpress: true,
        expressViewAcceptedSizeH: 750.w,
        expressViewAcceptedSizeW: 1334.h,
        userID: "user",
        mediaExtra: "media_extra");
    print('loadRewardAd load success: ${r.isSuccessful}');
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion = await fluocean.platformVersion;
    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      builder: (context, child) {
        ScreenUtil.init(context,
            width: 750, height: 1334, allowFontScaling: false);
        return child;
      },
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text('Running on: $_platformVersion\n'),
              RaisedButton(
                onPressed: loadSplashAd,
                child: Text('开屏广告'),
              ),
              RaisedButton(
                onPressed: loadRewardAd,
                child: Text('激励广告'),
              ),
            ],
          ),
        ),
      ),
    );
  }

  // 请求权限
  void requestPermission() async {
    Map<Permission, PermissionStatus> statuses = await [
      Permission.phone,
      // Permission.location,
      // Permission.storage,
    ].request();
    //校验权限
    if (statuses[Permission.location] != PermissionStatus.granted) {
      print("无位置权限");
    }
  }
}
