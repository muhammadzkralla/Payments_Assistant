package com.zkrallah.paymentsassistant.ui.payments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zkrallah.paymentsassistant.local.GymDatabase
import com.zkrallah.paymentsassistant.local.entities.Payment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentsViewModel : ViewModel() {

    private val database = GymDatabase.getInstance()

    private val _payments = MutableLiveData<List<Payment>>()
    val payments: LiveData<List<Payment>> = _payments

    fun getPayments(){
        viewModelScope.launch (Dispatchers.IO){
            _payments.postValue(database.paymentDAO().getPayments())
        }
    }

    fun deletePayment(payment: Payment){
        viewModelScope.launch (Dispatchers.IO){
            database.paymentDAO().deletePayment(payment.paymentId)
        }
    }

}