package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var buttons = listOf<Button>()
    private var textCalculate = Constants.DEFAULT.DEFAULT_STRING_EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setItemOnClicks()
    }

    private fun setItemOnClicks() {
        with(binding) {
            buttons = listOf(
                buttonNumberZero,
                buttonNumberOne,
                buttonNumberTwo,
                buttonNumberThree,
                buttonNumberFour,
                buttonNumberFive,
                buttonNumberSix,
                buttonNumberSeven,
                buttonNumberEight,
                buttonNumberNine,
                buttonDot,
                buttonOperatorDiv,
                buttonOperatorMinus,
                buttonOperatorMul,
                buttonOperatorPlus,
                buttonResult
            )
        }
        buttons.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(view: View?) = with(binding) {
        when (view) {
            buttonDot           -> addTextCalculator(Constants.NUMBER.DOT)
            buttonNumberZero    -> addTextCalculator(Constants.NUMBER.NUMBER_ZERO)
            buttonNumberOne     -> addTextCalculator(Constants.NUMBER.NUMBER_ONE)
            buttonNumberTwo     -> addTextCalculator(Constants.NUMBER.NUMBER_TWO)
            buttonNumberThree   -> addTextCalculator(Constants.NUMBER.NUMBER_THREE)
            buttonNumberFour    -> addTextCalculator(Constants.NUMBER.NUMBER_FOUR)
            buttonNumberFive    -> addTextCalculator(Constants.NUMBER.NUMBER_FIVE)
            buttonNumberSix     -> addTextCalculator(Constants.NUMBER.NUMBER_SIX)
            buttonNumberSeven   -> addTextCalculator(Constants.NUMBER.NUMBER_SEVEN)
            buttonNumberEight   -> addTextCalculator(Constants.NUMBER.NUMBER_EIGHT)
            buttonNumberNine    -> addTextCalculator(Constants.NUMBER.NUMBER_NINE)
            buttonOperatorDiv   -> addTextCalculator(Constants.OPERATOR.OPERATOR_DIV)
            buttonOperatorMinus -> addTextCalculator(Constants.OPERATOR.OPERATOR_MINUS)
            buttonOperatorPlus  -> addTextCalculator(Constants.OPERATOR.OPERATOR_PLUS)
            buttonOperatorMul   -> addTextCalculator(Constants.OPERATOR.OPERATOR_MUL)
            buttonResult        -> {
                val arrayPrefix = convertPrefix(textCalculate)
                val result = calculate(arrayPrefix).toString()
                binding.tvResult.text = result
                textCalculate = Constants.DEFAULT.DEFAULT_STRING_EMPTY
            }
        }
    }

    private fun addTextCalculator(text: String) {
        textCalculate += text
        binding.textViewCalculator.text = textCalculate
    }

    private fun priority(operator: String): Int {
        return when (operator) {
            Constants.OPERATOR.OPERATOR_PLUS, Constants.OPERATOR.OPERATOR_MINUS -> Constants.PRIORITY.PRIORITY_ONE
            Constants.OPERATOR.OPERATOR_DIV, Constants.OPERATOR.OPERATOR_MUL -> Constants.PRIORITY.PRIORITY_TWO
            else -> Constants.PRIORITY.PRIORITY_DEFAULT
        }
    }

    private fun checkOperator(text: String): Boolean {
        if (text == Constants.OPERATOR.OPERATOR_PLUS ||
            text == Constants.OPERATOR.OPERATOR_MUL ||
            text == Constants.OPERATOR.OPERATOR_MINUS ||
            text == Constants.OPERATOR.OPERATOR_DIV
        ) return true
        return false
    }

    private fun calculate(arrayPrefix: ArrayList<String>): Double {
        val stackNumber = Stack<Double>()
        for (i in arrayPrefix) {
            val num = i.toDoubleOrNull()
            if (num != null) stackNumber.push(num)
            else {
                val number1 = stackNumber.pop().toDouble()
                val number2 = stackNumber.pop().toDouble()
                when (i) {
                    Constants.OPERATOR.OPERATOR_PLUS -> stackNumber.push(number2 + number1)
                    Constants.OPERATOR.OPERATOR_MINUS -> stackNumber.push(number2 - number1)
                    Constants.OPERATOR.OPERATOR_DIV -> stackNumber.push(number2 / number1)
                    Constants.OPERATOR.OPERATOR_MUL -> stackNumber.push(number2 * number1)
                }
            }

        }
        return if (stackNumber.isNotEmpty()) stackNumber.pop().toDouble() else Constants.DEFAULT.DEFAULT_DOUBLE_ZERO
    }

    private fun convertPrefix(text: String): ArrayList<String> {
        val arrayPrefix = ArrayList<String>()
        val stackOperator = Stack<String>()
        var num = Constants.DEFAULT.DEFAULT_STRING_EMPTY
        for (i in text.indices) {
            if (!checkOperator(text[i].toString())) {
                num += text[i]
                if (i == text.length - Constants.DEFAULT.DEFAULT_NUMBER_ONE) {
                    arrayPrefix.add(num)
                    num = Constants.DEFAULT.DEFAULT_STRING_EMPTY
                }
            } else {
                arrayPrefix.add(num)
                num = Constants.DEFAULT.DEFAULT_STRING_EMPTY
                while (!stackOperator.isEmpty() && priority(text[i].toString()) <= priority(
                        stackOperator.peek()
                    )
                ) {
                    arrayPrefix.add(stackOperator.pop())
                }
                stackOperator.push(text[i].toString())
            }
        }
        while (stackOperator.isNotEmpty()) {
            arrayPrefix.add(stackOperator.pop())
        }
        if (num.isNotEmpty()) {
            arrayPrefix.add(num)
        }
        return arrayPrefix

    }

    override fun onBackPressed() {
        binding.textViewCalculator.text = Constants.DEFAULT.DEFAULT_ZERO
        binding.tvResult.text = Constants.DEFAULT.DEFAULT_ZERO
        textCalculate = Constants.DEFAULT.DEFAULT_STRING_EMPTY
    }
}
