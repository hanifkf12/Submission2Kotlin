package com.hanifkf.submission2.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanifkf.submission2.Adapter.FavoriteAdapter
import com.hanifkf.submission2.Database.database
import com.hanifkf.submission2.DetailActivity
import com.hanifkf.submission2.Model.Favorite
import com.hanifkf.submission2.R
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.coroutines.experimental.selects.select
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoriteFragment : Fragment() {
    private var favorites: MutableList<Favorite> = mutableListOf()
    private lateinit var adapter: FavoriteAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_favorite, container, false)

        view.recycle_fav.layoutManager = LinearLayoutManager(ctx)
        adapter = FavoriteAdapter(ctx, favorites){
            startActivity<DetailActivity>("idEvent" to it.idEvent , "idHome" to it.idHome, "idAway" to it.idAway,"event" to it.event,
                    "date" to it.date, "scoreHome" to it.scoreHome,"homeTeam" to it.homeTeam, "awayScore" to it.awayScore,"awayTeam" to it.awayTeam)
        }
        view.recycle_fav.adapter = adapter
        showFavorite(view)

        view.swipeRefresh2.onRefresh {
            favorites.clear()
            showFavorite(view)
        }

        return view
    }

    private fun showFavorite(view: View){
        context?.database?.use{
            view.swipeRefresh2.isRefreshing = false
            val result = select(Favorite.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<Favorite>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
    }


}
