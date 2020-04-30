package com.timmytruong.dogbreeds.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel

    private val dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel.refresh()

        dogs_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        refresh_layout.setOnRefreshListener {
            dogs_list.visibility = View.GONE
            list_error.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refresh_layout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel()
    {
        viewModel.dogs.observe(this, Observer
        {
            it?.let {
                dogs_list.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(it)
            }
        })

        viewModel.dogsLoadError.observe(this, Observer
        {
            it?.let {
                list_error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {
            it?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it)
                {
                    list_error.visibility = View.GONE
                    dogs_list.visibility = View.GONE
                }
            }
        })
    }
}
