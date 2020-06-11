import 'dart:async';

import 'package:fluocean/response.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

MethodChannel _channel = const MethodChannel('qyxing.cn/fluocean')
  ..setMethodCallHandler(_methodHandler);

const int NETWORK_STATE_MOBILE = 1;
const int NETWORK_STATE_2G = 2;
const int NETWORK_STATE_3G = 3;
const int NETWORK_STATE_WIFI = 4;
const int NETWORK_STATE_4G = 5;

Future<String> get platformVersion async {
  final String version = await _channel.invokeMethod('getPlatformVersion');
  return version;
}

StreamController<OceanResponse> _eventHandlerController =
    new StreamController.broadcast();

Stream<OceanResponse> get eventHandler => _eventHandlerController.stream;

Future _methodHandler(MethodCall methodCall) {
  var response = OceanResponse.create(methodCall.method, methodCall.arguments);
  _eventHandlerController.add(response);
  return Future.value();
}

// 注册
Future<OceanResponse> registerPangolin({
  @required String appId,
  @required bool useTextureView,
  @required String appName,
  @required bool allowShowNotify,
  @required bool allowShowPageWhenScreenLock,
  @required bool debug,
  @required bool supportMultiProcess,
  List<int> directDownloadNetworkType,
}) async {
  Map map = await _channel.invokeMethod<Map>('registerPangolin', {
    'appId': appId,
    'useTextureView': useTextureView,
    'appName': appName,
    'allowShowNotify': allowShowNotify,
    'allowShowPageWhenScreenLock': allowShowPageWhenScreenLock,
    'debug': debug,
    'supportMultiProcess': supportMultiProcess,
    'directDownloadNetworkType': directDownloadNetworkType ??
        [
          NETWORK_STATE_MOBILE,
          NETWORK_STATE_3G,
          NETWORK_STATE_4G,
          NETWORK_STATE_WIFI
        ]
  });
  return OceanResponse.create('onResponse', map);
}

// 开屏广告
Future<OceanResponse> loadSplashAd( {@required String codeId, @required bool debug}) async {
  Map map = await _channel.invokeMethod<Map>('loadSplashAd', {'codeId': codeId, 'debug': debug});
  return OceanResponse.create('onResponse', map);
}
