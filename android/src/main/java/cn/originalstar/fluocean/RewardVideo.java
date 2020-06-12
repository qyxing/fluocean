package cn.originalstar.fluocean;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;

import java.util.HashMap;
import java.util.Map;

import cn.originalstar.fluocean.utils.TToast;
import cn.originalstar.fluocean.vo.OceanResponse;
import io.flutter.plugin.common.MethodChannel;

public class RewardVideo implements TTAdNative.RewardVideoAdListener,
        TTRewardVideoAd.RewardAdInteractionListener, TTAppDownloadListener {
    private static final String TAG = "RewardVideo";
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;

    public String codeId = null;
    public Boolean supportDeepLink = true;
    public String rewardName = null;
    public int rewardAmount = 0;
    public double expressViewAcceptedSizeW = 500;
    public double expressViewAcceptedSizeH = 500;
    public String userID = "user123";
    public String mediaExtra = "media_extra";
    public int orientation = TTAdConstant.HORIZONTAL;

    public boolean isExpress = false; //是否请求模板广告
    public boolean debug = true;
    public static MethodChannel _channel;
    public Context context;
    public Activity activity;

    public void init() {
        //step1:初始化sdk
        TTAdManager ttAdManager = AdManagerHolder.get();
        //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,

        // 防止获取不了imei时候，下载类广告没有填充的问题。
        // AdManagerHolder.get().requestPermissionIfNecessary(context);

        //step3:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(context);

        loadAd((float) expressViewAcceptedSizeW,
                (float) expressViewAcceptedSizeH);
    }

    private boolean mHasShowDownloadActive = false;

    private void loadAd(float expressViewAcceptedSizeW,
                        float expressViewAcceptedSizeH) {
        AdSlot.Builder builder = new AdSlot.Builder().setCodeId(codeId)
                .setSupportDeepLink(supportDeepLink)
                .setRewardName(rewardName) //奖励的名称
                .setRewardAmount(rewardAmount)  //奖励的数量
                .setUserID(userID)//用户id,必传参数
                .setMediaExtra(mediaExtra) //附加参数，可选
                .setOrientation(orientation);
        if (isExpress) {
            builder.setExpressViewAcceptedSize(expressViewAcceptedSizeW,
                    expressViewAcceptedSizeH);
        }
        AdSlot adSlot = builder.build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, this);
    }

    private String getAdType(int type) {
        switch (type) {
            case TTAdConstant.AD_TYPE_COMMON_VIDEO:
                return "普通激励视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE_VIDEO:
                return "Playable激励视频，type=" + type;
            case TTAdConstant.AD_TYPE_PLAYABLE:
                return "纯Playable，type=" + type;
        }
        return "未知类型+type=" + type;
    }

    private void showToast(String msg) {
        Log.d(TAG, msg);
        if (debug) {
            TToast.show(context, msg);
        }
    }

    /**
     * RewardVideoAdListener
     */
    @Override
    public void onError(int code, String message) {
        Log.e(TAG, "onError: " + code + ", " + String.valueOf(message));
        showToast(message);
    }

    //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
    @Override
    public void onRewardVideoCached() {
        showToast("rewardVideoAd video cached");
        mttRewardVideoAd.showRewardVideoAd(activity,
                TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test");
        mttRewardVideoAd = null;
    }

    //视频广告的素材加载完毕，比如视频url等，在此回调后，
    // 可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
    @Override
    public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
        showToast("rewardVideoAd loaded 广告类型："
                + getAdType(ad.getRewardVideoAdType()));
        mttRewardVideoAd = ad;
        mttRewardVideoAd.setRewardAdInteractionListener(this);
        mttRewardVideoAd.setDownloadListener(this);
    }

    /**
     * setRewardAdInteractionListener
     */
    @Override
    public void onAdShow() {
        showToast("rewardVideoAd show");
        _channel.invokeMethod(
                "onRewardResponse",
                OceanResponse.success("rendered"));
    }

    @Override
    public void onAdVideoBarClick() {
        showToast("rewardVideoAd bar click");
    }

    @Override
    public void onAdClose() {
        showToast("rewardVideoAd close");
    }

    //视频播放完成回调
    @Override
    public void onVideoComplete() {
        showToast("rewardVideoAd complete");
    }

    @Override
    public void onVideoError() {
        showToast("rewardVideoAd error");
    }

    //视频播放完成后，奖励验证回调，
    // rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
    @Override
    public void onRewardVerify(boolean verify, int amount, String name) {
        showToast("verify:" + verify + " amount:" + amount +
                " name:" + name);
        _channel.invokeMethod(
                "onRewardResponse",
                OceanResponse.reward(0,
                        "success", verify, amount, name));
    }

    @Override
    public void onSkippedVideo() {
        showToast("rewardVideoAd has onSkippedVideo");
    }

    /**
     * TTAppDownloadListener
     */
    @Override
    public void onIdle() {
        mHasShowDownloadActive = false;
    }

    @Override
    public void onDownloadActive(long totalBytes, long currBytes,
                                 String fileName, String appName) {
        if (debug) {
            Log.d("DML", "onDownloadActive==totalBytes="
                    + totalBytes + ",currBytes=" + currBytes
                    + ",fileName=" + fileName + ",appName=" + appName);
        }
        if (!mHasShowDownloadActive) {
            mHasShowDownloadActive = true;
            showToast("下载中，点击下载区域暂停");
        }
    }

    @Override
    public void onDownloadPaused(long totalBytes, long currBytes,
                                 String fileName, String appName) {
        if (debug) {
            Log.d("DML", "onDownloadPaused===totalBytes="
                    + totalBytes + ",currBytes=" + currBytes + ",fileName="
                    + fileName + ",appName=" + appName);
            TToast.show(context, "下载暂停，点击下载区域继续",
                    Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onDownloadFailed(long totalBytes, long currBytes,
                                 String fileName, String appName) {
        if (debug) {
            Log.d("DML", "onDownloadFailed==totalBytes=" +
                    totalBytes + ",currBytes=" + currBytes + ",fileName="
                    + fileName + ",appName=" + appName);
            TToast.show(context, "下载失败，点击下载区域重新下载",
                    Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onDownloadFinished(long totalBytes,
                                   String fileName, String appName) {
        if (debug) {
            Log.d("DML", "onDownloadFinished==totalBytes="
                    + totalBytes + ",fileName=" + fileName
                    + ",appName=" + appName);
            TToast.show(context, "下载完成，点击下载区域重新下载",
                    Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onInstalled(String fileName, String appName) {
        if (debug) {
            Log.d("DML", "onInstalled=="
                    + ",fileName=" + fileName + ",appName=" + appName);
            TToast.show(context, "安装完成，点击下载区域打开",
                    Toast.LENGTH_LONG);
        }
    }

}
