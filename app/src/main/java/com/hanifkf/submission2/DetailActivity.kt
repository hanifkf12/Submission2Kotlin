package com.hanifkf.submission2

import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.hanifkf.submission2.Database.database
import com.hanifkf.submission2.Model.Fav
import com.hanifkf.submission2.Model.Favorite
import com.hanifkf.submission2.R.drawable.ic_add_to_favorites
import com.hanifkf.submission2.R.drawable.ic_added_to_favorites
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.ctx
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.toast
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {
    private var menuItem: Menu? = null
    private lateinit var favorite: Fav
    private var isFavorite: Boolean = false
    private lateinit var params: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        progressBar.visibility = View.VISIBLE
        val base_url_event ="https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id="
        val base_url_team = "https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id="

        intent = intent
        params = intent.getStringExtra("idEvent")
        val params1 = intent.getStringExtra("idHome")
        val params2 = intent.getStringExtra("idAway")
        val event = intent.getStringExtra("event")
        val date = intent.getStringExtra("date")
        val scoreHome = intent.getStringExtra("scoreHome")
        val homeTeam = intent.getStringExtra("homeTeam")
        val awayScore = intent.getStringExtra("awayScore")
        val awayTeam = intent.getStringExtra("awayTeam")
        favorite = Fav(params,date,homeTeam,scoreHome,awayTeam,awayScore,params1,params2,event)

        getHomeTeam(base_url_team,params1)
        getAwayTeam(base_url_team,params2)
        getDetail(base_url_event,params,event)
        favoriteState()
    }
    private fun addToFavorite(){
        try {
            database.use {
                insert(Favorite.TABLE_FAVORITE,
                        Favorite.EVENT_ID to favorite.idEvent,
                        Favorite.EVENT_DATE to favorite.date,
                        Favorite.HOME_TEAM to favorite.homeTeam,
                        Favorite.SCORE_HOME to favorite.scoreHome,
                        Favorite.AWAY_TEAM to favorite.awayTeam,
                        Favorite.AWAY_SCORE to favorite.awayScore,
                        Favorite.ID_HOME to favorite.idHome,
                        Favorite.ID_AWAY to favorite.idAway,
                        Favorite.EVENT to favorite.event)
            }
            toast("Add To Favorite")
        }catch (e: SQLiteConstraintException){
            toast(e.localizedMessage)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.add_to_favorite ->{
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }



    fun getHomeTeam(url : String,params:String){
        val home_url = url+params
        AndroidNetworking.get(home_url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        //Log.d("dasdasd", response.toString())
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
                            }else if(event.equals("prev")){
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

    private fun removeFromFavorite(){
        try{
            database.use {
                delete(Favorite.TABLE_FAVORITE,"(EVENT_ID = {id})" ,
                        "id" to params)
            }
            toast("Removed from Favorite")
        }catch (e :SQLiteConstraintException){
            toast(e.localizedMessage)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, ic_add_to_favorites)
    }
    private fun favoriteState(){
        database.use {
            val result = select(Favorite.TABLE_FAVORITE)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to params)
            val favorite = result.parseList(classParser<Favorite>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }
}
