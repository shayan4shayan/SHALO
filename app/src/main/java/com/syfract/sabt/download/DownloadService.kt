package com.syfract.sabt.download

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.util.Log

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 *
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
class DownloadService(private val listener: DownloadListener) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        onHandleIntent(intent)
    }

    private fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.getStringExtra(ACTION)
            when {
                ACTION_COMPLETED == action -> {
                    val path = intent.getStringExtra(EXTRA_PATH)
                    handleActionComplete(path)
                    Log.d("Service", "Callback function called")
                }
                ACTION_PROGRESS == action -> {
                    val progress = intent.getIntExtra(EXTRA_PROGRESS, 0)
                    handleActionProgress(progress)
                }
                ACTION_FAILED == action -> {
                    handleActionFailed()
                }
            }
        }
    }

    /**
     * handle action Failed
     */
    private fun handleActionFailed() {
        listener.onFailed()
    }

    /**
     * Handle action Complete in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionComplete(path: String) {
        listener.onComplete(path)
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private fun handleActionProgress(progress: Int) {
        listener.onUpdateProgress(progress)
    }

    companion object {

        public val ACTION = "com.syfract.sabt.download.action"

        // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
        private val ACTION_COMPLETED = "com.syfract.sabt.download.action.COMPLETED"
        private val ACTION_PROGRESS = "com.syfract.sabt.download.action.PROGRESS"
        private val ACTION_FAILED = "com.syfract.sabt.download.action.FAILED"

        private val EXTRA_PATH = "com.syfract.sabt.download.extra.PATH"
        private val EXTRA_PROGRESS = "com.syfract.sabt.download.extra.PROGRESS"

        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        fun startActionComplete(context: Context, path: String) {
            val intent = Intent()
            intent.action = ACTION
            intent.putExtra(ACTION, ACTION_COMPLETED)
            intent.putExtra(EXTRA_PATH, path)
            context.sendBroadcast(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        fun startActionProgress(context: Context, progress: Int) {
            val intent = Intent()
            intent.action = ACTION
            intent.putExtra(ACTION, ACTION_PROGRESS)
            intent.putExtra(EXTRA_PROGRESS, progress)
            context.sendBroadcast(intent)
        }

        fun startActionFailed(context: Context) {
            val intent = Intent()
            intent.putExtra(ACTION, ACTION_FAILED)
            context.sendBroadcast(intent)
        }
    }

    public interface DownloadListener {
        fun onUpdateProgress(prgress: Int)
        fun onComplete(path: String)
        fun onFailed()
    }
}
