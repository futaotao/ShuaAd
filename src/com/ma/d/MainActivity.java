package com.ma.d;

import java.io.InputStream;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.ma.d.ad.BD;
import com.ma.d.util.PropertyUtil;
import com.test.R;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {

	private static long EXIT_TIME = 0;
	LinearLayout mainLayout = null;

//	QQ mQQ = null;
	
	BD mBD = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);

//		mQQ = QQ.getInstance(getQQkey(this), getQQb(this), getQQi(this));
//		mQQ.init(this);
		mBD = BD.getInstance(getBDkey(this),getBDb(this), getBDi(this));
		mBD.init(this);

		initAllLayout();

		Toast.makeText(
				this,
				getResources().getString(R.string.app) + "<>"
						+ getPackageName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initAllLayout() {
		int count = Integer.parseInt(getCount(this));
		LinearLayout layout = null;
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		for (int i = 0; i < count; i++) {
			layout = new LinearLayout(this);
			layout.setLayoutParams(params);
			layout.setGravity(Gravity.CENTER);
			if (i % 2 == 0) {
			    layout.setBackgroundColor(Color.BLACK);
				layout.addView(mBD.getBanner(this));
			} else {
			    layout.setBackgroundColor(Color.WHITE);
//				layout.addView(mQQ.getBanner(this));
			}
			// layout.setId(i);
			// layout.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// String msg = "click " + v.getId() + " item!";
			// Log.e("test", "click " + v.getId() + " item!");
			// Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT)
			// .show();
			// }
			// });
			mainLayout.addView(layout);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - EXIT_TIME) > 2000) {
				Toast.makeText(this, "Twice Exit!", Toast.LENGTH_SHORT).show();
//				mQQ.showInterstitial(this);
				// mBD.showInterstitial(this);
				EXIT_TIME = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * @desc bd key
	 * @param context
	 * @return
	 */
	private String getBDkey(Context context) {
		return PropertyUtil.getConfig(context, "bd", "");
	}
	
	/**
	 * @desc bd banner key
	 * @param context
	 * @return
	 */
	private String getBDb(Context context) {
		return PropertyUtil.getConfig(context, "bd_b", "");
	}
	
	/**
	 * @desc bd interstitial key
	 * @param context
	 * @return
	 */
	private String getBDi(Context context) {
		return PropertyUtil.getConfig(context, "bd_i", "");
	}

	/**
	 * @desc qq key
	 * @param context
	 * @return
	 */
	private String getQQkey(Context context) {
		return PropertyUtil.getConfig(context, "qq", "");
	}

	/**
	 * @desc qq banner key
	 * @param context
	 * @return
	 */
	private String getQQb(Context context) {
		return PropertyUtil.getConfig(context, "qq_b", "");
	}

	/**
	 * @desc qq interstitial key
	 * @param context
	 * @return
	 */
	private String getQQi(Context context) {
		return PropertyUtil.getConfig(context, "qq_i", "");
	}

	private String getCount(Context context) {
		return PropertyUtil.getConfig(context, "count", "20");
	}

	public static Properties getProperty(InputStream is) {
		Properties properties = new Properties();
		if (is != null) {
			try {
				properties.load(is);
				return properties;
			} catch (Exception e) {
			}
		}
		return null;
	}
}
