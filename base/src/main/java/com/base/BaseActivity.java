package com.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.base.receiver.ConnectionInterface;
import com.extra.util.typeface.TypeFaceUtil;


/**
 * Created by shayan on 6/30/2017.
 */

public class BaseActivity extends AppCompatActivity implements BaseUIInterface, ConnectionInterface, View.OnClickListener {

    String TAG = getClass().getSimpleName();

    protected CoordinatorLayout layout;

    AlertDialog dialog;

    boolean displayBackButton = true;
    private String dialogTag;

    public void setDisplayBackButton(boolean displayBackButton) {
        this.displayBackButton = displayBackButton;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.primary));
        }
    }

    @Override
    public void setTitle(int titleId) {
        String str = getString(titleId);
        setTitle(str);
    }

    @Override
    public void setTitle(CharSequence title) {
        SpannableString str = TypeFaceUtil.setTypeFace(this, title.toString());
        super.setTitle(str);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onResume() {
        super.onResume();
        ActionBar ab = getSupportActionBar();
        if (ab != null && displayBackButton) {
            ab.setHomeButtonEnabled(true);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        layout = (CoordinatorLayout) findViewById(R.id.container);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }

    @Override
    public void displayToast(@NonNull String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayToast(@StringRes int message) {
        String msg = getString(message);
        displayToast(msg);
    }

    @Override
    public void displaySnackBar(@NonNull String message, String btnText) {
        if (layout == null) return;
        Snackbar.make(layout, message, BaseTransientBottomBar.LENGTH_LONG).setAction(btnText, this)
                .show();
    }

    @Override
    public void displaySnackBar(@StringRes int message, @StringRes int btnText) {
        String msg = getString(message);
        String btn = getString(btnText);
        displaySnackBar(msg, btn);
    }

    @Override
    public void displaySnackBar(@NonNull String message) {
        if (layout == null) return;
        Snackbar.make(layout, message, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void displaySnackBar(@StringRes int message) {
        String msg = getString(message);
        displaySnackBar(msg);
    }

    @Override
    public void displayMessageDialog(String title, String message) {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.show();
        dialog.setContentView(R.layout.dialog_message);
        dialog.findViewById(R.id.ok).setOnClickListener(this);
        TextView titleView, descriptionView;
        titleView = (TextView) dialog.findViewById(R.id.text_title);
        descriptionView = (TextView) dialog.findViewById(R.id.text_description);
        titleView.setText(title);
        descriptionView.setText(message);
    }

    @Override
    public void displayMessageDialog(@StringRes int title, @StringRes int message) {
        String titleString = getString(title);
        String msg = getString(message);
        displayMessageDialog(titleString, msg);
    }

    @Override
    public void displayYesNoDialog(String title, String description, String TAG) {

        this.dialogTag = TAG;
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.show();
        dialog.setContentView(R.layout.dialog_yes_no);
        dialog.findViewById(R.id.yes).setOnClickListener(this);
        dialog.findViewById(R.id.no).setOnClickListener(this);
        TextView titleView, descriptionView;
        titleView = (TextView) dialog.findViewById(R.id.text_title);
        descriptionView = (TextView) dialog.findViewById(R.id.text_description);
        titleView.setText(title);
        descriptionView.setText(description);
    }

    @Override
    public void displayYesNoDialog(String title, String description) {
        displayYesNoDialog(title, description, null);
    }

    @Override
    public void displayYesNoDialog(int title, int description) {
        displayYesNoDialog(title, description, null);
    }

    @Override
    public void displayYesNoDialog(@StringRes int title, @StringRes int description, String TAG) {
        String titleString = getString(title);
        String msg = getString(description);
        displayYesNoDialog(titleString, msg, TAG);
    }

    @Override
    public void displayYesNoCancelDialog(String title, String description) {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.show();
        dialog.setContentView(R.layout.dialog_yes_no_cancel);
        dialog.findViewById(R.id.yes).setOnClickListener(this);
        dialog.findViewById(R.id.no).setOnClickListener(this);
        dialog.findViewById(R.id.cancel).setOnClickListener(this);
        TextView titleView, descriptionView;
        titleView = (TextView) dialog.findViewById(R.id.text_title);
        descriptionView = (TextView) findViewById(R.id.text_description);
        titleView.setText(title);
        descriptionView.setText(description);
    }

    @Override
    public void displayYesNoCancelDialog(@StringRes int title, @StringRes int description) {
        String titleString = getString(title);
        String msg = getString(description);
        displayYesNoCancelDialog(titleString, msg);
    }

    @Override
    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void displayProgressDialog() {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        dismissDialog();
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_progress);
        dialog.findViewById(R.id.cancel).setOnClickListener(this);
        dialog.setCancelable(false);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(this, c);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @SuppressLint("HardwareIds")
    @Override
    public String getDeviceId() {
        return Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onNoConnection() {
        Log.d(TAG, "onNoConnection: device disconnected");
    }

    @Override
    public void onConnected() {
        Log.d(TAG, "onConnected: device connected");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancel) {
            dismissDialog();
            onProgressCancel();
            onCancelClick(dialogTag);
        } else if (view.getId() == R.id.ok) {
            dismissDialog();
            onOkClick(dialogTag);
        } else if (view.getId() == R.id.yes) {
            dismissDialog();
            onYesClick(dialogTag);
        } else if (view.getId() == R.id.no) {
            dialog.cancel();
            onNoClick(dialogTag);
        } else if (view.getId() == R.id.snackbar_action) {
            onSnackBarButtonClick();
        }
    }

    @Override
    public void onSnackBarButtonClick() {
        Log.d(TAG, "onSnackBarButtonClick: no action set");
    }

    @Override
    public void onCancelClick(String dialogTag) {
        Log.d(TAG, "onCancelClick: no action set");
    }

    @Override
    public void onNoClick(String dialogTag) {
        Log.d(TAG, "onNoClick: no action set");
    }

    @Override
    public void onYesClick(String dialogTag) {
        Log.d(TAG, "onYesClick: no action set");
    }

    @Override
    public void onOkClick(String dialogTag) {
        Log.d(TAG, "onOkClick: no action set");
    }

    @Override
    public void onProgressCancel() {
        Log.d(TAG, "onProgressCancel: no action set");
    }
}
