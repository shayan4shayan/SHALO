package com.syfract.sabt.main

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.WindowManager
import com.syfract.sabt.R
import kotlinx.android.synthetic.main.dialog_new_package.*

/**
 * Created by shayan4shayan on 4/3/18.
 */
class NewPackageDialog(context: Context, val uri: Uri, val callback: Callback) : AlertDialog(context) {
    override fun show() {
        super.show()
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        setContentView(R.layout.dialog_new_package)
        btnUpload.setOnClickListener { upload() }
    }

    private fun upload() {
        callback.onUpload(uri, textDescription.text.toString())
        dismiss()
    }

    interface Callback {
        fun onUpload(uri: Uri, description: String)
    }
}