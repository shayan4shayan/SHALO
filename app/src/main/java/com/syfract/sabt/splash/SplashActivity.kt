package com.syfract.sabt.splash

import android.os.Bundle
import com.base.BaseActivity
import com.extra.util.AccountManager
import com.syfract.sabt.R
import com.syfract.sabt.main.MainActivity
import com.syfract.sabt.signup.SignupActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (AccountManager.getInstanse(this).isLogin) {
            startActivity(MainActivity::class.java)
        } else {
            startActivity(SignupActivity::class.java)
        }

        finishActivity()
    }
}
