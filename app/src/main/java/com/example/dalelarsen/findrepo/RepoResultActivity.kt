package com.example.dalelarsen.findrepo

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val retriever = GitHubRetriever()

        val searchTerm = intent.getStringExtra("searchTerm")

        if (searchTerm != null) {
            val callback = object : Callback<GitHubSearchResult> {
                override fun onResponse(call: Call<GitHubSearchResult>?, response: Response<GitHubSearchResult>?) {
                    val searchResult = response?.body()
                    if (searchResult != null) {
                        listRepos(searchResult!!.items)
                    }
                }

                override fun onFailure(call: Call<GitHubSearchResult>?, t: Throwable?) {
                    println("Not Working!!")
                }
            }

            retriever.searchRepos(callback, searchTerm)

        } else {
            val userName = intent.getStringExtra("userName")
            val callback = object : Callback<List<GitHubRepo>> {
                override fun onResponse(call: Call<List<GitHubRepo>>?, response: Response<List<GitHubRepo>>?) {
                    if (response?.code() == 404) {
                        println("User does not exist")
                        val view = findViewById<View>(R.id.searchView)
                        Snackbar.make(view, "User does not exist", Snackbar.LENGTH_LONG).show()
                    } else {

                        val repos = response?.body()
                        if (repos != null) {
                            listRepos(repos)
                        }
                    }
                }

                override fun onFailure(call: Call<List<GitHubRepo>>?, t: Throwable?) {
                    println("In onFailure!!")
                }

            }

            retriever.userRepos(callback, userName)
        }

    }


    fun listRepos(repos: List<GitHubRepo>) {
        val repoListView = findViewById<ListView>(R.id.repoListView)
        repoListView.setOnItemClickListener { adapterView, view, i, l ->
            val selectedRepo = repos!![i]

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepo.html_url))
            startActivity(intent)
        }

        val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, repos!!)
        repoListView.adapter = adapter

    }


}

class RepoAdapter (context: Context?, resource: Int, objects: List<GitHubRepo>?) : ArrayAdapter<GitHubRepo>(context, resource, objects) {
    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val repoRow = inflater.inflate(R.layout.repo_list_row, parent, false)
        val repoText = repoRow.findViewById<TextView>(R.id.repoTextView)
        val imageView = repoRow.findViewById<ImageView>(R.id.repoImageView)

        val repoItem = getItem(position)
        repoText.text = repoItem.full_name

        Picasso.with(context).load(repoItem.owner.avatar_url).into(imageView);

        return repoRow
    }
}