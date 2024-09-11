package com.example.melodymix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.melodymix.adapter.CategoryAdapter
import com.example.melodymix.adapter.SectionSongListAdapter
import com.example.melodymix.databinding.ActivityMainBinding
import com.example.melodymix.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import java.util.Locale.Category

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        getCategories()

        setupSection("section_1",binding.section1MainLayout,binding.section1Title,binding.section1RecyclerView)
        setupSection("section_2",binding.section2MainLayout,binding.section2Title,binding.section2RecyclerView)
        setupSection("section_3",binding.section3MainLayout,binding.section3Title,binding.section3RecyclerView)
    }

    override fun onBackPressed() {
        super.onBackPressed()


    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    fun showPlayerView(){
        binding.playerView.setOnClickListener {
            startActivity(Intent(this,PlayerActivity::class.java))
        }
        MyExoplayer.getCurrentSong()?.let {
            binding.playerView.visibility=View.VISIBLE
            binding.songTitleTextView.text="Now playing" +it.title
            Glide.with(binding.songCoverImageView)
                .load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                )
                .into(binding.songCoverImageView)

        }?.run {
            binding.playerView.visibility=View.GONE
        }

    }

//    categories

    fun getCategories(){
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener {
                val  categoryList=it.toObjects(CategoryModel::class.java)
                setupCategoryRecyclerView(categoryList)
            }
    }
    fun setupCategoryRecyclerView(categoryList: List<CategoryModel>){
        categoryAdapter= CategoryAdapter(categoryList)
        binding.categoriesRecyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.categoriesRecyclerView.adapter=categoryAdapter

    }

//    section
    fun setupSection(id:String,mainLayout:RelativeLayout,titleView:TextView,recyclerView:RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section=it.toObject(CategoryModel::class.java)
                section?.apply {
                    mainLayout.visibility=View.VISIBLE
                    titleView.text=name
                    recyclerView.layoutManager=LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerView.adapter=SectionSongListAdapter(songs)
                    mainLayout.setOnClickListener {
                        SongsListActivity.category=section
                        startActivity(Intent(this@MainActivity,SongsListActivity::class.java))

                    }
                }
            }
    }
}