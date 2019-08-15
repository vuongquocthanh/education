package com.spctn.education.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spctn.education.GetAllSchoolQuery
import com.spctn.education.R
import kotlinx.android.synthetic.main.item_school_chose.view.*

class SchoolYearAdapter(private val context: Context, private val listener: SchoolYearListener,
                        private val schoolYears: List<GetAllSchoolQuery.Academic>)
    : RecyclerView.Adapter<SchoolYearAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_school_chose, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return schoolYears.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.tvName.text = schoolYears[p1].name()
        p0.bindClick(schoolYears[p1].id().toLong())
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindClick(id: Long){
            itemView.setOnClickListener { listener.itemSchoolYearClick(id) }
        }
    }

    interface SchoolYearListener{
        fun itemSchoolYearClick(id: Long)
    }

}