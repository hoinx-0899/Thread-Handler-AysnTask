package com.sun.thread_handler_aysntask

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), View.OnClickListener, Runnable {

    private lateinit var thread: Thread
    private lateinit var handler: Handler
    private lateinit var handler2: Handler
    private var handlerPost: Handler = Handler()

    private lateinit var timerTask: TimerTask
    private lateinit var timer: Timer

    private var sdf = SimpleDateFormat("hh:mm:ss aaa")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Hanlder using Message
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 1) {
                    val student = msg.obj as Student
                    tv_run.text = student.name + student.age
                } else {
                    tv_run1.text = msg.arg1.toString() + ""
                }
            }
        }

        handler2 = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                Log.d("sum", msg.arg1.toString())
                tv_run5.text = msg.arg1.toString()
            }
        }
        registerListener()
    }

    private fun registerListener() {
        btn_run.setOnClickListener(this)
        btn_run1.setOnClickListener(this)
        btn_run2.setOnClickListener(this)
        btn_run3.setOnClickListener(this)
        btn_run5.setOnClickListener(this)
        btn_run4.setOnClickListener(this)

    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_run -> {
                thread = Thread(this)
                thread.start()
            }
            R.id.btn_run1 -> {
                Thread {
                    for (i in 0..99) {
                        val message = Message()
                        message.what = 2
                        message.arg1 = i
                        message.target = handler
                        message.sendToTarget()
                        SystemClock.sleep(500)
                    }
                }.start()

            }
            R.id.btn_run2 -> {

                Thread(Runnable {
                    for (i in 0..99) {
                        SystemClock.sleep(500)
                        //Using handler Post
                        handlerPost.post {
                            tv_run2.text = i.toString()
                        }
                    }
                }).start()

            }
            R.id.btn_run3 -> {
                val intent = Intent(this, AysncTaskActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_run5 -> {

                val taskExecutor = Executors.newFixedThreadPool(2)
                //Có 2 thread trong pool. Mỗi thread sẽ thực hiện tính toán tổng của mảng gồm 10 phần tử
                for (i in 0 until 2) {
                    taskExecutor.execute {
                        val threadId = (Thread.currentThread().id % 2).toInt()
                        Log.d("Thread-id", threadId.toString())
                        val start = threadId * 10 / 2
                        val stop = threadId * 10 / 2 + 10 / 2
                        var sum = 0
                        //Có 2 thread tính toán thì mỗi thread tính một nửa số phẩn tử trong mảng
                        for (j in start until stop) {
                            sum += list()[j]
                        }
                        val message = Message()
                        message.what = threadId
                        message.arg1 = sum
                        message.target = handler2
                        message.sendToTarget()
                    }
                }

            }

            R.id.btn_run4 -> {
                val random = Random()
                timerTask = object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            val calendar = Calendar.getInstance()
                            val value = sdf.format(calendar.time)
                            txtTimer.text = value
                            val alpha = 225
                            val red = random.nextInt(256)
                            val green = random.nextInt(256)
                            val blue = random.nextInt(256)
                            val color = Color.argb(alpha, red, green, blue)
                            background.setBackgroundColor(color)
                        }
                    }
                }
                timer = Timer()
                timer.schedule(timerTask, 0, 1000)
            }

        }

    }

    @Synchronized
    override fun run() {
        for (i in 0..99) {
            //Tạo ra Message
            val message = Message()
            //Đưa dữ liệu vào message
            message.arg1 = i
            message.obj = Student("Hội", i.toString() + "")
            //What là id của message
            message.what = 1
            //Đưa message vào handler
            message.target = handler
            //Gửi message qua Main Thread UI
            message.sendToTarget()

            SystemClock.sleep(500)
        }

    }

    private fun list(): IntArray {
        val anArray = IntArray(10)
        for (i in 0 until anArray.size) {
            anArray[i] = i
        }
        return anArray
    }


}
