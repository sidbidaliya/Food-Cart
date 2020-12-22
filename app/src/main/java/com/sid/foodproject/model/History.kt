package com.sid.foodproject.model

import org.json.JSONArray

class History(
    val OrderId: String,
    val RestName: String,
    val TotalCost: String,
    val TimeDate: String,
    val FoodItems: JSONArray
)