package com.simple.leakfortest;

import android.util.Log;

import com.squareup.leakcanary.AbstractAnalysisResultService;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.CanaryLog;
import com.squareup.leakcanary.HeapDump;
import com.squareup.leakcanary.LeakCanary;

import static com.simple.leakfortest.StorageUtils.saveResult;

/**
 * Created by mrsimple on 9/3/17.
 */
public class LeakDumpService extends AbstractAnalysisResultService {

    @Override
    protected final void onHeapAnalyzed(HeapDump heapDump, AnalysisResult result) {
        Log.e("", "*** onHeapAnalyzed in onHeapAnalyzed ");
        String leakInfo = LeakCanary.leakInfo(this, heapDump, result, false);
        CanaryLog.d(leakInfo);
        // save leak info
        saveResult(leakInfo);
    }
}