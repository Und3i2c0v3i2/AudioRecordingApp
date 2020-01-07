package practice.und3i2c0v3i2.audiorecordingapp.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import practice.und3i2c0v3i2.audiorecordingapp.NewMemoFragment;
import practice.und3i2c0v3i2.audiorecordingapp.R;
import practice.und3i2c0v3i2.audiorecordingapp.SavedMemosFragment;
import practice.und3i2c0v3i2.audiorecordingapp.model.RecordItem;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.record_tab_label, R.string.memos_tab_label};
    private final Context mContext;


    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {

            case 0:
                fragment = new NewMemoFragment();
                break;

            case 1:
                fragment = SavedMemosFragment.newInstance();
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}