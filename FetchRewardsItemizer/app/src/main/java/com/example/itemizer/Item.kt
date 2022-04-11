package com.example.itemizer

/*
 Item class is a data class to represent each item: its ID, list ID, and name.

 Long is used for id and listId as numeric IDs can get pretty large. Even though the ones
 in the currently proffered json file could fit into in an Int, later versions of that file could have
 IDs that are larger.
 */
data class Item (
    val id: Long,
    val listId: Long,
    val name: String?
)