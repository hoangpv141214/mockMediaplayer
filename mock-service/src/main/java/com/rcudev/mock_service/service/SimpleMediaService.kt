package com.rcudev.mock_service.service

import android.content.Intent
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.rcudev.mock_service.service.notification.SimpleMediaNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SimpleMediaService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var notificationManager: SimpleMediaNotificationManager

    @UnstableApi
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager.startNotificationService(
            mediaSessionService = this,
            mediaSession = mediaSession
        )

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            release()
            if (player.playbackState != Player.STATE_IDLE) {
                player.seekTo(0)
                player.playWhenReady = false
                player.stop()
            }
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession =
        mediaSession
}