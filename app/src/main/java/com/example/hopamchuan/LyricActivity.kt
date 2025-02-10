package com.example.hopamchuan

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.data.ScrapeLyric
import com.example.hopamchuan.databinding.ActivityLyricBinding

class LyricActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLyricBinding
    private lateinit var scLyric: ScrapeLyric

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLyricBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displaySong()
        addEventBtnBack()
    }

    private fun addEventBtnBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun displaySong() {
        val i = intent

        val note = i.getStringExtra("note")
        val lyric = i.getStringExtra("lyric")
        val songTitle = i.getStringExtra("title")

        scLyric = ScrapeLyric()
        binding.txtTitle.text = songTitle

        if (note == "") {
            binding.cardNote.visibility = View.GONE
        } else {
            binding.txtNote.text = scLyric.formatChord(note.toString())
        }
        binding.txtLyric.text = scLyric.formatChord(lyric.toString())
    }
}