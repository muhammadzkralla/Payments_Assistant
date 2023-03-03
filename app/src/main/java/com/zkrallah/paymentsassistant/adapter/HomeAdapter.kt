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

class HomeAdapter(private val list: MutableList<User>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {

        fun onDeleteClicked(user: User, position: Int)
        fun onHistoryClicked(user: User)

    }

    fun setItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false), mListener)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTV.text = list[position].name
        holder.startDateTV.text = "From: ${list[position].subscriptionDate}"
        holder.endDateTV.text = "To: ${list[position].endSubscriptionDate}"
        holder.priceTV.text = "Price: ${list[position].subscriptionPrice}$"
        holder.idTV.text = "ID : ${list[position].userId}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val nameTV: TextView = itemView.findViewById(R.id.name)
        val startDateTV: TextView = itemView.findViewById(R.id.start_date)
        val endDateTV: TextView = itemView.findViewById(R.id.end_date)
        val priceTV: TextView = itemView.findViewById(R.id.price)
        val idTV: TextView = itemView.findViewById(R.id.userId)
        private val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_btn)
        private val historyBtn: ImageButton = itemView.findViewById(R.id.history_btn)

        init {
            deleteBtn.setOnClickListener {
                listener.onDeleteClicked(list[adapterPosition], adapterPosition)
            }
            historyBtn.setOnClickListener {
                listener.onHistoryClicked(list[adapterPosition])
            }
        }
    }
}