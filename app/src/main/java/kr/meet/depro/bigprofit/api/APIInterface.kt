package kr.meet.depro.bigprofit.api

import kr.meet.depro.bigprofit.model.Product
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query
/*
data class post(var userId:String,var id:String,var title:String,var body:String)*/
interface APIInterface {

    @FormUrlEncoded
    @GET("posts")
    fun getRequest(@Query("store") store: String,
                   @Query("count") count: Int,
                   @Query("event") event: Int,
                   @Query("page") page: Int): retrofit2.Call<ArrayList<Product>>

/*    fun getRequest(@Query("userId") userId: String,
                   @Query("id") id: String,
                   @Query("title") title: String,
                   @Query("body") body: String): retrofit2.Call<ArrayList<post>>*/

}