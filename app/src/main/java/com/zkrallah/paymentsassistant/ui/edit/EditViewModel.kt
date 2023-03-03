package com.zkrallah.paymentsassistant.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zkrallah.paymentsassistant.local.GymDatabase
import com.zkrallah.paymentsassistant.local.entities.Payment
import com.zkrallah.paymentsassistant.local.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {

    private val database = GymDatabase.getInstance()

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun getUsers(){
        viewModelScope.launch (Dispatchers.IO) {
            _users.postValue(database.userDAO().getUsers())
        }
    }

    fun insert(user: User){
        viewModelScope.launch (Dispatchers.IO){
            database.userDAO().insertUser(user)
        }
    }

    fun update(user: User){
        viewModelScope.launch (Dispatchers.IO){
            database.userDAO().updateUser(user)
        }
    }

    fun pay(payment: Payment){
        viewModelScope.launch (Dispatchers.IO){
            database.paymentDAO().insertPayment(payment)
        }
    }
}