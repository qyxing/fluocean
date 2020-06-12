package cn.originalstar.fluocean;

import android.content.Context;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTCustomController;

import java.util.List;

public class AdManagerHolder {

    private static boolean sInit;


    public static TTAdManager get() {
        if (!sInit) {
            throw new RuntimeException("TTAdSdk还未初始化");
        }
        return TTAdSdk.getAdManager();
    }

    public static void init(
            Context context, String appId,  Boolean useTextureView,
            String appName, Boolean allowShowNotify,
            Boolean allowShowPageWhenScreenLock, Boolean debug,
            Boolean supportMultiProcess,
            List<Integer> directDownloadNetworkType,
            TTCustomController c) {
        if (!sInit) {
            TTAdSdk.init(
                    context,
                    buildConfig(context, appId, useTextureView, appName,
                            allowShowNotify, allowShowPageWhenScreenLock, debug,
                            supportMultiProcess, directDownloadNetworkType, c));
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(
            Context context, String appId, Boolean useTextureView,
            String appName, Boolean allowShowNotify,
            Boolean allowShowPageWhenScreenLock, Boolean debug,
            Boolean supportMultiProcess,
            List<Integer> directDownloadNetworkType, TTCustomController c) {
        int[] d = new int[directDownloadNetworkType.size()];
        for (int i = 0; i < directDownloadNetworkType.size(); i++) {
            d[i] = directDownloadNetworkType.get(i);
        }
        TTAdConfig.Builder builder =  new TTAdConfig.Builder()
                .appId(appId)
                .useTextureView(useTextureView)
                .appName(appName)
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(allowShowNotify)
                .allowShowPageWhenScreenLock(allowShowPageWhenScreenLock)
                .debug(debug)
                .directDownloadNetworkType(d)
                .supportMultiProcess(supportMultiProcess)
                .needClearTaskReset();
        builder.customController(c);
        return builder.build();
    }
}
