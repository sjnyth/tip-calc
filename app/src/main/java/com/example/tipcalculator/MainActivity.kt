package com.example.tipcalculator

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.tipcalculator.databinding.ActivityMainBinding
import kotlin.math.ceil
import android.widget.ArrayAdapter
import android.view.View
import android.widget.AdapterView


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize and populate the Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.party_sizes,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.partySizeSpinner.adapter = adapter

        // Handle Spinner item selection
        binding.partySizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedValue = parent?.getItemAtPosition(position).toString()
                println("Selected party size: $selectedValue")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected if necessary
            }
        }

        updateTipPercentageLabel()

        binding.calculate.setOnClickListener { calculateTip() }
        binding.tipSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTipPercentageLabel()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun updateTipPercentageLabel() {
        binding.tipPercentage.text = getString(R.string.tip_percent, binding.tipSlider.progress)
    }

    private fun calculateTip() {
        val billAmount = binding.billTotal.text.toString().toDoubleOrNull()
        if (billAmount == null || billAmount < 0) {
            binding.billTotal.error = "Invalid bill amount"
            return
        }

        val tipPercentage = binding.tipSlider.progress.toDouble() / 100.0


        var tipAmount = billAmount * tipPercentage

        if (binding.roundTip.isChecked) {
            tipAmount = ceil(tipAmount)
        }

        val totalAmount = billAmount + tipAmount

        val partySize = binding.partySizeSpinner.selectedItem.toString().toInt()

        if (partySize != 1) {
            val amountPerPerson = totalAmount / partySize
            binding.totalAmount.text = "Total Amount: $%.2f\n\n$%.2f per person.".format(totalAmount, amountPerPerson)
        } else {
            binding.totalAmount.text = "Total Amount: $%.2f".format(totalAmount)
        }

    }
}
