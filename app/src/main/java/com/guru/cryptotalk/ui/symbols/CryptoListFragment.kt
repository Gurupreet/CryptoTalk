package com.guru.cryptotalk.ui.symbols


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.CryptoListFirebaseSync
import com.guru.cryptotalk.data.api.model.Crypto
import kotlinx.android.synthetic.main.fragment_crypto_list.*


class CryptoListFragment : Fragment() {

    private lateinit var cryptoListAdapter: CryptoListAdapter
    private val list = ArrayList<Crypto>()
    private var cryptoSync: CryptoListFirebaseSync? = null
    private var position: Int = 0;

    companion object {
        private const val POSITION = "position"
        fun newInstance(position: Int) = CryptoListFragment().apply {
            arguments = bundleOf(
                POSITION to position)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(POSITION)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crypto_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        startCryptoSync()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cryptoSync?.stopFirebaseSync()
        cryptoSync = null
    }

    private fun setupRecyclerView() {
        cryptoListAdapter = CryptoListAdapter(list)
        crypto_recyclerview.adapter = cryptoListAdapter
        crypto_recyclerview.layoutManager = LinearLayoutManager(context)
        cryptoListAdapter.notifyDataSetChanged()
    }

    private fun startCryptoSync() {
        if (cryptoSync == null) {
            cryptoSync = CryptoListFirebaseSync()
            list.clear()
            cryptoListAdapter.notifyDataSetChanged()
            cryptoSync?.startCryptoFirebbaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    var crypto = result as Crypto
                    when (observerType) {
                        FirebaseObserverType.CHILD_ADDED -> {

                                list.add(crypto)
                                list.reverse()
                                cryptoListAdapter.notifyDataSetChanged()

                            //reloadData()
                        }

                        FirebaseObserverType.CHILD_CHANGED -> {
                            val iterator :MutableListIterator<Crypto> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().name == crypto.name) {
                                    iterator.set(crypto)
                                    cryptoListAdapter.notifyItemChanged(index)
                                    break
                                }
                                index++
                            }
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<Crypto> = list.listIterator()
                            var index = 0
                            while (iterator.hasNext()) {
                                if (iterator.next().name == crypto.name) {
                                    iterator.remove()
                                    cryptoListAdapter.notifyItemRemoved(index)
                                    break
                                }
                                index++
                            }
                        }
                    }

                }

                override fun onFailure(result: Any) {
                  //  reloadData()
                }
            })

        }
    }

}
