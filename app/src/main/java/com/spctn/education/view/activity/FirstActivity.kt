package com.spctn.education.view.activity

import android.content.Intent
import android.os.Bundle
import com.spctn.education.Main2Activity
import com.spctn.education.R
import com.spctn.education.base.BaseActivity
import com.spctn.education.util.Constant

class FirstActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        if (tinyDB.getString(Constant.USER_NAME) != "" && tinyDB.getString(Constant.PASS_WORD) != ""){
            startActivity(Intent(this, Main2Activity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
