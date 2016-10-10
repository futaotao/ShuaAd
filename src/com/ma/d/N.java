package com.ma.d;

import java.util.Comparator;

import android.net.wifi.ScanResult;

public class N implements Comparator<ScanResult> {
	public int compare(ScanResult paramScanResult1, ScanResult paramScanResult2) {
		return paramScanResult2.level - paramScanResult1.level;
	}
}
