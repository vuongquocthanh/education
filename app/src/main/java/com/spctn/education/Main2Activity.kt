package com.spctn.education

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import com.spctn.education.base.BaseActivity
import com.spctn.education.view.activity.LoginActivity
import com.spctn.education.view.fragment.MessageFragment
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.layout_drawer_menu.*
import kotlinx.android.synthetic.main.layout_toolbar_main.*

class Main2Activity : BaseActivity() {
    var ids = intArrayOf()
    private var messageFragment = MessageFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        for (i in ids.indices){
            ids[i] = i+1
        }
        loadFragment(messageFragment)
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
