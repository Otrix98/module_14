package com.example.module_14

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_map.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import kotlin.random.Random

class MainFragment: Fragment (R.layout.fragment_main) {

    val avatarLinks = listOf(
        "https://i.pinimg.com/736x/a2/0c/72/a20c7217d3084bfd949e5db4822f05d0--green-knight-capture-the-flag.jpg",
        "https://pbs.twimg.com/media/DijazhkWsAAPOXK.jpg:large",
        "https://i.pinimg.com/736x/69/aa/dd/69aadd69cc93f9a4eefb69e0154e878c.jpg",
        "https://i.pinimg.com/originals/2b/02/82/2b02827aa41b5ead59399fda7e8eeb86.jpg",
        "https://i.pinimg.com/originals/57/9f/64/579f64060c026211fa07aa8bdc735396.png",
        "https://i.pinimg.com/736x/79/b9/e8/79b9e8e389ec2edc12d1dacbf824eba6.jpg",
        "https://i.pinimg.com/originals/06/96/59/0696597cc9e3e31cdef0b65cd7a727bd.jpg"
    )

//    fun updateCurrentItem() {
//        currentItem = Map (
//            id = Random.nextLong(),
//            avatarLink = avatarLinks.random(),
//            map = "",
//            time = selectedInstant ?: Instant.now()
//        )
//    }

//var currentCoordinates: String = "coord"

    var currentItem = Map (
        id = Random.nextLong(),
        avatarLink = avatarLinks.random(),
        map = "currentCoord",
        time = Instant.now()
    )

    private var currentMap: List<Map> = emptyList()

    private var MapAdapter: MapAdapter? = null

    private var rationaleDialog: AlertDialog? = null

    private var selectedInstant: Instant? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLocationInfo()
        button.setOnClickListener {
            getCurrentLocationWithPermissionCheck()
            showLocationInfo()
            addMap()
        }
        isTextVisiable()
        initList()
        MapAdapter?.items = (currentMap)
    }

    private fun getCurrentLocationWithPermissionCheck() {
       val isLocationPermissionGranted =  ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
       ) == PackageManager.PERMISSION_GRANTED

        if (isLocationPermissionGranted) {
            showLocationInfo()
        } else {
            val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (needRationale) {
                showLocationRationaleDialog()
            } else {
                requestLocationPermissions()
            }
    }}



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            showLocationInfo()
        } else {
            val needRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (needRationale) {
                showLocationRationaleDialog()
            } else {
                requestLocationPermissions()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showLocationInfo(){
        LocationServices.getFusedLocationProviderClient(requireContext())
            .lastLocation
            .addOnSuccessListener {
                it?.let {
                    currentItem.map = """
                        Lat = ${it.latitude}
                        Lng = ${it.longitude}
                        Speed = ${it.speed}
                        Accuracy = ${it.accuracy}
                    """.trimIndent()
                } ?: Toast.makeText(activity, "Локация отсутствует",  Toast.LENGTH_SHORT).show()
            }
            .addOnCanceledListener {
                Toast.makeText(activity, "Запрос локации был отменён",  Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Запрос локации завершился неудачно",  Toast.LENGTH_SHORT).show()
            }

    }

    private fun showLocationRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Необходимо одобрение разрешения для отображения информации")
            .setPositiveButton("OK", {_, _-> requestLocationPermissions()})
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rationaleDialog?.dismiss()
        rationaleDialog = null
        MapAdapter = null
    }



    private fun requestLocationPermissions() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 54
    }

    fun isTextVisiable(){
        if (currentMap.isEmpty()) {
            textEmpty.visibility = View.VISIBLE
        } else if (currentMap.isNotEmpty()){
            textEmpty.visibility = View.GONE
        }
    }


    private fun initList() {
        MapAdapter = MapAdapter { position ->
            val positionTime = MapAdapter?.items?.get(position)?.time
            initTimePicker(positionTime, position)
        }
        with(maplist) {
            adapter = MapAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            itemAnimator = FlipInLeftYAnimator()
        }
    }

    private fun initTimePicker(time: Instant?, position: Int) {
        val currentDateTime = LocalDateTime.ofInstant(time, ZoneId.systemDefault())

        DatePickerDialog(
            requireContext(),
            {_, year, month, dayOfMonth ->
                TimePickerDialog(
                    requireContext(),
                    {_, hourOfDay, minute ->

//                        fun updateTime(position: Int){
//                            currentMap[position].time = (selectedInstant ?: Instant.now())
//                            MapAdapter?.items = (currentMap)
//
//                    }
                        val zonedDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, hourOfDay, minute)
                            .atZone(ZoneId.systemDefault())

                        selectedInstant = zonedDateTime.toInstant()


                        updateTime(position)


                    },
                    currentDateTime.hour,
                    currentDateTime.minute,
                    true
                ).show()
            },
            currentDateTime.year,
            currentDateTime.month.value -1,
            currentDateTime.dayOfMonth
        )
            .show()
    }

    fun updateTime(position: Int) {
        currentMap[position].time = (selectedInstant ?: Instant.now())
        MapAdapter?.items = (currentMap)
        MapAdapter?.notifyDataSetChanged()
    }


    private fun deleteMap( position: Int) {

        currentMap = currentMap.filterIndexed { index, food -> index != position }
        MapAdapter?.items = (currentMap)
        isTextVisiable()
    }

    private fun addMap() {
        selectedInstant = null
        val newMap = currentItem.let {
            it.copy(id = Random.nextLong())
        }
        currentMap = listOf(newMap) + currentMap
        MapAdapter?.items = (currentMap)
        isTextVisiable()
        maplist.scrollToPosition(0)
    }

}