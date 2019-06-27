package com.sun.thread_handler_aysntask

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_asynctask.*
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AysncTaskActivity : AppCompatActivity(), View.OnClickListener {
    private var syn: AsyncTask<Int, String, Boolean>? = null
    private lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asynctask)
        executor = Executors.newFixedThreadPool(2)
        initView()
    }

    private fun initView() {
        btn_run_one.setOnClickListener(this)
        btn_run_two.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_run_one -> {
                runOne()
            }
            R.id.btn_run_two -> {
                runTwo()
            }
            R.id.btn_cancel -> {
                syn?.cancel(true)
            }
        }
    }

    private fun runOne() {
        syn = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Int, String, Boolean>() {
            override fun doInBackground(vararg p0: Int?): Boolean {
                //thuc hien tren thread khac
                for (i in 0 until p0[0]!!) {
                    if (isCancelled) break
                    SystemClock.sleep(500)
                    publishProgress(i.toString() + "")
                }
                val rd = Random()

                return rd.nextInt(2) == 0
            }

            override fun onPreExecute() {

            }

            override fun onProgressUpdate(vararg values: String) {
                tv_run_one.text = values[0]
            }

            override fun onPostExecute(result: Boolean?) {
                if (result!!) {
                    tv_run_one.text = "true"
                } else {
                    tv_run_one.text = "false"
                }
            }
        }
        syn?.execute(100)


    }

    private fun runTwo() {

        syn = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Int, String, Boolean>() {
            override fun doInBackground(vararg p0: Int?): Boolean {
                //thuc hien tren thread khac
                for (i in 0 until p0[0]!!) {
                    if (isCancelled) break
                    SystemClock.sleep(500)
                    publishProgress(i.toString() + "")
                }
                val rd = Random()

                return rd.nextInt(2) == 0
            }

            override fun onPreExecute() {

            }

            override fun onProgressUpdate(vararg values: String) {
                tv_run_two.text = values[0]
            }

            override fun onPostExecute(result: Boolean?) {
                if (result!!) {
                    tv_run_two.text = "true"
                } else {
                    tv_run_two.text = "false"
                }
            }
        }
        syn?.executeOnExecutor(executor, 100)


    }
}

