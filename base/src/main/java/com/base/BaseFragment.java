package com.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 * Created by shayan on 7/7/2017.
 * base fragment that handled some errors and display messages
 */

public class BaseFragment extends Fragment implements BaseUIInterface {

    protected IFragment listener = new IFragment() {
        @Override
        public void displayToast(@NonNull String message) {

        }

        @Override
        public void displayToast(@StringRes int message) {

        }

        @Override
        public void displaySnackBar(@NonNull String message, String btnText) {

        }

        @Override
        public void displaySnackBar(@StringRes int message, @StringRes int btnText) {

        }

        @Override
        public void displaySnackBar(@NonNull String message) {

        }

        @Override
        public void displaySnackBar(@StringRes int message) {

        }

        @Override
        public void displayMessageDialog(String title, String message) {

        }

        @Override
        public void displayMessageDialog(@StringRes int title, @StringRes int message) {

        }

        @Override
        public void displayYesNoDialog(String title, String description, String TAG) {

        }

        @Override
        public void displayYesNoDialog(String title, String description) {

        }

        @Override
        public void displayYesNoDialog(int title, int description) {

        }

        @Override
        public void displayYesNoDialog(@StringRes int title, @StringRes int description, String TAG) {

        }

        @Override
        public void displayYesNoCancelDialog(String title, String description) {

        }

        @Override
        public void displayYesNoCancelDialog(@StringRes int title, @StringRes int description) {

        }

        @Override
        public void displayProgressDialog() {

        }

        @Override
        public void finishActivity() {

        }

        @Override
        public void startActivity(Class c) {

        }

        @Override
        public void startActivity(Class c, Bundle bundle) {

        }

        @Override
        public String getDeviceId() {
            return null;
        }

        @Override
        public void dismissDialog() {

        }

        @Override
        public void onSnackBarButtonClick() {

        }

        @Override
        public void onCancelClick(String tag) {

        }

        @Override
        public void onNoClick(String tag) {

        }

        @Override
        public void onYesClick(String tag) {

        }

        @Override
        public void onOkClick(String tag) {

        }

        @Override
        public void onProgressCancel() {

        }
    };

    public BaseFragment() {
        super();
    }

    public void setListener(IFragment listener) {
        this.listener = listener;
    }

    public void onConnected() {
        listener.displayToast(getString(R.string.msg_connected_to_network));
    }

    @Override
    public void displayToast(@NonNull String message) {
        listener.displayToast(message);
    }

    @Override
    public void displayToast(int message) {
        listener.displayToast(message);
    }

    @Override
    public void displaySnackBar(@NonNull String message, String btnTex) {
        this.listener.displaySnackBar(message, btnTex);
    }

    @Override
    public void displaySnackBar(int message, int btnText) {
        listener.displaySnackBar(message, btnText);
    }

    @Override
    public void displaySnackBar(@NonNull String message) {
        listener.displaySnackBar(message);
    }

    @Override
    public void displaySnackBar(int message) {
        listener.displaySnackBar(message);
    }

    @Override
    public void displayMessageDialog(String title, String message) {
        listener.displayMessageDialog(title, message);
    }

    @Override
    public void displayMessageDialog(int title, int message) {
        listener.displayMessageDialog(title, message);
    }

    @Override
    public void displayYesNoDialog(String title, String description, String TAG) {
        listener.displayYesNoDialog(title, description, TAG);
    }

    @Override
    public void displayYesNoDialog(String title, String description) {
        listener.displayYesNoDialog(title, description);
    }

    @Override
    public void displayYesNoDialog(int title, int description) {
        listener.displayYesNoDialog(title, description);
    }

    @Override
    public void displayYesNoDialog(int title, int description, String TAG) {
        listener.displayYesNoDialog(title, description, TAG);
    }

    @Override
    public void displayYesNoCancelDialog(String title, String description) {
        listener.displayYesNoCancelDialog(title, description);
    }

    @Override
    public void displayYesNoCancelDialog(int title, int description) {
        listener.displayYesNoCancelDialog(title, description);
    }

    @Override
    public void displayProgressDialog() {
        listener.displayProgressDialog();
    }

    @Override
    public void finishActivity() {
        listener.finishActivity();
    }

    @Override
    public void startActivity(Class c) {
        Intent intent = new Intent(getContext(), c);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class c, Bundle bundle) {
        listener.startActivity(c, bundle);
    }

    @Override
    public String getDeviceId() {
        return listener.getDeviceId();
    }

    @Override
    public void dismissDialog() {
        listener.dismissDialog();
    }

    @Override
    public void onSnackBarButtonClick() {
        listener.onSnackBarButtonClick();
    }

    @Override
    public void onCancelClick(String tag) {
        listener.onCancelClick(tag);
    }

    @Override
    public void onNoClick(String tag) {
        listener.onNoClick(tag);
    }

    @Override
    public void onYesClick(String tag) {

        listener.onYesClick(tag);
    }

    @Override
    public void onOkClick(String tag) {

        listener.onOkClick(tag);
    }

    @Override
    public void onProgressCancel() {
        listener.onProgressCancel();
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (getView() == null) return null;
        return getView().findViewById(id);
    }

}
