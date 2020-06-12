package cn.originalstar.fluocean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;

import cn.originalstar.fluocean.utils.TToast;
import cn.originalstar.fluocean.utils.UIUtils;

public class SplashActivity extends Activity implements
        TTAdNative.SplashAdListener, TTSplashAd.AdInteractionListener,
        TTAppDownloadListener {

    private static final String TAG = "SplashActivity";

    private TTAdNative mTTAdNative;
    private FrameLayout splashContainer;

    private boolean forceGoMain;

    private static final int AD_TIME_OUT = 5000;

    private String codeId = "";
    private Boolean debug = true;
    private boolean isExpress = false;
    private boolean mHasLoaded = false;

    boolean hasShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mTTAdNative = AdManagerHolder.get().createAdNative(this);
        splashContainer = (FrameLayout) findViewById(R.id.splash_container);
        Intent intent = getIntent();
        codeId = intent.getStringExtra("codeId");
        debug = intent.getBooleanExtra("debug", false);
        getExtraInfo();
        loadSplashAd();
    }

    @Override
    protected void onResume() {
        //判断是否该跳转到主页面
        if (forceGoMain) {
            goToMainActivity();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        forceGoMain = true;
        super.onStop();
    }

    private void loadSplashAd() {
        AdSlot.Builder builder = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920);
        if (isExpress) {
            float expressViewWidth = UIUtils.getScreenWidthDp(this);
            float expressViewHeight = UIUtils.getHeight(this);
            builder.setExpressViewAcceptedSize(
                    expressViewWidth, expressViewHeight);
        }
        AdSlot adSlot = builder.build();
        mTTAdNative.loadSplashAd(adSlot, this, AD_TIME_OUT);
    }

    private void getExtraInfo() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String codeId = intent.getStringExtra("splash_rit");
        if (!TextUtils.isEmpty(codeId)) {
            this.codeId = codeId;
        }
        isExpress = intent.getBooleanExtra("is_express", false);
    }

    private void goToMainActivity() {
        splashContainer.removeAllViews();
        this.finish();
    }

    private void showToast(String msg) {
        Log.d(TAG, msg);
        if (debug) {
            TToast.show(this, msg);
        }
    }


    @Override
    public void onError(int i, String s) {
        mHasLoaded = true;
        showToast(s);
        goToMainActivity();
    }

    @Override
    public void onTimeout() {
        showToast("开屏广告加载超时");
        mHasLoaded = true;
        goToMainActivity();
    }

    @Override
    public void onSplashAdLoad(TTSplashAd ad) {
        mHasLoaded = true;
        if (ad == null) {
            return;
        }
        //获取SplashView
        View view = ad.getSplashView();
        if (view != null && splashContainer != null
                && !SplashActivity.this.isFinishing()) {
            splashContainer.removeAllViews();
            splashContainer.addView(view);
            //ad.setNotAllowSdkCountdown();
        } else {
            goToMainActivity();
            return;
        }
        ad.setSplashInteractionListener(this);
        if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            ad.setDownloadListener(this);
        }
    }

    @Override
    public void onAdClicked(View view, int i) {
        showToast("开屏广告点击");
    }

    @Override
    public void onAdShow(View view, int i) {
        showToast("开屏广告展示");
    }

    @Override
    public void onAdSkip() {
        showToast("开屏广告跳过");
        goToMainActivity();
    }

    @Override
    public void onAdTimeOver() {
        showToast("开屏广告倒计时结束");
        goToMainActivity();
    }

    @Override
    public void onIdle() {

    }

    @Override
    public void onDownloadActive(long l, long l1, String s, String s1) {
        if (!hasShow) {
            hasShow = true;
            showToast("下载中...");
        }
    }

    @Override
    public void onDownloadPaused(long l, long l1, String s, String s1) {
        showToast("下载暂停...");
    }

    @Override
    public void onDownloadFailed(long l, long l1, String s, String s1) {
        showToast("下载失败...");
    }

    @Override
    public void onDownloadFinished(long l, String s, String s1) {
        showToast("下载完成...");
    }

    @Override
    public void onInstalled(String s, String s1) {
        showToast("安装完成...");
    }
}
