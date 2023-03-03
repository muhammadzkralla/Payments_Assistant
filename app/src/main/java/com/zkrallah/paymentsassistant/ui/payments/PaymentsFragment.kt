package com.zkrallah.paymentsassistant.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zkrallah.paymentsassistant.adapter.PaymentAdapter
import com.zkrallah.paymentsassistant.databinding.FragmentPaymentsBinding
import com.zkrallah.paymentsassistant.local.entities.Payment

class PaymentsFragment : Fragment() {

    private var _binding: FragmentPaymentsBinding? = null
    private lateinit var viewModel: PaymentsViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PaymentsViewModel::class.java]
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        val layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL,
            false)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        binding.recyclerPayments.layoutManager = layoutManager

        updateUI()

        swipeRefreshLayout = binding.container
        swipeRefreshLayout.setOnRefreshListener {
            updateUI()
        }

        return binding.root
    }

    private fun updateUI() {
        viewModel.getPayments()
        viewModel.payments.observe(viewLifecycleOwner) {
            it?.let {
                val adapter = PaymentAdapter(it as MutableList<Payment>)
                adapter.setItemClickListener(object : PaymentAdapter.OnItemClickListener {
                    override fun onDeleteClicker(payment: Payment, position: Int) {
                        viewModel.deletePayment(payment)
                        adapter.removeItem(position)
                    }

                })
                binding.recyclerPayments.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}