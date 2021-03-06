package com.gdgchicagowest.windycitydevcon.data

import com.gdgchicagowest.windycitydevcon.model.Venue
import com.google.firebase.database.*

class FirebaseVenueProvider : VenueProvider {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val queries: MutableMap<Any, Query> = mutableMapOf()
    private val listeners: MutableMap<Any, ValueEventListener> = mutableMapOf()

    override fun addVenueListener(key: Any, onComplete: (Venue?) -> Unit) {
        if (queries[key] != null) {
            removeVenueListener(queries)
        }

        val listener = object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot?) {
                onComplete(data?.getValue(Venue::class.java))
            }

            override fun onCancelled(e: DatabaseError?) {
                onComplete(null)
            }
        }
        listeners[key] = listener

        val query = database.child("venue")
        query.addValueEventListener(listener)
        queries[key] = query
    }

    override fun removeVenueListener(key: Any) {
        val query = queries[key]
        query?.removeEventListener(listeners[key])

        queries.remove(key)
        listeners.remove(key)
    }
}