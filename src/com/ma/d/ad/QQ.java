package com.ma.d.ad;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;

public class QQ implements AdAdapter {

	private static final String TAG = QQ.class.getSimpleName();
	private static long EXIT_TIME = 2000;
	public static QQ mInstance = null;

	public String app_id = "";
	public String banner_pos_id = "";
	public String interstitial_pos_id = "";

	private SharedPreferences preferences = null;

	public static QQ getInstance(String app_key, String banner_pos_key,
			String interstitial_pos_key) {
		try {
			Class.forName("com.qq.e.comm.managers.GDTADManager");
		} catch (Exception e) {
			return null;
		}
		if (null == mInstance) {
			mInstance = new QQ(app_key, banner_pos_key, interstitial_pos_key);
		}
		return mInstance;
	}

	public QQ(String app_key, String banner_pos_key, String interstitial_pos_key) {
		this.app_id = app_key;
		this.banner_pos_id = banner_pos_key;
		this.interstitial_pos_id = interstitial_pos_key;
	}

	@Override
	public void init(Activity act) {

	}

	@Override
	public void showBanner(Activity act, int position) {
		if (act == null) {
			return;
		}

		if (TextUtils.isEmpty(app_id) || TextUtils.isEmpty(banner_pos_id)) {
			return;
		}

		// 创建Banner广告AdView对象
		// appId : 在 http://e.qq.com/dev/ 能看到的app唯一字符串
		// posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
		BannerView adView = new BannerView(act, ADSize.BANNER, app_id,
				banner_pos_id);
		// 设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
		adView.setRefresh(30);
		adView.setADListener(new AbstractBannerADListener() {

			@Override
			public void onNoAD(int arg0) {
				Log.e(TAG, "failed" + arg0);
			}

			@Override
			public void onADReceiv() {
				Log.e(TAG, "success");
			}

			@Override
			public void onADClicked() {
				super.onADClicked();
			}

			@Override
			public void onADExposure() {
				super.onADExposure();
			}

		});
		/* 发起广告请求，收到广告数据后会展示数据 */
		adView.loadAD();

		LinearLayout layout = new LinearLayout(act);
		layout.setId(position);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = getGravity(position);
		layout.addView(adView, params);

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		act.addContentView(layout, layoutParams);
	}

	@Override
	public void showInterstitial(Activity act) {
		if (act == null) {
			return;
		}

		if (TextUtils.isEmpty(app_id) || TextUtils.isEmpty(interstitial_pos_id)) {
			return;
		}

		final InterstitialAD iad = new InterstitialAD(act, app_id,
				interstitial_pos_id);
		iad.setADListener(new AbstractInterstitialADListener() {

			@Override
			public void onNoAD(int arg0) {
				Log.e(TAG, "i failed");
			}

			@Override
			public void onADReceive() {
				Log.e(TAG, "i success");
				/*
				 * 展示插屏广告，仅在回调接口的adreceive事件发生后调用才有效。
				 */
				iad.show();

			}

			@Override
			public void onADClicked() {
				super.onADClicked();
			}

		});
		// 请求插屏广告，每次重新请求都可以调用此方法。
		iad.loadAD();
	}

	@Override
	public boolean exit(Activity act, boolean show, int k, KeyEvent e) {
		if (act == null) {
			return false;
		}
		if (k == KeyEvent.KEYCODE_BACK && e.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - EXIT_TIME) > 2000) {
				Toast.makeText(act, getExitToast(act), Toast.LENGTH_SHORT)
						.show();
				if (show) {
					showInterstitial(act);
				}
				EXIT_TIME = System.currentTimeMillis();
			} else {
				act.finish();
				System.exit(0);
			}
			return true;
		}
		return false;
	}

	private String getExitToast(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))
			return "再按一次退出程序";
		else
			return "Press again to exit";
	}

	/**
	 * @param position
	 * @return
	 * @description 控制当前banner广告展示位置
	 */
	private int getGravity(int position) {
		switch (position) {
		case 1:
			return Gravity.TOP | Gravity.LEFT;
		case 2:
			return Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		case 3:
			return Gravity.TOP | Gravity.RIGHT;
		case 4:
			return Gravity.CENTER_VERTICAL | Gravity.LEFT;
		case 5:
			return Gravity.CENTER;
		case 6:
			return Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		case 7:
			return Gravity.BOTTOM | Gravity.LEFT;
		case 8:
			return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		case 9:
			return Gravity.BOTTOM | Gravity.RIGHT;
		}
		return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
	}

	@Override
	public View getBanner(Activity act) {
		BannerView adView = new BannerView(act, ADSize.BANNER, app_id,
				banner_pos_id);
		// 设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
		adView.setRefresh(0);
		adView.setADListener(new AbstractBannerADListener() {

			@Override
			public void onNoAD(int arg0) {
				Log.e(TAG, "failed" + arg0);
			}

			@Override
			public void onADReceiv() {
				Log.e(TAG, "success");
			}

			@Override
			public void onADClicked() {
				super.onADClicked();
			}

			@Override
			public void onADExposure() {
				super.onADExposure();
			}

		});
		/* 发起广告请求，收到广告数据后会展示数据 */
		adView.loadAD();
		return adView;
	}

}
