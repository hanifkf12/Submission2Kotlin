package com.hanifkf.submission2


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hanifkf.submission2.Adapter.ScoreAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_prev.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PrevFragment : Fragment() {
    private lateinit var adapter: ScoreAdapter
    private var score : MutableList<Score> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_prev, container, false)
        view.progressBar1.visibility = View.VISIBLE
        val url = getString(R.string.url_prev)
        getData(url,view)
        view.swipeRefresh.setOnRefreshListener {
            getData(url,view)
        }


        view.recycle_prev.layoutManager = LinearLayoutManager(ctx)
        adapter =  ScoreAdapter(ctx, score){
            //toast(it.idEvent)
            startActivity<DetailActivity>("idEvent" to it.idEvent , "idHome" to it.idHome, "idAway" to it.idAway,"event" to "prev")
        }
        view.recycle_prev.adapter = adapter

        return view

    }

    fun getData(url:String , view: View){
        score.clear()
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object :JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d("Response", response.toString())
                        val jsonArray = response!!.getJSONArray("events")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            score.add(Score(jsonObject.optString("idEvent"),jsonObject.optString("dateEvent"),jsonObject.optString("strHomeTeam")
                            ,jsonObject.optString("intHomeScore"),jsonObject.optString("strAwayTeam"),jsonObject.optString("intAwayScore")
                            ,jsonObject.optString("idHomeTeam"),jsonObject.optString("idAwayTeam")))
                        }
                        score.addAll(score)
                        adapter.notifyDataSetChanged()

                        view.swipeRefresh.isRefreshing = false
                        view.progressBar1.visibility = View.GONE
                    }

                    override fun onError(anError: ANError?) {

                    }

                })

    }


}
