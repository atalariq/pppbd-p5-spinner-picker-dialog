package com.example.countryapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.text.format.DateFormat
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.countryapp.databinding.ActivityMainBinding
import com.example.countryapp.databinding.DialogExitBinding

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var provinces: Array<String>
    private val countries = arrayOf(
        "Australia",
        "Brazil",
        "Canada",
        "China",
        "France",
        "Germany",
        "Indonesia",
        "Italy",
        "Japan",
        "Russia",
        "Saudi Arabia",
        "South Korea",
        "United Kingdom",
        "United States",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        provinces = resources.getStringArray(R.array.provinces)

        with(binding) {
            val adapterCountry =
                ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, countries)
            val adapterProvince =
                ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, provinces)

            adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerCountry.adapter = adapterCountry
            spinnerProvince.adapter = adapterProvince

            spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long
                ) {
                    Toast.makeText(this@MainActivity, countries[position], Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

            datePicker.init(
                datePicker.year, datePicker.month, datePicker.dayOfMonth
            ) { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                Toast.makeText(this@MainActivity, selectedDate, Toast.LENGTH_SHORT).show()
            }

            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                Toast.makeText(this@MainActivity, selectedTime, Toast.LENGTH_SHORT).show()
            }

            btnShowCalendar.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "datePicker")
            }

            btnShowTimePicker.setOnClickListener {
                val timePicker = TimePicker()
                timePicker.show(supportFragmentManager, "timePicker")
            }

            btnShowAlertDialog.setOnClickListener {
                val builder = AlertDialog.Builder(this@MainActivity)
                with(builder) {
                    setTitle("Konfirmasi")
                    setMessage("Apakah Anda yakin ingin keluar?")
                    setPositiveButton("Ya") { _, _ ->
                        finish()
                    }
                    setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }

            btnShowCustomDialog.setOnClickListener {
                val dialogExit = DialogExit()
                dialogExit.show(supportFragmentManager, "dialogExit")
            }

        }

    }

    override fun onDateSet(
        view: android.widget.DatePicker?,
        year: Int,
        month: Int,
        dayOfMonth: Int
    ) {
        val selectedDate = "$dayOfMonth/${month + 1}/$year"
        Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
    }

    override fun onTimeSet(view: android.widget.TimePicker?, hourOfDay: Int, minute: Int) {
        val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
        Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
    }

}

class DatePicker : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireActivity(), activity as DatePickerDialog.OnDateSetListener, year, month, day
        )
    }

}

class TimePicker : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireActivity(),
            activity as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        )
    }
}

class DialogExit : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val binding = DialogExitBinding.inflate(inflater)
        with(binding) {
            btnYes.setOnClickListener {
                requireActivity().finish()
            }
            btnNo.setOnClickListener {
                dialog?.dismiss()
            }
        }
        builder.setView(binding.root)
        return builder.create()
    }
}
