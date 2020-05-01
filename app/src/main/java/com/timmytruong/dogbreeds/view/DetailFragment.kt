package com.timmytruong.dogbreeds.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.databinding.FragmentDetailBinding
import com.timmytruong.dogbreeds.model.DogPalette
import com.timmytruong.dogbreeds.util.getProgressDrawable
import com.timmytruong.dogbreeds.util.loadImage
import com.timmytruong.dogbreeds.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment()
{
    private var dogUuid = 0

    private lateinit var detailViewModel: DetailsViewModel

    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        // Inflate the layout for this fragment
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid

            detailViewModel.fetch(dogUuid)
        }

        observeLiveData()
    }

    private fun observeLiveData()
    {
        detailViewModel.dogLiveData.observe(this, Observer {
            it?.let { dog ->
                dataBinding.dog = dog

                dog.imageUrl?.let {
                    setupBackgroundColour(it)
                }
            }
        })
    }

    private fun setupBackgroundColour(url: String)
    {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?)
                {
                    Palette.from(resource).generate {
                        val intColour = it?.lightMutedSwatch?.rgb ?: 0
                        val myPalette = DogPalette(intColour)
                        dataBinding.palette = myPalette
                    }
                }
            })
    }
}
