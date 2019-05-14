package com.guru.cryptotalk.ui.symbols

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.model.Crypto
import kotlinx.android.synthetic.main.row_crypto_list_item.view.*
import kotlin.math.round

class CryptoListAdapter(private val list: ArrayList<Crypto>): RecyclerView.Adapter<CryptoListAdapter.CryptoListVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoListVH {
        return CryptoListVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_crypto_list_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CryptoListVH, position: Int) {
       holder.bind(list[position])
    }

    class CryptoListVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind (crypto: Crypto) {
            itemView.symbol.text = crypto?.name?.replace("/", " => ")
            itemView.name.text = "exchange: "+crypto?.exchange
            itemView.last_price.text = crypto?.last_price.toString()
            if (crypto?.day_change!! > 0) {
                itemView.day_change.text = round(crypto?.day_change_percentage!!).toString() + "%"
                itemView.day_change.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                itemView.bar_view.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else {
                itemView.day_change.text = round(crypto?.day_change_percentage!!).toString() + "%"
                itemView.day_change.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                itemView.bar_view.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.red))
            }
            itemView.hour.text = "1.03%"
            itemView.day.text = crypto?.day_change_percentage.toString()+"%"
            itemView.week.text = "1.01%"
            itemView.high.text = crypto?.day_high.toString()
            itemView.low.text = crypto?.day_low.toString()
            itemView.vol.text = crypto?.volume_traded.toString()
            if (crypto.name!!.startsWith("ETH")) {
            Glide.with(itemView.context)
                .load(R.drawable.eth)
                .into(itemView.logo)
            } else {
                try {
                    Glide.with(itemView.context)
                        .load(itemView.context.resources.getIdentifier(crypto?.symbol!!.toLowerCase(), "drawable", itemView.context.packageName))
                        .into(itemView.logo)
                } catch (e: Exception) {

                }
            }
        }
    }


}