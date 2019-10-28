package com.guru.cryptotalk.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guru.cryptotalk.R
import kotlinx.android.synthetic.main.row_token_search_item.view.*

class TokenSearchAdapter(val tokenSelectedListerner: OnTokenSelectedListerner ,val list: ArrayList<String>): RecyclerView.Adapter<TokenSearchAdapter.TokenSearchVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TokenSearchVH {
        return TokenSearchVH(LayoutInflater.from(parent.context).inflate(R.layout.row_token_search_item, parent, false))
    }

    fun setData(newlist: List<String>) {
        list.clear()
        list.addAll(newlist)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(vh: TokenSearchVH, position: Int) {
        vh.bind(list[position], tokenSelectedListerner)
    }


    class TokenSearchVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(token: String, tokenSelectedListerner: OnTokenSelectedListerner) {
            itemView.token.text = token
            try {
                Glide.with(itemView.context)
                    .load(itemView.context.resources.getIdentifier(token.toLowerCase(), "drawable", itemView.context.packageName))
                    .into(itemView.logo)
            } catch (e: Exception) {

            }
            itemView.setOnClickListener { tokenSelectedListerner.OnTokenSelected(token) }
        }

    }

    interface OnTokenSelectedListerner {
        fun OnTokenSelected(token: String)
    }
}