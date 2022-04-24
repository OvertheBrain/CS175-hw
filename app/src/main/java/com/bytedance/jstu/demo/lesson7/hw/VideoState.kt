package com.bytedance.jstu.demo.lesson7.hw

import android.media.MediaPlayer
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VideoState : ViewModel() {

    public val videoUri: MutableLiveData<Uri?> by lazy {
        MutableLiveData<Uri?>()
    }
    public var videoCurrentPosition: Int=0
    public val videoDuration: Int = 1

}
