package com.gdscandroid.loginproject.Volunteer

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.*
import com.bumptech.glide.util.Util
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_vol_quest_apply.*
import java.util.*

class VolQuestApplyActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener  {

    private lateinit var pickedtimetxt: TextView
    private lateinit var post: Button
    private lateinit var pick: Button
    private var isPickedTime: Boolean = false

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vol_quest_apply)

        pickedtimetxt = findViewById(R.id.pickedtime_txt)
        post = findViewById(R.id.post)
        pick = findViewById(R.id.pick)

        pick.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month,day)
            datePickerDialog.show()
        }

        post.setOnClickListener {
            if(nov_edit.text.toString().trim().isBlank()){
                Toast.makeText(this,"Please enter required volunteers",Toast.LENGTH_SHORT).show()
            }else if(info_edit.text.toString().trim().isBlank()){
                Toast.makeText(this,"Please fill the info of quest",Toast.LENGTH_SHORT).show()
            }else if(locatn_txt.text.toString().trim().isBlank()){
                Toast.makeText(this,"Please enter the quest location",Toast.LENGTH_SHORT).show()
            }else{
                val pd = ProgressDialog(this)
                pd.setCancelable(false)
                pd.setMessage("Please Wait...Data is Uploading !!")
                pd.show()
                val nov=nov_edit.text.toString().trim()
                val info=info_edit.text.toString()
                val location=locatn_txt.text.toString()
                val uid:String = Utility.getUid(this).toString()
                val database = FirebaseDatabase.getInstance().reference.child("PersonalQuestData").child(uid).push()
                var ranid = database.key.toString()
                ranid = ranid.substring(1,ranid.length-1)
                val dbRef=FirebaseDatabase.getInstance().reference.child("PersonalQuestData")
                    .child(Utility.getUid(this).toString())
                dbRef.child("noofVolunteers").setValue(nov)
                dbRef.child("Info").setValue(info)
                dbRef.child("Location").setValue(location)
                dbRef.child("QuestId").setValue(ranid)
                dbRef.child("Phone").setValue(Utility.getMobile(this).toString())
                dbRef.child("time").setValue(pickedtimetxt.text.toString())
                dbRef.child("Latitude").setValue(Utility.getLatitude(this).toString())
                dbRef.child("Longitude").setValue(Utility.getLongitude(this).toString())
                dbRef.child("Status").setValue("Upcoming")
                dbRef.child("Leadname").setValue(Utility.getName(this).toString())
                dbRef.child("LeftVolunteers").setValue(nov)
                dbRef.child("VolunteerUid").setValue(uid)
                dbRef.child("LeadPic").setValue(Utility.getProfile(this).toString())

                val dbRef2=FirebaseDatabase.getInstance().reference.child("QuestData")
                    .child(ranid)
                dbRef2.child("noofVolunteers").setValue(nov)
                dbRef2.child("Info").setValue(info)
                dbRef2.child("Location").setValue(location)
                dbRef2.child("QuestId").setValue(ranid)
                dbRef2.child("VolunteerUid").setValue(uid)
                dbRef2.child("Phone").setValue(Utility.getMobile(this).toString())
                dbRef2.child("time").setValue(pickedtimetxt.text.toString())
                dbRef2.child("Latitude").setValue(Utility.getLatitude(this).toString())
                dbRef2.child("Longitude").setValue(Utility.getLongitude(this).toString())
                dbRef2.child("Status").setValue("Upcoming")
                dbRef2.child("Leadname").setValue(Utility.getName(this).toString())
                dbRef2.child("LeftVolunteers").setValue(nov)
                dbRef2.child("LeadPic").setValue(Utility.getProfile(this).toString())
                pd.dismiss()
                finish()
            }
        }

    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, this, hour, minute,
            DateFormat.is24HourFormat(this)
        )
        timePickerDialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        pickedtimetxt.text = ""+myYear+"/"+myMonth+"/"+myDay+" , "+myHour+":"+myMinute
        //pickedtimetxt.text = "Year: " + myYear + "\n" + "Month: " + myMonth + "\n" + "Day: " + myDay + "\n" + "Hour: " + myHour + "\n" + "Minute: " + myMinute
        isPickedTime = true
    }
}