package com.example.g015c1123.myscheduler

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.support.v4.app.FragmentActivity
import android.widget.DatePicker


class ScheduleEditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var realm: Realm

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_edit)
        realm=Realm.getDefaultInstance()
        taskmin.maxValue=90
        taskmin.minValue=1
        taskmin.wrapSelectorWheel=true
        taskmin.setOnValueChangedListener { taskmin, oldVal, newVal ->min_text.text="$newVal"+"分"  }

        val scheduleId = intent?.getLongExtra("schedule_id",-1L)
        if(scheduleId !=-1L){
            val schedule = realm.where<Schedule>().equalTo("id",scheduleId).findFirst()
            dateEdit.setText(android.text.format.DateFormat.format("yyyy/MM/dd",schedule?.date))
            titleEdit.setText(schedule?.title)
            detailEdit.setText(schedule?.detail)
            taskmin.value= schedule?.time!!
            min_text.text="${taskmin.value}"+"分"
            delete.visibility=View.VISIBLE
            task.visibility=View.VISIBLE
        }else{
            delete.visibility=View.INVISIBLE
            task.visibility=View.INVISIBLE
        }

        save.setOnClickListener {
            when(scheduleId){
                -1L ->{
                    realm.executeTransaction {
                        val maxId = realm.where<Schedule>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1
                        val schedule = realm.createObject<Schedule>(nextId)
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            schedule.date = it
                        }
                        schedule.title = titleEdit.text.toString()
                        schedule.detail = detailEdit.text.toString()
                        schedule.time = taskmin.value
                    }
                    alert("追加しました") {
                        yesButton { finish() }
                    }.show()
                }
                else ->{
                realm.executeTransaction {
                    val schedule = realm.where<Schedule>().equalTo("id",scheduleId).findFirst()
                    dateEdit.text.toString().toDate("yyyy/MM/dd")?.let{
                        schedule?.date=it
                    }
                    schedule?.title=titleEdit.text.toString()
                    schedule?.detail=detailEdit.text.toString()
                    schedule?.time=taskmin.value
                }
                    alert("修正しました") {
                        yesButton { finish() }
                    }.show()
                }
            }
        }

        editcancel.setOnClickListener { finish() }

        delete.setOnClickListener {
            realm.executeTransaction {
                realm.where<Schedule>().equalTo("id",scheduleId)?.findFirst()?.deleteFromRealm()
            }
            alert("削除しました") {
                yesButton { finish() }
            }.show()
        }

        task.setOnClickListener {
            var intent=Intent(this,TimerActivity::class.java)
             intent.putExtra("TASK_TIME",taskmin.value.toLong())
             startActivity(intent)
             finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"):Date?{
        val sdFormat = try {
            SimpleDateFormat(pattern)
        }catch (e:IllegalAccessException){
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            }catch (e:ParseException){
                null
            }
        }
        return date
    }


    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        var str = String.format(Locale.US, "%d/%d/%d", year, monthOfYear+1, dayOfMonth)
        dateEdit.text = str
    }


    fun showDatePickerDialog(v: View) {
        val newFragment = DatePick()
        newFragment.show(supportFragmentManager, "datePicker")

    }

}
