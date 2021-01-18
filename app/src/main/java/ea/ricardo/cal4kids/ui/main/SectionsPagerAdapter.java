package ea.ricardo.cal4kids.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ea.ricardo.cal4kids.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.easyMode, R.string.normalMode,R.string.extremeMode};
    private final Context mContext;
    private String nombreJug;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String nombreJug) {
        super(fm);
        mContext = context;
        this.nombreJug = nombreJug;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position + 1, nombreJug);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}