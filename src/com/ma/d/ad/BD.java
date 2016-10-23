package com.ma.d.ad;

import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.baidu.mobads.BaiduManager;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;

public class BD implements AdAdapter {

	private static final String TAG = BD.class.getSimpleName();
	private static long EXIT_TIME = 2000;
	public static BD mInstance = null;

	public String bd_key = "";
	public String bd_banner = "";
	public String bd_interstitial = "";
	private SharedPreferences preferences = null;

	public static BD getInstance(String key, String banner, String intertitial) {
		try {
			Class.forName("com.baidu.mobads.BaiduManager");
		} catch (Exception e) {
			return null;
		}
		if (null == mInstance) {
			mInstance = new BD(key, banner, intertitial);
		}
		return mInstance;
	}

	public BD(String key, String banner, String intertitial) {
		this.bd_key = key;
		this.bd_banner = banner;
		this.bd_interstitial = intertitial;
	}

	@Override
	public void init(Activity act) {
		if (act == null) {
			return;
		}
		BaiduManager.init(act);
		AdView.setAppSid(act, bd_key);
	}

	@Override
	public void showBanner(Activity act, int position) {
		if (act == null) {
			return;
		}

		AdView adView = new AdView(act, bd_banner);
		adView.setListener(new AdViewListener() {

			@Override
			public void onAdSwitch() {
				Log.e(TAG, "switch");
			}

			@Override
			public void onAdShow(JSONObject arg0) {
				Log.e(TAG, "success");
			}

			@Override
			public void onAdReady(AdView arg0) {
				Log.e(TAG, "ready");
			}

			@Override
			public void onAdFailed(String arg0) {
				Log.e(TAG, "failed");
			}

			@Override
			public void onAdClose(JSONObject arg0) {
				Log.e(TAG, "close");
			}

			@Override
			public void onAdClick(JSONObject arg0) {
				Log.e(TAG, "click");
			}

		});

		RelativeLayout layout = new RelativeLayout(act);
		layout.setId(position);
		layout.addView(adView, getLayoutParams(act, position));
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		act.addContentView(layout, layoutParams);
	}

	@Override
	public void showInterstitial(final Activity act) {
		if (act == null) {
			return;
		}

		final InterstitialAd iad = new InterstitialAd(act, bd_interstitial);
		iad.setListener(new InterstitialAdListener() {

			@Override
			public void onAdReady() {
				Log.e(TAG, "i success");
				iad.showAd(act);
			}

			@Override
			public void onAdPresent() {

			}

			@Override
			public void onAdFailed(String arg0) {
				Log.e(TAG, "i failed:" + arg0);

			}

			@Override
			public void onAdDismissed() {

			}

			@Override
			public void onAdClick(InterstitialAd arg0) {
			}
		});
		iad.loadAd();
	}

	private static RelativeLayout.LayoutParams getLayoutParams(Activity act,
			int position) {
		RelativeLayout.LayoutParams bannerLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		switch (position) {
		case 1:
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			break;
		case 2:
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			break;
		case 3:
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			break;
		case 4:
			bannerLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			break;
		case 5:
			bannerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			break;
		case 6:
			bannerLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			break;
		case 7:
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			break;
		case 8:
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			bannerLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			break;
		case 9:
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			bannerLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			break;
		}
		return bannerLayoutParams;
	}

	@Override
	public View getBanner(Activity act) {
		AdView adView = new AdView(act, bd_banner);
		adView.setListener(new AdViewListener() {

			@Override
			public void onAdSwitch() {
				Log.e(TAG, "switch");
			}

			@Override
			public void onAdShow(JSONObject arg0) {
				Log.e(TAG, "success");
			}

			@Override
			public void onAdReady(AdView arg0) {
				Log.e(TAG, "ready");
			}

			@Override
			public void onAdFailed(String arg0) {
				Log.e(TAG, "failed");
			}

			@Override
			public void onAdClose(JSONObject arg0) {
				Log.e(TAG, "close");
			}

			@Override
			public void onAdClick(JSONObject arg0) {
				Log.e(TAG, "click");
			}

		});

		return adView;
	}

}
