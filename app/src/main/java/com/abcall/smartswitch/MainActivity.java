package com.abcall.smartswitch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import com.abcall.smartswitch.baseclass.BaseActivity;
import com.chris.base.ui.BaseMainFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView mImage;

    private BaseMainFragment mFragmentOne;

    private final static String TAG = "TAG";

    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mFragmentOne = (BaseMainFragment) fm.getFragment(savedInstanceState, TAG);
        }

//        GlideApp.with(this).load("http://guolin.tech/book.png").into(mImage);
        initFragment();
    }

    private void initFragment() {
        if (mFragmentOne == null) {
            mFragmentOne = new BaseMainFragment();
            Bundle bundle = new Bundle();
//            bundle.putBoolean(INTENT_BOOLEAN_LAZYLOAD, false);
            bundle.putString("TITLE", "1");
            mFragmentOne.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment, mFragmentOne).commit();
        }
    }

    @OnClick(R.id.imageView)
    public void onViewClicked() {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFragmentOne.isAdded()) {
            fm.putFragment(outState, TAG, mFragmentOne);
        }
    }
}
