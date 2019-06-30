package com.spctn.education.mvp.presenter

import android.annotation.SuppressLint
import android.util.Log
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.spctn.education.LoginMutation
import com.spctn.education.api.ApiUtil
import com.spctn.education.mvp.Presenter
import com.spctn.education.mvp.View
import com.spctn.education.mvp.view.LoginViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter : Presenter{
    private lateinit var viewPresenter: LoginViewPresenter
    private val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun login(email: String, password: String){
        val loginMutation: LoginMutation = LoginMutation.builder()
            .email(email)
            .password(password)
            .build()

        val loginCall = ApiUtil.apolloClient()
            .mutate(loginMutation)

        compositeDisposable.add(Rx2Apollo.from(loginCall)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onReceiver, this::onError))
    }

    private fun onReceiver(response: Response<LoginMutation.Data>){
        Log.d("LOGINLOG", "respone: "+ response.toString())
        if(response.data()!!.tokenAuth() != null){
            viewPresenter.loginSuccess(response.data()!!.tokenAuth()!!)
        }else{
            viewPresenter.showError("Đăng nhập thất bại")
        }
    }

    private fun onError(throwable: Throwable){
        Log.d("LOGINLOG", "error: "+throwable.message)
        viewPresenter.showError("Đăng nhập thất bại")
    }

    override fun onAttach(view: View) {
        viewPresenter = view as LoginViewPresenter
    }

    override fun disapose() {
        compositeDisposable.dispose()
    }
}