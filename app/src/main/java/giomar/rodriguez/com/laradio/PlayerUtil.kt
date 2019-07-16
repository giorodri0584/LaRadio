package giomar.rodriguez.com.laradio

import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.createWithNotificationChannel
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import giomar.rodriguez.com.laradio.model.StationViewModel

class PlayerUtil: Service(){


    var trackSelector: TrackSelector? = null
    var dataSourceFactory: DefaultDataSourceFactory? = null
    var exoPlayer: SimpleExoPlayer? = null
    lateinit var model: StationViewModel
    var url: String? = null
    lateinit var playerNotificationManager: PlayerNotificationManager
    val context: Context = this

    override fun onCreate() {
        super.onCreate()



        trackSelector = DefaultTrackSelector()
        dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "La Radio Dominicana"));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle: Bundle? = intent!!.extras
        url = bundle!!.getString("url")
        val mediaSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(bundle!!.getString("url")))
        exoPlayer!!.prepare(mediaSource)
        exoPlayer!!.playWhenReady = true

        playerNotificationManager = createWithNotificationChannel(
            context, "Playback", R.string.playback_la_radio_dominicana, 90,
            NotificationAdapter(bundle!!.getString("title"), bundle!!.getString("description"), bundle!!.getString("logoUrl"), context)
        )


        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        Log.i("Player", "Player OnDestroy")
        exoPlayer!!.release()
        exoPlayer = null
        Log.i("Player", "Player Destroyed")
        super.onDestroy()
    }

}