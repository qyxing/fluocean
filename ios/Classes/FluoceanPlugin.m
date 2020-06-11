#import "FluoceanPlugin.h"
#if __has_include(<fluocean/fluocean-Swift.h>)
#import <fluocean/fluocean-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "fluocean-Swift.h"
#endif
#import <BUAdSDK/BUAdSDK.h>

@interface FluoceanPlugin ()<BUSplashAdDelegate>

@end

@implementation FluoceanPlugin

FlutterMethodChannel* globalMethodChannel;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    // [SwiftFluoceanPlugin registerWithRegistrar:registrar];
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"qyxing.cn/fluocean"
                                     binaryMessenger:[registrar messenger]];
    FluoceanPlugin* instance = [[FluoceanPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
    
    globalMethodChannel = channel;
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"getPlatformVersion" isEqualToString:call.method]) {
        result([NSString stringWithFormat:@"iOS %@",[[UIDevice currentDevice] systemVersion]]);
        return;
    }
    if ([@"registerPangolin" isEqualToString:call.method]) {
        [self registerPangolin: call result:result];
        return;
    }
    if ([@"loadSplashAd" isEqualToString:call.method]) {
        [self loadSplashAd: call result:result];
        return;
    }
    result(FlutterMethodNotImplemented);
}

// 注册
- (void)registerPangolin:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSString* appId = call.arguments[@"appId"];
    if ([@"" isEqualToString:appId]) {
        result(@{@"code":[NSNumber numberWithInt:400],@"message":@"参数错误"});
        return;
    }
    [BUAdSDKManager setAppID:appId];
    result(@{@"code":[NSNumber numberWithInt:0],@"message":@"success"});
}

// 开屏广告
- (void)loadSplashAd:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSString* mCodeId = call.arguments[@"codeId"];
    BOOL debug = call.arguments[@"debug"];
    [BUAdSDKManager setIsPaidApp:NO];
    if(debug){
        [BUAdSDKManager setLoglevel:BUAdSDKLogLevelDebug];
    }else{
        [BUAdSDKManager setLoglevel:BUAdSDKLogLevelError];
    }
    CGRect frame = [UIScreen mainScreen].bounds;
    BUSplashAdView *splashView = [[BUSplashAdView alloc] initWithSlotID:mCodeId frame:frame];
    splashView.delegate = self;
    UIWindow *keyWindow = [UIApplication sharedApplication].windows.firstObject;
    [splashView loadAdData];
    [keyWindow.rootViewController.view addSubview:splashView];
    splashView.rootViewController = keyWindow.rootViewController;
    
    result(@{@"code":[NSNumber numberWithInt:0],@"message":@"success"});
}

//展示视频用
- (UIViewController *)rootViewController{
    UIViewController *rootVC = [[UIApplication sharedApplication].delegate window].rootViewController;
    UIViewController *parent = rootVC;
    while((parent = rootVC.presentingViewController) != nil){
        rootVC = parent;
    }
    while ([rootVC isKindOfClass:[UINavigationController class]]) {
        rootVC = [(UINavigationController *)rootVC topViewController];
    }
    return rootVC;
}

// 开屏广告回调方法
- (void)splashAdDidLoad:(BUSplashAdView *)splashAd {
    NSLog(@"splashAdDidLoad");
    NSLog(@"mediaExt-%@",splashAd.mediaExt);
}

- (void)splashAdDidClose:(BUSplashAdView *)splashAd {
    NSLog(@"splashAdView AdDidClose");
    [splashAd removeFromSuperview];
}

- (void)splashAd:(BUSplashAdView *)splashAd didFailWithError:(NSError *)error {
    NSLog(@"splashAdView load data fail");
    [splashAd removeFromSuperview];
    NSLog(@"error code : %ld , error message : %@",(long)error.code,error.description);
}

- (void)splashAdDidCloseOtherController:(BUSplashAdView *)splashAd interactionType:(BUInteractionType)interactionType {
    NSString *str = @"";
    if (interactionType == BUInteractionTypePage) {
        str = @"ladingpage";
    } else if (interactionType == BUInteractionTypeVideoAdDetail) {
        str = @"videoDetail";
    } else {
        str = @"appstoreInApp";
    }
#pragma clang diagnostic push
#pragma clang diagnostic ignored"-Wdeprecated-declarations"
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:str message:[NSString stringWithFormat:@"%s",__func__] delegate:self cancelButtonTitle:nil otherButtonTitles:@"ok", nil];
    [alert show];
#pragma clang diagnostic pop
}

@end
