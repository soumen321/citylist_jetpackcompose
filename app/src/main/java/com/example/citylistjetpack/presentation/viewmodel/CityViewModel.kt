package com.example.citylistjetpack.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.domain.usecase.UserCaseCityList
import com.example.citylistjetpack.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val userCaseCityList: UserCaseCityList
) : ViewModel() {

    var state by mutableStateOf(CityState())

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    init {
        fetchCity()
    }

    private fun fetchCity(isRefresh:Boolean = false) {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
            )
            when(val result = userCaseCityList(isRefresh)) {


                is Resource.Success -> {
                    delay(2000)
                    state = state.copy(
                        weatherInfo = result.data,
                        isLoading = false,
                        onComplete = true,
                        error = null,
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message,
                    )
                }

            }
        }
    }

    fun refreshStates() {
        fetchCity(true)
    }


    data class CityState(
        val weatherInfo: List<CityList> =  emptyList(),
        val isLoading: Boolean = false,
        val onComplete:Boolean = false,
        val error: String? = null,

        )

}