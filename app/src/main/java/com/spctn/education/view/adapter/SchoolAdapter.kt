package com.spctn.education.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spctn.education.GetAllSchoolQuery
import com.spctn.education.R
import kotlinx.android.synthetic.main.item_school_chose.view.*

class SchoolAdapter(private val context: Context, private val listener: SchoolListener, private val schools: List<GetAllSchoolQuery.AllSchool>)
    : RecyclerView.Adapter<SchoolAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_school_chose, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return schools.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemView.tvName.text = schools[p1].name()
        p0.bindClick(p1)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindClick(position: Int){
            itemView.setOnClickListener { listener.itemClick(position) }
        }
    }

    interface SchoolListener{
        fun itemClick(position: Int)
    }

}