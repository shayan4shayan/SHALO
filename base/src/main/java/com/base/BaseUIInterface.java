package com.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by shayan on 6/30/2017.
 */

public interface BaseUIInterface {

    /**
     * display toast message
     *
     * @param message to be show
     */
    void displayToast(@NonNull String message);

    void displayToast(@StringRes int message);

    /**
     * display SnackBar message
     * snackBar button clickListener calls method with name 'onSnackBarButtonClick'
     *
     * @param message to be show
     * @param btnText text of button in snackbar
     */
    void displaySnackBar(@NonNull String message, String btnText);

    void displaySnackBar(@StringRes int message, @StringRes int btnText);

    /**
     * display snackbar
     *
     * @param message to be show
     */
    void displaySnackBar(@NonNull String message);

    void displaySnackBar(@StringRes int message);

    /**
     * displays a dialog for showing message with a simple 'OK' button
     * 'OK' clickListener calls method with name 'onOkClick'
     *
     * @param title   is title of the dialog
     * @param message is description of the dialog
     */
    void displayMessageDialog(String title, String message);

    void displayMessageDialog(@StringRes int title, @StringRes int message);

    /**
     * displays a dialog with two buttons 'yes' and 'no'
     * 'yes' clickListener calls method with name 'onYesClick'
     * 'no' clickListener calls method with name 'onNoClick'
     *
     * @param title       is title of dialog
     * @param description is description if the dialog
     */
    void displayYesNoDialog(String title, String description, String TAG);

    void displayYesNoDialog(@StringRes int title, @StringRes int description, String TAG);

    void displayYesNoDialog(String title, String description);

    void displayYesNoDialog(@StringRes int title, @StringRes int description);

    /**
     * displays a dialog with three buttons 'yes' and 'no' and 'cancel'
     * 'yes' clickListener calls method with name 'onYesClick'
     * 'no' clickListener calls method with name 'onNoClick'
     * 'cancel' clickListener calls method with name 'onCancelClick'
     *
     * @param title       is title of the dialog
     * @param description is description of the dialog
     */
    void displayYesNoCancelDialog(String title, String description);

    void displayYesNoCancelDialog(@StringRes int title, @StringRes int description);

    /**
     * displays a progress dialog with cancel button
     */
    void displayProgressDialog();

    /**
     * to end activity
     */
    void finishActivity();

    /**
     * start a new Activity with class name
     *
     * @param c
     */
    void startActivity(Class c);

    void startActivity(Class c, Bundle bundle);

    /**
     * gets android device id
     *
     * @return device id
     */
    String getDeviceId();

    /**
     * dismiss displayed dialog
     * <p>
     * ignores if dialog is null or dialog is not showing
     */
    void dismissDialog();

    void onSnackBarButtonClick();

    void onCancelClick(String tag);

    void onNoClick(String tag);

    void onYesClick(String tag);

    void onOkClick(String tag);

    void onProgressCancel();
}
