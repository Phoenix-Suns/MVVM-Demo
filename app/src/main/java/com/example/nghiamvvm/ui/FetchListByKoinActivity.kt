package com.example.nghiamvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nghiamvvm.R
import com.example.nghiamvvm.data.MovieDao
import com.example.nghiamvvm.data.MovieEntity
import com.example.nghiamvvm.models.Resource
import com.example.nghiamvvm.network.MovieApiService
import com.example.nghiamvvm.network.MovieRepository
import com.example.nghiamvvm.ui.adapter.MoviesAdapter
import org.koin.android.ext.android.inject
import kotlinx.android.synthetic.main.activity_fetch_by_koin.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FetchListByKoinActivity : AppCompatActivity() {
    private var TAG = this::class.java.name

    private lateinit var adapter: MoviesAdapter

    private val viewModel: MovieList2ViewModel by viewModel()
    private val nghiaStr: String by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_by_koin)

        Log.e("Nghia", nghiaStr)

        setupKeywordRecyclerView()
        initViewModel()
    }

    private fun setupKeywordRecyclerView() {
        adapter = MoviesAdapter(this)
        recyclerView.adapter = adapter

        //val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
    }

    private fun initViewModel() {
        viewModel.getMoviesLiveData().observeForever { resource: Resource<List<MovieEntity>>? ->
            if (resource?.isLoading == true) {
                displayLoading(true)

            } else if (resource?.data != null && resource.data.isNotEmpty()) {
                updateRecyclerView(resource.data)
                displayLoading(false)

            } else {
                showError("${resource?.message}")
                displayLoading(false)
            }
        }
        viewModel.loadMoreMovies()
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


/**
 * We are injecting the MovieDao class
 * and the MovieApiService class to the ViewModel.
 **/
class MovieList2ViewModel constructor(
    movieDao: MovieDao,
    movieApiService: MovieApiService
) : ViewModel() {

    /* You can see we are initialising the MovieRepository class here */
    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)

    /* We are using LiveData to update the UI with the data changes.*/
    private val moviesListLiveData = MutableLiveData<Resource<List<MovieEntity>>>()

    /* Method called by UI to fetch movies list */
    fun loadMoreMovies() {
        movieRepository.loadMoviesByType()
            .subscribe { resource -> getMoviesLiveData().postValue(resource) }
    }

    /* LiveData observed by the UI */
    fun getMoviesLiveData() = moviesListLiveData
}
