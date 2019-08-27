package com.example.nghiamvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nghiamvvm.AppConstants
import com.example.nghiamvvm.R
import com.example.nghiamvvm.data.MovieEntity
import com.example.nghiamvvm.network.MovieApiService
import com.example.nghiamvvm.ui.adapter.MoviesAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_fetch_list_basic.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class FetchListBasicActivity : AppCompatActivity() {
    private var TAG = this::class.java.name

    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_list_basic)

        setupKeywordRecyclerView()
        fetchKeyword()
    }

    private fun setupKeywordRecyclerView() {
        adapter = MoviesAdapter(this)
        recyclerView.adapter = adapter

        //val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
    }

    private fun fetchKeyword() {
        displayLoading(true)

        val movieService = MovieService.getInstance().create(MovieApiService::class.java).fetchMoviesByType()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({response ->
                updateRecyclerView(response.results)
                displayLoading(false)
            }, { e ->
                showError("${e.message}")
                displayLoading(false)
            })
    }

    private fun updateRecyclerView(keywordList: List<MovieEntity>) {
        this.adapter.setItems(keywordList)
        this.adapter.notifyDataSetChanged()
    }

    private fun showError(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayLoading(isShow: Boolean) {
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}

private class MovieService {
    companion object {
        var mRetrofit: Retrofit? = null

        fun getInstance(): Retrofit {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit!!
        }
    }
}
