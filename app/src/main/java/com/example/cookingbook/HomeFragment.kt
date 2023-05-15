package com.example.cookingbook

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

class HomeFragment : Fragment() {

    lateinit var imageView: ImageView;
    lateinit var objectAnimator: ObjectAnimator;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        imageView = view.findViewById(R.id.tomatoe_img)

        val animationMove = ObjectAnimator.ofFloat(imageView, "translationX", -400f, 400f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val animationRotate = ObjectAnimator.ofFloat(imageView, "rotation", -400f, 400f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val set = AnimatorSet()
        set.playTogether(animationMove, animationRotate)
        set.start()

        return view;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {

            }
    }
}