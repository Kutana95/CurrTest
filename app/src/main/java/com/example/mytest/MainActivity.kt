package com.example.mytest

import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor


class MainActivity : AppCompatActivity() {

    lateinit var convertButton: Button
    private lateinit var currencyTextTo: TextView
    private lateinit var currencyTextFrom: TextView
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private val courseCurr = mapOf("USD" to 70, "EUR" to 86, "RUB" to 1)


    //сохранить выбранную валюту spinner
    var curTo: String? = null
    var curFrom: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initSpinnerAdapters()
        //для установлки фильтра не более 15 в целочисленно, 2 после запятой
        currencyTextFrom.filters += DecimalDigitsInputFilter(15, 2)
        addEditTextListener()




    }

    //лисенер на изменение "продать"
    private fun addEditTextListener() {

        //для добавления пробела между символами
        currencyTextFrom.addTextChangedListener(OwnWatcher())
        //для постдействий в конце редактирования - персчетов
        currencyTextFrom.setOnEditorActionListener { v, actionId, event ->

            //Log.d("MyLog", "в лисеенере")

            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    doSomething(); true
                }
                else -> false
            }
        }

    }

    //при изменении продать - изменить поле купить, обновить поле продать?
    private fun doSomething() {
        convertButton.isEnabled = true

        val num : Double = currencyTextFrom.text.toString().toDouble() //toInt()
        val courseFromTo = getExchangeCourse(curFrom, curTo)
        Log.d("MyLog", "kurs = $courseFromTo")
        //num * course
        //отобразить данные в поле купить = продать / getExchangeCourse
        //округлить floor
        val resultFromTo = floor(num / courseFromTo).toInt()

        Log.d("MyLog", "округлить купить = $resultFromTo")
        /*если введено значение меньше единицы валюты, в которую пересчет???
        пусть будет да, тогда нужно получить курс, перевести , округлить вниз и проверить
        Если = 0 тогда атеншн получается*/
        if (resultFromTo == 0) {

            val textError = "Недостаточно средств"
            val durationError = Toast.LENGTH_SHORT

            val toastError = Toast.makeText(applicationContext, textError, durationError)
            toastError.setGravity(Gravity.CENTER, 0, 0)
            //заблокировать кнопку обменять

        }
        
        currencyTextTo.text = (resultFromTo).toString()
        //пересчитать продать - вынести в отдельную функцию
        val resultToFrom = resultFromTo*courseFromTo
        //2 знака после запятой
        val roundResultToFrom = Math.round(resultToFrom * 100).toDouble() / 100
        Log.d("MyLog", "пересчитать продатьь = $roundResultToFrom")
        currencyTextFrom.text = roundResultToFrom.toString()




    }

    //добавить асинхрон
    private fun getExchangeCourse(curFrom: String?, curTo: String?): Float {

        //не могу использовать courseCurr.getOrDefault(curFrom, 1) из-за возможного значения null
        //продумать courseCurr.getOrDefault(curFrom, 1) !! maybe
        Log.d("MyLog", "валюта из = $curFrom  валюта в $curTo")
        return (courseCurr[curTo]?.toFloat()!! / courseCurr[curFrom]?.toFloat()!!)

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


    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        var mPattern: Pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((,[0-9]{0," + (digitsAfterZero - 1) + "})?)||(,)?")
        override fun filter(
                source: CharSequence,
                start: Int,
                end: Int,
                dest: Spanned,
                dstart: Int,
                dend: Int
        ): CharSequence? {
            val matcher: Matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else  null
        }

    }

    class OwnWatcher : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            Log.d("MyLog", "on text changed")


        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {
            // Remove spacing char
            Log.d("MyLog", "фаеук text changed $s")

            if (s.isNotEmpty() && s.length % 4 == 0) {
                val c = s[s.length - 1]
                if (space == c) {
                    s.delete(s.length - 1, s.length)
                    Log.d("MyLog", "я удаляю из  $s элемент $c")
                }
            }
            // Insert char where needed.
            if (s.isNotEmpty() && s.length % 4 == 0) {
                Log.d("MyLog", "я изменяю $s")
                val c = s[s.length - 1]
                // Only if its a digit where there should be a space we insert a space
                Log.d("MyLog", "наша с равно $c")
                if (Character.isDigit(c) && TextUtils.split(s.toString(), space.toString()).size <= 3) {

                    s.insert(s.length - 1, space.toString())
                    Log.d("MyLog", " $c dobavil probel $s")
                }
            }
        }

        companion object {
            // Change this to what you want... ' ', '-' etc..
            private const val space = ' '
        }
    }
}




//класс джигитов в отдельный файл
//тест: продать обрезает ли число или только отображение десятичной?
// var mPattern: Pattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")


//мысли
// /*
//            // formatter
//            // formatter
//            val format = DecimalFormat()
//            // custom symbol
//            // custom symbol
//            val customSymbols = DecimalFormatSymbols()
//            customSymbols.groupingSeparator = ' '
//            format.decimalFormatSymbols = customSymbols
//            format.format(s)*/

  



