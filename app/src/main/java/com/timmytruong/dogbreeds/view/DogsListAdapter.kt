package com.timmytruong.dogbreeds.view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.timmytruong.dogbreeds.R
import com.timmytruong.dogbreeds.model.DogBreed
import com.timmytruong.dogbreeds.util.getProgressDrawable
import com.timmytruong.dogbreeds.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed>) : RecyclerView.Adapter<DogsListAdapter.DogViewHolder>()
{
    class DogViewHolder(var view: View): RecyclerView.ViewHolder(view)
    {

    }

    fun updateDogList(newDogsList: List<DogBreed>)
    {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size

    override fun onBindViewHolder(holder: DogViewHolder, position: Int)
    {
        holder.view.name.text = dogsList[position].dogBreed
        holder.view.lifespan.text = dogsList[position].lifespan
        holder.view.setOnClickListener {
            val action = ListFragmentDirections.actionDetailFragment()
            action.dogUuid = dogsList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }
        holder.view.image_view.loadImage(dogsList[position].imageUrl, getProgressDrawable(context = holder.view.context))
    }
}