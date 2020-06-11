package cn.originalstar.fluocean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.List;

import cn.originalstar.fluocean.vo.OceanResponse;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FluoceanPlugin */
public class FluoceanPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  
  private MethodChannel channel;
  private Context applicationContext;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    this.applicationContext = flutterPluginBinding.getApplicationContext();
    // channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "fluocean");
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "qyxing.cn/fluocean");
    channel.setMethodCallHandler(this);
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "qyxing.cn/fluocean");
    channel.setMethodCallHandler(new FluoceanPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
      return;
    }
    if(call.method.equals("registerPangolin")){
      registerPangolin(call, result);
      return;
    }
    if(call.method.equals("loadSplashAd")){
      loadSplashAd(call, result);
      return;
    }
    result.notImplemented();
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  // 开屏广告
  private void loadSplashAd(MethodCall call, Result result){
    String codeId = call.argument("codeId");
    Boolean debug = call.argument("debug");
    Intent intent = new Intent();
    intent.setClass(activity, SplashActivity.class);
    intent.putExtra("codeId", codeId);
    intent.putExtra("debug", debug);
    result.success(OceanResponse.success("success"));
    activity.startActivity(intent);
  }

  // 穿山甲SDK初始化
  private void registerPangolin(MethodCall call, Result result){
    String appId = call.argument("appId");
    Boolean useTextureView = call.argument("useTextureView");
    String appName = call.argument("appName");
    Boolean allowShowNotify = call.argument("allowShowNotify");
    Boolean allowShowPageWhenScreenLock = call.argument("allowShowPageWhenScreenLock");
    Boolean debug = call.argument("debug");
    Boolean supportMultiProcess = call.argument("supportMultiProcess");
    List<Integer> directDownloadNetworkType = call.argument("directDownloadNetworkType");
    if(useTextureView == null){
      useTextureView = false;
    }
    if (allowShowNotify == null){
      allowShowNotify = true;
    }
    if (allowShowPageWhenScreenLock == null){
      allowShowPageWhenScreenLock = true;
    }
    if (debug == null){
      debug = false;
    }
    if (supportMultiProcess == null){
      supportMultiProcess = false;
    }
    if (appId == null || appId.trim().isEmpty()){
      result.success(OceanResponse.error(400, "appId不能为null"));
      return;
    }
    if (appName == null || appName.trim().isEmpty()) {
      result.success(OceanResponse.error(400, "appName不能为null"));
      return;
    }
    try{
      AdManagerHolder.init(applicationContext, appId, useTextureView, appName, allowShowNotify, allowShowPageWhenScreenLock, debug, supportMultiProcess,directDownloadNetworkType);
      result.success(OceanResponse.success("success"));
    }catch (Exception e){
      result.success(OceanResponse.error(500, e.getMessage()));
    }
  }

}
