package com.timmytruong.dogbreeds.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_dog.*

class DetailFragment : Fragment() {

    private var dogUuid = 0

    private lateinit var detailViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)

        detailViewModel.fetch()

        observeLiveData()
    }

    fun observeLiveData()
    {
        detailViewModel.dogLiveData.observe(this, Observer {
            it?.let {
                dog_name.text = it.dogBreed
                dog_lifespan.text = it.lifespan
                dog_temperament.text = it.temperament
                dog_purpose.text = it.bredFor
            }
        })
    }
}
