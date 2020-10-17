package es.iessaladillo.pedrojoya.exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding
import es.iessaladillo.pedrojoya.exchange.tools.SoftInputUtils


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lblAmount.requestFocus()
        setupViews()

    }

    private fun setupViews() {
        binding.lblAmount.selectAll()
        addAmount()
        checkSelector()
        binding.btnExchange.setOnClickListener { exchange() }
        binding.lblAmount.setOnEditorActionListener(OnEditorActionListener { _, _, _ ->
            exchange()
            true
        })

    }

    private fun exchange() {

        val amount = binding.lblAmount.text.toString().toDouble()
        var result = 0.00
        lateinit var symbolFrom: Currency
        lateinit var symbolTo: Currency

        when (binding.fromGroup.checkedRadioButtonId) {
            binding.fromDollar.id -> {
                symbolFrom = Currency.DOLLAR
                when (binding.toGroup.checkedRadioButtonId) {
                    binding.toEuro.id -> {
                        result = Currency.DOLLAR.fromDollar(amount)
                        symbolTo = Currency.EURO
                    }
                    binding.toPound.id -> {
                        result = Currency.DOLLAR.fromDollar(amount)
                        symbolTo = Currency.POUND
                    }
                }

            }
            binding.fromEuro.id -> {
                symbolFrom = Currency.EURO
                when (binding.toGroup.checkedRadioButtonId) {
                    binding.toDollar.id -> {
                        result = Currency.EURO.toDollar(amount)
                        symbolTo = Currency.DOLLAR
                    }
                    binding.toPound.id -> {
                        result = Currency.EURO.toDollar(amount)
                        result = Currency.POUND.fromDollar(result)
                        symbolTo = Currency.POUND
                    }
                }
            }
            binding.fromPound.id -> {
                symbolFrom = Currency.POUND
                when (binding.toGroup.checkedRadioButtonId) {
                    binding.toEuro.id -> {
                        result = Currency.POUND.toDollar(amount)
                        result = Currency.EURO.fromDollar(result)
                        symbolTo = Currency.EURO
                    }
                    binding.toDollar.id -> {
                        result = Currency.DOLLAR.toDollar(amount)
                        symbolTo = Currency.DOLLAR
                    }
                }
            }

        }
        showMessage(amount, result, symbolFrom, symbolTo)
    }

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    private fun showMessage(amount: Double, result: Double, symbolFrom: Currency,symbolTo: Currency) {
        SoftInputUtils.hideSoftKeyboard(binding.btnExchange)
        Toast.makeText(this, "${amount.format(2)} ${symbolFrom.symbol} = ${result.format(2)} ${symbolTo.symbol}", Toast.LENGTH_SHORT).show()

    }

    private fun checkSelector() {
        binding.fromGroup.setOnCheckedChangeListener { group, checkId -> changeIcon(group, checkId) }
        binding.toGroup.setOnCheckedChangeListener { group, checkId -> changeIcon(group, checkId) }

    }

    private fun changeImage(image: ImageView, currency: Currency) {
        image.setImageResource(currency.drawableResId)
    }

    private fun checkEnable(dollar: Boolean, euro: Boolean, pound: Boolean, group: RadioGroup){
        if (group !== binding.fromGroup){
            binding.fromDollar.isEnabled = dollar
            binding.fromEuro.isEnabled = euro
            binding.fromPound.isEnabled = pound
        }else{
            binding.toDollar.isEnabled = dollar
            binding.toEuro.isEnabled = euro
            binding.toPound.isEnabled = pound
        }

    }

    private fun changeIcon(group: RadioGroup, checkId: Int) {

        when (checkId) {
            binding.fromDollar.id -> {
                changeImage(binding.fromCurrencyImage, Currency.DOLLAR)
                checkEnable(dollar = false, euro = true, pound = true, group = group)
            }
            binding.fromEuro.id -> {
                changeImage(binding.fromCurrencyImage, Currency.EURO)
                checkEnable(dollar = true, euro = false, pound = true, group = group)
            }
            binding.fromPound.id -> {
                changeImage(binding.fromCurrencyImage, Currency.POUND)
                checkEnable(dollar = true, euro = true, pound = false, group = group)
            }
            binding.toDollar.id -> {
                changeImage(binding.toCurrencyImage, Currency.DOLLAR)
                checkEnable(dollar = false, euro = true, pound = true, group = group)
            }
            binding.toEuro.id -> {
                changeImage(binding.toCurrencyImage, Currency.EURO)
                checkEnable(dollar = true, euro = false, pound = true, group = group)
            }
            binding.toPound.id -> {
                changeImage(binding.toCurrencyImage, Currency.POUND)
                checkEnable(dollar = true, euro = true, pound = false, group = group)
            }
        }

    }

    private fun extra_Dots(args: String){
        var extraDot = false
        val str = binding.lblAmount.text

        for (i in 0 until binding.lblAmount.length()){
            if(str[i] == '.'){
                if (extraDot){
                    binding.lblAmount.setText(getString(R.string.value))
                    binding.lblAmount.selectAll()
                }
                extraDot = true
            }
        }
    }

    private fun addAmount() {
        binding.lblAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.isEmpty() || p0.startsWith('.') || p0.startsWith("00")) {
                    binding.lblAmount.setText(getString(R.string.value))
                    binding.lblAmount.selectAll()
                }
                extra_Dots(p0.toString())


            }


        })

    }

}