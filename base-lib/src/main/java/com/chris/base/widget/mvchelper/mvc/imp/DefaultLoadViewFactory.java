package com.chris.base.widget.mvchelper.mvc.imp;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chris.base.R;
import com.chris.base.SuperApp;
import com.chris.base.util.CheckEmpty;
import com.chris.base.util.PhoneUtil;
import com.chris.base.util.Toastor;
import com.chris.base.widget.mvchelper.mvc.ILoadViewFactory;
import com.chris.base.widget.mvchelper.view.vary.VaryViewHelper;


public class DefaultLoadViewFactory implements ILoadViewFactory {

    private Activity mActivity;
    private static String loadMoreText;
    private String emptyText;
    private int tipImage;
    private boolean hasFooter = true;
    private boolean isShowFail = false;

    public DefaultLoadViewFactory() {
        loadMoreText = null;
        emptyText = null;
        tipImage = 0;
        hasFooter = true;
    }

    public DefaultLoadViewFactory(Activity activity) {
        this.mActivity = activity;
        loadMoreText = null;
        emptyText = null;
        tipImage = 0;
        hasFooter = true;
    }

    public void setLoadMoreText(String loadMoreText) {
        this.loadMoreText = loadMoreText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

    public void setTipImage(int tipImage) {
        this.tipImage = tipImage;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public void setShowFail(boolean isShowFail) {
        this.isShowFail = isShowFail;
    }

    @Override
    public ILoadMoreView madeLoadMoreView() {
        return new LoadMoreHelper(hasFooter);
    }

    @Override
    public ILoadView madeLoadView() {
        return new LoadViewHelper(mActivity, emptyText, tipImage, isShowFail);
    }

    private static class LoadMoreHelper implements ILoadMoreView {

        protected TextView footView;

        protected OnClickListener onClickRefreshListener;

        protected boolean hasFooter;

        public LoadMoreHelper(boolean hasFooter) {
            this.hasFooter = hasFooter;
        }

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
            LinearLayout layout = (LinearLayout) footViewHolder.addFootView(R.layout.mvchelper_load_footer);
            footView = (TextView) layout.findViewById(R.id.footer);
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
            if (hasFooter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        PhoneUtil.getScreenWidth(SuperApp.getContext())
                        , SuperApp.getInstance().getResources().getDimensionPixelSize(R.dimen.dp_48));
                footView.setLayoutParams(params);
                if (CheckEmpty.isEmpty(loadMoreText)) {
                    footView.setText("点击加载更多");
                } else {
                    footView.setText(loadMoreText);
                }
                footView.setOnClickListener(onClickRefreshListener);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        PhoneUtil.getScreenWidth(SuperApp.getContext())
                        , 1);
                footView.setLayoutParams(params);
                footView.setText("");
                footView.setOnClickListener(null);
            }
        }

        @Override
        public void showLoading() {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    PhoneUtil.getScreenWidth(SuperApp.getContext())
                    , SuperApp.getInstance().getResources().getDimensionPixelSize(R.dimen.dp_48));
            footView.setLayoutParams(params);

            footView.setText("正在加载中..");
            footView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            if (hasFooter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        PhoneUtil.getScreenWidth(SuperApp.getContext())
                        , SuperApp.getInstance().getResources().getDimensionPixelSize(R.dimen.dp_48));
                footView.setLayoutParams(params);

                footView.setText("加载失败，点击重新加载");
                footView.setOnClickListener(onClickRefreshListener);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        PhoneUtil.getScreenWidth(SuperApp.getContext())
                        , 1);
                footView.setLayoutParams(params);

                footView.setText("");
                footView.setOnClickListener(null);
            }
        }

        @Override
        public void showNomore() {
            if (hasFooter) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        PhoneUtil.getScreenWidth(SuperApp.getContext())
                        , SuperApp.getInstance().getResources().getDimensionPixelSize(R.dimen.dp_48));
                footView.setLayoutParams(params);
                footView.setText("已是全部");
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        PhoneUtil.getScreenWidth(SuperApp.getContext()), 1);
                footView.setLayoutParams(params);
                footView.setText("");
            }
            footView.setOnClickListener(null);
        }

    }

    private static class LoadViewHelper implements ILoadView {
        private VaryViewHelper helper;
        private OnClickListener onClickRefreshListener;
        private Activity mActivity;
        private String emptyText;
        private int tipImage;
        private boolean isShowFail;

        public LoadViewHelper(Activity activity, String emptyText, int tipImage, boolean isShowFail) {
            this.mActivity = activity;
            this.emptyText = emptyText;
            this.tipImage = tipImage;
            this.isShowFail = isShowFail;
        }

        @Override
        public void init(View switchView, OnClickListener onClickRefreshListener) {
            this.onClickRefreshListener = onClickRefreshListener;
            helper = new VaryViewHelper(switchView);
        }

        @Override
        public void restore() {
            helper.restoreView();
        }

        @Override
        public void showLoading() {
            Context context = helper.getContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setBackgroundColor(0xFFFFFFFF);

            TextView textView = new TextView(context);
            textView.setText("加载中...");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.sp_13_5));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(0xFF8E8B9B);
            layout.addView(textView);

            helper.showLayout(layout);
        }

        @Override
        public void tipFail(Exception exception) {
            if (exception != null && exception.getMessage() != null) {
                if (mActivity != null && !mActivity.isFinishing()) {
//                    mActivity.showErrorMsg(exception.getMessage());
                    new Toastor().showToast(exception.getMessage());
                }
            }
        }

        @Override
        public void showFail(Exception exception) {
            Context context = helper.getContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setBackgroundColor(0xFFFFFFFF);

            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.status_404);
            int size = context.getResources().getDimensionPixelOffset(R.dimen.dp_150);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            params.setMargins(0, 0, 0, context.getResources().getDimensionPixelOffset(R.dimen.dp_12));
            imageView.setOnClickListener(onClickRefreshListener);
            layout.addView(imageView, params);

            TextView textView = new TextView(context);
            textView.setText("访问失败，请检查网络连接");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.sp_13_5));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xFF8E8B9B);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            param.setMargins(0, 0, 0, context.getResources().getDimensionPixelOffset(R.dimen.dp_50));
            layout.addView(textView, param);

            helper.showLayout(layout);

            if (isShowFail) {
                if (exception != null && exception.getMessage() != null) {
                    if (mActivity != null && !mActivity.isFinishing()) {
//                        mActivity.showErrorMsg(exception.getMessage());
                        new Toastor().showToast(exception.getMessage());
                    }
                }
            }
        }

        @Override
        public void showEmpty() {
            Context context = helper.getContext();

            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setBackgroundColor(0xFFFFFFFF);

            if (tipImage != 0) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(tipImage);
                int size = context.getResources().getDimensionPixelOffset(R.dimen.dp_150);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                params.setMargins(0, 0, 0, context.getResources().getDimensionPixelOffset(R.dimen.dp_12));
                layout.addView(imageView, params);
            }

            TextView textView = new TextView(context);
            if (CheckEmpty.isEmpty(emptyText)) {
                textView.setText("暂无数据");
            } else {
                textView.setText(emptyText);
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.sp_13_5));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(0xFF8E8B9B);

            if (tipImage != 0) {
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                param.setMargins(0, 0, 0, context.getResources().getDimensionPixelOffset(R.dimen.dp_50));
                layout.addView(textView, param);
            } else {
                layout.addView(textView);
            }
            helper.showLayout(layout);
        }
    }
}
