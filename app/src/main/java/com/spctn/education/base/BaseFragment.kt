package com.spctn.education.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.Fragment
import com.spctn.education.R
import com.spctn.education.util.TinyDB
import com.wang.avi.AVLoadingIndicatorView

abstract class BaseFragment : Fragment() {
    protected var mContext: Context? = null
    protected lateinit var mActivity: Activity

    lateinit var tinyDB: TinyDB

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is Activity) mActivity = context
        mContext = context

        tinyDB = TinyDB(context!!)
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    @SuppressLint("InflateParams")
    fun progressLoadingDialog(context: Context): Dialog {
        val dialogBuilder = android.support.v7.app.AlertDialog.Builder(context)
        val inflater = layoutInflater
        val viewDialog = inflater.inflate(R.layout.dialog_load_progress, null)
        dialogBuilder.setView(viewDialog)
        val dialog = dialogBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val avi = viewDialog.findViewById<AVLoadingIndicatorView>(R.id.avi)
        avi.show()
        return dialog
    }
}