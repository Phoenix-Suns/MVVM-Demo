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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nghiamvvm.R
import com.example.nghiamvvm.data.MovieDao
import com.example.nghiamvvm.data.MovieEntity
import com.example.nghiamvvm.models.Resource
import com.example.nghiamvvm.network.MovieApiService
import com.example.nghiamvvm.network.MovieRepository
import com.example.nghiamvvm.ui.adapter.MoviesAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_fetch_by_dagger.*
import javax.inject.Inject

class FetchListByDaggerActivity : AppCompatActivity() {
    private var TAG = this::class.java.name

    private lateinit var adapter: MoviesAdapter

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: Movie1ViewModel

    @Inject
    lateinit var age123: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)   // init Dragger
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_by_dagger)

        setupKeywordRecyclerView()
        //fetchKeyword()
        initViewModel()

        Log.e("age123", age123)
    }

    private fun setupKeywordRecyclerView() {
        adapter = MoviesAdapter(this)
        recyclerView.adapter = adapter

        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager
    }

    private fun fetchKeyword() {
        /*val movieService = MovieService2.getInstance().create(MovieApiService::class.java).fetchMoviesByType()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({response ->
                updateRecyclerView(response.results)
            }, { e ->
                showError("${e.message}")
            })*/
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(Movie1ViewModel::class.java)
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
class Movie1ViewModel @Inject constructor(
    movieDao: MovieDao,
    movieApiService: MovieApiService
) : ViewModel() {

    private val movieRepository: MovieRepository = MovieRepository(movieDao, movieApiService)
    private val moviesListLiveData = MutableLiveData<Resource<List<MovieEntity>>>() // LiveData to UpdateUI

    /* Method called by UI to fetch movies list */
    fun loadMoreMovies() {
        movieRepository.loadMoviesByType()
            .subscribe { resource -> getMoviesLiveData().postValue(resource) }
    }

    /* LiveData observed by the UI */
    fun getMoviesLiveData() = moviesListLiveData
}
