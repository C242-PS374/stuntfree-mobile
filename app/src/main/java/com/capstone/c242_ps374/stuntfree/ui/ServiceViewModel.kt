package com.capstone.c242_ps374.stuntfree.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.c242_ps374.stuntfree.data.model.DayItem
import com.capstone.c242_ps374.stuntfree.data.service.Service
import com.capstone.c242_ps374.stuntfree.data.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository
) : ViewModel() {

    // LiveData untuk daftar layanan utama
    private val _services = MutableLiveData<List<Service>>()
    val services: LiveData<List<Service>> = _services

    // LiveData untuk daftar tombol
    private val _buttonServices = MutableLiveData<List<Service>>()
    val buttonServices: LiveData<List<Service>> = _buttonServices

    // LiveData untuk status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // LiveData untuk pesan error
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _weekDays = MutableLiveData<List<DayItem>>()
    val weekDays: LiveData<List<DayItem>> = _weekDays

    private val _todayDate = MutableLiveData<String>()
    val todayDate: LiveData<String> = _todayDate

    private val _childAge = MutableLiveData<String>()
    val childAge: LiveData<String> = _childAge

    private var allServices: List<Service> = listOf()

    private var lastClickedButton: String? = null // Tombol terakhir yang diklik

    /**
     * Mengambil semua layanan dari repository.
     * Data utama disimpan di [allServices] dan ditampilkan di [_services].
     */
    fun fetchServices() {
        _isLoading.postValue(true)
        _error.postValue(null)
        viewModelScope.launch {
            val result = serviceRepository.getServices()
            result.onSuccess { response ->
                allServices = response.listStory
                _services.postValue(allServices)
            }.onFailure { exception ->
                _error.postValue(exception.message)
            }.also {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Mengambil semua layanan sekaligus mengisi data untuk tombol.
     */
    fun fetchServicesWithButton() {
        _isLoading.postValue(true)
        _error.postValue(null)
        viewModelScope.launch {
            val result = serviceRepository.getServices()
            result.onSuccess { response ->
                allServices = response.listStory

                // Data utama
                _services.postValue(allServices)

                // Data tombol: Hanya item unik berdasarkan nama (aman untuk null)
                val buttonList = allServices.filter { !it.name.isNullOrEmpty() }.distinctBy { it.name }
                _buttonServices.postValue(buttonList)
            }.onFailure { exception ->
                _error.postValue(exception.message)
            }.also {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Memfilter daftar layanan berdasarkan nama tombol yang diklik.
     */
    fun filterServicesByButton(buttonName: String) {
        if (lastClickedButton == buttonName) {
            // Jika tombol yang sama diklik, reset filter
            lastClickedButton = null
            _services.postValue(allServices)
        } else {
            // Terapkan filter
            lastClickedButton = buttonName
            val filteredList = allServices.filter { it.name == buttonName }
            _services.postValue(filteredList)
        }
    }

    /**
     * Mengembalikan daftar layanan ke semua data.
     */
    private fun resetFilter() {
        _services.postValue(allServices)
    }

    /**
     * Mencari layanan berdasarkan query.
     */
    fun searchServices(query: String) {
        if (query.isEmpty()) {
            resetFilter()
        } else {
            val filteredList = allServices.filter {
                it.name?.contains(query, ignoreCase = true) ?: false
            }
            _services.postValue(filteredList)
        }
    }

//    private fun generateWeekDays() {
//        val weekDaysList = mutableListOf<DayItem>()
//        val calendar = Calendar.getInstance()
//
//        for (i in 0..6) {
//            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.time)
//            val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
//            val isSelected = (i == 0) // Hari pertama adalah hari ini
//            weekDaysList.add(DayItem(date, dayName, isSelected))
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//        }
//
//        _weekDays.value = weekDaysList
//    }

    fun generateDummyData() {
        _todayDate.value = "27-November-2024"

        val dummyData = listOf(
            DayItem("27", "Monday", true),  // Today
            DayItem("28", "Tuesday", false),
            DayItem("29", "Wednesday", false),
            DayItem("30", "Thursday", false),
            DayItem("01", "Friday", false),
            DayItem("02", "Saturday", false),
            DayItem("03", "Sunday", false)
        )
        _weekDays.value = dummyData
    }

    fun generateChildAge() {
        _childAge.value = "Day 4"
    }
}
