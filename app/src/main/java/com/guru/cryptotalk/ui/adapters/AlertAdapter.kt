package com.guru.cryptotalk.ui.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.guru.cryptotalk.Constants
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.model.Alert
import kotlinx.android.synthetic.main.row_alert_item.view.*

class AlertAdapter(val list: ArrayList<Alert>): RecyclerView.Adapter<AlertAdapter.AlertVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AlertVH {
        return AlertVH(LayoutInflater.from(parent.context).inflate(R.layout.row_alert_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(vh: AlertVH, position: Int) {
        vh.bind(list[position])
    }


    class AlertVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(alert: Alert) {
            itemView.symbol.text = alert.request.from_tkn+" - "+ alert.request.to_tkn
            itemView.from_amount.text = alert.request.amt.toString()+" "+alert.request.from_tkn
       //     itemView.to_amount.text = alert.toAmount.toString()+" "+alert.toToken+" ("+Constants.getUpArrow()+" "+alert.percent.toString()+"%)"
            itemView.status.text = alert.status
            if (alert.hash.isNotEmpty()) {
                itemView.status.text = "View on Etherscan"
                itemView.status.setOnClickListener {
                    openHash(alert.hash, it.context)
                }
            }
            itemView.to_amount.text = alert.request.amt.toString()+" "+alert.request.from_tkn
            if (alert.status == "triggered") {
                itemView.bar_view.background = ContextCompat.getDrawable(itemView.context, R.color.grey)
            } else {
                itemView.bar_view.background = ContextCompat.getDrawable(itemView.context, R.color.green)
            }
            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, FriendsProfileActivity::class.java)
//                intent.putExtra("userId", user.userId)
//                itemView.context.startActivity(intent)
            }
        }

        fun openHash(hash: String, context: Context) {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.launchUrl(context, Uri.parse(hash))
        }
    }
}