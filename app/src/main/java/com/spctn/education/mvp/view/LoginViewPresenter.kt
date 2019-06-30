package com.spctn.education.mvp.view

import com.spctn.education.LoginMutation
import com.spctn.education.mvp.View

interface LoginViewPresenter: View {
    fun loginSuccess(token: LoginMutation.TokenAuth)
}