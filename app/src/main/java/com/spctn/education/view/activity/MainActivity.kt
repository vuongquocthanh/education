package com.spctn.education.view.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.View
import android.widget.Toast
import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.R
import com.spctn.education.base.BaseActivity
import com.spctn.education.mvp.presenter.SmsMessagePresenter
import com.spctn.education.mvp.view.SmsMessageViewPresenter
import com.spctn.education.view.adapter.SmsMessageAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : BaseActivity(), SmsMessageViewPresenter {
    private val smsMessagePresenter: SmsMessagePresenter = SmsMessagePresenter()

    private var progressLoading: Dialog? = null
    private lateinit var adapter: SmsMessageAdapter
    private var smsMessages: ArrayList<GetSmsMessagesQuery.Object> = ArrayList()

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
        progressLoading?.dismiss()
        smsMessages.addAll(message)
        adapter.notifyDataSetChanged()
    }

    override fun showError(message: String) {
        progressLoading?.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //--------------------------------------------------------------------------

    private fun loadSmsMessage(){
        progressLoading?.show()
        smsMessagePresenter.getSmsMessage(page!!, 1)
    }
    private fun setEventClick(){
        ivMore.setOnClickListener { moreClick() }
        btnSend.setOnClickListener {  }
    }

    private fun moreClick(){
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

}
