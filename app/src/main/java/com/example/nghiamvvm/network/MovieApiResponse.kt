package com.example.nghiamvvm.network

import com.example.nghiamvvm.data.MovieEntity

data class MovieApiResponse(val page: Long,
                            val results: List<MovieEntity>,
                            val total_results: Long,
                            val total_pages: Long)