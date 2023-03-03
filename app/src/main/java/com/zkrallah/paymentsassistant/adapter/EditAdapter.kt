package com.zkrallah.paymentsassistant.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zkrallah.paymentsassistant.R
import com.zkrallah.paymentsassistant.local.entities.User

class EditAdapter(private val list: MutableList<User>) : RecyclerView.Adapter<EditAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {

        fun onEditClicked(user: User, position: Int)
        fun onPayClicked(user: User)

    }

    fun setItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.edit_user_item, parent, false), mListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTV.text = list[position].name
        holder.endDataTV.text = "Subscription ends: ${list[position].endSubscriptionDate}"
        holder.idTV.text = "ID : ${list[position].userId}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(user: User){
        list.add(user)
        notifyItemInserted(list.size)
    }
    fun editItem(user: User, position: Int){
        list[position] = user
        notifyItemChanged(position)
    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val nameTV: TextView = itemView.findViewById(R.id.name)
        val endDataTV: TextView = itemView.findViewById(R.id.end_date)
        val idTV: TextView = itemView.findViewById(R.id.id)
        private val payBtn: ImageButton = itemView.findViewById(R.id.pay_btn)
        private val editBtn: ImageButton = itemView.findViewById(R.id.edit_btn)

        init {
            editBtn.setOnClickListener {
                listener.onEditClicked(list[adapterPosition], adapterPosition)
            }

            payBtn.setOnClickListener {
                listener.onPayClicked(list[adapterPosition])
            }
        }
    }
}