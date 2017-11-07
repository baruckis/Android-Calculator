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
import java.text.DecimalFormatSymbols
import java.text.ParseException

// Extends AppCompatActivity class and implements Listener interface from HistoryActionListDialogFragment.
class MainActivity : AppCompatActivity(), HistoryActionListDialogFragment.Listener {

    // Than we use this function to assign button, because once done its value won't change.
    // Keyword by is delegate, which adds a special behaviour to a property.
    private val button0: Button by bind(R.id.button_0)
    private val button1: Button by bind(R.id.button_1)
    private val button2: Button by bind(R.id.button_2)
    private val button3: Button by bind(R.id.button_3)
    private val button4: Button by bind(R.id.button_4)
    private val button5: Button by bind(R.id.button_5)
    private val button6: Button by bind(R.id.button_6)
    private val button7: Button by bind(R.id.button_7)
    private val button8: Button by bind(R.id.button_8)
    private val button9: Button by bind(R.id.button_9)

    private val buttonMemoryClear: Button by bind(R.id.button_memory_clear)
    private val buttonMemoryRecall: Button by bind(R.id.button_memory_recall)
    private val buttonMemoryAdd: Button by bind(R.id.button_memory_add)
    private val buttonMemorySubtract: Button by bind(R.id.button_memory_subtract)
    private val buttonMemoryStore: Button by bind(R.id.button_memory_store)

    private val buttonPercentage: Button by bind(R.id.button_percentage)
    private val buttonRoot: Button by bind(R.id.button_root)
    private val buttonSquare: Button by bind(R.id.button_square)
    private val buttonFraction: Button by bind(R.id.button_fraction)
    private val buttonCE: Button by bind(R.id.button_ce)
    private val buttonC: Button by bind(R.id.button_c)
    private val buttonBackspace: Button by bind(R.id.button_backspace)
    private val buttonDivision: Button by bind(R.id.button_division)
    private val buttonMultiplication: Button by bind(R.id.button_multiplication)
    private val buttonSubtraction: Button by bind(R.id.button_subtraction)
    private val buttonAddition: Button by bind(R.id.button_addition)
    private val buttonEqual: Button by bind(R.id.button_equal)
    private val buttonPlusMinus: Button by bind(R.id.button_plus_minus)
    private val buttonComma: Button by bind(R.id.button_comma)

    private val textViewHistoryText: TextView by bind(R.id.number_history)
    private val textViewCurrentNumber: AppCompatTextView by bind(R.id.number_current)

    private var isFutureOperationButtonClicked: Boolean = false
    private var isInstantOperationButtonClicked: Boolean = false
    private var isEqualButtonClicked: Boolean = false

    private var currentNumber: Double = 0.0 // Value can be changed.
    private var currentResult: Double = 0.0
    private var memory: Double = 0.0

    private var historyText = "" // Recognize type of variable without declaring it.
    private var historyInstantOperationText = ""
    private var historyActionList: ArrayList<String> = ArrayList()

    private val ZERO: String = "0" // Value cannot be changed.
    private val ONE: String = "1"
    private val TWO: String = "2"
    private val THREE: String = "3"
    private val FOUR: String = "4"
    private val FIVE: String = "5"
    private val SIX: String = "6"
    private val SEVEN: String = "7"
    private val EIGHT: String = "8"
    private val NINE: String = "9"

    private val INIT = ""

    private val ADDITION = " + "
    private val SUBTRACTION = " − "
    private val MULTIPLICATION = " × "
    private val DIVISION = " ÷ "

    private val PERCENTAGE = ""
    private val ROOT = "√"
    private val SQUARE = "sqr"
    private val FRACTION = "1/"

    private val NEGATE = "negate"
    private val COMMA = ","
    private val EQUAL = " = "

    private var currentOperation = INIT


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

        buttonAddition.setOnClickListener {
            onFutureOperationButtonClick(ADDITION)
        }

        buttonSubtraction.setOnClickListener {
            onFutureOperationButtonClick(SUBTRACTION)
        }

        buttonMultiplication.setOnClickListener {
            onFutureOperationButtonClick(MULTIPLICATION)
        }

        buttonDivision.setOnClickListener {
            onFutureOperationButtonClick(DIVISION)
        }

        buttonCE.setOnClickListener {
            clearEntry()
        }

