package giomar.rodriguez.com.laradio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.gson.Gson
import giomar.rodriguez.com.laradio.model.Station
import giomar.rodriguez.com.laradio.model.StationViewModel
import giomar.rodriguez.com.laradio.model.Supplier
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var model: StationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this).get(StationViewModel::class.java)

        val stationObserver = Observer<Station> { station ->
            Log.i("station", station.name)
            tvPlayerTitle.text = station.frequency + " FM"
            tvPlayerSubtitle.text = station.name + " - " + station.ciudad
        }

        val sharePref: SharedPreferences = this.getSharedPreferences("STATION", Context.MODE_PRIVATE)
        val gson: Gson = Gson()
        val json: String = sharePref.getString("station", "")
        if(json != "") {
            val station: Station = gson.fromJson(json, Station::class.java)
            Log.i("station", station.toString())
            model.currentStation.setValue(station)
        }else{
            model.currentStation.setValue(Supplier.stations[0])
        }
        model.currentStation.observe(this, stationObserver)

//        val intent: Intent = Intent(this, PlayerUtil::class.java)
//        Util.startForegroundService(this, intent)

        //val player: PlayerUtil = PlayerUtil()

        rvStation.layoutManager = LinearLayoutManager(this)
        rvStation.adapter = StationAdapter(this, Supplier.stations, model, adView)

        MobileAds.initialize(this, "ca-app-pub-6174585484194945~8868427541")
        val interstitialAd: InterstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = "ca-app-pub-6174585484194945/4829538784"
        interstitialAd.loadAd(AdRequest.Builder().build())
        interstitialAd.adListener = object: AdListener(){
            override fun onAdLoaded() {
                interstitialAd.show()
            }
        }

    }

}
