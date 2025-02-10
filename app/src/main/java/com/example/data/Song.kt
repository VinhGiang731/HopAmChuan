package com.example.data

data class Song(
    val songTitle: String = "",
    val authorName: String = "",
    val urlLyric: String = "",
    val chordList: String = ""
) {
    override fun toString(): String {
        return "Song(songTitle=$songTitle, authorName=$authorName, urlLyric=$urlLyric, chordList='$chordList')"
    }
}