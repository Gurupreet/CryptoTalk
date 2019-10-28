package com.guru.cryptotalk.ui.alerts


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.guru.cryptotalk.App
import com.guru.cryptotalk.R
import com.guru.cryptotalk.data.api.SharedPrefrenceManager
import com.guru.cryptotalk.data.api.api.*
import com.guru.cryptotalk.data.api.firebase.FirebaseAuthManager
import com.guru.cryptotalk.data.api.firebase.FirebaseManager
import com.guru.cryptotalk.data.api.firebase.FirebaseObserverType
import com.guru.cryptotalk.data.api.firebase.FirebaseResponseCompletionHandler
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.AlertFirebaseSync
import com.guru.cryptotalk.data.api.firebase.FirebaseSyncs.CryptoListFirebaseSync
import com.guru.cryptotalk.data.api.model.Alert
import com.guru.cryptotalk.data.api.model.Crypto
import com.guru.cryptotalk.ui.adapters.AlertAdapter
import com.guru.cryptotalk.ui.wallet.ImportWalletActivity
import kotlinx.android.synthetic.main.fragment_alerts.*
import org.json.JSONObject
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AlertsFragment : Fragment() {
    private lateinit var adapter: AlertAdapter

    private val list = ArrayList<Alert>()
    private var alertFirebaseSync: AlertFirebaseSync? = null
    private var cryptoSync: CryptoListFirebaseSync? = null
    var crypto: Crypto? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertFirebaseSync?.stopFirebaseSync()
        alertFirebaseSync = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        wallet.setOnClickListener {
           WalletBottomSheetDialog().show(fragmentManager!!, "")
        }
        fab.setOnClickListener {
            startActivity(AddAlertActivity.getIntent(it.context))
        }
        wallet_address.text = App.cred?.address ?: "Tap to Add Wallet"
        SharedPrefrenceManager.keyData.observe(this, Observer {
            App.cred = Credentials.create(SharedPrefrenceManager.getPrivateKey(context!!))
            FirebaseManager.getInstance().updateUser("addr", App.cred?.address!!)
            getBalance()
        })
        getBalance()
        startAlertSync()
        startCryptoSync()
    }

    private fun startCryptoSync() {
        if (cryptoSync == null) {
            cryptoSync =  CryptoListFirebaseSync()
            cryptoSync?.startCryptoFirebbaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    crypto = result as Crypto

                }

                override fun onFailure(result: Any) {
                    //  reloadData()
                }
            })

        }
    }

    private fun setUpRecyclerview() {
        adapter = AlertAdapter(list)
        val layoutManager = LinearLayoutManager(context)
        alert_recyclerview.adapter = adapter
        alert_recyclerview.layoutManager = layoutManager
    }

    private fun getBalance() {
        if (App.cred != null) {
            wallet_address.text = App.cred?.address ?: "Tap to Add Wallet"
            balance?.text = "getting balance .."

            ApiManager.getInstance().getBalance(App.cred!!.address, object : ApiResponseHandler {
                override fun onComplete(data: Any) {
                    val apiResponse = data as ApiResponse
                    balance?.text = App.df.format(apiResponse.ETH) + " ETH"
                }
                override fun onFailed(data: Any) {
                    balance?.text = "Error getting balance."
                }

            })
        }
    }

    private fun startAlertSync() {
        if (alertFirebaseSync == null && FirebaseAuthManager.getInstance().getCurrentUserId().isNotEmpty()) {
            alertFirebaseSync = AlertFirebaseSync()
            list.clear()
            adapter.notifyDataSetChanged()
            alertFirebaseSync?.startAlertFirebaseSync(object : FirebaseResponseCompletionHandler {
                override fun onSuccess(result: Any, observerType: FirebaseObserverType) {
                    var alert = result as Alert
                    when (observerType) {
                        FirebaseObserverType.CHILD_ADDED -> {
                            list.add(alert)
                            list.reverse()
                           adapter.notifyDataSetChanged()

                            //reloadData()
                        }

                        FirebaseObserverType.CHILD_CHANGED -> {
                            val iterator :MutableListIterator<Alert> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().key == alert.key) {
                                    iterator.set(alert)
                                    adapter.notifyItemChanged(index)
                                    break
                                }
                                index++
                            }
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<Alert> = list.listIterator()
                            var index = 0
                            while (iterator.hasNext()) {
                                if (iterator.next().key == alert.key) {
                                    iterator.remove()
                                    adapter.notifyItemRemoved(index)
                                    break
                                }
                                index++
                            }
                        }
                    }

                }

                override fun onFailure(result: Any) {
                    //  reloadData()
                    adapter.notifyDataSetChanged()
                }
            })

        }
    }

}
