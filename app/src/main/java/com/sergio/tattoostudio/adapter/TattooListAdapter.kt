package com.sergio.tattoostudio.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.sergio.tattoostudio.R
import com.sergio.tattoostudio.entity.TattooInformation

class TattooListAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<TattooListAdapter.TattooListViewHolder>() {

    private var list : List<TattooInformation> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TattooListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_tattoo, parent, false)
        return TattooListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TattooListViewHolder, position: Int) {
        holder.set(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(tattooList: List<TattooInformation>) {
        this.list = tattooList
        notifyDataSetChanged()
    }

    inner class TattooListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener{
        val photo : AppCompatImageView = itemView.findViewById(R.id.imageView_tattooPhoto)
        private val name: AppCompatTextView = itemView.findViewById(R.id.textView_tattooName)
        private val description: AppCompatTextView = itemView.findViewById(R.id.textView_tattooDescription)
        private val tattooDate: AppCompatTextView = itemView.findViewById(R.id.textView_tattooDate)
        private val tattooValue: AppCompatTextView = itemView.findViewById(R.id.textView_tattooValue)

        fun set(tattoo: TattooInformation) {
            name.text = tattoo.tattooName
            description.text = tattoo.tattooDescription
            tattooDate.text = tattoo.tattooDate
            tattooValue.text = tattoo.tattooPrice
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
