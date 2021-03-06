package com.zlm.hp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import java.util.Date;

import base.utils.LoggerUtil;

/**
 * 下载音频广播监听
 * Created by zhangliangming on 2017/9/9.
 */

public class DownloadAudioReceiver {

    private static final String base_action = "com.zlm.hp";
    private LoggerUtil logger;
    /**
     * 是否注册成功
     */
    private boolean isRegisterSuccess = false;
    private Context mContext;

    /**
     * 注册成功广播
     */
    private String ACTION_DOWMLOADMUSICSUCCESS = base_action + ".download.music.success_" + new Date().getTime();
    public static final String ACTION_DOWMLOADMUSICWAIT = base_action + ".download.music.wait";
    public static final String ACTION_DOWMLOADMUSICDOWNLOADING = base_action + ".download.music.downloading";
    public static final String ACTION_DOWMLOADMUSICDOWNPAUSE = base_action + ".download.music.pause";
    public static final String ACTION_DOWMLOADMUSICDOWNCANCEL = base_action + ".download.music.cancel";
    public static final String ACTION_DOWMLOADMUSICDOWNERROR = base_action + "download.music.error";
    public static final String ACTION_DOWMLOADMUSICDOWNFINISH = base_action + ".download.music.finish";
    public static final String ACTION_DOWMLOADMUSICDOWNADD = base_action + ".download.music.add";

    private BroadcastReceiver mDownloadAudioBroadcastReceiver;
    private IntentFilter mDownloadAudioIntentFilter;
    private DownloadAudioReceiverListener mDownloadAudioReceiverListener;

    public DownloadAudioReceiver(Context context) {
        this.mContext = context;
        logger = LoggerUtil.getZhangLogger(context);

        //
        mDownloadAudioIntentFilter = new IntentFilter();
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICSUCCESS);

        //
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICWAIT);
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICDOWNLOADING);
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICDOWNPAUSE);
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICDOWNCANCEL);
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICDOWNERROR);
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICDOWNFINISH);
        mDownloadAudioIntentFilter.addAction(ACTION_DOWMLOADMUSICDOWNADD);
    }


    /**
     *
     */
    private Handler mDownloadAudioHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (mDownloadAudioReceiverListener != null) {
                Intent intent = (Intent) msg.obj;

                if (intent.getAction().equals(ACTION_DOWMLOADMUSICSUCCESS)) {
                    isRegisterSuccess = true;
                } else {
                    mDownloadAudioReceiverListener.onReceive(mContext, intent);
                }
            }
        }
    };

    /**
     * 注册广播
     *
     * @param context
     */
    public void registerReceiver(Context context) {
        if (mDownloadAudioBroadcastReceiver == null) {
            //
            mDownloadAudioBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    Message msg = new Message();
                    msg.obj = intent;
                    mDownloadAudioHandler.sendMessage(msg);


                }
            };

            mContext.registerReceiver(mDownloadAudioBroadcastReceiver, mDownloadAudioIntentFilter);
            //发送注册成功广播
            Intent successIntent = new Intent(ACTION_DOWMLOADMUSICSUCCESS);
            successIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            mContext.sendBroadcast(successIntent);

        }
    }

    /**
     * 取消注册广播
     */
    public void unregisterReceiver(Context context) {
        if (mDownloadAudioBroadcastReceiver != null && isRegisterSuccess) {

            mContext.unregisterReceiver(mDownloadAudioBroadcastReceiver);

        }

    }


    ///////////////////////////////////
    public interface DownloadAudioReceiverListener {
        void onReceive(Context context, Intent intent);
    }

    public void setDownloadAudioReceiverListener(DownloadAudioReceiverListener mDownloadAudioReceiverListener) {
        this.mDownloadAudioReceiverListener = mDownloadAudioReceiverListener;
    }
}