        buttonC.setOnClickListener {
            currentNumber = 0.0
            currentResult = 0.0
            currentOperation = INIT

            historyText = ""
            historyInstantOperationText = ""

            textViewCurrentNumber.text = formatDoubleToString(currentNumber)
            textViewHistoryText.text = historyText

            isFutureOperationButtonClicked = false
            isEqualButtonClicked = false
            isInstantOperationButtonClicked = false
        }

        buttonBackspace.setOnClickListener {

            if (isFutureOperationButtonClicked || isInstantOperationButtonClicked || isEqualButtonClicked) return@setOnClickListener

            var currentValue: String = textViewCurrentNumber.text.toString()

            val charsLimit = if (currentValue.first().isDigit()) 1 else 2

            if (currentValue.length > charsLimit)
                currentValue = currentValue.substring(0, currentValue.length - 1)
            else
                currentValue = ZERO

            textViewCurrentNumber.text = currentValue
            currentNumber = formatStringToDouble(currentValue)
        }

        buttonPlusMinus.setOnClickListener {

            val currentValue: String = textViewCurrentNumber.text.toString()

            currentNumber = formatStringToDouble(currentValue)
            if (currentNumber == 0.0) return@setOnClickListener

            currentNumber *= -1
            textViewCurrentNumber.text = formatDoubleToString(currentNumber)

            if (isInstantOperationButtonClicked) {
                historyInstantOperationText = "($historyInstantOperationText)"
                historyInstantOperationText = StringBuilder().append(NEGATE).append(historyInstantOperationText).toString()
                textViewHistoryText.text = StringBuilder().append(historyText).append(currentOperation).append(historyInstantOperationText).toString()
            }

            if (isEqualButtonClicked) {
                currentOperation = INIT
            }

            isFutureOperationButtonClicked = false
            isEqualButtonClicked = false
        }

        buttonComma.setOnClickListener {

            var currentValue: String = textViewCurrentNumber.text.toString()

            if (isFutureOperationButtonClicked || isInstantOperationButtonClicked || isEqualButtonClicked) {
                currentValue = StringBuilder().append(ZERO).append(COMMA).toString()
                if (isInstantOperationButtonClicked) {
                    historyInstantOperationText = ""
                    textViewHistoryText.text = StringBuilder().append(historyText).append(currentOperation).toString()
                }
                if (isEqualButtonClicked) currentOperation = INIT
                currentNumber = 0.0
            } else if (currentValue.contains(COMMA)) {
                return@setOnClickListener
            } else currentValue = StringBuilder().append(currentValue).append(COMMA).toString()

            textViewCurrentNumber.text = currentValue

            isFutureOperationButtonClicked = false
            isInstantOperationButtonClicked = false
            isEqualButtonClicked = false
        }

        buttonEqual.setOnClickListener {

            if (isFutureOperationButtonClicked) {
                currentNumber = currentResult
            }

            val historyAllText = calculateResult()

            Toast.makeText(applicationContext, historyAllText, Toast.LENGTH_LONG).show()

            historyActionList.add(historyAllText)

            historyText = StringBuilder().append(formatDoubleToString(currentResult)).toString()

            textViewHistoryText.text = ""

            isFutureOperationButtonClicked = false
            isEqualButtonClicked = true
        }

        buttonPercentage.setOnClickListener {
            onInstantOperationButtonClick(PERCENTAGE)
        }

        buttonRoot.setOnClickListener {
            onInstantOperationButtonClick(ROOT)
        }

        buttonSquare.setOnClickListener {
            onInstantOperationButtonClick(SQUARE)
        }

        buttonFraction.setOnClickListener {
            onInstantOperationButtonClick(FRACTION)
        }

        buttonMemoryClear.isEnabled = false
        buttonMemoryClear.setOnClickListener {

            buttonMemoryClear.isEnabled = false
            buttonMemoryRecall.isEnabled = false

            memory = 0.0

            Toast.makeText(applicationContext, getString(R.string.memory_cleared_toast), Toast.LENGTH_SHORT).show()
        }

        buttonMemoryRecall.isEnabled = false
        buttonMemoryRecall.setOnClickListener {

            clearEntry(memory)

            Toast.makeText(applicationContext, getString(R.string.memory_recalled_toast), Toast.LENGTH_SHORT).show()
        }

