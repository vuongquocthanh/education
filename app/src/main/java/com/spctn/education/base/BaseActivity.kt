package com.spctn.education.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.spctn.education.R
import com.spctn.education.util.TinyDB
import com.wang.avi.AVLoadingIndicatorView

abstract class BaseActivity : AppCompatActivity(){
    lateinit var tinyDB: TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tinyDB = TinyDB(this)
    }

    fun progressLoadingDialog(context: Context): Dialog {
        val dialogBuilder = AlertDialog.Builder(context)
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