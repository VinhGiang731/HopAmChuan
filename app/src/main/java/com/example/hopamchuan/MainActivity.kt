package com.example.hopamchuan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.data.CustomAdapterSong
import com.example.data.ScrapeLyric
import com.example.data.Song
import com.example.hopamchuan.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CustomAdapterSong
    private lateinit var list: MutableList<Song>
    private lateinit var database: DatabaseReference
    private lateinit var scLyric: ScrapeLyric

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = mutableListOf()
        database = FirebaseDatabase.getInstance().getReference("NhacBallad")

        setUpAdapterListSong()
        getDataFromFireBase()
        addEventBtnSearch()
    }

    private fun addEventBtnSearch() {
        binding.btnSearch.setOnClickListener {
            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)
        }
    }

    private fun getDataFromFireBase() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(this@MainActivity, "Connect", Toast.LENGTH_SHORT).show()
                    list.clear()
                    for (song in snapshot.children) {
                        val songTitle = song.child("song_title").getValue(String::class.java) ?: ""
                        val authorName =
                            song.child("author_name").getValue(String::class.java) ?: ""
                        val urlLyric = song.child("url_lyric").getValue(String::class.java) ?: ""
                        val chordList = song.child("chord_list").getValue(String::class.java) ?: ""

                        val songItem = Song(songTitle, authorName, urlLyric, chordList)

                        list.add(songItem)
                        adapter.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                        binding.lvListSong.visibility = View.VISIBLE
                    }

                } else {
                    Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "FirebaseError\", \"Read database failed!: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("FirebaseError", "Read database failed!: ${error.message}")

            }
        })
    }

    private fun setUpAdapterListSong() {
        val i = Intent(this, LyricActivity::class.java)
        adapter = CustomAdapterSong(list, this, object : ItemClick {
            override fun onClickItem(pos: Int) {
                scLyric = ScrapeLyric(list[pos].urlLyric)
                i.putExtra("title", list[pos].songTitle)
                scLyric.scrapeWeb(
                    onLyric = { lyric ->
                        i.putExtra("lyric", lyric)
                    },
                    onNote = { note ->
                        i.putExtra("note", note)
                        startActivity(i)
                    }
                )
            }

            override fun onLongItem(pos: Int) {
                Toast.makeText(this@MainActivity, "Long", Toast.LENGTH_SHORT).show()
            }
        })

        binding.lvListSong.adapter = adapter
    }
}