package com.example.g015c1123.myscheduler

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat.startActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import org.jetbrains.anko.startActivity
import java.security.AccessController

class ScheduleAdapter(data: OrderedRealmCollection<Schedule>?) : RealmBaseAdapter<Schedule>(data) {

    inner class ViewHolder(cell: View){
        val date=cell.findViewById<TextView>(R.id.text1)
        val title=cell.findViewById<TextView>(R.id.text2)
        val fab=cell.findViewById<FloatingActionButton>(R.id.FAB)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        when(convertView){
            null->{
                val inflater = LayoutInflater.from(parent?.context)
                //変更箇所
                //view = inflater.inflate(android.R.layout.simple_list_item_2,parent,false)
                view = inflater.inflate(R.layout.list_item,parent,false)
                viewHolder = ViewHolder(view)
                view.tag=viewHolder
            }
            else->{
                view=convertView
                viewHolder=view.tag as ViewHolder
            }
        }

        //項目が選択されたときの処理
        adapterData?.run {
            val schedule = get(position)
            viewHolder.date.text=android.text.format.DateFormat.format("yyyy/MM/dd",schedule.date)
            viewHolder.title.text=schedule.title
            viewHolder.fab.setOnClickListener {
                var intent = Intent(view.context,TimerActivity::class.java)
                intent.putExtra("TASK_TIME",schedule.time.toLong())
                view.context.startActivity(intent)
            }
        }
        return view
    }
}