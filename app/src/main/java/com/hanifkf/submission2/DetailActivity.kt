package com.hanifkf.submission2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.ctx
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        progressBar.visibility = View.VISIBLE
        val base_url_event ="https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id="
        val base_url_team = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id="

        intent = intent
        val params = intent.getStringExtra("idEvent")
        val params1 = intent.getStringExtra("idHome")
        val params2 = intent.getStringExtra("idAway")
        val event = intent.getStringExtra("event");

        getHomeTeam(base_url_team,params1)
        getAwayTeam(base_url_team,params2)
        getDetail(base_url_event,params,event)
    }

    fun getHomeTeam(url : String,params:String){
        val home_url = url+params
        AndroidNetworking.get(home_url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d("dasdasd", response.toString())
                        val jsonArray = response!!.getJSONArray("teams")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            home_team_detail.text = jsonObject.optString("strTeam")
                            Glide.with(ctx).load(jsonObject.optString("strTeamBadge")).into(imgHome)
                        }
                    }

                    override fun onError(anError: ANError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
    }

    fun getAwayTeam(url:String , params: String){
        val away_url = url+params
        AndroidNetworking.get(away_url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d("dasdasd", response.toString())
                        val jsonArray = response!!.getJSONArray("teams")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            away_team_detail.text = jsonObject.optString("strTeam")
                            Glide.with(ctx).load(jsonObject.optString("strTeamBadge")).into(imgAway)
                        }
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(anError: ANError?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
    }

    fun getDetail(url:String, params: String, event:String){

        val event_url = url+params
        AndroidNetworking.get(event_url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        val jsonArray = response!!.getJSONArray("events")
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            date_match_detail.text =jsonObject.optString("dateEvent")
                            if (event.equals("next")){
                                home_score_detail.text = ""
                                away_score_detail.text = ""
                            }else{
                                home_score_detail.text = jsonObject.optString("intHomeScore")
                                away_score_detail.text = jsonObject.optString("intAwayScore")
                                scorer_home.text = jsonObject.optString("strHomeGoalDetails")
                                scorer_away.text = jsonObject.optString("strAwayGoalDetails")
                                shots_home.text = jsonObject.optString("intHomeShots")
                                shots_away.text = jsonObject.optString("intAwayShots")
                                gk_home.text = jsonObject.optString("strHomeLineupGoalkeeper")
                                gk_away.text = jsonObject.optString("strAwayLineupGoalkeeper")
                                def_home.text = jsonObject.optString("strHomeLineupDefense")
                                def_away.text = jsonObject.optString("strAwayLineupDefense")
                                mid_home.text = jsonObject.optString("strHomeLineupMidfield")
                                mid_away.text = jsonObject.optString("strAwayLineupMidfield")
                                fw_home.text = jsonObject.optString("strHomeLineupForward")
                                fw_away.text = jsonObject.optString("strAwayLineupForward")
                                subs_home.text = jsonObject.optString("strHomeLineupSubstitutes")
                                subs_away.text = jsonObject.optString("strAwayLineupSubstitutes")
                            }


                        }
                    }

                    override fun onError(anError: ANError?) {

                    }

                })
    }
}
