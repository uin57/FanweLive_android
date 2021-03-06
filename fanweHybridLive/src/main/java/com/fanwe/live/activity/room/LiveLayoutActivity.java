package com.fanwe.live.activity.room;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fanwe.games.model.PluginModel;
import com.fanwe.library.listener.SDViewVisibilityCallback;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDReplaceableLayout;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.appview.animator.GiftAnimatorCar2;
import com.fanwe.live.appview.room.RoomGiftGifView;
import com.fanwe.live.appview.room.RoomGiftPlayView;
import com.fanwe.live.appview.room.RoomHeartView;
import com.fanwe.live.appview.room.RoomInfoView;
import com.fanwe.live.appview.room.RoomMsgView;
import com.fanwe.live.appview.room.RoomPopMsgView;
import com.fanwe.live.appview.room.RoomPrivateRemoveViewerView;
import com.fanwe.live.appview.room.RoomSendMsgView;
import com.fanwe.live.appview.room.RoomViewerJoinRoomView;
import com.fanwe.live.dialog.LiveAddViewerDialog;
import com.fanwe.live.dialog.LiveChatC2CDialog;
import com.fanwe.live.dialog.LiveRechargeDialog;
import com.fanwe.live.dialog.LiveRedEnvelopeNewDialog;
import com.fanwe.live.gift.CakeFragment;
import com.fanwe.live.gift.CarFragment;
import com.fanwe.live.gift.CastleFragment;
import com.fanwe.live.gift.ForeverFragment;
import com.fanwe.live.gift.FragmentOps;
import com.fanwe.live.gift.MarryFragment;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.LiveQualityData;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * 公共界面
 */
public class LiveLayoutActivity extends LiveActivity
{
    protected RoomInfoView mRoomInfoView;
    protected RoomGiftPlayView mRoomGiftPlayView;
    protected RoomPopMsgView mRoomPopMsgView;
    protected RoomViewerJoinRoomView mRoomViewerJoinRoomView;
    protected RoomMsgView mRoomMsgView;
    protected RoomSendMsgView mRoomSendMsgView;
    protected RoomHeartView mRoomHeartView;
    protected RoomGiftGifView mRoomGiftGifView;
    protected RoomPrivateRemoveViewerView mRoomPrivateRemoveViewerView;

