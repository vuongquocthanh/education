package com.spctn.education.view.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.R
import com.spctn.education.base.BaseActivity
import com.spctn.education.mvp.presenter.SmsMessagePresenter
import com.spctn.education.mvp.view.SmsMessageViewPresenter
import com.spctn.education.view.adapter.SmsMessageAdapter
import com.spctn.education.view.dialog.ConfirmSendSMSDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.lang.Exception

class MainActivity : BaseActivity(), SmsMessageViewPresenter {
    private lateinit var confirmSendSMSDialog: ConfirmSendSMSDialog
    private val smsMessagePresenter: SmsMessagePresenter = SmsMessagePresenter()
    private val SMS_PERMISSION_CODE = 0
    private var progressLoading: Dialog? = null
    private lateinit var adapter: SmsMessageAdapter
    private var smsMessages: ArrayList<GetSmsMessagesQuery.Object> = ArrayList()
    private var smsMessagesSelected: ArrayList<GetSmsMessagesQuery.Object> = ArrayList()
    private var page: Long? = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smsMessagePresenter.onAttach(this)
        progressLoading = progressLoadingDialog(this)
        loadSmsMessage()

        tvTitle.text = getString(R.string.title_message)

        adapter = SmsMessageAdapter(smsMessages)
        rvSmsMessage.layoutManager = LinearLayoutManager(this)
        rvSmsMessage.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager(this).orientation))
        rvSmsMessage.adapter = adapter

        ivBack.visibility = View.INVISIBLE
        setEventClick()

    }

    override fun onDestroy() {
        super.onDestroy()
        smsMessagePresenter.disapose()
    }

    //----------------------------SmsMessagePresenter--------------------------

    override fun showSmsMessages(message: List<GetSmsMessagesQuery.Object>) {
        srlSmsMessage.isRefreshing = false
        adapter.clearAllMessageSelected()
        progressLoading?.dismiss()
        smsMessages.clear()
        smsMessages.addAll(message)
        adapter.notifyDataSetChanged()
    }

    override fun showError(message: String) {
        srlSmsMessage.isRefreshing = false
        adapter.clearAllMessageSelected()
        progressLoading?.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //--------------------------------------------------------------------------

    private fun loadSmsMessage() {
        progressLoading?.show()
        smsMessagePresenter.getSmsMessage(page!!, 1)
    }

    private fun setEventClick() {
        ivMore.setOnClickListener { moreClick() }
        btnSend.setOnClickListener { btSendClick() }
        srlSmsMessage.setOnRefreshListener {
            srlSmsMessage.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark)
            loadSmsMessage()
        }
    }

    private fun moreClick() {
        val popupMenu = PopupMenu(this, ivMore)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout -> {
                    tinyDB.clear()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            false
        }
        popupMenu.show()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
            }
        }
    }

    private fun sendSMS(phone: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phone, null, message, null, null)
        } catch (e: Exception) {
            Toast.makeText(this, "Can't send Message!!", Toast.LENGTH_LONG).show()
            Log.d("ERROR", e.localizedMessage)
        }
    }

    private fun btSendClick() {
        checkPermission()
        confirmSendSMSDialog = ConfirmSendSMSDialog(object : ConfirmSendSMSDialog.ConfirmSendSMSListener {
            override fun btSendDialogClick() {
                if (smsMessagesSelected.isNullOrEmpty()) {
                    smsMessagesSelected.addAll(smsMessages)
                    for (i in 0 until smsMessagesSelected.size) {
                        sendSMS(smsMessagesSelected[i].phone()!!, smsMessagesSelected[i].content()!!)
                    }
                } else {
                    smsMessagesSelected.clear()
                    smsMessagesSelected.addAll(adapter.getAllMessageSelected())
                    for (i in 0 until smsMessagesSelected.size) {
                        sendSMS(smsMessagesSelected[i].phone()!!, smsMessagesSelected[i].content()!!)
                    }
                }
                Toast.makeText(this@MainActivity, "Đã gửi tin nhắn thành công!", Toast.LENGTH_LONG).show()
            }
        })
        confirmSendSMSDialog.show(supportFragmentManager, "Dialog")
    }

}
