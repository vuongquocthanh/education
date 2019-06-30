package com.spctn.education.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.R
import kotlinx.android.synthetic.main.item_sms_message.view.*

class SmsMessageAdapter (private var messages: List<GetSmsMessagesQuery.Object>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return SmsMessageViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_sms_message, p0, false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val message = messages[p1]
        p0.itemView.tvSmsMessage.text = message.content()
        p0.itemView.tvPhoneNum.text = message.phone()
    }

    internal class SmsMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}