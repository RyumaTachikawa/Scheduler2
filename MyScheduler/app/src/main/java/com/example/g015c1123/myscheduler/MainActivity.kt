package com.example.g015c1123.myscheduler

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v4.app.FragmentActivity
import android.widget.DatePicker
import android.widget.TimePicker

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        repo_button.setOnClickListener {
            val intent = Intent(this,ReportActivity::class.java)
            startActivity(intent)
        }

        se_button.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)
        }

        //Realmのテーブルに変更が生じたときに使う
        //val realmconfigration=RealmConfiguration.Builder().build()
        //Realm.deleteRealm(realmconfigration)


        realm= Realm.getDefaultInstance()
        val schedules = realm.where<Schedule>().findAll()
        listView.adapter=ScheduleAdapter(schedules)

        fab.setOnClickListener {
           startActivity<ScheduleEditActivity>()
        }

        listView.setOnItemClickListener { parent , view , position , id ->
            val schedule = parent.getItemAtPosition(position) as Schedule
            startActivity<ScheduleEditActivity>("schedule_id" to schedule.id)
        }

    }

    /*override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        getMenuInflater().inflate(R.menu.menu_main,menu)
        menu.setHeaderTitle("コンテキストメニュー")

    }*/

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

/*
    //オプションメニュー
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.getItemId()) {
            R.string.action_settings ->{
                val intent = Intent(this,ReportActivity::class.java)
                return true}
            else -> {super.onOptionsItemSelected(item)
                    return false}
        }


       when (item.itemId) {
            R.id.action_report-> {
                var intent = Intent(this,ReportActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }*/

    //}

}
