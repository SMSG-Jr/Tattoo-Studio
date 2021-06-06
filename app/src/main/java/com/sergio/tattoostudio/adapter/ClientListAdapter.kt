package com.sergio.tattoostudio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.ClientInformation

class ClientListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<ClientListAdapter.ClientListViewHolder>() {

    private var list : List<ClientInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_client, parent, false)
        return ClientListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClientListViewHolder, position: Int) {
        holder.set(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(clientList: List<ClientInformation>) {
        this.list = clientList
        notifyDataSetChanged()
    }

    inner class ClientListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val photo : AppCompatImageView = itemView.findViewById(R.id.imageView_client_photo)
        val name: AppCompatTextView = itemView.findViewById(R.id.textView_client_name)
        private val tattooDate: AppCompatTextView = itemView.findViewById(R.id.textView_tattoo_date)

        fun set(client: ClientInformation) {
            name.text = client.cname
            tattooDate.text = client.cdateOfLastTattoo
        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
            listener.onItemClick(position)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}