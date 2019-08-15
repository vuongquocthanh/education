package com.spctn.education.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import com.spctn.education.R
import com.spctn.education.base.BaseActivity
import com.spctn.education.util.Constant
import com.spctn.education.view.fragment.MessageFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer_menu.*
import kotlinx.android.synthetic.main.layout_toolbar_main.*

class MainActivity : BaseActivity() {
    private var messageFragment = MessageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTitle.text = getString(R.string.title_message)
        loadFragment(messageFragment)

        setEventClick()
    }

    private fun setEventClick(){
        ivMenu.setOnClickListener { menuClick() }
        clMessageManagement.setOnClickListener { clMessageFragmentClick() }
        clLogout.setOnClickListener { clLogoutClick() }
    }

    private fun menuClick() {
        drawerLayout.openDrawer(Gravity.START)
    }

    private fun clMessageFragmentClick() {
        drawerLayout.closeDrawers()
        tvTitle.text = getString(R.string.title_message)
        loadFragment(messageFragment)
    }

    private fun clLogoutClick(){
        tinyDB.clear()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun loadFragment(fragment: Fragment) {
        val backStateName = fragment.javaClass.name
        val manager = supportFragmentManager
        val curFragment = supportFragmentManager.findFragmentById(R.id.content)
        if (curFragment != null) {
            val ft = manager.beginTransaction()
            ft.replace(R.id.content, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        } else {
            val ft = manager.beginTransaction()
            ft.add(R.id.content, fragment)
//            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }
}
