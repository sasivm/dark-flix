package com.example.darkflix.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darkflix.API.MoviesDBAPIInterface
import com.example.darkflix.API.RetrofitClient
import com.example.darkflix.Adapters.GroupListAdapter
import com.example.darkflix.Model.GenreModel
import com.example.darkflix.Model.HomeCatModel
import com.example.darkflix.Model.MovieSearchModel
import com.example.darkflix.R
import com.example.darkflix.Repository.MovieSearchRepo
import com.example.darkflix.Utility.AppConstants
import com.example.darkflix.databinding.FragmentHomeBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private var isLoaded = false
    // BottomNavView

    private var homeReqList: ArrayList<HomeCatModel> = ArrayList()
    private var layoutManager: LinearLayoutManager? = null
    private var listGroup_rv: RecyclerView? = null
    private var HOME_CAT_LISTS: ArrayList<ArrayList<MovieSearchModel>> = ArrayList()
    private var genreList: ArrayList<GenreModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        listGroup_rv = view.findViewById(R.id.cat_list_group_rv)
        if(!isLoaded) {
            homeReqList = ArrayList()

            // Lang wise
            homeReqList.add(HomeCatModel("Tamil Movies", AppConstants.FIND_LANG_MOVIE_TYPE, "movie", "now_playing", "ta"))
            homeReqList.add(HomeCatModel("Telugu Movies", AppConstants.FIND_LANG_MOVIE_TYPE, "movie", "now_playing", "te"))
            homeReqList.add(HomeCatModel("Malayalam Movies", AppConstants.FIND_LANG_MOVIE_TYPE, "movie", "now_playing", "ml"))
            homeReqList.add(HomeCatModel("Kannada Movies", AppConstants.FIND_LANG_MOVIE_TYPE, "movie", "now_playing", "kn"))

            homeReqList.add(HomeCatModel("Trending Movies", AppConstants.MOVIE_TYPE, "movie", "now_playing"))
            homeReqList.add(HomeCatModel("Trending TV Shows", AppConstants.SHOWS_TYPE, "tv", "on_the_air"))
            homeReqList.add(HomeCatModel("Top Rated Movies", AppConstants.MOVIE_TYPE, "movie", "top_rated"))
            homeReqList.add(HomeCatModel("Top Rated TV Shows", AppConstants.MOVIE_TYPE, "tv", "top_rated"))

            getGenreList()
        } else {
            HOME_CAT_LISTS = MovieSearchRepo.getList()
            homeReqList = MovieSearchRepo.getHomeReqList()
            genreList = MovieSearchRepo.getGenreReqList()

            Log.i("All results are", "Retrived from local")
            initRecyclerView()
        }
        return view
    }

    private fun getAllHomeList() {
        Log.i("genreList.size", genreList.size.toString())

        val requests: MutableList<Observable<ResponseBody>> = ArrayList()
        val apiInterface = RetrofitClient.getRftInstance().create(
            MoviesDBAPIInterface::class.java)

        for (i in 0 until homeReqList.size) {
            val req = homeReqList[i]
            if(req.listType == AppConstants.FIND_LANG_MOVIE_TYPE) {
                requests.add(apiInterface.getMoviesByReg(req.orgLang, AppConstants.MOVIESDB_APIKEY))
            } else {
                requests.add(apiInterface.getSearchList(req.rqeType, req.reqState, AppConstants.MOVIESDB_APIKEY))
            }
        }

        for (i in 0 until genreList.size) { // Adding Genre list to homeReqList
            val listTitle: String = genreList.get(i).name.capitalize() + " Movies"
            homeReqList.add(HomeCatModel(listTitle, AppConstants.GENRE_TYPE, "genre", "top_rated"))
        }

        for (j in 0 until genreList.size) { // appending Genre API request in homeReqList
            requests.add(apiInterface.getMoviesByGenre(genreList.get(j).id, AppConstants.MOVIESDB_APIKEY))
        }

        val combined: Observable<List<ResponseBody>> = Observable.zip(requests) { responses ->
            Log.i("response in : Observable.zip", responses.toString())
            val results = mutableListOf<ResponseBody>()
            for (response in responses) {
                results.add(response as ResponseBody)
            }
            results
        }
        combined
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ results ->
                // do something with the list of responses
                Log.i("subscribe res resBody size: ", results.size.toString())
                getMoviesData(results)
            }, { throwable ->
                Log.i("Throwable ", throwable.message.toString())
                Log.i("Throwable ", throwable.toString())
            })
    }

    private fun getGenreList() {
        // Modify code to optimize
        val apiInterface = RetrofitClient.getRftInstance().create(
            MoviesDBAPIInterface::class.java)
        apiInterface.getGenreListInfo(AppConstants.MOVIESDB_APIKEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                    result ->
                val resStr = result.string()
                Log.i("getGenreList resBody : ", resStr)
                getGenreListData(resStr)
                getAllHomeList()
            }, { error ->
                error.printStackTrace()
                getAllHomeList()
            })

    }

    private fun getGenreListData(responseStr: String) {
        Log.i("response str = ", responseStr)
        val jObj = JSONObject(responseStr)

        val genresResList: JSONArray = jObj.getJSONArray("genres")

        for (i in 0 until genresResList.length()) {
            val obj = genresResList.getJSONObject(i)
            val genre = GenreModel()
            genre.id = obj.getInt("id")
            genre.name = obj.getString("name")
            genreList.add(genre)
        }
    }

    private fun getMoviesData(results: List<ResponseBody>) {
        for (idx in results.indices) {
            val str = results[idx].string()
            val jObj = JSONObject(str)
            val ls: JSONArray = jObj.getJSONArray("results")
            val movList:ArrayList<MovieSearchModel> = ArrayList()

            val DEFAULT_VALUE = ""

            for (i in 0 until ls.length()) {
                val obj = ls.getJSONObject(i)
                val search = MovieSearchModel()
                val isTypeMovie = obj.has("title")

                search.id = obj.getInt("id")
                search.poster_path = obj.getString("poster_path")
                search.backdrop_path = obj.getString("backdrop_path")
                search.vote_average = obj.getString("vote_average")
                search.overview = obj.getString("overview")
                if(obj.has("release_date")) {
                    search.release_date = obj.getString("release_date")
                } else {
                    search.release_date = DEFAULT_VALUE
                }
                if(obj.has("popularity")) {
                    search.popularity = obj.getString("popularity")
                } else {
                    search.popularity = DEFAULT_VALUE
                }
                if (isTypeMovie) {
                    search.title = obj.getString("title")
                } else {
                    search.name = obj.getString("name")
                }
                search.type = if (!isTypeMovie) AppConstants.SHOWS_TYPE else AppConstants.MOVIE_TYPE

                movList.add(search)
            }

            HOME_CAT_LISTS.add(movList)
        }
        MovieSearchRepo.setList(HOME_CAT_LISTS)
        MovieSearchRepo.setHomeReqList(homeReqList)
        MovieSearchRepo.setGenreReqList(genreList)

        initRecyclerView()
    }

    private fun initRecyclerView() {
//        for(i in HOME_CAT_LISTS.indices) {
        Log.i("ls size: on start", HOME_CAT_LISTS.size.toString())
        layoutManager = LinearLayoutManager(context)
        layoutManager!!.orientation = RecyclerView.VERTICAL
        listGroup_rv?.layoutManager = layoutManager
        val listAdapter = GroupListAdapter(context, homeReqList)
        listAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        listGroup_rv?.adapter = listAdapter
        listAdapter.notifyDataSetChanged()
        isLoaded = true
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}