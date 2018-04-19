package com.masalab.masato.githubfeed.view.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.githubapi.Failure;
import com.masalab.masato.githubfeed.view.ViewTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Masato on 2018/02/03.
 */

public abstract class BaseFragment extends Fragment implements ErrorFragment.TryAgainListener, OnBackPressedListener {

    private List<ViewTask> ViewTaskQueue = new ArrayList<>();
    private LoadingFragment loadingFragment;
    private ErrorFragment errorFragment;
    private boolean ViewTaskSafe;
    private boolean isVisibleInPager;
    private String name;

    public boolean isVisibleInPager() {
        return isVisibleInPager;
    }

    public void setVisibilityInPager(boolean isVisible) {
        doSafeViewTask(() -> {
            this.isVisibleInPager = isVisible;
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    baseFragment.setVisibilityInPager(isVisible);
                }
            }
        });
    }

    public abstract boolean isZombie();

    @Override
    public void onResume() {
        super.onResume();
        ViewTaskSafe = true;
        execQueuedTasks();
    }

    @Override
    public boolean onBackPressed() {
        List<Fragment> childFragments = getChildFragmentManager().getFragments();
        boolean processed = false;
        for (Fragment fragment : childFragments) {
            if (fragment instanceof OnBackPressedListener) {
                OnBackPressedListener listener = (OnBackPressedListener) fragment;
                processed = listener.onBackPressed();
            }
        }
        return processed;
    }

    public void showErrorFragment(int motherId, Failure failure, String errorMessage) {
        doSafeViewTask(() -> {
            if (errorFragment != null) {
                return;
            }
            errorFragment = FragmentFactory.createErrorFragment(failure, errorMessage);
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.add(motherId, errorFragment);
            ft.commit();
        });
    }

    public void removeErrorFragment() {
        doSafeViewTask(() -> {
            if (errorFragment == null) {
                return;
            }
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(errorFragment);
            ft.commit();
            errorFragment = null;
        });
    }

    public void showLoadingFragment(int motherId) {
        doSafeViewTask(() -> {
            if (loadingFragment != null) {
                return;
            }
            loadingFragment = new LoadingFragment();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.add(motherId, loadingFragment);
            ft.commit();
        });
    }

    public void removeLoadingFragment() {
        doSafeViewTask(() -> {
            if (loadingFragment == null) {
                return;
            }
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fade_animation, R.anim.fade_animation);
            ft.remove(loadingFragment);
            ft.commit();
            loadingFragment = null;
        });
    }

    private void execQueuedTasks() {
        for (ViewTask viewTask : ViewTaskQueue) {
            viewTask.execute();
        }
        ViewTaskQueue.clear();
    }

    protected void doSafeViewTask(ViewTask viewTask) {
        if (ViewTaskSafe) {
            viewTask.execute();
        } else {
            ViewTaskQueue.add(viewTask);
        }
    }

    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    public void showToast(int stringId) {
        Toast.makeText(getContext(), stringId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        ViewTaskSafe = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    };
}
