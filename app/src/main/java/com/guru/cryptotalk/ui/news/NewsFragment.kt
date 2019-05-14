package com.guru.cryptotalk.ui.news


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.guru.cryptotalk.R
import com.guru.cryptotalk.ui.news.NewsListAdapter

class NewsFragment : Fragment() {
    private lateinit var newsListAdapter: NewsListAdapter
   // private val list = ArrayList<>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  setupRecyclerView()

    }
//    private fun setupRecyclerView() {
//        newsListAdapter = NewsListAdapter(list)
//        news_recyclerview.adapter = newsListAdapter
//        news_recyclerview.layoutManager = LinearLayoutManager(context)
//        val dividerItemDecoration = DividerItemDecoration(context, 0)
//        news_recyclerview.addItemDecoration(dividerItemDecoration)
//        newsListAdapter.notifyDataSetChanged()
//
//    }





}
