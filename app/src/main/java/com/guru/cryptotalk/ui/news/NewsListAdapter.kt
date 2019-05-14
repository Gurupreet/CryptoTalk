package com.guru.cryptotalk.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.model.News

class NewsListAdapter(private val list: ArrayList<News>): RecyclerView.Adapter<NewsListAdapter.NewsVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        return NewsVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_news_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        holder.bind(list[position])
    }

    class NewsVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind (news: News) {
//           itemView.news_title.text = news.title
//            itemView.source.text = news.body
//
//            Glide.with(itemView.context)
//                .load(news.imageurl)
//                .into(itemView.news_image)
        }
    }
}