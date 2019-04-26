package kr.meet.depro.bigprofit.api

import kr.meet.depro.bigprofit.model.Product
import kr.meet.depro.bigprofit.model.SearchItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
interface APIInterface {

/*
data class post(var userId:String,var id:String,var title:String,var body:String)*/

    @GET("search")
    fun searchItem(@Query("q") name: String): Call<List<SearchItem>>
    @GET("products")
    fun getRequest(@Query("store") store: String,
                   @Query("count") count: Int,
                   @Query("event") event: Int,
                   @Query("page") page: Int): Call<ArrayList<Product>>

/*    fun getRequest(@Query("userId") userId: String,
                   @Query("id") id: String,
                   @Query("title") title: String,
                   @Query("body") body: String): retrofit2.Call<ArrayList<post>>*/

}