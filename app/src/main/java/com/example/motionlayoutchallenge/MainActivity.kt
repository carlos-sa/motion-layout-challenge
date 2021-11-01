package com.example.motionlayoutchallenge

import android.os.Build
import com.example.motionlayoutchallenge.PilotInfo
import com.example.motionlayoutchallenge.PilotListAdapter
import com.example.motionlayoutchallenge.R

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.IllegalStateException
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class MainActivity : AppCompatActivity() {

    private val pilotList = mutableListOf(
            PilotInfo("Ayrton Senna", "4245", "senna"),
            PilotInfo("Rubens Barrichello", "4245", "barrichello"),
            PilotInfo("Michael Schumacher", "4245", "schumacher"),
            PilotInfo("Alain Prost", "4245", "prost")
    )

    private val pilotListAdapter = PilotListAdapter(pilotList)

    private lateinit var rv: RecyclerView


    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.d("MainActivity", "Alou")

        rv = findViewById(R.id.position_rv)


        val scope = CoroutineScope(Job() + Dispatchers.Main)

        tickerFlow(3000L).onEach {
            changePositions()
        }.launchIn(
            scope
        )

        with(rv) {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = pilotListAdapter
        }


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun changePositions() {
        Log.d("MainActivity", "Changing random positions")

        val toChange = Random.nextInt(1, 4)
        val fasterPilot = pilotList[toChange]
        val slowerPilot = pilotList[toChange - 1]

        val view1: MotionLayout = rv.findViewWithTag<MotionLayout>(fasterPilot.pilotName)
        val view2: MotionLayout = rv.findViewWithTag<MotionLayout>(slowerPilot.pilotName)

        view1.elevation = 1f
        view1.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
            }

            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                Log.d("Tag", currentId.toString())
                adjustPilotsList(toChange)
                view1.setTransitionListener(null)
                view2.setTransitionListener(null)
                view2.progress = 0f
                view1.elevation = 0f
                view2.setTransition(R.id.start, R.id.up)
                view1.progress = 0f
                view1.setTransition(R.id.start, R.id.up)

            }
        })

        view1.transitionToState(R.id.up)
        view2.transitionToState(R.id.down)

    }

    fun adjustPilotsList(fasterPilotPosition: Int) {
        val fasterPilot = pilotList.removeAt(fasterPilotPosition)
        pilotList.add(fasterPilotPosition - 1, fasterPilot)
        try {
            pilotListAdapter.notifyDataSetChanged()

        } catch (ex: IllegalStateException) {
            Log.d("MainActivity", "IllegalState")
        }
    }

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun tickerFlow(period: Long) = flow {
        delay(period)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

}