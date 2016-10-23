package com.ma.d.ad;

import android.app.Activity;
import android.view.View;

public interface AdAdapter {

	public void init(Activity act);

	public void showBanner(Activity act, int position);

	public View getBanner(Activity act);

	public void showInterstitial(Activity act);

}
