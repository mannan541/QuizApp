package com.android.tutorial.activity

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView
import com.android.tutorial.R
import kotlinx.android.synthetic.main.activity_stop_watch.*

class StopWatch : AppCompatActivity() {


    var handler: Handler? = null
    var hour: TextView? = null
    var minute: TextView? = null
    var seconds: TextView? = null
    var milli_seconds: TextView? = null

    internal var MillisecondTime: Long = 0
    internal var StartTime: Long = 0
    internal var TimeBuff: Long = 0
    internal var UpdateTime = 0L


    internal var Seconds: Int = 0
    internal var Minutes: Int = 0
    internal var MilliSeconds: Int = 0

    internal var flag: Boolean = false

    var startButton: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_watch)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.hide();
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        startButton = findViewById(R.id.startButton) as ImageButton?
        bindViews()
    }

    private fun bindViews() {

        hour = findViewById(R.id.hour) as TextView?
        minute = findViewById(R.id.minute) as TextView?
        seconds = findViewById(R.id.seconds) as TextView?
        milli_seconds = findViewById(R.id.milli_second) as TextView?


        startButton?.setOnClickListener {
            if (flag) {
                handler?.removeCallbacks(runnable)
                startButton?.setImageResource(R.drawable.ic_play)
                flag = false
            } else {
                startButton?.setImageResource(R.drawable.pause)
                StartTime = SystemClock.uptimeMillis()
                handler?.postDelayed(runnable, 0)
                flag = true
            }

        }

        handler = Handler()

    }


    var runnable: Runnable = object : Runnable {

        override fun run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime

            UpdateTime = TimeBuff + MillisecondTime

            Seconds = (UpdateTime / 1000).toInt()

            Minutes = Seconds / 60

            Seconds = Seconds % 60

            MilliSeconds = (UpdateTime % 1000).toInt()


            if (Minutes.toString().length < 2) {
                minute?.text = "0" + Minutes.toString()
            } else {
                minute?.text = Minutes.toString()
            }
            if (Seconds.toString().length < 2) {
                seconds?.text = "0" + Seconds.toString()
            } else {
                seconds?.text = Seconds.toString()
            }

            milli_seconds?.text = MilliSeconds.toString()

            handler?.postDelayed(this, 0)
        }

    }

}
