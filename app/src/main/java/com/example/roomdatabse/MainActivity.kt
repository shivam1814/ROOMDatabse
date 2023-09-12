package com.example.roomdatabse

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.roomdatabse.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appDb: AppDatabase
    private val tag = "roomDatabase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        appDb = AppDatabase.getDatabase(this)

        binding.btnInsert.setOnClickListener {
            writeData()
        }

        binding.btnRead.setOnClickListener {
            readData()
        }

        binding.btnDelete.setOnClickListener {
            deleteData()
        }

        binding.btnDeleteOne.setOnClickListener {
            deleteOne()
        }

        binding.btnGetAllData.setOnClickListener {
            getAllData()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeData() {

        val firstName = binding.etxtFname.text.toString()
        val lastName = binding.etxtLname.text.toString()
        val rollNo = binding.etxtRollno.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            val student = Student(null, firstName, lastName, rollNo.toInt())
            GlobalScope.launch {
                val data = appDb.studentDao().findRollNoData(rollNo.toInt())
                if (data) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@MainActivity, "enter different roll no", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    appDb.studentDao().insert(student)
                    binding.etxtFname.text.clear()
                    binding.etxtLname.text.clear()
                    binding.etxtRollno.text.clear()
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@MainActivity, "successfully written", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "please enter data", Toast.LENGTH_SHORT).show()
        }

    }

    private suspend fun displayData(student: Student) {

        withContext(Dispatchers.Main) {

            binding.txtFname.text = student.firstName
            binding.txtLname.text = student.lastName
            binding.txtRollNo.text = student.rollNo.toString()

        }

    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    private fun readData() {

        val rollNo = binding.etxtRollnoSearch.text.toString()

        if (rollNo.isNotEmpty()) {

            GlobalScope.launch {
                val dataExist = appDb.studentDao().findRollNoData(rollNo.toInt())
                if (dataExist) {
                    Log.d("dataExists", "data found")
                    val data = appDb.studentDao().getRollNoData(rollNo.toInt())
                    displayData(data)
                } else {
                    Log.d("dataExists", "data not found")
                    binding.txtFname.text = "data not found"
                    binding.txtRollNo.text = ""
                    binding.txtLname.text = ""
                }
            }

        } else {
            Toast.makeText(this, "enter specific number", Toast.LENGTH_SHORT).show()
        }

    }


    private fun deleteData() {

        GlobalScope.launch {
            appDb.studentDao().deletAll()
        }

    }

    private fun deleteOne() {

        val rollNo = binding.etxtRollnoSearch.text.toString()

        if (rollNo.isNotEmpty()) {

            GlobalScope.launch {
                appDb.studentDao().delete(rollNo.toInt())
            }

        } else {
            Toast.makeText(this, "enter specific roll no", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getAllData() {
        GlobalScope.launch {
            val data = appDb.studentDao().getAll()
            Log.d(tag, "$data")
        }
    }

}

//this is room database project.