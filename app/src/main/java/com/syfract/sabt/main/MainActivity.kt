package com.syfract.sabt.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.base.BaseActivity
import com.base.api.BaseApiInterface
import com.syfract.sabt.DownloadDialog
import com.syfract.sabt.Model
import com.syfract.sabt.MyFileProvider
import com.syfract.sabt.R
import com.syfract.sabt.api.PackagesListApi
import com.syfract.sabt.api.UploadFileApi
import com.syfract.sabt.download.DownloadService
import com.syfract.sabt.parser.Parser
import com.syfract.sabt.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : BaseActivity(),
        NewPackageDialog.Callback, BaseApiInterface, Adapter.OnListClickListener,
        DownloadService.DownloadListener {
    override fun onLogout() {
        finish()
        com.extra.util.AccountManager.getInstanse(this).logout()
        startActivity(SignupActivity::class.java)
    }

    override fun onUpdateProgress(prgress: Int) {
        runOnUiThread { dialog.onUpdateProgress(prgress) }
    }

    override fun onComplete(path: String) {
        dialog.dismiss()
        Log.d("Download", "onComplete called : " + path)
        val mimeTypeMap = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path))
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = MyFileProvider.getUriForFile(this, "com.syfract.sabt.MyFileProvider", File(path))
        Log.d(javaClass.simpleName, "onClick: " + mimeTypeMap)
        intent.setDataAndType(uri, mimeTypeMap)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override fun onFailed() {
        dismissDialog()
        Toast.makeText(this, "Download Error", Toast.LENGTH_LONG).show()
    }

    override fun onItemClicked(app: Model.Application) {

    }

    override fun onItemActionClicked(app: Model.Application) {
        val url = app.download
        downloadApp(url)
    }

    lateinit var dialog: DownloadDialog

    private fun downloadApp(url: String) {
        dialog = DownloadDialog(this, url)
        dialog.show()
    }

    override fun onResponse(response: String?) {
        val apps = Parser.parsePackage(response!!)
        apps.forEach { updateApplication(it) }
        initList(apps)
    }

    private fun initList(apps: List<Model.Application>) {
        val adapter = Adapter(apps, this)
        recycler.adapter = adapter
    }

    private fun updateApplication(app: Model.Application) {
        val filtered = pkgAppsList!!.filter { it.packageName == app.packageName }
        if (filtered.isEmpty()) {
            Log.d("MainActivity", "${app.packageName} not found")
            app.installedVersion = 0
            return
        }
        app.installedVersion = filtered[0].versionCode

        Log.d("MainActivity", "${app.installedVersion}")
    }

    override fun onError(message: String?) {
        displaySnackBar(message!!, "retry")
    }

    private val uploadListener = object : BaseApiInterface {
        override fun onLogout() {

        }

        override fun onError(message: String?) {
            displayToast(message!!)
            dismissDialog()
        }

        override fun onResponse(response: String?) {
            displayToast("New Package submitted")
            dismissDialog()
        }
    }

    override fun onUpload(uri: Uri, description: String) {
        displayProgressDialog()
        val api = UploadFileApi(this, uri)
        api.addPostParam("description", description)
        api.setmApiInterface(uploadListener)
        api.execute()
    }

    private var pkgAppsList: MutableList<PackageInfo>? = null
        get() = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

    val receiver = DownloadService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setDisplayBackButton(false)

        updatePackageList()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(DownloadService.ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private fun updatePackageList(): Boolean {
        PackagesListApi(this).setmApiInterface(this).start()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.add("Add Package")
        item?.setOnMenuItemClickListener { addNewPackage() }
        val rItem = menu?.add("Refresh")
        rItem?.setOnMenuItemClickListener { updatePackageList() }
        rItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    private fun addNewPackage(): Boolean {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/vnd.android.package-archive"

        val i = Intent.createChooser(intent, "Select Apk file")
        startActivityForResult(i, 10)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                uri?.let { NewPackageDialog(this, it, this).show() }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 10) {
            if (grantResults.isEmpty()) {
                displayToast("We need storage permission to download files")
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSnackBarButtonClick() {
        updatePackageList()
    }
}
