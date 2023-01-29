package com.example.thedonsdarling

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "debug"
const val WINNINGTAG = "WINNER"
val dbGame = Firebase.firestore.collection("game")
val dbPlayers = Firebase.firestore.collection("players")