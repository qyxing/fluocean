package cn.originalstar.fluocean;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;

import java.util.List;

public class AdManagerHolder {

    private static boolean sInit;


    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk还未初始化");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(Context context, String appId, Boolean useTextureView, String appName, Boolean allowShowNotify, Boolean allowShowPageWhenScreenLock, Boolean debug, Boolean supportMultiProcess, List<Integer> directDownloadNetworkType) {
        doInit(context, appId, useTextureView, appName, allowShowNotify, allowShowPageWhenScreenLock, debug, supportMultiProcess, directDownloadNetworkType);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(Context context, String appId, Boolean useTextureView, String appName, Boolean allowShowNotify, Boolean allowShowPageWhenScreenLock, Boolean debug, Boolean supportMultiProcess, List<Integer> directDownloadNetworkType) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context, appId, useTextureView, appName, allowShowNotify, allowShowPageWhenScreenLock, debug, supportMultiProcess, directDownloadNetworkType));
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(Context context, String appId, Boolean useTextureView, String appName, Boolean allowShowNotify, Boolean allowShowPageWhenScreenLock, Boolean debug, Boolean supportMultiProcess, List<Integer> directDownloadNetworkType) {

        int[] d = new int[directDownloadNetworkType.size()];
        for (int i = 0; i < directDownloadNetworkType.size(); i++) {
            d[i] = directDownloadNetworkType.get(i);
        }
        return new TTAdConfig.Builder()
                .appId(appId)
                .useTextureView(useTextureView)
                .appName(appName)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(allowShowNotify)
                .allowShowPageWhenScreenLock(allowShowPageWhenScreenLock)
                .debug(debug)
                .directDownloadNetworkType(d)
                .supportMultiProcess(supportMultiProcess)
                .needClearTaskReset()
                .build();
    }
}
