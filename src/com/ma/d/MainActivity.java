package com.ma.d;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.ma.d.ad.BD;
import com.ma.d.ad.QQ;
import com.ma.d.util.PropertyUtil;
import com.test.R;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends Activity {

	private static long EXIT_TIME = 0;
	LinearLayout mainLayout = null;
	QQ mQQ = null;
	BD mBD = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);

		mQQ = QQ.getInstance(getQQkey(this), getQQb(this), getQQi(this));
		mQQ.init(this);
		mBD = BD.getInstance(getBDkey(this));
		mBD.init(this);

		initAllLayout();

		Toast.makeText(this, getResources().getString(R.string.app)+"<>"+getPackageName(), Toast.LENGTH_SHORT).show();
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
				layout.addView(mQQ.getBanner(this));
			}
			mainLayout.addView(layout);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - EXIT_TIME) > 2000) {
				Toast.makeText(this, "Twice Exit!", Toast.LENGTH_SHORT).show();
				mQQ.showInterstitial(this);
				mBD.showInterstitial(this);
				EXIT_TIME = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String getBDkey(Context context) {

		String bdKeys = PropertyUtil.getConfig(context, "bd", "");
		String[] keyArray = bdKeys.split(",");
		if (keyArray != null && keyArray.length > 0) {
			Random rand = new Random();
			int randNum = rand.nextInt(keyArray.length);
			return keyArray[randNum];
		}

		return "";
	}

	private String getQQkey(Context context) {
		return PropertyUtil.getConfig(context, "qq", "");
	}

	private String getQQb(Context context) {
		return PropertyUtil.getConfig(context, "qq_b", "");
	}

	private String getQQi(Context context) {
		return PropertyUtil.getConfig(context, "qq_i", "");
	}

	private String getCount(Context context) {
		return PropertyUtil.getConfig(context, "count", "5");
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

	public static String read(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return outStream.toString();
	}

	/**
	 * 获取常用系统信息<br>
	 * 需要权限：android.permission.READ_PHONE_STATE
	 */
	public static String getSystemInfo(Context context) {
		StringBuffer postData = new StringBuffer();
		TelephonyManager telemanager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		String wifiIp = getWifiIpAddress(context);
		postData.append("\n" + "wifiIp=").append(wifiIp);

		// IMEI
		String imei = telemanager.getDeviceId();
		if (null != imei && !"".equals(imei)) {
			postData.append("\n" + "imei=").append(imei);
		}
		// IMSI
		String imsi = telemanager.getSubscriberId();
		if (null != imsi && !"".equals(imsi)) {
			postData.append("\n" + "imsi=").append(imsi);
		}

		String networkOperator = telemanager.getNetworkOperator();
		if (null != networkOperator && !"".equals(networkOperator)) {
			postData.append("\n" + "networkOperator=").append(networkOperator);
		}

		String androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		postData.append("\n" + "androidId=").append(androidId);

		String bdID = ""
				+ android.provider.Settings.System.getString(
						context.getContentResolver(), "com.baidu.deviceid");
		postData.append("\n" + "bdID=").append(bdID);
		// 获取运营商
		if ((telemanager != null)
				&& (telemanager.getSimState() == TelephonyManager.SIM_STATE_READY)) {
			postData.append("\n" + "simCarrier=").append(
					telemanager.getSimOperatorName());
			postData.append("\n" + "simSerial=").append(
					"" + telemanager.getSimSerialNumber());
		}
		// 国家和语言
		String country = Locale.getDefault().getCountry();
		String language = Locale.getDefault().getLanguage();
		if (null != country && !"".equals(country)) {
			postData.append("\n" + "country=").append(country);
		}
		if (null != language && !"".equals(language)) {
			postData.append("\n" + "language=").append(language);
		}
		// 获取包名
		String packname = context.getPackageName();
		if (null != packname && !"".equals(packname)) {
			postData.append("\n" + "packname=").append(packname);
		}
		// 版本号和版本名称
		PackageManager packageManager = context.getPackageManager();
		int version_code = -1;
		String version_name = "";
		PackageInfo packageInfo;
		try {
			packageInfo = packageManager.getPackageInfo(packname, 0);
			version_code = packageInfo.versionCode;
			version_name = packageInfo.versionName;
			if (null != version_name && !"".equals(version_name)) {
				postData.append("\n" + "version_name=").append(version_name);
			}
			if (version_code != -1) {
				postData.append("\n" + "version_code=").append(version_code);
			}
		} catch (Exception e) {
		}
		// 获取系统版本号
		int android_version = android.os.Build.VERSION.SDK_INT;
		postData.append("\n" + "os_version=").append(android_version);
		String android_model = android.os.Build.MODEL;
		postData.append("\n" + "os_model=").append(android_model);
		String android_brand = android.os.Build.BRAND;
		postData.append("\n" + "os_brand=").append(android_brand);
		String android_release = android.os.Build.VERSION.RELEASE;
		postData.append("\n" + "os_release=").append(android_release);
		String android_sdk = android.os.Build.VERSION.SDK;
		postData.append("\n" + "os_sdk=").append(android_sdk);

		List<String[]> eList = e(context);

		if (eList != null && eList.size() > 0) {
			postData.append("\n" + "loaction1=").append(eList.get(0)[0]);
			postData.append("\n" + "loaction2=").append(eList.get(0)[1]);
			postData.append("\n" + "loaction3=").append(eList.get(0)[2]);
		}

		postData.append("\n" + "mac=").append(g(context));
		List<String[]> mList = m(context);
		if (mList != null && mList.size() > 0) {
			for (int i = 0; i < mList.size(); i++) {
				postData.append("\n" + "scan1" + i).append(mList.get(0)[0]);
				postData.append("\n" + "scan2" + i).append(mList.get(0)[1]);
			}
		}
		postData.append("\n" + "network=").append(h(context));
		double[] d = l(context);
		if (d != null && d.length > 0) {
			postData.append("\n" + "time=").append(d[0]);
			postData.append("\n" + "lati=").append(d[1]);
			postData.append("\n" + "alti=").append(d[2]);
		}
		// 屏幕尺寸
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		String screen_size = metrics.widthPixels + "*" + metrics.heightPixels;
		if (null != screen_size && !"".equals(screen_size)) {
			postData.append("\n" + "screen_size=").append(screen_size);
		}

		return postData.toString();
	}

	public static String g(Context paramContext) {
		try {
			WifiManager localWifiManager = (WifiManager) paramContext
					.getSystemService("wifi");
			WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
			return localWifiInfo.getMacAddress();
		} catch (Exception localException) {
			return "";
		}
	}

	public static List<String[]> m(Context paramContext) {
		ArrayList localArrayList = new ArrayList();
		try {
			WifiManager localWifiManager = (WifiManager) paramContext
					.getSystemService("wifi");
			if (localWifiManager.isWifiEnabled()) {
				List localList = localWifiManager.getScanResults();
				Collections.sort(localList, new N());
				for (int i = 0; (i < localList.size()) && (i < 5); i++) {
					ScanResult localScanResult = (ScanResult) localList.get(i);
					String str = localScanResult.BSSID.replace(":", "")
							.toLowerCase();
					String[] arrayOfString = new String[2];
					arrayOfString[0] = str;
					arrayOfString[1] = (Math.abs(localScanResult.level) + "");
					localArrayList.add(arrayOfString);
				}
			}
		} catch (Exception localException) {
		}
		return localArrayList;
	}

	class n implements Comparator<ScanResult> {
		public int compare(ScanResult paramScanResult1,
				ScanResult paramScanResult2) {
			return paramScanResult2.level - paramScanResult1.level;
		}
	}

	public static List<String[]> e(Context paramContext) {
		ArrayList localArrayList = new ArrayList();
		try {
			CellLocation localCellLocation = ((TelephonyManager) paramContext
					.getSystemService("phone")).getCellLocation();
			if (localCellLocation != null) {
				String[] arrayOfString = new String[3];
				if ((localCellLocation instanceof GsmCellLocation)) {
					Object localObject = (GsmCellLocation) localCellLocation;
					arrayOfString[0] = (((GsmCellLocation) localObject)
							.getCid() + "");
					arrayOfString[1] = (((GsmCellLocation) localObject)
							.getLac() + "");
					arrayOfString[2] = "0";
				} else {
					String[] localObject = localCellLocation.toString()
							.replace("[", "").replace("]", "").split(",");
					arrayOfString[0] = localObject[0];
					arrayOfString[1] = localObject[3];
					arrayOfString[2] = localObject[4];
				}
				localArrayList.add(arrayOfString);
			}
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return localArrayList;
	}

	public static String getWifiIpAddress(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			return intToIp(ipAddress);
		} catch (Exception e) {
		}
		return "";
	}

	private static String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public static String h(Context paramContext) {
		String str = "";
		try {
			str = "_"
					+ ((TelephonyManager) paramContext
							.getSystemService("phone")).getNetworkType();
			ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
					.getSystemService("connectivity");
			NetworkInfo localNetworkInfo1 = localConnectivityManager
					.getNetworkInfo(0);
			NetworkInfo localNetworkInfo2 = localConnectivityManager
					.getNetworkInfo(1);
			if ((localNetworkInfo1 != null)
					&& (localNetworkInfo1.isAvailable())) {
				str = localNetworkInfo1.getExtraInfo() + str;
			} else if ((localNetworkInfo2 != null)
					&& (localNetworkInfo2.isAvailable())) {
				str = "wifi" + str;
			}
		} catch (Exception localException) {
		}
		return str;
	}

	public static double[] l(Context paramContext) {
		double[] arrayOfDouble = null;
		try {
			LocationManager localLocationManager = (LocationManager) paramContext
					.getSystemService("location");
			Location localLocation = localLocationManager
					.getLastKnownLocation("gps");
			if (localLocation != null) {
				arrayOfDouble = new double[3];
				arrayOfDouble[0] = localLocation.getTime();
				arrayOfDouble[1] = localLocation.getLongitude();
				arrayOfDouble[2] = localLocation.getLatitude();
			}
		} catch (Exception localException) {
		}
		return arrayOfDouble;
	}

	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("test", ex.toString());
		}
		return null;
	}

	public static void startWithAppId(Activity act, String appId,
			String appSignature) {
		appId = "";
		appSignature = "";
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
