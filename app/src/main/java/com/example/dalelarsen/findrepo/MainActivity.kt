package com.example.dalelarsen.findrepo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchText = findViewById<EditText>(R.id.searchText)

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val intent = Intent(this, SearchResultActivity::class.java)
            intent.putExtra("searchTerm", searchText.text.toString())
            startActivity(intent)
        }

        val userRepoEditText = findViewById<EditText>(R.id.userRepoEditText)
        val viewRepoButton = findViewById<Button>(R.id.userRepoButton)
        viewRepoButton.setOnClickListener {
            val intent = Intent(this, SearchResultActivity::class.java)
            intent.putExtra("userName", userRepoEditText.text.toString())
            startActivity(intent)
        }


    }
}
