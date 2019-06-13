package ru.limeek.organizer.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.limeek.organizer.data.model.news.Article
import ru.limeek.organizer.databinding.NewsItemBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsVH>() {

    var dataset = listOf<Article>()
    lateinit var onItemClick: (Article) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsVH(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        holder.binding.article = dataset[position]
        Picasso.get()
                .load(holder.binding.article!!.urlToImage)
                .resize(150,150)
                .into(holder.binding.ivNews)

        holder.binding.root.setOnClickListener { onItemClick.invoke(holder.binding.article!!) }
    }

    class NewsVH(var binding: NewsItemBinding): RecyclerView.ViewHolder(binding.root)
}