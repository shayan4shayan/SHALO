package com.syfract.sabt.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.BaseFragment
import com.base.api.BaseApiInterface
import com.extra.util.AccountManager
import com.syfract.sabt.R
import com.syfract.sabt.api.LoginApi
import com.syfract.sabt.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONObject

/**
 * Created by shayan4shayan on 4/5/18.
 */
class FragmentLogin : BaseFragment(), BaseApiInterface {
    override fun onLogout() {

    }

    override fun onError(message: String?) {
        displayToast(R.string.error_login)
    }

    override fun onResponse(response: String?) {
        val obj = JSONObject(response)
        AccountManager.getInstanse(context).token = obj.getString("token")
        AccountManager.getInstanse(context).isLogin = true
        startActivity(MainActivity::class.java)
        finishActivity()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        login.setOnClickListener { performLogin() }
    }

    private fun performLogin() {
        val username = textUsername.text.toString()
        val password = textPassword.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            displayToast(R.string.msg_username_or_password_empty)
        } else {
            sendLoginRequest(username, password)
        }
    }

    private fun sendLoginRequest(username: String, password: String) {
        LoginApi(context!!, username, password).setmApiInterface(this)
                .start()
    }
}