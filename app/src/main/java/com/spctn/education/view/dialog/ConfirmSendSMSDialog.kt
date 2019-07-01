package com.spctn.education.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.spctn.education.R
import kotlinx.android.synthetic.main.dialog_confirm.view.*

@SuppressLint("ValidFragment")
class ConfirmSendSMSDialog(private var listener:ConfirmSendSMSListener) : DialogFragment(){
    private lateinit var viewDialog:View


    interface ConfirmSendSMSListener{
        fun btSendDialogClick()
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        viewDialog = inflater.inflate(R.layout.dialog_confirm, null)
        builder.setView(viewDialog)
        viewDialog.btSend.setOnClickListener {
            listener.btSendDialogClick()
            dismiss()
        }
        viewDialog.btCancel.setOnClickListener {
            dismiss()
        }
        return builder.create()
    }
}