package ru.limeek.organizer.model.news

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class News : Parcelable {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("totalResults")
    @Expose
    var totalResults: Int? = null
    @SerializedName("articles")
    @Expose
    var articles: List<Article>? = null

    protected constructor(`in`: Parcel) {
        this.status = `in`.readValue(String::class.java.classLoader) as String
        this.totalResults = `in`.readValue(Int::class.java.classLoader) as Int
        `in`.readList(this.articles, ru.limeek.organizer.model.news.Article::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(totalResults)
        dest.writeList(articles)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<News> = object : Parcelable.Creator<News> {

            override fun createFromParcel(`in`: Parcel): News {
                return News(`in`)
            }

            override fun newArray(size: Int): Array<News?> {
                return arrayOfNulls(size)
            }

        }
    }

}
