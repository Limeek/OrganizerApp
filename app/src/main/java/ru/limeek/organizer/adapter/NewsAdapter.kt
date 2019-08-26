package ru.limeek.organizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*
import ru.limeek.organizer.R
import ru.limeek.organizer.data.model.news.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsVH>() {

    var dataset = listOf<Article>()
    lateinit var onItemClick: (Article) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsVH(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: NewsVH, position: Int) {
        holder.itemView.tvTitle.text = dataset[position].title
        holder.itemView.tvSource.text = dataset[position].source?.name

        Picasso.get()
                .load(dataset[position].urlToImage)
                .resize(150,150)
                .into(holder.itemView.ivNews)

        holder.itemView.setOnClickListener { onItemClick.invoke(dataset[position]) }
    }

    class NewsVH(view: View): RecyclerView.ViewHolder(view)
}