package kr.meet.depro.bigprofit.api

import kr.meet.depro.bigprofit.model.`List<SearchItem>`
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("search")
    fun searchItem(@Query("q") name: String): Call<List<`List<SearchItem>`>>

}