        buttonMemoryAdd.setOnClickListener {

            buttonMemoryClear.isEnabled = true
            buttonMemoryRecall.isEnabled = true

            val currentValue: String = textViewCurrentNumber.text.toString()
            val thisOperationNumber: Double = formatStringToDouble(currentValue)

            val newMemory = memory + thisOperationNumber

            // Strings in Kotlin can include references to variables that are interpolated.
            // In addition to simple variable references, they can also include any expression enclosed in curly braces.
            // Also you can still do the string concatenation if you like using plus sign.
            Toast.makeText(applicationContext, getString(R.string.memory_added_toast) + "${formatDoubleToString(memory)} + ${formatDoubleToString(thisOperationNumber)} = ${formatDoubleToString(newMemory)}", Toast.LENGTH_LONG).show()

            memory = newMemory
        }

        buttonMemorySubtract.setOnClickListener {

            buttonMemoryClear.isEnabled = true
            buttonMemoryRecall.isEnabled = true

            val currentValue: String = textViewCurrentNumber.text.toString()
            val thisOperationNumber: Double = formatStringToDouble(currentValue)

            val newMemory = memory - thisOperationNumber

            Toast.makeText(applicationContext, getString(R.string.memory_subtracted_toast) + "${formatDoubleToString(memory)} - ${formatDoubleToString(thisOperationNumber)} = ${formatDoubleToString(newMemory)}", Toast.LENGTH_LONG).show()

            memory = newMemory
        }

