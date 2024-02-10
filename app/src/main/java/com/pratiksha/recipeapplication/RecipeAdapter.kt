package com.pratiksha.recipeapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipeAdapter(
//    private  var clickListener : ItemClickListener,
    private var arrayList: ArrayList<Recipe>,
    private  var context : Context
//    private  var   clickListener : ItemClickListener
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {

        val name : TextView = view.findViewById(R.id.recipeNameTextView)
//        val ingredient : TextView = view.findViewById(R.id.ingredientTextView)
//        val instruction :TextView = view.findViewById(R.id.instructionTextView)
        val image : ImageView = view.findViewById(R.id.recipeImgView)
        val recipeCard : CardView = view.findViewById(R.id.recipeCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_items_layout, parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeAdapter.ViewHolder, position: Int) {

        holder.name.text = arrayList[position].recipeName
//        holder.ingredient.text = arrayList[position].ingredient
//        holder.instruction.text = arrayList[position].recipeName
//        holder.image.setImageResource(arrayList[position].downloadImageUri)

        loadImageFromeUrl(arrayList[position].downloadImageUri,holder.image)

        holder.recipeCard.setOnClickListener {

            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("recipeId",arrayList[position].recipeId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


    private fun loadImageFromeUrl(downloadImageUri: String, image: ImageView) {

        if( downloadImageUri != null) {

            Glide.with(context).load(downloadImageUri).placeholder(R.drawable.add_image)
                .error(R.drawable.add_image).into(image)

        }

    }

//    interface ItemClickListener {
//        fun onItemClick(recipe: Recipe?)
//    }

}




