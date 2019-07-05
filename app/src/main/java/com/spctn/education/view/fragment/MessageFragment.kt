package com.spctn.education.view.fragment

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.R
import com.spctn.education.base.BaseFragment
import com.spctn.education.mvp.presenter.SmsMessagePresenter
import com.spctn.education.mvp.view.SmsMessageViewPresenter
import com.spctn.education.view.adapter.SmsMessageAdapter
import com.spctn.education.view.dialog.ConfirmSendSMSDialog
import kotlinx.android.synthetic.main.fragment_message.*

class MessageFragment : BaseFragment(), SmsMessageViewPresenter {

    private lateinit var confirmSendSMSDialog: ConfirmSendSMSDialog
    private val smsMessagePresenter: SmsMessagePresenter = SmsMessagePresenter()
    private val SMS_PERMISSION_CODE = 0
    private var progressLoading: Dialog? = null
    private lateinit var adapter: SmsMessageAdapter
    private var smsMessages: ArrayList<GetSmsMessagesQuery.Object> = ArrayList()
    private var ids:ArrayList<String> = ArrayList()
    private var smsMessagesSelected: ArrayList<GetSmsMessagesQuery.Object> = ArrayList()
    private var page: Long? = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()

        smsMessagePresenter.onAttach(this)
        progressLoading = progressLoadingDialog(context!!)
        loadSmsMessage()

        adapter = SmsMessageAdapter(smsMessages)
        rvSmsMessage.layoutManager = LinearLayoutManager(context!!)
        rvSmsMessage.addItemDecoration(DividerItemDecoration(context!!, LinearLayoutManager(context!!).orientation))
        rvSmsMessage.adapter = adapter

//        ivBack.visibility = View.INVISIBLE

        setEventClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        smsMessagePresenter.disapose()
    }

    override fun showSmsMessages(message: List<GetSmsMessagesQuery.Object>) {
        srlSmsMessage.isRefreshing = false
        progressLoading?.dismiss()
        smsMessages.clear()
        smsMessages.addAll(message)
        adapter.notifyDataSetChanged()
    }

    override fun showError(message: String) {
        srlSmsMessage.isRefreshing = false
        adapter.clearAllMessageSelected()
        progressLoading?.dismiss()
        Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
    }

    //--------------------------------------------------------------------------

    private fun loadSmsMessage() {
        progressLoading?.show()
        smsMessagePresenter.getSmsMessage(page!!, 1)
    }

    private fun setEventClick() {
//        ivMore.setOnClickListener { moreClick() }
        btnSend.setOnClickListener { btSendClick() }
        srlSmsMessage.setOnRefreshListener {
            smsMessagesSelected.clear()
            adapter.clearAllMessageSelected()
            srlSmsMessage.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
            loadSmsMessage()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                context!!, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE),
                    SMS_PERMISSION_CODE
                )
            }
        }
    }

    private fun sendSMS(id:String, phone: String, message: String) {
        Log.d("PHONELOG", phone)
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phone, null, message, null, null)
            ids.add(id)
            
        } catch (e: Exception) {
            Toast.makeText(context!!, "Không thể gửi tin nhắn!!", Toast.LENGTH_LONG).show()
            Log.d("ERROR", e.localizedMessage)
        }

    }

    private fun btSendClick() {
        confirmSendSMSDialog = ConfirmSendSMSDialog(object : ConfirmSendSMSDialog.ConfirmSendSMSListener {
            override fun btSendDialogClick() {
                smsMessagesSelected.addAll(adapter.getAllMessageSelected())
                if (smsMessagesSelected.size <= 0) {
                    smsMessagesSelected.addAll(smsMessages)
                }

                for (i in 0 until smsMessagesSelected.size) {
                    sendSMS(smsMessagesSelected[i].id(),smsMessagesSelected[i].phone()!!, smsMessagesSelected[i].content()!!)
                }

                Toast.makeText(activity!!, "Đã gửi tin nhắn thành công!", Toast.LENGTH_LONG).show()
            }
        })
        confirmSendSMSDialog.show(activity!!.supportFragmentManager, "Dialog")
    }
}