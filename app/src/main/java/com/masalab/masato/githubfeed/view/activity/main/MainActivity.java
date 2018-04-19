package com.masalab.masato.githubfeed.view.activity.main;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.masalab.masato.githubfeed.R;
import com.masalab.masato.githubfeed.navigator.Navigator;
import com.masalab.masato.githubfeed.view.MainView;
import com.masalab.masato.githubfeed.view.fragment.TryAgainDialogFragment;

public class MainActivity extends AppCompatActivity implements MainView, TryAgainDialogFragment.TryAgainListener {

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onTryAgain() {
        presenter.tryAgain();
    }

    @Override
    public void showTryAgainDialog() {
        DialogFragment dialogFragment = new TryAgainDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "");
    }

    @Override
    public void showLogInView() {
        Navigator.navigateToLogInView(this);
    }

    @Override
    public void showHomeView() {
        Navigator.navigateToHomeView(this);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_LONG).show();
    }

}
