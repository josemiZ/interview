package com.josemiz.interview.utils

import android.content.res.AssetManager
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.*

//This function only loads files from assets directory
fun AssetManager.readFile(fileName: String) = open(fileName)
    .bufferedReader()
    .use(BufferedReader::readText)

private const val isoPattern = "yyyy-MM-dd hh:mm:ss ZZZZZ"
private const val dayPattern = "E, MMMM dd"

fun convertLongToTimeDate(time: Long): String {
    val date = Date(time)
    return convertDateToString(date, dayPattern)
}

fun convertJsonToDateString(time: String): String {
    val date = convertStringToDate(time)
    return convertDateToString(date ?: Date(), dayPattern)
}

private fun convertStringToDate(time: String): Date? {
    val format = SimpleDateFormat(isoPattern, Locale.getDefault())
    return format.parse(time)
}

private fun convertDateToString(date: Date, pattern: String): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}

fun convertDateToHourRange(startTime: String, endTime: String): String {
    val startDate = convertStringToDate(startTime)
    val endDate = convertStringToDate(endTime)
    val startCalendar = Calendar.getInstance()
    startCalendar.time = startDate ?: Date()
    val startHour = startCalendar.get(Calendar.HOUR_OF_DAY)
    val startMinute = startCalendar.get(Calendar.MINUTE)
    val endCalendar = Calendar.getInstance()
    endCalendar.time = endDate ?: Date()
    val endHour = endCalendar.get(Calendar.HOUR_OF_DAY)
    val endMinute = endCalendar.get(Calendar.MINUTE)
    return "${setTimeString(startHour, startMinute)} - ${setTimeString(endHour, endMinute)}"
}

fun setTimeString(hour: Int, minute: Int): String {
    return when {
        hour > 12 -> "${hour - 12}:$minute PM"
        hour == 12 -> "$hour:$minute M"
        else -> "$hour:$minute AM"
    }
}

fun formatCurrentDateToModel(date: Long?, hour: Int?, minute: Int?): String {
    val currentDate = date?.let(::Date) ?: Date()
    val calendar = GregorianCalendar()
    calendar.time = currentDate
    hour?.let { calendar.set(Calendar.HOUR, it) }
    minute?.let { calendar.set(Calendar.MINUTE, it) }
    val fixedDate = calendar.time
    return convertDateToString(fixedDate, isoPattern)
}