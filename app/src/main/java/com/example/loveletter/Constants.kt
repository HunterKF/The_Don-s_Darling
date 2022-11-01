package com.example.loveletter

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val TAG = "debug"
val dbGame = Firebase.firestore.collection("game")
val dbPlayers = Firebase.firestore.collection("players")