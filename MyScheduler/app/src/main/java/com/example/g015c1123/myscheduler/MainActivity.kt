package com.example.g015c1123.myscheduler

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

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
        //Realmのテーブルに変更が生じたときに使う(Realmの設定の消去)
        //val realmconfigration=RealmConfiguration.Builder().build()
        //Realm.deleteRealm(realmconfigration)

        //listViewにデータベースの項目を表示する処理
        realm= Realm.getDefaultInstance()
        val schedules = realm.where<Schedule>().findAll()
        listView.adapter=ScheduleAdapter(schedules)


        fab.setOnClickListener {
           startActivity<ScheduleEditActivity>()
        }

        //listViewの項目を選択したときの処理
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/
}
