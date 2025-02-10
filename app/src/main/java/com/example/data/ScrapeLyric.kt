package com.example.data

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ScrapeLyric(private val url: String? = "") {
    fun scrapeWeb(onLyric: (String) -> Unit, onNote: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val document = Jsoup.connect(url).get()

                val getSongNote = document.select("div.song-lyric-note")
                val getLyric = document.select("div.pre > div.chord_lyric_line")
                val lyric = getLyric.joinToString("\n") { it.text() }
                val note = getSongNote.joinToString("\n") { it.text() }

                withContext(Dispatchers.Main) {
                    onLyric(lyric)
                    onNote(note)
                }
            } catch (e: Exception) {
                Log.e("Scraping Error", "Lỗi: ${e.message}")
                withContext(Dispatchers.Main) {
                    onLyric("Failed get data: ${e.message}")
                    onNote("Failed get data: ${e.message}")
                }
            }
        }
    }

     fun formatChord(lyric: String): SpannableString {
        val spannable = SpannableString(lyric)
        val pattern = "\\[[^\\]]+\\]".toRegex()

        val matcher = pattern.findAll(lyric)

        for (match in matcher) {
            spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                match.range.first,
                match.range.last + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannable
    }
}