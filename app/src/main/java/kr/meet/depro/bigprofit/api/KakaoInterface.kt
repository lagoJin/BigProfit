package kr.meet.depro.bigprofit.api

import kr.meet.depro.bigprofit.model.Mart
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoInterface {

    @Headers("Authorization: KakaoAK b7c1a8e83d1fd5e24dc96826399400a9")
    @GET("category.json")
    fun getMarts(@Query("category_group_code") category: String = "CS2", @Query("x") longitude: Float = 127.024612f,
                 @Query("y") latitude: Float = 37.532600f, @Query("radius") radius: Int = 500): Call<Mart>

}