package com.example.hopamchuan

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hopamchuan.databinding.ActivitySearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var database: DatabaseReference
    private val list = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("NhacBallad")
        binding.edtSearch.requestFocus()

        setUpAdapter(list)
        addEventBtnBack()
        addEventEdtSearch()
    }

    private fun addEventEdtSearch() {
        binding.edtSearch.addTextChangedListener {
            val searchText = binding.edtSearch.text.toString().trim()
            if (searchText.isNotEmpty() && searchText.length > 1) {
                val query = database.orderByChild("song_title")
                    .startAt(searchText)
                    .endAt("$searchText\uf8ff")

                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        if (snapshot.exists()) {
                            for (song in snapshot.children) {
                                val songTitle =
                                    song.child("song_title").getValue(String::class.java) ?: ""
                                list.add(songTitle)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }

    private fun setUpAdapter(list: ArrayList<String>) {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        binding.lvListSong.adapter = adapter
    }

    private fun addEventBtnBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}