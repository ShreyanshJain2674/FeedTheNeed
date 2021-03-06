package com.gdscandroid.feedtheneed.Volunteer

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gdscandroid.feedtheneed.Donator.DonorData
import com.gdscandroid.feedtheneed.R
import com.gdscandroid.feedtheneed.Utility
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_donator_items.rvDonor
import kotlinx.android.synthetic.main.fragment_vol_things.*


class VolThingsFragment : Fragment() {

    val dbRef= FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vol_things, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(requireContext()).load(Utility.getProfile(requireActivity()).toString()).into(profile_fragg12)

        val volThingsData=ArrayList<DonorData>()
        val volThingsRVAdapter= VolThingsRVAdapter(volThingsData)
        rvDonor.layoutManager= LinearLayoutManager(activity)
        rvDonor.adapter=volThingsRVAdapter

        val postListener3= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var post3: DonorData?=snapshot.getValue(DonorData::class.java)
                val lati=post3?.latitude
                val longi=post3?.longitude

                val startPoint = Location("locationA")
                if (lati != null) {
                    startPoint.setLatitude(lati.toDouble())
                }
                if(longi!=null){
                    startPoint.setLongitude(longi.toDouble())
                }

                val endPoint = Location("locationA")
                endPoint.setLatitude(activity?.let { Utility.getLatitude(it) }!!.toDouble())
                endPoint.setLongitude(activity?.let { Utility.getLongitude(it) }!!.toDouble())

                val distance: Double = startPoint.distanceTo(endPoint)/1000.0
                Log.d("raatmekaam",distance.toString())

                if(post3!=null && (post3.status=="Posted" || post3.status=="Booked") && distance<=10){
                    post3.bookId=snapshot.key.toString()
                    post3.name=Utility.getName(activity!!).toString()
                    volThingsData.add(0,post3)
                }
                volThingsRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                var post3: DonorData?=snapshot.getValue(DonorData::class.java)
                for(i in 0..volThingsData.size-1){
                    if(volThingsData[i].bookId.toString().equals(snapshot.key)){
                        post3!!.bookId=snapshot.key.toString()
                        post3.name=Utility.getName(activity!!).toString()
                        volThingsData.set(i, post3)
                    }
                }
                volThingsRVAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
       dbRef.child("DonatorPost").addChildEventListener(postListener3) }

    }