    private RelativeLayout rl_root_layout; // UI表现层根部局
    private SDReplaceableLayout fl_bottom_extend; // 底部扩展

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);

        // 关闭房间监听
        findViewById(R.id.view_close_room).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickCloseRoom(v);
            }
        });
    }

    @Override
    public void onKeyboardVisibilityChange(boolean visible, int height)
    {
        if (rl_root_layout != null)
        {
            if (visible)
            {
                rl_root_layout.scrollBy(0, height);
            } else
            {
                rl_root_layout.scrollTo(0, 0);
                rl_root_layout.requestLayout();
                showSendMsgView(false);
            }
        }
        super.onKeyboardVisibilityChange(visible, height);
    }

    protected void initLayout(View view)
    {
        rl_root_layout = (RelativeLayout) view.findViewById(R.id.rl_root_layout);
        initBottomExtend();

        addRoomInfoView();
        addRoomGiftPlayView();
        addRoomGiftGifView();
        addRoomPopMsgView();
        addRoomViewerJoinRoomView();
        addRoomMsgView();
        addRoomSendMsgView();
        addRoomHeartView();
        addRoomBottomView();
    }

    /**
     * 初始化底部扩展
     */
    private void initBottomExtend()
    {
        fl_bottom_extend = (SDReplaceableLayout) findViewById(R.id.fl_bottom_extend);
        if (fl_bottom_extend != null)
        {
            fl_bottom_extend.addCallback(new SDReplaceableLayout.SDReplaceableLayoutCallback()
            {
                @Override
                public void onContentReplaced(View view)
                {
                    showBottomExtendSwitch(true);
                    onContentVisibilityChanged(view, view.getVisibility());
                }

                @Override
                public void onContentRemoved(View view)
                {
                    showBottomExtendSwitch(false);
                }

                @Override
                public void onContentVisibilityChanged(View view, int visibility)
                {
                    if (View.VISIBLE == visibility)
                    {
                        onShowBottomExtend();
                    } else
                    {
                        onHideBottomExtend();
                    }
                }
            });
        }
    }

    /**
     * 替换底部扩展
     *
     * @param view
     */
    protected void replaceBottomExtend(View view)
    {
        fl_bottom_extend.replaceContent(view);
    }

    /**
     * 切换显示隐藏底部扩展
     */
    protected void toggleBottomExtend()
    {
        fl_bottom_extend.toggleContentVisibleOrGone();
    }

    /**
     * 底部扩展显示回调
     */
    protected void onShowBottomExtend()
    {
        LogUtil.i("onShowBottomExtend");
    }

    /**
     * 底部扩展隐藏回调
     */
    protected void onHideBottomExtend()
    {
        LogUtil.i("onHideBottomExtend");
    }

    /**
     * 显示隐藏底部扩展开关
     *
     * @param show
     */
    protected void showBottomExtendSwitch(boolean show) {
        // 子类实现
        LogUtil.i("showBottomExtendSwitch:" + show);
    }

    /**
     * 发送礼物的view显示
     *
     * @param view
     */
    protected void onShowSendGiftView(View view) {
        showBottomView(false);
        showMsgView(false);
    }

    /**
     * 发送礼物的view隐藏
     *
     * @param view
     */
    protected void onHideSendGiftView(View view) {
        showBottomView(true);
        showMsgView(true);
    }

    /**
     * 发送消息的view显示
     *
     * @param view
     */
    protected void onShowSendMsgView(View view)
    {
        showBottomView(false);
    }

    /**
     * 发送消息的view隐藏
     *
     * @param view
     */
    protected void onHideSendMsgView(View view)
    {
        showBottomView(true);
    }

    /**
     * 房间信息
     */
    protected void addRoomInfoView()
    {
        if (mRoomInfoView == null)
        {
            mRoomInfoView = new RoomInfoView(this);
            mRoomInfoView.setClickListener(roomInfoClickListener);
            replaceView(R.id.fl_live_room_info, mRoomInfoView);
        }
    }

    /**
     * 礼物播放
     */
    protected void addRoomGiftPlayView()
    {
        if (mRoomGiftPlayView == null)
        {
            mRoomGiftPlayView = new RoomGiftPlayView(this);
            replaceView(R.id.fl_live_gift_play, mRoomGiftPlayView);
        }
    }

    /**
     * gif动画
     */
    protected void addRoomGiftGifView()
    {
        if (mRoomGiftGifView == null)
        {
            mRoomGiftGifView = new RoomGiftGifView(this);
            mRoomGiftGifView.setCallback(new RoomGiftGifView.SendBigGiftAnimationCallback() {
                @Override
                public void addBigAnimation(CustomMsgGift msg) {
                    String type = msg.getAnim_type();
                    if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.CAKE)) {
                        FragmentOps.addFragment(getSupportFragmentManager(),new CakeFragment(),"cake",R.id.fl_live_gift_anim);
                    } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.CASTLE)) {
                        FragmentOps.addFragment(getSupportFragmentManager(),new CastleFragment(),"castle",R.id.fl_live_gift_anim);
                    } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.STAGE)) {
                        FragmentOps.addFragment(getSupportFragmentManager(),new CarFragment(),"car",R.id.fl_live_gift_anim);
                    } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.WEDDING)) {
                        FragmentOps.addFragment(getSupportFragmentManager(),new MarryFragment(),"marry",R.id.fl_live_gift_anim);
                    } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.THREELIFETIMES)) {
                        FragmentOps.addFragment(getSupportFragmentManager(),new ForeverFragment(),"forever",R.id.fl_live_gift_anim);
                    }
                }
            });
            replaceView(R.id.fl_live_gift_gif, mRoomGiftGifView);
        }
    }

    /**
     * 弹幕
     */
    protected void addRoomPopMsgView()
    {
        if (mRoomPopMsgView == null)
        {
            mRoomPopMsgView = new RoomPopMsgView(this);
            replaceView(R.id.fl_live_pop_msg, mRoomPopMsgView);
        }
    }

    /**
     * 进入提示
     */
    protected void addRoomViewerJoinRoomView()
    {
        if (mRoomViewerJoinRoomView == null)
        {
            mRoomViewerJoinRoomView = new RoomViewerJoinRoomView(this);
            replaceView(R.id.fl_live_viewer_join_room, mRoomViewerJoinRoomView);
        }
    }

    /**
     * 聊天列表
     */
    protected void addRoomMsgView()
    {
        if (mRoomMsgView == null)
        {
            mRoomMsgView = new RoomMsgView(this);
            mRoomMsgView.setLayoutParams(new ViewGroup.LayoutParams(
                    SDViewUtil.getScreenWidthPercent(0.8f),
                    SDViewUtil.getScreenHeightPercent(0.2f)));
            replaceView(R.id.fl_live_msg, mRoomMsgView);
        }
    }

    /**
     * 发送消息
     */
    protected void addRoomSendMsgView()
    {
        if (mRoomSendMsgView == null)
        {
            mRoomSendMsgView = new RoomSendMsgView(this);
            mRoomSendMsgView.addVisibilityCallback(new SDViewVisibilityCallback()
            {
                @Override
                public void onViewVisibilityChanged(View view, int visibility)
                {
                    if (View.VISIBLE == visibility)
                    {
                        onShowSendMsgView(view);
                    } else
                    {
                        onHideSendMsgView(view);
                    }
                }
            });
            replaceView(R.id.fl_live_send_msg, mRoomSendMsgView);
        }
    }

    /**
     * 点亮
     */
    protected void addRoomHeartView()
    {
        if (mRoomHeartView == null)
        {
            mRoomHeartView = new RoomHeartView(this);
            mRoomHeartView.setLayoutParams(new ViewGroup.LayoutParams(
                    SDResourcesUtil.getDimensionPixelSize(R.dimen.width_live_bottom_menu) * 3,
                    SDViewUtil.getScreenWidthPercent(0.5f)));
            replaceView(R.id.fl_live_heart, mRoomHeartView);
        }
    }

    /**
     * 底部菜单
     */
    protected void addRoomBottomView()
    {
        //子类实现
    }

    /**
     * 私密直播踢人
     */
    protected void addRoomPrivateRemoveViewerView()
    {
        removeView(mRoomPrivateRemoveViewerView);
        mRoomPrivateRemoveViewerView = new RoomPrivateRemoveViewerView(this);
        addView(mRoomPrivateRemoveViewerView);
    }

    /**
     * 结束界面
     */
    protected void addLiveFinish()
    {
        // 子类实现
    }

    /**
     * 房间信息view点击监听
     */
    private RoomInfoView.ClickListener roomInfoClickListener = new RoomInfoView.ClickListener()
    {
        @Override
        public void onClickAddViewer(View v)
        {
            LiveLayoutActivity.this.onClickAddViewer(v);
        }

        @Override
        public void onClickMinusViewer(View v)
        {
            LiveLayoutActivity.this.onClickMinusViewer(v);
        }
    };

    /**
     * 私密直播点击加号加人
     *
     * @param v
     */
    protected void onClickAddViewer(View v)
    {
        LiveAddViewerDialog dialog = new LiveAddViewerDialog(this, getRoomInfo().getPrivate_share());
        dialog.showBottom();
    }

    /**
     * 私密直播点击减号踢人
     *
     * @param v
     */
    protected void onClickMinusViewer(View v)
    {
        addRoomPrivateRemoveViewerView();
    }

    @Override
    public void onBsRefreshViewerList(List<UserModel> listModel)
    {
        super.onBsRefreshViewerList(listModel);
        mRoomInfoView.onLiveRefreshViewerList(listModel);
    }

    @Override
    public void onBsRemoveViewer(UserModel model)
    {
        super.onBsRemoveViewer(model);
        mRoomInfoView.onLiveRemoveViewer(model);
    }

    @Override
    public void onBsInsertViewer(int position, UserModel model)
    {
        super.onBsInsertViewer(position, model);
        mRoomInfoView.onLiveInsertViewer(position, model);
    }

    @Override
    public void onBsTicketChange(long ticket)
    {
        super.onBsTicketChange(ticket);
        mRoomInfoView.updateTicket(ticket);
    }

    @Override
    public void onBsViewerNumberChange(int viewerNumber)
    {
        super.onBsViewerNumberChange(viewerNumber);
        mRoomInfoView.updateViewerNumber(viewerNumber);
    }

    @Override
    public void onBsBindCreaterData(UserModel model)
    {
        super.onBsBindCreaterData(model);
        mRoomInfoView.bindCreaterData(model);
    }

    @Override
    public void onBsShowOperateViewer(boolean show)
    {
        super.onBsShowOperateViewer(show);
        mRoomInfoView.showOperateViewerView(show);
    }

    @Override
    public void onBsUpdateLiveQualityData(LiveQualityData data)
    {
        super.onBsUpdateLiveQualityData(data);
        mRoomInfoView.getSdkInfoView().updateLiveQuality(data);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel)
    {
        super.onBsRequestRoomInfoSuccess(actModel);

        mRoomInfoView.bindData(actModel);
        bindShowShareView();
        getLiveBusiness().startLiveQualityLooper(this);
    }

    /**
     * 显示充值窗口
     * 整合到基类？
     */
    protected void showRechargeDialog()
    {
        LiveRechargeDialog dialog = new LiveRechargeDialog(this);
        dialog.show();
    }

    /**
     * 点击关闭
     *
     * @param v
     */
    protected void onClickCloseRoom(View v)
    {
        //子类实现
    }

    /**
     * 点击打开发送窗口
     *
     * @param v
     */
    protected void onClickMenuSendMsg(View v)
    {
        showSendMsgView(true);
    }

    @Override
    public void openSendMsg(String content)
    {
        super.openSendMsg(content);
        showSendMsgView(true);
        mRoomSendMsgView.setContent(content);
    }

    /**
     * 点击私聊消息
     *
     * @param v
     */
    protected void onClickMenuPrivateMsg(View v)
    {
        LiveChatC2CDialog dialog = new LiveChatC2CDialog(this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {

            @Override
            public void onDismiss(DialogInterface dialog)
            {

            }
        });
        dialog.showBottom();
    }

    /**
     * 绑定是否显示分享view
     */
    protected void bindShowShareView()
    {
    }

    /**
     * 主播普通插件点击
     *
     * @param model
     */
    protected void onClickCreaterPluginNormal(PluginModel model)
    {

    }

    /**
     * 主播游戏插件点击
     *
     * @param model
     */
    protected void onClickCreaterPluginGame(PluginModel model)
    {

    }

    protected void replaceBankerView(View view)
    {
        replaceView(R.id.fl_container_banker, view);
    }

    /**
     * 点击分享
     *
     * @param v
     */
    protected void onClickMenuShare(View v)
    {
        openShare(new UMShareListener()
        {
            @Override
            public void onStart(SHARE_MEDIA share_media)
            {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media)
            {
                getLiveBusiness().sendShareSuccessMsg();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable)
            {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media)
            {
            }
        });
    }

    /**
     * 隐藏发送礼物
     */
    protected void hideSendGiftView()
    {

    }

    /**
     * 显示隐藏底部菜单view
     */
    protected void showBottomView(boolean show)
    {
        // 子类实现
    }

    /**
     * 显示隐藏消息列表
     */
    protected void showMsgView(boolean show)
    {
        if (show)
        {
            mRoomMsgView.getVisibilityHandler().setVisible(true);
        } else
        {
            mRoomMsgView.getVisibilityHandler().setInvisible(true);
        }
    }

    /**
     * 显示隐藏发送消息view
     */
    protected void showSendMsgView(boolean show)
    {
        if (show)
        {
            SDViewUtil.setVisible(mRoomSendMsgView);
        } else
        {
            SDViewUtil.setInvisible(mRoomSendMsgView);
        }
    }

    /**
     * 发送消息view是否可见
     *
     * @return
     */
    protected boolean isSendMsgViewVisible()
    {
        if (mRoomSendMsgView == null)
        {
            return false;
        }

        return mRoomSendMsgView.isVisible();
    }

    /**
     * 发送礼物view是否可见
     *
     * @return
     */
    protected boolean isSendGiftViewVisible()
    {
        return false;
    }

    @Override
    public void onMsgRedEnvelope(CustomMsgRedEnvelope msg)
    {
        super.onMsgRedEnvelope(msg);
        LiveRedEnvelopeNewDialog dialog = new LiveRedEnvelopeNewDialog(this, msg);
        dialog.show();
    }

    @Override
    public void onBsViewerShowCreaterLeave(boolean show)
    {
        super.onBsViewerShowCreaterLeave(show);
        if (isAuctioning())
        {
            mRoomInfoView.showCreaterLeave(false);
        } else if (getRoomInfo() != null && getRoomInfo().getIs_bm() == 1)
        {
            mRoomInfoView.showCreaterLeave(false);
        } else
        {
            mRoomInfoView.showCreaterLeave(show);
        }
    }
}
