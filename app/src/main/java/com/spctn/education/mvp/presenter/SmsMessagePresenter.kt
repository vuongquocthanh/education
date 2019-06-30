package com.spctn.education.mvp.presenter

import android.util.Log
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.api.ApiUtil
import com.spctn.education.mvp.Presenter
import com.spctn.education.mvp.View
import com.spctn.education.mvp.view.SmsMessageViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SmsMessagePresenter: Presenter {
    private lateinit var viewPresenter: SmsMessageViewPresenter
    private val compositeDisposable = CompositeDisposable()

    fun getSmsMessage(page: Long, academicid: Long){
        val getSmsMessagesQuery: GetSmsMessagesQuery = GetSmsMessagesQuery.builder()
            .page(page)
            .academicid(academicid)
            .build()

        val getSmsMessageCall = ApiUtil.apolloClient()
            .query(getSmsMessagesQuery)

        compositeDisposable.add(Rx2Apollo.from(getSmsMessageCall)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReceiver, this::onError))
    }

    private fun onReceiver(response: Response<GetSmsMessagesQuery.Data>){
        if (response.data()!!.paginatedAcademicSmsmessages()!!.objects() != null){
            viewPresenter.showSmsMessages(response.data()!!.paginatedAcademicSmsmessages()!!.objects()!!)
        }else{
            viewPresenter.showError("Oop! Có lỗi xảy ra")
        }
    }

    private fun onError(throwable: Throwable){
        Log.d("SMSMESSAGELOG", throwable.message)
        viewPresenter.showError("Oop! Có lỗi xảy ra")
    }

    override fun onAttach(view: View) {
        viewPresenter = view as SmsMessageViewPresenter
    }

    override fun disapose() {
        compositeDisposable.dispose()
    }
}