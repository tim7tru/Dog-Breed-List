package com.timmytruong.dogbreeds.view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.databinding.ItemDogBinding
import com.timmytruong.dogbreeds.model.DogBreed
import com.timmytruong.dogbreeds.util.getProgressDrawable
import com.timmytruong.dogbreeds.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed> = arrayListOf()) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>(), DogClickListener
{
    class DogViewHolder(var view: ItemDogBinding): RecyclerView.ViewHolder(view.root)

    fun updateDogList(newDogsList: List<DogBreed>)
    {
        dogsList.clear()

        dogsList.addAll(newDogsList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)

        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater, R.layout.item_dog, parent, false)

        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int)
    {
        holder.view.dog = dogsList[position]

        holder.view.listener = this
    }

    override fun onDogClicked(view: View)
    {
        val action = ListFragmentDirections.actionDetailFragment()

        action.dogUuid = view.dog_id.text.toString().toInt()

        Navigation.findNavController(view).navigate(action)
    }
}