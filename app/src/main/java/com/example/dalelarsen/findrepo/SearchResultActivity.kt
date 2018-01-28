package com.example.dalelarsen.findrepo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")

        val callback = object : Callback<GitHubSearchResult> {
            override fun onResponse(call: Call<GitHubSearchResult>?, response: Response<GitHubSearchResult>?) {
                val searchResult = response?.body()
                if (searchResult != null) {
                    for (repo in searchResult!!.items) {
                        println(repo.full_name)
                    }
                }
            }

            override fun onFailure(call: Call<GitHubSearchResult>?, t: Throwable?) {
                println("Not Working!!")
            }
        }

        val retriever = GitHubRetriever()

        retriever.searchRepos(callback, searchTerm)
    }
}
