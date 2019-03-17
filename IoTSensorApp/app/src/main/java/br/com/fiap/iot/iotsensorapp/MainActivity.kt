package br.com.fiap.iot.iotsensorapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*


import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MessageActivity"

    private var mDatabase: DatabaseReference? = null
    private var mMessageReference: DatabaseReference? = null
    private var mMessageListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDatabase  = FirebaseDatabase.getInstance().reference
        mMessageReference = FirebaseDatabase.getInstance().getReference("sensores")

        ultrasonico.text = "Aguardando Leitura..."
        som.text = "Aguardando Leitura..."
        luminosidade.text = "Aguardando Leitura..."


    }

    private fun getStatusSensor(sensor:String?): String{
        if(sensor!=null && sensor!!.length > 0){
            var statusSensor = sensor.substring(0,4)

            if(statusSensor=="0002")
                return "Não Ativado"

            if(statusSensor=="0001")
                return "Ativado"

            return statusSensor
        }
        return "Sem leitura"
    }

    override fun onStart() {
        super.onStart()
        val messageListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val sensor = dataSnapshot.getValue(Sensores::class.java)

                    Log.e(TAG, "onDataChange: Message data is updated: " + sensor!!.toString())

                    ultrasonico.text = getStatusSensor(sensor!!.S0001)
                    som.text = getStatusSensor(sensor!!.S0002)
                    luminosidade.text = getStatusSensor(sensor!!.S0003)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.e(TAG, "onCancelled: Failed to read message")

                ultrasonico.text = ""
                som.text = ""
                luminosidade.text = ""
            }
        }

        mMessageReference!!.addValueEventListener(messageListener)

        // copy for removing at onStop()
        mMessageListener = messageListener
    }

    override fun onStop() {
        super.onStop()

        if (mMessageListener != null) {
            mMessageReference!!.removeEventListener(mMessageListener)
        }
    }
}
