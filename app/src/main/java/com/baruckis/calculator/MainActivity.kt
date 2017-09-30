/*
 * Copyright 2017 Andrius Baruckis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baruckis.calculator

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), HistoryActionListDialogFragment.Listener {

    // Buttons shouldn’t be mutable, because once its value is assigned it won’t change
    // Keyword by is delegate, which adds a special behaviour to a property.
    private val button0 : Button by bind(R.id.button_0)
    private val button1 : Button by bind(R.id.button_1)
    private val button2 : Button by bind(R.id.button_2)
    private val button3 : Button by bind(R.id.button_3)
    private val button4 : Button by bind(R.id.button_4)
    private val button5 : Button by bind(R.id.button_5)
    private val button6 : Button by bind(R.id.button_6)
    private val button7 : Button by bind(R.id.button_7)
    private val button8 : Button by bind(R.id.button_8)
    private val button9 : Button by bind(R.id.button_9)

    private val buttonMemoryClear : Button by bind(R.id.button_memory_clear)
    private val buttonMemoryRecall : Button by bind(R.id.button_memory_recall)
    private val buttonMemoryAdd : Button by bind(R.id.button_memory_add)
    private val buttonMemorySubtract : Button by bind(R.id.button_memory_subtract)
    private val buttonMemoryStore : Button by bind(R.id.button_memory_store)

    private val buttonPercentage : Button by bind(R.id.button_percentage)
    private val buttonSquareRoot : Button by bind(R.id.button_square_root)
    private val buttonSquare : Button by bind(R.id.button_square)
    private val buttonFraction : Button by bind(R.id.button_fraction)
    private val buttonCE : Button by bind(R.id.button_ce)
    private val buttonC : Button by bind(R.id.button_c)
    private val buttonBackspace : Button by bind(R.id.button_backspace)
    private val buttonDivision : Button by bind(R.id.button_division)
    private val buttonMultiplication : Button by bind(R.id.button_multiplication)
    private val buttonSubtraction: Button by bind(R.id.button_subtraction)
    private val buttonAddition: Button by bind(R.id.button_addition)
    private val buttonEqual : Button by bind(R.id.button_equal)
    private val buttonPlusMinus : Button by bind(R.id.button_plus_minus)
    private val buttonComma : Button by bind(R.id.button_comma)

    private val textViewHistoryText: TextView by bind(R.id.number_history)
    private val textViewCurrentNumber: AppCompatTextView by bind(R.id.number_current)

    private var isOperationButtonClicked : Boolean = false
    private var isEqualButtonClicked : Boolean = false

    private var currentNumber : Double = 0.0
    private var currentResult : Double = 0.0

    private var historyText = ""

    private val ZERO : String = "0"
    private val ONE : String = "1"
    private val TWO : String = "2"
    private val THREE : String = "3"
    private val FOUR : String = "4"
    private val FIVE : String = "5"
    private val SIX : String = "6"
    private val SEVEN : String = "7"
    private val EIGHT : String = "8"
    private val NINE : String = "9"

    private val INIT = ""

    private val ADDITION = " + "
    private val SUBTRACTION = " − "
    private val MULTIPLICATION = " × "
    private val DIVISION = " ÷ "

    private val EQUAL = " = "

    private var currentAction = INIT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kotlin gives ability to use functional programming concept - lambda expressions. Thanks to them it's possible to define only actions that we want to do instead of declaring an anonymous class every time for click listener implementation.
        button0.setOnClickListener {
            onNumberButtonClick(ZERO)
        }

        button1.setOnClickListener {
            onNumberButtonClick(ONE)
        }

        button2.setOnClickListener {
            onNumberButtonClick(TWO)
        }

        button3.setOnClickListener {
            onNumberButtonClick(THREE)
        }

        button4.setOnClickListener {
            onNumberButtonClick(FOUR)
        }

        button5.setOnClickListener {
            onNumberButtonClick(FIVE)
        }

        button6.setOnClickListener {
            onNumberButtonClick(SIX)
        }

        button7.setOnClickListener {
            onNumberButtonClick(SEVEN)
        }

        button8.setOnClickListener {
            onNumberButtonClick(EIGHT)
        }

        button9.setOnClickListener {
            onNumberButtonClick(NINE)
        }

        buttonAddition.setOnClickListener{
            onOperationButtonClick(ADDITION)
        }

        buttonSubtraction.setOnClickListener{
            onOperationButtonClick(SUBTRACTION)
        }

        buttonMultiplication.setOnClickListener{
            onOperationButtonClick(MULTIPLICATION)
        }

        buttonDivision.setOnClickListener{
            onOperationButtonClick(DIVISION)
        }

        buttonCE.setOnClickListener{
            currentNumber = 0.0
            textViewCurrentNumber.text = removeZero(currentNumber)
        }

        buttonC.setOnClickListener{
            currentNumber = 0.0
            currentResult = 0.0
            currentAction = INIT
            textViewCurrentNumber.text = removeZero(currentNumber)
            historyText = ""
            textViewHistoryText.text = historyText
            isOperationButtonClicked = false
            isEqualButtonClicked = false
        }

        buttonEqual.setOnClickListener{

            if (isOperationButtonClicked) { currentNumber = currentResult }

            var historyAllText = calculateResult()

            Toast.makeText(applicationContext, historyAllText, Toast.LENGTH_LONG).show()

            historyText = StringBuilder().append(removeZero(currentResult)).toString()

            textViewHistoryText.text = ""

            isOperationButtonClicked = false
            isEqualButtonClicked = true;
        }
    }

    private fun onNumberButtonClick(number : String) {

        var currentValue : String = textViewCurrentNumber.text.toString()
        currentValue = if (currentValue.equals(ZERO) || isOperationButtonClicked || isEqualButtonClicked) number else StringBuilder().append(currentValue).append(number).toString()
        textViewCurrentNumber.text = currentValue
        currentNumber = currentValue.toDouble()

        if (isEqualButtonClicked) {
            currentAction = INIT
        }

        isOperationButtonClicked = false
        isEqualButtonClicked = false
    }


    private fun onOperationButtonClick(action : String) {

        if (!isOperationButtonClicked && !isEqualButtonClicked) {
            calculateResult()
        }

        currentAction = action

        textViewHistoryText.text = StringBuilder().append(historyText).append(action).toString()

        isOperationButtonClicked = true
        isEqualButtonClicked = false
    }


    private fun calculateResult() : String {

        when (currentAction) {
            INIT -> {
                currentResult = currentNumber
                historyText = StringBuilder().append(textViewHistoryText.text.toString()).toString()
            }
            ADDITION -> currentResult = currentResult + currentNumber
            SUBTRACTION -> currentResult = currentResult - currentNumber
            MULTIPLICATION -> currentResult = currentResult * currentNumber
            DIVISION -> currentResult = currentResult / currentNumber
        }

        textViewCurrentNumber.text = removeZero(currentResult)

        historyText = StringBuilder().append(historyText).append(currentAction).append(removeZero(currentNumber)).toString()

        return StringBuilder().append(historyText).append(EQUAL).append(removeZero(currentResult)).toString()
    }


    fun fmt(d: Double): String {
        return if (d == d.toLong().toDouble())
            String.format("%d", d.toLong())
        else
            String.format("%s", d)
    }

    private fun removeZero(number: Double): String {
        val format = DecimalFormat("#.###############")
        return format.format(number)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_item_history -> {
                HistoryActionListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onItemClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    // extension functions to add a behaviour to our Activity
    // lazy means it won’t be initialised right away but the first time the value is actually needed
    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        // function will be called only by the main thread to improve performance
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
    }
}
