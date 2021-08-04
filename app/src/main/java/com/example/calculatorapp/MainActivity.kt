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
                btnNumberZero,
                btnNumberOne,
                btnNumberTwo,
                btnNumberThree,
                btnNumberFour,
                btnNumberFive,
                btnNumberSix,
                btnNumberSeven,
                btnNumberEight,
                btnNumberNine,
                btnDot,
                btnOperatorDiv,
                btnOperatorMinus,
                btnOperatorMul,
                btnOperatorPlus,
                btnResult
            )
        }
        buttons.forEach { it.setOnClickListener(this) }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_dot -> addTextCalculator(Constants.NUMBER.DOT)
            R.id.btn_number_zero -> addTextCalculator(Constants.NUMBER.NUMBER_ZERO)
            R.id.btn_number_one -> addTextCalculator(Constants.NUMBER.NUMBER_ONE)
            R.id.btn_number_two -> addTextCalculator(Constants.NUMBER.NUMBER_TWO)
            R.id.btn_number_three -> addTextCalculator(Constants.NUMBER.NUMBER_THREE)
            R.id.btn_number_four -> addTextCalculator(Constants.NUMBER.NUMBER_FOUR)
            R.id.btn_number_five -> addTextCalculator(Constants.NUMBER.NUMBER_FIVE)
            R.id.btn_number_six -> addTextCalculator(Constants.NUMBER.NUMBER_SIX)
            R.id.btn_number_seven -> addTextCalculator(Constants.NUMBER.NUMBER_SEVEN)
            R.id.btn_number_eight -> addTextCalculator(Constants.NUMBER.NUMBER_EIGHT)
            R.id.btn_number_nine -> addTextCalculator(Constants.NUMBER.NUMBER_NINE)
            R.id.btn_operator_div -> addTextCalculator(Constants.OPERATOR.OPERATOR_DIV)
            R.id.btn_operator_minus -> addTextCalculator(Constants.OPERATOR.OPERATOR_MINUS)
            R.id.btn_operator_plus -> addTextCalculator(Constants.OPERATOR.OPERATOR_PLUS)
            R.id.btn_operator_mul -> addTextCalculator(Constants.OPERATOR.OPERATOR_MUL)
            R.id.btn_result -> {
                val arrayPrefix = convertPrefix(textCalculate)
                val result = calculate(arrayPrefix).toString()
                binding.tvResult.text = result
                textCalculate = Constants.DEFAULT.DEFAULT_STRING_EMPTY
            }
        }
    }

    private fun addTextCalculator(text: String) {
        textCalculate += text
        binding.tvCalculator.text = textCalculate
    }

    private fun priority(operator: String): Int {
        return when (operator) {
            Constants.OPERATOR.OPERATOR_PLUS, Constants.OPERATOR.OPERATOR_MINUS -> 1
            Constants.OPERATOR.OPERATOR_DIV, Constants.OPERATOR.OPERATOR_MUL -> 2
            else -> -1
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
            val num: Double? = i.toDoubleOrNull()
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
        return if (!stackNumber.isEmpty()) stackNumber.pop().toDouble() else 0.0
    }

    private fun convertPrefix(text: String): ArrayList<String> {
        val arrayPrefix = ArrayList<String>()
        val stackOperator = Stack<String>()
        var num = Constants.DEFAULT.DEFAULT_STRING_EMPTY
        for (i in text.indices) {
            if (!checkOperator(text[i].toString())) {
                num += text[i]
                if (i == text.length - 1) {
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
        while (!stackOperator.isEmpty()) {
            arrayPrefix.add(stackOperator.pop())
        }
        if (num.isNotEmpty()) {
            arrayPrefix.add(num)
        }
        return arrayPrefix

    }

    override fun onBackPressed() {
        binding.tvCalculator.text = Constants.DEFAULT.DEFAULT_ZERO
        binding.tvResult.text = Constants.DEFAULT.DEFAULT_ZERO
        textCalculate = Constants.DEFAULT.DEFAULT_STRING_EMPTY
    }
}
