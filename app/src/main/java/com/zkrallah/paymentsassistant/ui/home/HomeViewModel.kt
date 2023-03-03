package com.zkrallah.paymentsassistant.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zkrallah.paymentsassistant.local.GymDatabase
import com.zkrallah.paymentsassistant.local.entities.Payment
import com.zkrallah.paymentsassistant.local.entities.User
import com.zkrallah.paymentsassistant.local.entities.UserWithPayments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val database = GymDatabase.getInstance()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    private val _payments = MutableLiveData<UserWithPayments?>()
    val payments: LiveData<UserWithPayments?> =_payments

    fun getUsers(){
        viewModelScope.launch (Dispatchers.IO) {
            _users.postValue(database.userDAO().getUsers())
        }
    }

    fun deleteUser(id: Long){
        viewModelScope.launch (Dispatchers.IO) {
            database.userDAO().deleteUser(id)
        }
    }

    fun getUserPaymentHistory(user: User){
        viewModelScope.launch (Dispatchers.IO){
            _payments.postValue(database.userDAO().getUserWithPaymentsById(user.userId))
        }
    }

    fun deletePayment(payment: Payment){
        viewModelScope.launch (Dispatchers.IO){
            database.paymentDAO().deletePayment(payment.paymentId)
        }
    }
}