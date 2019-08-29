package com.example.nghiamvvm.ui

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nghiamvvm.R
import com.example.nghiamvvm.data.MovieDao
import com.example.nghiamvvm.data.MovieEntity
import com.example.nghiamvvm.models.Resource
import com.example.nghiamvvm.network.MovieApiService
import com.example.nghiamvvm.network.MovieRepository
import com.example.nghiamvvm.ui.adapter.MoviesAdapter
import com.example.nghiamvvm.di_dagger.component.ApiModule
import com.example.nghiamvvm.di_dagger.component.DbModule
import kotlinx.android.synthetic.main.activity_fetch_list_mvvm.*

class FetchListByMVVMActivity : AppCompatActivity() {
    private var TAG = this::class.java.name

    private lateinit var adapter: MoviesAdapter
    //private lateinit var viewModel: NameViewModel
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetch_list_mvvm)

        setupKeywordRecyclerView()
        setupViewModel()
//        fetchKeyword()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
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


    private fun setupKeywordRecyclerView() {
        adapter = MoviesAdapter(this)
        recyclerView.adapter = adapter

        //val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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

class MovieViewModel(application: Application) : AndroidViewModel(application) {

    private var movieDao: MovieDao
    private var movieApiService: MovieApiService
    private var movieRepository: MovieRepository

    private val moviesListLiveData = MutableLiveData<Resource<List<MovieEntity>>>()

    init {
        val database = DbModule().provideDatabase(application)
        movieDao = database.movieDao()
        movieApiService = ApiModule().run {
            val okHttpClient = this.provideOkhttpClient(this.provideCache(application))
            val retrofit = this.provideRetrofit(this.provideGson(), okHttpClient)
            return@run this.provideMovieApiService(retrofit)
        }

        movieRepository = MovieRepository(movieDao, movieApiService)
    }

    fun loadMoreMovies() {
        movieRepository.loadMoviesByType()
            .subscribe { resource -> getMoviesLiveData().postValue(resource) }
    }

    fun getMoviesLiveData() = moviesListLiveData
}
