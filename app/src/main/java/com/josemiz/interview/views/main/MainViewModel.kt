package com.josemiz.interview.views.main

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josemiz.interview.models.ShiftListModel
import com.josemiz.interview.models.ShiftModel
import com.josemiz.interview.utils.readFile
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainViewModel : ViewModel() {

    private val _shiftList = MutableLiveData<List<ShiftModel>>()
    val shiftList: LiveData<List<ShiftModel>> get() = _shiftList

    private var shifts: ShiftListModel? = ShiftListModel(emptyList())

    fun loadShifts(assets: AssetManager) {
        val json = assets.readFile("shifts.json")
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(ShiftListModel::class.java)
        shifts = jsonAdapter.fromJson(json)
        _shiftList.value = shifts?.shifts ?: emptyList()
    }

    fun addShift(shift: ShiftModel?) {
        shift ?: return
        val list: ArrayList<ShiftModel> = shifts?.shifts?.let(::ArrayList) ?: ArrayList()
        list.add(shift)
        shifts?.shifts = list
        _shiftList.value = list
    }

}