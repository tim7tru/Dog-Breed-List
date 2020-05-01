package com.timmytruong.dogbreeds.view

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
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
import com.timmytruong.dogbreeds.databinding.SendSmsDialogBinding
import com.timmytruong.dogbreeds.model.DogBreed
import com.timmytruong.dogbreeds.model.DogPalette
import com.timmytruong.dogbreeds.model.SmsInfo
import com.timmytruong.dogbreeds.viewmodel.DetailsViewModel

class DetailFragment : Fragment()
{
    private var dogUuid = 0

    private lateinit var detailViewModel: DetailsViewModel

    private lateinit var dataBinding: FragmentDetailBinding

    private var sendSmsStarted = false

    private var currentDog: DogBreed? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        setHasOptionsMenu(true)

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

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
            currentDog = it
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
            .into(object : CustomTarget<Bitmap>()
            {
                override fun onLoadCleared(placeholder: Drawable?)
                {
                }

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId)
        {
            R.id.action_send_sms ->
            {
                sendSmsStarted = true

                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share ->
            {
                val intent = Intent(Intent.ACTION_SEND)

                intent.type = "text/plain"

                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed")

                intent.putExtra(Intent.EXTRA_TEXT, "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}")

                intent.putExtra(Intent.EXTRA_STREAM, currentDog?.imageUrl)

                startActivity(Intent.createChooser(intent, "Share with"))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onPermissionResult(permissionGranted: Boolean)
    {
        if (sendSmsStarted && permissionGranted)
        {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}",
                    currentDog?.imageUrl
                )

                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                dialogBinding.smsInfo = smsInfo

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { _, _ ->
                        if (!dialogBinding.smsDestination.text.isNullOrEmpty())
                        {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo)
    {
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val sms = SmsManager.getDefault()

        sms.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null)
    }
}
