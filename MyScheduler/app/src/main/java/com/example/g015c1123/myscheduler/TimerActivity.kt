package com.example.g015c1123.myscheduler

import android.media.AudioManager
import android.media.SoundPool
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.content.Intent
import kotlinx.android.synthetic.main.activity_timer.*
import org.jetbrains.anko.startActivity

class TimerActivity : AppCompatActivity() {
    private lateinit var soundPool: SoundPool
    private var soundResId =0

    inner class TimerActivity(millisInFuture: Long,countDownInterval: Long):
            CountDownTimer(millisInFuture, countDownInterval){
        var isRunning = false
        var countmin:Long=0

        //時間の反映
        override fun onTick(millisUntilFinished: Long){
            var minute:Long = millisUntilFinished / 1000L / 60L
            var second:Long = millisUntilFinished /1000L % 60L
            //%1d=minute,%2$02d=secondを2桁で
            timerText.text="%1$02d:%2$02d".format(minute,second)
            countmin=millisUntilFinished
        }

        override fun onFinish(){
            timerText.text="00:00"
            soundPool.play(soundResId,1.0f,100f,0,0,1.0f)
            return
        }
    }

    //main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        var taskmin=intent?.getLongExtra("TASK_TIME",1)
        //テスト用変数
        //var min:Long=1*1000L*60L
        if (taskmin!! <10) {
            timerText.text="0${taskmin}:00"
        }else{
            timerText.text="${taskmin}:00"
        }

        var timer = TimerActivity(taskmin*1000L*60L,100)

        //テスト用
        // timerText.text="01:00"
        playStop.setOnClickListener {
            when(timer.isRunning){
                true ->timer.apply {
                    isRunning=false
                    timer=TimerActivity(countmin,100)
                    countmin=timer.countmin
                    cancel()
                    playStop.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                }
                false ->timer.apply {
                    isRunning=true
                    start()
                    playStop.setImageResource(R.drawable.ic_stop_black_24dp)
                }
            }
        }

        back.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        soundPool = SoundPool(2, AudioManager.STREAM_ALARM,0)
        soundResId = soundPool.load(this,R.raw.bellsound,1)
    }

    override fun onPause() {
        super.onPause()
        soundPool.release()

    }
}
