package com.example.dalelarsen.findrepo

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

        val searchTerm = intent.getStringExtra("searchTerm")

        val callback = object : Callback<GitHubSearchResult> {
            override fun onResponse(call: Call<GitHubSearchResult>?, response: Response<GitHubSearchResult>?) {
                val searchResult = response?.body()
                if (searchResult != null) {
                    for (repo in searchResult!!.items) {
                        println(repo.full_name)
                    }
                    val repoListView = findViewById<ListView>(R.id.repoListView)
                    repoListView.setOnItemClickListener { adapterView, view, i, l ->
                        val selectedRepo = searchResult!!.items[i]

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepo.html_url))
                        startActivity(intent)
                    }

                    val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, searchResult!!.items)
                    repoListView.adapter = adapter
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