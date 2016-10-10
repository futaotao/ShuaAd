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

		// ����Banner���AdView����
		// appId : �� http://e.qq.com/dev/ �ܿ�����appΨһ�ַ���
		// posId : �� http://e.qq.com/dev/ ���ɵ����ִ������� appid ���� appkey
		BannerView adView = new BannerView(act, ADSize.BANNER, app_id,
				banner_pos_id);
		// ���ù���ֲ�ʱ�䣬Ϊ0��30~120֮������֣���λΪs,0��ʶ���Զ��ֲ�
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
		/* �����������յ�������ݺ��չʾ���� */
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
				 * չʾ������棬���ڻص��ӿڵ�adreceive�¼���������ò���Ч��
				 */
				iad.show();

			}

			@Override
			public void onADClicked() {
				super.onADClicked();
			}

		});
		// ���������棬ÿ���������󶼿��Ե��ô˷�����
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
			return "�ٰ�һ���˳�����";
		else
			return "Press again to exit";
	}

	/**
	 * @param position
	 * @return
	 * @description ���Ƶ�ǰbanner���չʾλ��
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
		// ���ù���ֲ�ʱ�䣬Ϊ0��30~120֮������֣���λΪs,0��ʶ���Զ��ֲ�
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
		/* �����������յ�������ݺ��չʾ���� */
		adView.loadAD();
		return adView;
	}

}
