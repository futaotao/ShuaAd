package com.ma.d.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Properties;

import android.content.Context;
import android.util.SparseArray;

public class PropertyUtil {

	private static SparseArray<SoftReference<Properties>> mSparseArr = new SparseArray<SoftReference<Properties>>();

	public static String getConfig(Context context, String name, String defval) {
		return getProperty(context, name, defval, "m/a/config");
	}

	private static String getProperty(Context context, String name,
			String defval, String filepath) {
		synchronized (PropertyUtil.class) {
			SoftReference<Properties> softRef = mSparseArr.get(filepath
					.hashCode());
			if (null == softRef || null == softRef.get()) {
				try {
					InputStream is;
					Properties prop = new Properties();
					is = context.getAssets().open(filepath);
					prop.load(is);
					is.close();
					softRef = new SoftReference<Properties>(prop);
					mSparseArr.put(filepath.hashCode(), softRef);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return softRef.get().getProperty(name, defval).trim();
		}
	}
}