        buttonMemoryStore.setOnClickListener {

            val currentValue: String = textViewCurrentNumber.text.toString()
            memory = formatStringToDouble(currentValue)

            buttonMemoryClear.isEnabled = true
            buttonMemoryRecall.isEnabled = true

            Toast.makeText(applicationContext, getString(R.string.memory_stored_toast) + formatDoubleToString(memory), Toast.LENGTH_SHORT).show()
        }

    }

    @Throws(IllegalArgumentException::class)
    private fun onNumberButtonClick(number: String, isHistory: Boolean = false) {

        var currentValue: String = textViewCurrentNumber.text.toString()
        // In Kotlin there is no more conditional operator ? : like it is in Java, which is used as a shortcut for setting a single variable to one of two states based on a single condition. Here everything can be conveniently done using if..else statement.
        // In Kotlin, using the equality operator == will call the equals method behind the scenes, so it's totaly acceptable to use it for string comparision.
        currentValue = if (currentValue == ZERO || isFutureOperationButtonClicked || isInstantOperationButtonClicked || isEqualButtonClicked || isHistory) number else StringBuilder().append(currentValue).append(number).toString()

        try {
            currentNumber = formatStringToDouble(currentValue)
        } catch (e: ParseException) {
            throw IllegalArgumentException("String must be number.")
        }

        textViewCurrentNumber.text = currentValue

        if (isEqualButtonClicked) {
            currentOperation = INIT
            historyText = ""
        }

        if (isInstantOperationButtonClicked) {
            historyInstantOperationText = ""
            textViewHistoryText.text = StringBuilder().append(historyText).append(currentOperation).toString()
            isInstantOperationButtonClicked = false
        }

        isFutureOperationButtonClicked = false
        isEqualButtonClicked = false
    }

    private fun onFutureOperationButtonClick(operation: String) {

        if (!isFutureOperationButtonClicked && !isEqualButtonClicked) {
            calculateResult()
        }

        currentOperation = operation

        if (isInstantOperationButtonClicked) {
            isInstantOperationButtonClicked = false
            historyText = textViewHistoryText.text.toString()
        }
        textViewHistoryText.text = StringBuilder().append(historyText).append(operation).toString()

        isFutureOperationButtonClicked = true
        isEqualButtonClicked = false
    }

    // Compared to switch/case statement in Java, here using when statement the argument can be literally anything, and the conditions for the branches too.
    // For example, Java (before version 7) does not support string in switch/case and you can achieve the desired result only by using an enum.
    // However in Kotlin these restrictions are gone.
    private fun onInstantOperationButtonClick(operation: String) {

        var currentValue: String = textViewCurrentNumber.text.toString()
        var thisOperationNumber: Double = formatStringToDouble(currentValue)

        currentValue = "(${formatDoubleToString(thisOperationNumber)})"

        when (operation) {
            PERCENTAGE -> {
                thisOperationNumber = (currentResult * thisOperationNumber) / 100
                currentValue = formatDoubleToString(thisOperationNumber)
            }
            // Later we use this property to find square root of the provided number.
            ROOT -> thisOperationNumber = thisOperationNumber.sqrt
            SQUARE -> thisOperationNumber = thisOperationNumber * thisOperationNumber
            FRACTION -> thisOperationNumber = 1 / thisOperationNumber
        }

        if (isInstantOperationButtonClicked) {
            historyInstantOperationText = "($historyInstantOperationText)"
            historyInstantOperationText = StringBuilder().append(operation).append(historyInstantOperationText).toString()
            textViewHistoryText.text = if (isEqualButtonClicked) historyInstantOperationText else StringBuilder().append(historyText).append(currentOperation).append(historyInstantOperationText).toString()
        } else if (isEqualButtonClicked) {
            historyInstantOperationText = StringBuilder().append(operation).append(currentValue).toString()
            textViewHistoryText.text = historyInstantOperationText
        } else {
            historyInstantOperationText = StringBuilder().append(operation).append(currentValue).toString()
            textViewHistoryText.text = StringBuilder().append(historyText).append(currentOperation).append(historyInstantOperationText).toString()
        }

        textViewCurrentNumber.text = formatDoubleToString(thisOperationNumber)

        if (isEqualButtonClicked) currentResult = thisOperationNumber else currentNumber = thisOperationNumber

        isInstantOperationButtonClicked = true
        isFutureOperationButtonClicked = false
    }

    private fun calculateResult(): String {

        when (currentOperation) {
            INIT -> {
                currentResult = currentNumber
                historyText = StringBuilder().append(textViewHistoryText.text.toString()).toString()
            }
            ADDITION -> currentResult = currentResult + currentNumber
            SUBTRACTION -> currentResult = currentResult - currentNumber
            MULTIPLICATION -> currentResult = currentResult * currentNumber
            DIVISION -> currentResult = currentResult / currentNumber
        }

        textViewCurrentNumber.text = formatDoubleToString(currentResult)

        if (isInstantOperationButtonClicked) {
            isInstantOperationButtonClicked = false
            historyText = textViewHistoryText.text.toString()
            if (isEqualButtonClicked) historyText = StringBuilder().append(historyText).append(currentOperation).append(formatDoubleToString(currentNumber)).toString()
        } else {
            historyText = StringBuilder().append(historyText).append(currentOperation).append(formatDoubleToString(currentNumber)).toString()
        }

        return StringBuilder().append(historyText).append(EQUAL).append(formatDoubleToString(currentResult)).toString()
    }

    private fun useNumberFormat(): DecimalFormat {

        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = ','

        val format = DecimalFormat("#.##############")
        format.decimalFormatSymbols = symbols

        return format
    }

    private fun formatDoubleToString(number: Double): String {
        return useNumberFormat().format(number)
    }

    private fun formatStringToDouble(number: String): Double {
        return useNumberFormat().parse(number).toDouble()
    }

    // Extension property provides similar mechanism.
    // Note that you have to define a getter method on your property for this to work.
    private val Double.sqrt: Double get() = Math.sqrt(this)

    private fun clearEntry(newNumber: Double = 0.0) {
        historyInstantOperationText = ""

        if (isEqualButtonClicked) {
            currentOperation = INIT
            historyText = ""
        }

        if (isInstantOperationButtonClicked) textViewHistoryText.text = StringBuilder().append(historyText).append(currentOperation).toString()

        isInstantOperationButtonClicked = false
        isFutureOperationButtonClicked = false
        isEqualButtonClicked = false

        currentNumber = newNumber
        textViewCurrentNumber.text = formatDoubleToString(newNumber)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Functions are defined using the “fun” keyword.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Safe call operator ? added to the variable before invoking the property instructs the compiler to invoke the property only if the value isn't null.
        when (item?.itemId) {
            R.id.menu_item_history -> {
                HistoryActionListDialogFragment.newInstance(historyActionList).show(getSupportFragmentManager(), "dialog")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onHistoryItemClicked(resultText: String) {

        try {
            onNumberButtonClick(resultText, true)
        } catch (e: IllegalArgumentException) {
            return
        }

        Toast.makeText(applicationContext, getString(R.string.history_result) + resultText, Toast.LENGTH_SHORT).show()
    }

    // Extension function created to add special behaviour to our Activity.
    // Here keyword lazy means it won’t be initialised right away but the first time the value is actually needed.
    fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        // Function will be called only by the main thread to improve performance.
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
    }
}