package com.syfract.sabt.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.base.BaseFragment
import com.base.api.BaseApiInterface
import com.extra.util.AccountManager
import com.syfract.sabt.R
import com.syfract.sabt.api.LoginApi
import com.syfract.sabt.api.RegisterApi
import com.syfract.sabt.main.MainActivity
import kotlinx.android.synthetic.main.fragment_register.*
import org.json.JSONObject

/**
 * Created by shayan4shayan on 4/5/18.
 *
 */
class FragmentRegister : BaseFragment(), BaseApiInterface {
    override fun onLogout() {

    }

    override fun onResponse(response: String?) {
        val obj = JSONObject(response)
        AccountManager.getInstanse(context).token = obj.getString("token")
        AccountManager.getInstanse(context).isLogin = true
        startActivity(MainActivity::class.java)
        finishActivity()
    }

    override fun onError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRegister.setOnClickListener { validateInputAndRegister() }
    }

    private val name
        get() = textName.text.toString()

    private val last
        get() = textLastName.text.toString()

    private val username
        get() = textUsername.text.toString()

    private val password
        get() = textPassword.text.toString()

    private val email
        get() = textEmail.text.toString()


    private fun validateInputAndRegister() {
        if (email.isEmpty() || name.isEmpty() || last.isEmpty() || password.isEmpty() || username.isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context!!, "input Error , please correct input", Toast.LENGTH_LONG).show()
            return
        }

        register()
    }

    private fun register() {
        displayProgressDialog()
        val callback = object : BaseApiInterface {
            override fun onLogout() {

            }

            override fun onResponse(response: String?) {
                LoginApi(context!!, username, password).setmApiInterface(this@FragmentRegister).start()
            }

            override fun onError(message: String?) {
                dismissDialog()
                displayToast(message!!)
            }
        }
        RegisterApi(context!!, name, last, email, username, password).setmApiInterface(callback).start()
    }
}