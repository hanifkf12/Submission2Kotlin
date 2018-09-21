package com.hanifkf.submission2.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hanifkf.submission2.R
import com.hanifkf.submission2.Score
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.score_view.view.*

class ScoreAdapter(private val context: Context, private val items: List<Score>, private val listener: (Score)->Unit) : RecyclerView.Adapter<ScoreAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.score_view,parent,false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(items: Score, listener: (Score) -> Unit){
            itemView.home_team.text = items.homeTeam
            itemView.away_team.text= items.awayTeam
            itemView.home_score.text = items.scoreHome
            itemView.away_score.text = items.awayScore
            itemView.date_match.text = items.date
            containerView.setOnClickListener {
                listener(items)
            }
        }

    }

}