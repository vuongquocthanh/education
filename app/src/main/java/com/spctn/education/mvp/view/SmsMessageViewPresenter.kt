package com.spctn.education.mvp.view

import com.spctn.education.GetSmsMessagesQuery
import com.spctn.education.mvp.View

interface SmsMessageViewPresenter: View {
    fun showSmsMessages(message: List<GetSmsMessagesQuery.Object>)
}