package com.example.mytest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.contracts.*


class MainActivity : AppCompatActivity() {

    lateinit var convertButton: Button
    private lateinit var currencyTextTo: TextView
    private lateinit var currencyTextFrom: TextView
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner


    //сохранить выбранную валюту spinner
    var curTo: String? = null
    var curFrom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initSpinnerAdapters()
        addEditTextListener()




    }

    private fun addEditTextListener() {
        currencyTextFrom.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {doSomething(); true
                }
                else -> false
            }
        }

        /*addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {}
        })
    }*/
    }

    private fun doSomething() {
        convertButton.isEnabled = true
        //ошибонька раз
        val num : Int = currencyTextFrom.text.toString().toInt()
        //val num = 1000

        val result = (num / getExchangeCourse(curFrom, curTo))
    }

    private fun getExchangeCourse(curFrom: String?, curTo: String?): Any {

        //ошибонька два
        val courseCurr = mapOf("USD" to 70, "EUR" to 86, "RUB" to 1)

        //courseCurr[currFrom] or getOrDefault(currFrom, 1))


        //заглушечка
        return 1

    }


    private fun initViews() {
        convertButton = findViewById(R.id.btChange)
        currencyTextTo = findViewById(R.id.txCountTo)
        currencyTextFrom = findViewById(R.id.txCountFrom)
        spinnerFrom = findViewById(R.id.spFrom)
        spinnerTo = findViewById(R.id.spTo)
        convertButton.isEnabled = false
    }

    private fun initSpinnerAdapters() {

        //Получаем список валют из Strings
        val stringArrayCurrency = resources.getStringArray(R.array.currency_variants)
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, stringArrayCurrency)
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Применяем адаптер к элементу spinner
        spinnerFrom.adapter = adapter
        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                curFrom = stringArrayCurrency[i]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                curFrom = "USD"
            }
        }




        spinnerTo.adapter = adapter
        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                curTo = stringArrayCurrency[i]
                //Log.d("MyLog", "Hello ${spinnerFrom.selectedItemPosition.toString()}")
                //Log.d("MyLog", "Selected cur to $curTo")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                curTo = "USD"
                //Log.d("MyLog", "Hello ${spinnerFrom.selectedItemPosition.toString()}")
                //Log.d("MyLog", "Selected cur to $curTo")
            }

        }




    }

    object CourseCurrency {
        const val USD = 70
        const val RUB = 1
        const val EUR = 86
    }
}

  



