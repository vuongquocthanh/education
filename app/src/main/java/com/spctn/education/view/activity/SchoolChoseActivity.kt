package com.spctn.education.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.spctn.education.GetAllSchoolQuery
import com.spctn.education.R
import com.spctn.education.base.BaseActivity
import com.spctn.education.mvp.presenter.SchoolPresenter
import com.spctn.education.mvp.view.SchoolViewPresenter
import com.spctn.education.util.Constant
import com.spctn.education.view.adapter.SchoolAdapter
import com.spctn.education.view.adapter.SchoolYearAdapter
import kotlinx.android.synthetic.main.activity_school_chose.*

class SchoolChoseActivity : BaseActivity(), SchoolViewPresenter, SchoolAdapter.SchoolListener, SchoolYearAdapter.SchoolYearListener {

    private val schoolPresenter: SchoolPresenter = SchoolPresenter()

    private lateinit var schoolAdapter: SchoolAdapter
    private lateinit var schoolYearAdapter: SchoolYearAdapter
    private val schools: ArrayList<GetAllSchoolQuery.AllSchool> = ArrayList()
    private val schoolYears: ArrayList<GetAllSchoolQuery.Academic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_chose)

        schoolPresenter.onAttach(this)
        loadSchool()

        schoolAdapter = SchoolAdapter(this, this, schools)
        rvSchool.layoutManager = LinearLayoutManager(this)
        rvSchool.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager(this).orientation))
        rvSchool.adapter = schoolAdapter

        schoolYearAdapter = SchoolYearAdapter(this, this, schoolYears)
        rvSchoolYear.layoutManager = LinearLayoutManager(this)
        rvSchoolYear.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager(this).orientation))
        rvSchoolYear.adapter = schoolYearAdapter
    }

    override fun onDestroy() {
        schoolPresenter.disapose()
        super.onDestroy()
    }


    // ---------------------------------- SchoolAdapter.SchoolListener ----------------------------------

    override fun itemClick(position: Int) {
        schoolYears.addAll(schools[position].academics()!!)
        schoolYearAdapter.notifyDataSetChanged()
    }


    // ---------------------------------- SchoolYearAdapter.SchoolYearListener ----------------------------------

    override fun itemSchoolYearClick(id: Long) {
        tinyDB.putLong(Constant.ACADEMIC_ID, id)
        startActivity(Intent(this, MainActivity::class.java))
    }


    // ---------------------------------- SmsMessageViewPresenter ----------------------------------

    override fun showSchool(schools: List<GetAllSchoolQuery.AllSchool>) {
        this.schools.addAll(schools)
        schoolAdapter.notifyDataSetChanged()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    // ---------------------------------- Class Functions ----------------------------------

    private fun loadSchool(){
        schoolPresenter.getSchool()
    }

}
