package com.hanifkf.submission2.Model



data class Favorite(val id:Long?, val idEvent:String?, val date:String? , val homeTeam:String? , val scoreHome : String?, val awayTeam:String? , val awayScore:String?
                    , val idHome:String?, val idAway:String?, val event: String?){
    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val EVENT_ID: String = "EVENT_ID"
        const val EVENT_DATE: String = "EVENT_DATE"
        const val HOME_TEAM: String = "HOME_TEAM"
        const val SCORE_HOME: String = "SCORE_HOME"
        const val AWAY_TEAM : String  = "AWAY_TEAM"
        const val AWAY_SCORE: String = "AWAY_SCORE"
        const val ID_HOME: String ="ID_HOME"
        const val ID_AWAY: String ="ID_AWAY"
        const val EVENT : String = "EVENT"
    }
}
