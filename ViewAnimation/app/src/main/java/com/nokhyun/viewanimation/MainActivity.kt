package com.nokhyun.viewanimation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.ChangeClipBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeScroll
import androidx.transition.Explode
import androidx.transition.Fade
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.nokhyun.viewanimation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var scene1: Scene
    private lateinit var scene2: Scene

    private val _testLiveData: MutableLiveData<Boolean> = MutableLiveData(false)
    val testLiveData: LiveData<Boolean> get() = _testLiveData

    private enum class TransitionType {
        CHANGE_BOUNDS,
        CHANGE_CLIP_BOUNDS,
        CHANGE_IMAGE_TRANS_FROM,
        CHANGE_SCROLL,
        EXPLODE,
        FADE,
        SLIDE,
        AUTO;

        fun get(): Transition? {
            return when (this) {
                CHANGE_BOUNDS -> {
                    ChangeBounds()
                }

                CHANGE_CLIP_BOUNDS -> {
                    ChangeClipBounds()
                }

                CHANGE_IMAGE_TRANS_FROM -> {
                    ChangeImageTransform()
                }

                CHANGE_SCROLL -> {
                    ChangeScroll()
                }

                EXPLODE -> {
                    Explode()
                }

                FADE -> {
                    Fade()
                }

                SLIDE -> {
                    Slide()
                }

                AUTO -> {
                    AutoTransition()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.main = this
        binding.lifecycleOwner = this

        // layout create
        scene1 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.scene_a, this)
        scene2 = Scene.getSceneForLayout(binding.sceneRoot, R.layout.scene_b, this)
        val transitionType1 = TransitionType.EXPLODE
        val transitionType2 = TransitionType.AUTO

        // code create
        val sceneCode = Scene(binding.sceneRoot, binding.sceneElement)

        binding.btnScene1.setOnClickListener {
            TransitionManager.go(scene1, transitionType1.get())
        }

        binding.btnScene2.setOnClickListener {
            _testLiveData.value = true
            TransitionManager.go(scene2, transitionType2.get())
        }
    }
}