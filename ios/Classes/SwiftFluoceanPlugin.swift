import Flutter
import UIKit
import BUAdSDK


public class SwiftFluoceanPlugin: NSObject, FlutterPlugin {
    
    public static var channel:FlutterMethodChannel!;
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        channel = FlutterMethodChannel(name: "qyxing.cn/fluocean", binaryMessenger: registrar.messenger())
        let instance = SwiftFluoceanPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if("getPlatformVersion" == call.method){
            result("iOS " + UIDevice.current.systemVersion)
            return;
        }
        if("registerPangolin" == call.method){
            registerPangolin(call: call, result: result);
            return;
        }
        if("loadSplashAd" == call.method){
            loadSplashAd(call: call, result: result);
            return;
        }
        
        result(FlutterMethodNotImplemented);
    }
    
    // 注册
    private func registerPangolin(call: FlutterMethodCall, result: @escaping FlutterResult){
        let args: NSDictionary = call.arguments as! NSDictionary
        let appId = args["appId"] as! String;
        
        BUAdSDKManager.setAppID(appId);
        result(success(message: "success"));
    }
    
    // 开屏广告
    private func loadSplashAd(call: FlutterMethodCall, result: @escaping FlutterResult){
        result(success(message: "success"));
    }
    
    private func success(message: String)  -> Dictionary<String, Any> {
        let d:[String: Any] = ["code":0, "message":message];
        return d;
    }
    
    private func fail(code: Int, message: String)  -> Dictionary<String, Any> {
        let d:[String: Any] = ["code":code, "message":message];
        return d;
    }
    
}
