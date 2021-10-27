package iuh.ad.PhanNguyenMaiPhuong.mssv19469121.changemaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import android.icu.util.UniversalTimeScale.toBigDecimal
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode


class MainActivity : AppCompatActivity() {
    private val MAX_PRICE: Double = 20 * 9999.0
    private val numOfDecimal: Int = 6
    private lateinit var prices: Array<TextView>
    private lateinit var newPrice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prices = arrayOf(
            twentyDollarQtyLabel, tenDollarQtyLabel,
            fiveDollarQtyLabel, oneDollarQtyLabel,
            twentyFiveCentQtyLabel, tenCentQtyLabel,
            fiveCentQtyLabel, oneCentQtyLabel
        )

        newPrice = if (
            currentAmountLabel.text.toString() == "0.00"
        ) "" else currentAmountLabel.text.toString()

        /* function that handles when a button is pressed */
        fun onButtonClicked(btn: TextView) {
            /* default value = -1 is clear button */
            var value: Int = -1

            /* change value when button is not clear button */
            if (btn != btnLabelClear) {
                value = btn.text.toString().toInt()
            }

            /* clear button is pressed */
            if (value == -1) {
                newPrice = ""
                currentAmountLabel.text = "0.00"
                prices.forEach { it -> it.text = "0" }
                return
            }

            /* change current price */
            newPrice += value.toString()

            var parsedNumber = newPrice.toDouble() / 100.0

            /* current amount is too big */
            if (parsedNumber > MAX_PRICE) {
                Toast.makeText(this, "Không thể nhập nữa", Toast.LENGTH_SHORT).show()
                return
            }

            currentAmountLabel.text = parsedNumber.toString()

            val listPrices = arrayOf(20.0, 10.0, 5.0, 1.0, 0.25, 0.1, 0.05, 0.01)
            for (i in listPrices.indices) {
                var amounts = (parsedNumber / listPrices[i]).toInt()
                prices[i].text = amounts.toString()
                parsedNumber -= amounts.toDouble() * listPrices[i]
                parsedNumber = parsedNumber.toBigDecimal().setScale(numOfDecimal, RoundingMode.UP).toDouble()
            }
        }

        /* array of button */
        arrayOf<TextView>(
            btnLabelOne, btnLabelTwo, btnLabelThree,
            btnLabelFour, btnLabelFive, btnLabelSix,
            btnLabelSeven, btnLabelEight, btnLabelNine,
            btnLabelZero, btnLabelClear
        ).forEach { it -> it.setOnClickListener() {
            onButtonClicked(it as TextView)
        }}
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var oldPrices: ArrayList<String> = ArrayList()

        for (i in prices) {
            oldPrices.add(i.text.toString())
        }

        outState.putString("currPrice", currentAmountLabel.text.toString())
        outState.putString("newPrice", newPrice)
        outState.putStringArrayList("listPrice", oldPrices)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentAmountLabel.text = savedInstanceState.getString("currPrice")
        newPrice = savedInstanceState.getString("newPrice")!!
        var oldPrices = savedInstanceState.getStringArrayList("listPrice")

        for (i in prices.indices) {
            prices[i].text = oldPrices?.get(i) ?: "0"
        }
    }
}