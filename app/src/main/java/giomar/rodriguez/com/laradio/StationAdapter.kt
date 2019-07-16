package giomar.rodriguez.com.laradio

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import giomar.rodriguez.com.laradio.model.Station
import kotlinx.android.synthetic.main.station_list_item.view.*
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import giomar.rodriguez.com.laradio.model.StationViewModel


class StationAdapter(val context: Context, val stations: List<Station>, val model: StationViewModel, val bannerView: AdView) : RecyclerView.Adapter<StationAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.station_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stations.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val station = stations[position]
        holder.setData(station, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var currentStation: Station? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {

//                tvPlayerTitle.text = stations[currentPosition].frequency + " FM"
//                tvPlayerSubtitle.text = stations[currentPosition].name + " - " + stations[currentPosition].ciudad
                model.currentStation.setValue(stations[currentPosition])
                val intent: Intent = Intent(context, PlayerNotification::class.java)
                intent.putExtra("url", stations[currentPosition].url)
                intent.putExtra("title", stations[currentPosition].name)
                intent.putExtra("description", stations[currentPosition].ciudad)
                intent.putExtra("logoUrl", stations[currentPosition].logoUrl)
                Util.startForegroundService(context, intent)
                SavePreferenceUtil.saveCurrentStation(context, stations[currentPosition])
                val adRequest = AdRequest.Builder().build()
                bannerView.loadAd(adRequest)
                bannerView.adListener = object: AdListener(){
                    override fun onAdLoaded() {

                    }
                }
                //PlayerUtil.play(stations[currentPosition].url)
            }
        }

        fun setData(station: Station, position: Int){
            itemView.tvStationTitle.text = station.name + " " + station.frequency
            itemView.tvStationSubtitle.text = station.ciudad
            Glide.with(context).load(station.logoUrl).into(itemView.ivStationAvatar)
            currentPosition = position

        }
    }
}