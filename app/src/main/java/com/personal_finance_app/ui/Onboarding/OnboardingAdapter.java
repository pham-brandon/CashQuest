package com.personal_finance_app.ui.Onboarding;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.personal_finance_app.R;
import java.util.ArrayList;
import java.util.List;

public class OnboardingAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentList;

    public OnboardingAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragmentList = new ArrayList<>();
        fragmentList.add(OnboardingFragment.newInstance(R.layout.fragment_onboarding1));
        fragmentList.add(OnboardingFragment.newInstance(R.layout.fragment_onboarding2));
        fragmentList.add(OnboardingFragment.newInstance(R.layout.fragment_onboarding3));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}