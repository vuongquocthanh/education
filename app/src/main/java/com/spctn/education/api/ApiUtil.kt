package com.spctn.education.api

import com.apollographql.apollo.ApolloClient
import okhttp3.OkHttpClient

object ApiUtil {
    private val GITHUB_AUTH_TOKEN: String = "bd7ad428f8c84aa39448081d87baa94f041abbb9"
    private val BASE_URL: String = "http://solienlacdientu.herokuapp.com/graphql/"

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val builder = original.newBuilder().method(original.method(), original.body())
//                builder.header("Authorization", "Bearer $GITHUB_AUTH_TOKEN")
                it.proceed(builder.build())
            }
            .build()
    }

    fun apolloClient(): ApolloClient {
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(getOkHttpClient())
            .build()
    }
}