package com.zkrallah.paymentsassistant.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zkrallah.paymentsassistant.R
import com.zkrallah.paymentsassistant.adapter.HomeAdapter
import com.zkrallah.paymentsassistant.adapter.PaymentAdapter
import com.zkrallah.paymentsassistant.databinding.FragmentHomeBinding
import com.zkrallah.paymentsassistant.local.entities.Payment
import com.zkrallah.paymentsassistant.local.entities.User


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var dialog: AlertDialog
    private lateinit var adapter: HomeAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recyclerHome.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        updateUI()

        swipeRefreshLayout = binding.container
        swipeRefreshLayout.setOnRefreshListener {
            updateUI()
        }

        return binding.root
    }

    private fun updateUI() {
        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner) { users ->
            users?.let { it ->
                adapter = HomeAdapter(it as MutableList<User>)
                binding.recyclerHome.adapter = adapter
                adapter.setItemClickListener(object : HomeAdapter.OnItemClickListener {
                    override fun onDeleteClicked(user: User, position: Int) {
                        buildAlertDialog(user, position)
                        dialog.show()
                    }

                    override fun onHistoryClicked(user: User) {
                        buildHistoryAlertDialog(user)
                        dialog.show()
                    }
                })
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun buildAlertDialog(user: User, position: Int) {
        val builder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
        builder.setCancelable(true)
        builder.setTitle("DELETE A USER")
        builder.setMessage(
            "Are you Sure you want to delete ${user.name} ?"
        )
        builder.setPositiveButton("DELETE") { _, _ ->
            viewModel.deleteUser(user.userId)
            adapter.removeItem(position)
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun buildHistoryAlertDialog(user: User) {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.user_payment_dialog, null)
        val builder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)

        builder.setCancelable(true)
        builder.setTitle("PAYMENT HISTORY")
        val recycler = dialogView.findViewById<RecyclerView>(R.id.recycler_user_payments)
        recycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        viewModel.getUserPaymentHistory(user)
        viewModel.payments.observe(viewLifecycleOwner) { data ->
            data?.let {
                val adapter = PaymentAdapter(it.payments as MutableList<Payment>)
                adapter.setItemClickListener(object : PaymentAdapter.OnItemClickListener {
                    override fun onDeleteClicker(payment: Payment, position: Int) {
                        viewModel.deletePayment(payment)
                        adapter.removeItem(position)
                    }
                })
                recycler.adapter = adapter
            }
        }
        builder.setView(dialogView)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}