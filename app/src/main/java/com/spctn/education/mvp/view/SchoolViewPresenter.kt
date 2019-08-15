package com.spctn.education.mvp.view

import com.spctn.education.GetAllSchoolQuery
import com.spctn.education.mvp.View

interface SchoolViewPresenter: View {
    fun showSchool(schools: List<GetAllSchoolQuery.AllSchool>)
}