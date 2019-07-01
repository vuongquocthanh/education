package com.spctn.education.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.R
import kotlinx.android.synthetic.main.item_sms_message.view.*

class SmsMessageAdapter (private var messages: List<GetSmsMessagesQuery.Object>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var messageSelected: ArrayList<GetSmsMessagesQuery.Object> = ArrayList()

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
        p0.itemView.setOnClickListener {
            if (messageSelected.contains(message)){
                messageSelected.remove(message)
                unHighLight(p0)
            }else{
                messageSelected.add(message)
                highLight(p0)
            }
        }

        if (messageSelected.contains(message)){
            highLight(p0)
        }else{
            unHighLight(p0)
        }
    }

    internal class SmsMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private fun highLight(holder:RecyclerView.ViewHolder){
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
        holder.itemView.imgWarning.setImageResource(R.drawable.ic_warning_selected)
    }
    private fun unHighLight(holder:RecyclerView.ViewHolder){
        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.colorWhite))
        holder.itemView.imgWarning.setImageResource(R.drawable.ic_warning_unselected)
    }
    fun getAllMessageSelected():ArrayList<GetSmsMessagesQuery.Object>{
        return messageSelected
    }
    fun clearAllMessageSelected(){
        messageSelected.clear()
    }

}