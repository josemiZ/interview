package com.josemiz.interview.views.create

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.DataBindingUtil
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.josemiz.interview.R
import com.josemiz.interview.databinding.ActivityCreateShiftBinding
import com.josemiz.interview.models.ShiftModel
import com.josemiz.interview.utils.convertLongToTimeDate
import com.josemiz.interview.utils.formatCurrentDateToModel
import com.josemiz.interview.utils.setTimeString
import java.util.*

class CreateShiftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateShiftBinding

    //Date picker values
    private var currentSelectedStartDate: Long? = null
    private var currentSelectedEndDate: Long? = null

    //Time picker values
    private var selectedStartHour: Int? = null
    private var selectedEndHour: Int? = null
    private var selectedStartMinute: Int? = null
    private var selectedEndMinute: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_shift)
        setupView()
    }

    private fun setupView() = with(binding) {
        ibStartDate.setOnClickListener { showDatePicker(currentSelectedStartDate, true) }
        ibEndDate.setOnClickListener { showDatePicker(currentSelectedEndDate, false) }
        ibStartTime.setOnClickListener {
            showTimePicker(
                selectedStartHour,
                selectedStartMinute,
                true
            )
        }
        ibEndTime.setOnClickListener { showTimePicker(selectedEndHour, selectedEndMinute, false) }
        initializeSpinner(R.array.employees_array, spEmployee)
        initializeSpinner(R.array.roles_array, spRole)
        initializeSpinner(R.array.colors_array, spColor)
        setupToolbar()
    }

    private fun showDatePicker(currentSelectedDate: Long?, isStartDate: Boolean) {
        val selectedDateInMillis = currentSelectedDate ?: System.currentTimeMillis()
        MaterialDatePicker.Builder.datePicker().setSelection(selectedDateInMillis).build().apply {
            addOnPositiveButtonClickListener { dateInMillis ->
                onDateSelected(
                    dateInMillis,
                    isStartDate
                )
            }
        }.show(supportFragmentManager, MaterialDatePicker::class.java.canonicalName)
    }

    private fun onDateSelected(dateTimeStampInMillis: Long, isStartDate: Boolean) {
        if (isStartDate) {
            currentSelectedStartDate = dateTimeStampInMillis
            binding.tvStartDateDescription.text = convertLongToTimeDate(dateTimeStampInMillis)
        } else {
            currentSelectedEndDate = dateTimeStampInMillis
            binding.tvEndDateDescription.text = convertLongToTimeDate(dateTimeStampInMillis)
        }
    }

    private fun showTimePicker(selectedHour: Int?, selectedMinute: Int?, isStartTime: Boolean) {
        val calendar = Calendar.getInstance()
        val hour = selectedHour ?: calendar.get(Calendar.HOUR)
        val minute = selectedMinute ?: calendar.get(Calendar.MINUTE)

        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(hour)
            .setMinute(minute)
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    onTimeSelected(
                        this.hour,
                        this.minute,
                        isStartTime
                    )
                }
            }.show(supportFragmentManager, MaterialTimePicker::class.java.canonicalName)
    }

    private fun onTimeSelected(hour: Int, minute: Int, isStartTime: Boolean) {
        if (isStartTime) {
            selectedStartHour = hour
            selectedStartMinute = minute
            binding.tvStartTimeDescription.text = setTimeString(hour, minute)
        } else {
            selectedEndHour = hour
            selectedEndMinute = minute
            binding.tvEndTimeDescription.text = setTimeString(hour, minute)
        }
    }

    private fun initializeSpinner(@ArrayRes textArrayResId: Int, spinner: AppCompatSpinner) {
        ArrayAdapter.createFromResource(
            this,
            textArrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_shift -> {
                saveShift()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveShift() = with(binding) {
        if (validateDateAndTime()) return@with
        val employee = spEmployee.selectedItem.toString()
        val role = spRole.selectedItem.toString()
        val color = spColor.selectedItem.toString()
        val shiftModel = ShiftModel(
            role,
            employee,
            formatCurrentDateToModel(
                currentSelectedStartDate,
                selectedStartHour,
                selectedStartMinute
            ),
            formatCurrentDateToModel(currentSelectedEndDate, selectedEndHour, selectedEndMinute),
            color
        )
        Intent().apply { putExtra("model", shiftModel) }
            .also { setResult(123, it) }
        finish()
    }

    private fun validateDateAndTime(): Boolean {
        if (currentSelectedStartDate == null || currentSelectedEndDate == null) {
            Toast.makeText(
                this,
                "Please select a date",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        if (selectedStartHour == null || selectedEndHour == null || selectedStartMinute == null || selectedEndMinute == null) {
            Toast.makeText(
                this,
                "Please select a time",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        //We choose to validate as the same date for now because of the json data, but this may change in the future
        if (currentSelectedStartDate != currentSelectedEndDate) {
            Toast.makeText(
                this,
                "It must be the current date",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        if (selectedStartHour!! > selectedEndHour!!) {
            Toast.makeText(
                this,
                "Start hour is higher than end hour",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        return false
    }

}