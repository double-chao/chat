package com.lcc.administrator.wechat.chat;

import android.content.Context;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;

public class HXSDKHelper {

    private boolean isInited = false;
    private final String TAG = "HXSDKHelper";

    private static HXSDKHelper hxsdkHelper;

    private HXSDKHelper() {
    }

    /**
     * 设置单影模式
     * synchronized 保证线程安全
     *
     * @return
     */
    public synchronized static HXSDKHelper getInstance() {
        if (hxsdkHelper == null) {
            hxsdkHelper = new HXSDKHelper();
        }
        return hxsdkHelper;
    }

    /**
     * 初始化HX SDK
     * synchronized 防止重复初始化
     *
     * @param mContext
     */
    public synchronized void initHXSDK(Context mContext) {

        if (!isInited) { //只初始化一次
            //设置自动登录 (调试阶段用不自动登录，发行时改用自动登录)
            EMChat.getInstance().setAutoLogin(true);
            EMChat.getInstance().init(mContext);
            /**
             * debugMode == true 时为打开，SDK会在log里输入调试信息
             * @param debugMode
             * 在做代码混淆的时候需要设置成false
             */
            EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
            initHXOptions();
            isInited = true;
        }

    }

    /**
     * 需要先设置 环信的好友体系
     */
    private void initHXOptions() {
        EMChatManager.getInstance().getChatOptions().setUseRoster(true);
        //配置好友验证  默认为不要验证  改为要验证
        EMChatManager.getInstance().getChatOptions().setAcceptInvitationAlways(false);
    }

    /**
     * HX 添加好友
     *
     * @param username
     * @param reason
     * @param emCallBack
     */
    public void addHXContact(final String username, final String reason, final EMCallBack emCallBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                EMContactManager.getInstance().addContact(username, reason);
                if (emCallBack != null) {
                    emCallBack.onSuccess();
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
                Log.i(TAG, "添加好友异常");
                if (emCallBack != null) {
                    emCallBack.onError(e.getErrorCode(), "添加好友失败");
                }
            }
            }
        }).start();

    }

    /**
     *   接受到好友的申请
     * @param username  用户
     * @param emCallBack
     */
    public void acceptHXInvatation(final String username, final EMCallBack emCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                EMChatManager.getInstance().acceptInvitation(username);
                if (emCallBack != null) {
                    emCallBack.onSuccess();
                }
            } catch (EaseMobException e) {
                e.printStackTrace();
                Log.i(TAG, "----接受好友申请异常--------");
                if (emCallBack != null) {
                    emCallBack.onError(e.getErrorCode(), "同意好友申请异常" + e.getMessage());
                }
            }
            }
        }).start();
    }

}
