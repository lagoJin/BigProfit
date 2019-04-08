package kr.meet.depro.bigprofit.api

import kr.meet.depro.bigprofit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient{

    companion object {

        private val interceptor = HttpLoggingInterceptor().apply {
            if(!BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()
        
        private val retrofit = Retrofit.Builder().apply {
            //baseUrl("https://openapi.naver.com/v1/search/")
            addConverterFactory(GsonConverterFactory.create())
            client(client)
        }.build().create(APIInterface::class.java)
    }
}
