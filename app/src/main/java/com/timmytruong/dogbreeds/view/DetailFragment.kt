package com.timmytruong.dogbreeds.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.util.getProgressDrawable
import com.timmytruong.dogbreeds.util.loadImage
import com.timmytruong.dogbreeds.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment()
{

    private var dogUuid = 0

    private lateinit var detailViewModel: DetailsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
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
        detailViewModel.dogLiveData.observe(this, Observer { dog ->
            dog?.let {

                context?.let { context ->
                    dog_image.loadImage(dog.imageUrl, getProgressDrawable(context))
                }

                dog_name.text = dog.dogBreed

                dog_lifespan.text = dog.lifespan

                dog_temperament.text = dog.temperament

                dog_purpose.text = dog.bredFor
            }
        })
    }
}
