package com.syfract.sabt.signup

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.util.Log
import com.base.BaseActivity
import com.base.IFragment
import com.syfract.sabt.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : BaseActivity(), TabLayout.OnTabSelectedListener, IFragment {

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }


    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        Log.d("TAG", tab!!.tag.toString())
        when (tab.tag) {
            LOGIN -> attachLogin()
            REGISTER -> attachRegister()
        }
    }

    companion object {
        const val LOGIN = "login"
        const val REGISTER = "register"
    }

    lateinit var loginFragment: FragmentLogin
    lateinit var registerFragment: FragmentRegister

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val login = tabLayout.newTab().setText(R.string.login).setTag(LOGIN)
        val register = tabLayout.newTab().setText(R.string.register).setTag(REGISTER)
        tabLayout.addTab(login)
        tabLayout.addTab(register)
        tabLayout.addOnTabSelectedListener(this)

        loginFragment = FragmentLogin()
        loginFragment.setListener(this)
        registerFragment = FragmentRegister()
        registerFragment.setListener(this)

        supportFragmentManager?.beginTransaction()?.add(R.id.layout, loginFragment)?.commit()
    }

    private fun attachLogin() {
        supportFragmentManager?.beginTransaction()?.replace(R.id.layout, loginFragment)?.commit()
    }

    private fun attachRegister() {
        supportFragmentManager?.beginTransaction()?.replace(R.id.layout, registerFragment)?.commit()
    }
}
