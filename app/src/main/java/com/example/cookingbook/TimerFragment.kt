package com.example.cookingbook

import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.VolumeShaper
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.cookingbook.extensions.vibratePhone


private const val ARG_PARAM_SECONDS = "seconds"
private const val ARG_PARAM_RUNNING = "running"
private const val ARG_PARAM_WAS_RUNNING = "wasRunning"

class TimerFragment : Fragment() {
    private var seconds = 0
    private var running = false
    private var wasRunning = false

    private lateinit var secondsView: EditText
    private lateinit var minutesView: EditText
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var pauseButton: Button
    private lateinit var alarmButton: Button

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var volumeShaper: VolumeShaper
    private lateinit var audioManager: AudioManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            seconds = it.getInt(ARG_PARAM_SECONDS)
            running = it.getBoolean(ARG_PARAM_RUNNING)
            wasRunning = it.getBoolean(ARG_PARAM_WAS_RUNNING)
        }
    }

    class MinMaxFilter(private val minValue: Int, private val maxValue: Int) : InputFilter {
        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dStart: Int,
            dEnd: Int
        ): CharSequence? {
            try {
                val input = Integer.parseInt(dest.toString() + source.toString())
                if (isInRange(minValue, maxValue, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return ""
        }

        private fun isInRange(a: Int, b: Int, c: Int): Boolean {
            return if (b > a) c in a..b else c in b..a
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        secondsView = view.findViewById(R.id.timer_seconds)
        minutesView = view.findViewById(R.id.timer_minutes)
        startButton = view.findViewById(R.id.start_timer_button)
        stopButton = view.findViewById(R.id.stop_timer_button)
        pauseButton = view.findViewById(R.id.pause_timer_button)
        alarmButton = view.findViewById(R.id.stop_alarm_button)

        secondsView.filters = arrayOf(MinMaxFilter(0, 59))
        minutesView.filters = arrayOf(MinMaxFilter(0, 180))

        startButton.setOnClickListener {
            if (secondsView.text.isEmpty() || minutesView.text.isEmpty()) {
                return@setOnClickListener
            }

            seconds = secondsView.text.toString().toInt() + minutesView.text.toString().toInt() * 60
            if (seconds <= 0) {
                return@setOnClickListener
            }

            running = true
            refreshUIStates()
        }

        pauseButton.setOnClickListener {
            running = false
            refreshUIStates()
        }

        stopButton.setOnClickListener {
            running = false
            seconds = 0
            secondsView.setText("%02d".format(0))
            minutesView.setText("%02d".format(0))
            refreshUIStates()
        }

        alarmButton.setOnClickListener {
            running = false
            stopAlarm()
            initVolumeShaper()
            refreshUIStates()
        }

        runTimer()
        refreshUIStates()

        return view
    }


    private fun refreshUIStates() {
        secondsView.isEnabled = !running && seconds == 0
        minutesView.isEnabled = !running && seconds == 0
        startButton.visibility = if (!running || (!running && seconds > 0)) View.VISIBLE else View.GONE
        pauseButton.visibility = if (running && !(this::mediaPlayer.isInitialized && mediaPlayer.isPlaying)) View.VISIBLE else View.GONE
        stopButton.visibility = if (seconds > 0) View.VISIBLE else View.GONE
        alarmButton.visibility = if (this::mediaPlayer.isInitialized && mediaPlayer.isPlaying) View.VISIBLE else View.GONE
    }

    private fun runTimer() {
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            if (running && seconds == 0) {
                playAlarm()
                refreshUIStates()
            } else if (running) {
                seconds--
                secondsView.setText("%02d".format(seconds % 60))
                minutesView.setText("%02d".format(seconds / 60))
            }

            handler.postDelayed({ runTimer() }, 1000)
        }
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
            refreshUIStates()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_PARAM_SECONDS, seconds)
        outState.putBoolean(ARG_PARAM_RUNNING, running)
        outState.putBoolean(ARG_PARAM_WAS_RUNNING, wasRunning)
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimerFragment()
    }


    private fun playAlarm() {
        if (!this::mediaPlayer.isInitialized) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm_sound)
            mediaPlayer!!.isLooping = true
            initVolumeShaper()
        }
        audioManager = requireContext().getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)

        volumeShaper.apply(VolumeShaper.Operation.PLAY)
        mediaPlayer!!.start()

        vibratePhone(300)
    }


    private fun initVolumeShaper() {
        val config: VolumeShaper.Configuration = VolumeShaper.Configuration.Builder()
            .setDuration(10000)
            .setCurve(floatArrayOf(0f, 1f), floatArrayOf(0f, 1f))
            .setInterpolatorType(VolumeShaper.Configuration.INTERPOLATOR_TYPE_CUBIC)
            .build()

        volumeShaper = mediaPlayer.createVolumeShaper(config)
    }


    private fun stopAlarm() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
        }
    }

    override fun onDestroy() {
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        super.onDestroy()
    }
}