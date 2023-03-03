package com.zkrallah.paymentsassistant.ui.edit

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zkrallah.paymentsassistant.R
import com.zkrallah.paymentsassistant.adapter.EditAdapter
import com.zkrallah.paymentsassistant.databinding.FragmentEditBinding
import com.zkrallah.paymentsassistant.local.entities.Payment
import com.zkrallah.paymentsassistant.local.entities.User
import java.text.SimpleDateFormat
import java.util.*


class EditFragment : Fragment() {

    private val calendar: Calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    private var _binding: FragmentEditBinding? = null
    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: EditViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: EditAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this)[EditViewModel::class.java]
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.recyclerEdit.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        updateUI()

        binding.fab.setOnClickListener {
            buildAddAlertDialog()
            dialog.show()
        }

        swipeRefreshLayout = binding.container
        swipeRefreshLayout.setOnRefreshListener {
            updateUI()
        }


        return binding.root
    }

    private fun updateUI() {
        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner){
            it?.let {
                adapter = EditAdapter(it as MutableList<User>)
                adapter.setItemClickListener(object: EditAdapter.OnItemClickListener{
                    override fun onEditClicked(user: User, position: Int) {
                        buildEditAlertDialog(user, position)
                        dialog.show()
                    }

                    override fun onPayClicked(user: User) {
                        val payment = Payment(
                            formatter.format(calendar.time).toString(),
                            user.subscriptionPrice,
                            user.userId,
                            user.name
                        )
                        viewModel.pay(payment)
                    }

                })
                binding.recyclerEdit.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun buildAddAlertDialog() {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_user, null)
        val edtName = dialogView.findViewById<EditText>(R.id.edt_name)
        val edtStartDate = dialogView.findViewById<EditText>(R.id.edt_start_date)
        val edtEndDate = dialogView.findViewById<EditText>(R.id.edt_end_date)
        val edtPrice = dialogView.findViewById<EditText>(R.id.edt_price)

        val startTime = formatter.format(calendar.time).toString()
        calendar.add(Calendar.MONTH, 1)
        val endTime = formatter.format(calendar.time).toString()
        calendar.add(Calendar.MONTH, -1)
        edtStartDate.setText(startTime)
        edtEndDate.setText(endTime)

        val builder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
        builder.setView(dialogView)
        builder.setCancelable(true)
        builder.setTitle("ADD A USER")
        builder.setMessage(
            "Fill in the data :"
        )
        builder.setPositiveButton("ADD") { _, _ ->
            if (edtName.text.isNotEmpty() && edtPrice.text.isNotEmpty()){
                val user = User(
                    edtName.text.toString(),
                    edtStartDate.text.toString(),
                    edtEndDate.text.toString(),
                    edtPrice.text.toString()
                )
                viewModel.insert(user)
                adapter.addItem(user)
            }else
                Toast
                    .makeText(activity,
                        "Discarded",
                        Toast.LENGTH_LONG)
                    .show()
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun buildEditAlertDialog(user: User, position: Int) {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.add_user, null)
        val edtName = dialogView.findViewById<EditText>(R.id.edt_name)
        val edtStartDate = dialogView.findViewById<EditText>(R.id.edt_start_date)
        val edtEndDate = dialogView.findViewById<EditText>(R.id.edt_end_date)
        val edtPrice = dialogView.findViewById<EditText>(R.id.edt_price)

        edtName.setText(user.name)
        edtStartDate.setText(user.subscriptionDate)
        edtEndDate.setText(user.endSubscriptionDate)
        edtPrice.setText(user.subscriptionPrice)

        val builder = AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)
        builder.setView(dialogView)
        builder.setCancelable(true)
        builder.setTitle("EDITING ${user.name}")
        builder.setMessage(
            "Fill in the data :"
        )

        builder.setPositiveButton("SAVE") { _, _ ->
            if (edtName.text.isNotEmpty() && edtPrice.text.isNotEmpty()){
                user.name = edtName.text.toString()
                user.subscriptionDate = edtStartDate.text.toString()
                user.endSubscriptionDate = edtEndDate.text.toString()
                user.subscriptionPrice = edtPrice.text.toString()
                adapter.editItem(user, position)
                viewModel.update(user)
            }else
                Toast
                    .makeText(activity,
                        "Discarded",
                        Toast.LENGTH_LONG)
                    .show()
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}