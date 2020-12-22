package com.sid.foodproject.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.sid.foodproject.R
import com.sid.foodproject.adapter.FaqRecyclerAdapter
import com.sid.foodproject.model.Faq

class FaqFragment : Fragment() {

    private lateinit var recyclerFaq : RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FaqRecyclerAdapter

    private val helpLineNumbers = arrayListOf<Faq>(
        Faq("Q1 How will be my orders delivered?","Orders are delivered directly by the Packaging Assist suppliers. Different items in an order could be fulfilled by different suppliers. We will share the contact details and amount payable for all deliveries via email and SMS."),
        Faq("Q2 How are you guys are different?","Best products - We only list the products which have passed our tests and are the best suitable for delivery. They will help in giving a better consumer experience, so you can get get repeated orders."),
        Faq("Q3 Is delivery free?","Yes. There are no delivery or handling charges."),
        Faq("Q4 Why is my location is not serviceable?","We are currently live only in a few cities. We will be expanding soon."),
        Faq("Q5 Can  place order on call?","Sorry, we don’t accept orders on call."),
        Faq("Q6 Will I get my entire order in single delivery?","This depends on the items you have ordered. We have partnered with different suppliers for different products to help you get the best pricing."),
        Faq("Q7 How have you selected these products?","We have show products which have passed our quality tests and are best suited for delivery. We will keep updating our catalog with better products. Stay tuned!"),
        Faq("Q8 When is the order delivered?","Our suppliers deliver the products within 3-4 days of orders being placed."),
        Faq("Q9 Will I get an invoice?","Yes. You will get your invoice from our supplier at the time of delivery."),
        Faq("Q10 When I make an online order, why there is an error message such as \"technical difficulty\" and what it does means? ","The error message comes up due to our network community problem, please try in few minutes later on a regular basis. If you still have same problems please contact our call center."),
        Faq("Q11 Why does my order came delay and is there any compensation? ","30 minutes is our delivery guarantee. There are several things that caused the delivery to be delay, iewe can’t find your address properly, parking is away, bad traffic, extreme weather, etc. You will be given free voucher as the compensation."),
        Faq("Q12 Do I need to tip my courier?","Tips aren't included and they aren't expected or required. You can always rate your experience after you order.")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faq, container, false)

        recyclerFaq = view.findViewById(R.id.recyclerFaq)
        layoutManager = LinearLayoutManager(activity)
        recyclerAdapter = FaqRecyclerAdapter(activity as Context,helpLineNumbers)
        recyclerFaq.adapter = recyclerAdapter
        recyclerFaq.layoutManager = LinearLayoutManager(context as Context)

        return view
    }

}
