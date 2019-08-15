package com.spctn.education.mvp.presenter

import com.apollographql.apollo.rx2.Rx2Apollo
import com.spctn.education.GetAllSchoolQuery
import com.spctn.education.api.ApiUtil
import com.spctn.education.mvp.Presenter
import com.spctn.education.mvp.View
import com.spctn.education.mvp.view.SchoolViewPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.apollographql.apollo.api.Response

class SchoolPresenter: Presenter {
    private lateinit var viewPresenter: SchoolViewPresenter
    private val compositeDisposable = CompositeDisposable()

    override fun onAttach(view: View) {
        viewPresenter = view as SchoolViewPresenter
    }

    fun getSchool(){
        val getSchoolQuery: GetAllSchoolQuery = GetAllSchoolQuery.builder()
            .build()

        val getAllSchoolQuery = ApiUtil.apolloClient()
            .query(getSchoolQuery)

        compositeDisposable.add(
            Rx2Apollo.from(getAllSchoolQuery)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onReceiver, this::onError))
    }

    private fun onReceiver(response: Response<GetAllSchoolQuery.Data> ){
        if (response.data()!!.allSchool()!!.size >0){
            viewPresenter.showSchool(response.data()!!.allSchool()!!)
        }
    }

    private fun onError(throwable: Throwable){

    }

    override fun disapose() {
        compositeDisposable.dispose()
    }
}