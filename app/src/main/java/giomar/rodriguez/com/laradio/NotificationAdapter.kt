package giomar.rodriguez.com.laradio

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import android.graphics.BitmapFactory
import android.R.attr.src
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutionException


class NotificationAdapter(val title: String, val description: String, val logoUrl: String, val context: Context) : PlayerNotificationManager.MediaDescriptionAdapter {

    override fun createCurrentContentIntent(player: Player?): PendingIntent? {
        val intent: Intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun getCurrentContentText(player: Player?): String? {
        return description
    }

    override fun getCurrentContentTitle(player: Player?): String {
        return title
    }

    override fun getCurrentLargeIcon(player: Player?, callback: PlayerNotificationManager.BitmapCallback?): Bitmap? {
        //val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(logoUrl))
        //val bitmap: Bitmap = Glide.with(context).asBitmap().load(logoUrl).into(120, 93).get()

        val thread = Thread {
            try {
                val uri = Uri.parse(logoUrl)
                val bitmap = Glide.with(context)
                    .asBitmap()
                    .load(uri)
                    .submit().get()
                callback!!.onBitmap(bitmap)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        thread.start()

        return null
    }
}