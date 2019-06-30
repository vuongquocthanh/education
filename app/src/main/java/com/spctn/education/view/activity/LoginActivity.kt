package com.spctn.education.view.activity

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.spctn.education.LoginMutation
import com.spctn.education.R
import com.spctn.education.base.BaseActivity
import com.spctn.education.mvp.presenter.LoginPresenter
import com.spctn.education.mvp.view.LoginViewPresenter
import com.spctn.education.util.Constant
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginViewPresenter {
    private val loginPresenter: LoginPresenter = LoginPresenter()
    private var progressLoading: Dialog? = null
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        loginPresenter.onAttach(this)

        progressLoading = progressLoadingDialog(this)

        setEventClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.disapose()
    }

    //---------------------------------LoginPresenter---------------------------------

    override fun loginSuccess(token: LoginMutation.TokenAuth) {
        progressLoading?.dismiss()
        tinyDB.putString(Constant.ACCESS_TOKEN, token.token()!!)
        if (cbRemmember.isChecked){
            tinyDB.putString(Constant.USER_NAME, email)
            tinyDB.putString(Constant.PASS_WORD, password)
        }

        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showError(message: String) {
        progressLoading?.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //---------------------------------------------------------------------------------

    private fun setEventClick(){
        btnLogin.setOnClickListener {
            loginClick()
        }
    }

    private fun loginClick(){
        email = edAccount.text.toString().trim()
        password = edPassword.text.toString().trim()
        if (email != "" && password != ""){
            progressLoading?.show()
            loginPresenter.login(email, password)
        }else{
            Toast.makeText(this, "Yêu cầu nhập đầy đủ thông tin", Toast.LENGTH_LONG).show()
        }
    }
}

