package kr.meet.depro.bigprofit.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        private val KAKAO_BASE_URL = "http://dapi.kakao.com/v2/local/search/"
        private val BIG_PROFIT_BASE_URL = "http://192.168.43.214:8080/"

        private val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            /*if (!BuildConfig.DEBUG) {

            }*/
        }
        private val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        /*val retrofit = Retrofit.Builder().apply {
            //baseUrl("https://openapi.naver.com/v1/search/")
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build().create(APIInterface::class.java)*/

        val kakaoApi = Retrofit.Builder().apply {
            baseUrl(KAKAO_BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build().create(KakaoInterface::class.java)

        val bigPrfitApi = Retrofit.Builder().apply {
            baseUrl(BIG_PROFIT_BASE_URL)
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build().create(APIInterface::class.java)




    }
}
