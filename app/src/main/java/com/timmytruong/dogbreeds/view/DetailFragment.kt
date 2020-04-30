package com.timmytruong.dogbreeds.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.databinding.FragmentDetailBinding
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
            it?.let {
                dataBinding.dog = it
            }
        })
    }
}
