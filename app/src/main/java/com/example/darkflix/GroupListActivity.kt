package com.example.darkflix

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.darkflix.API.MoviesDBAPIInterface
import com.example.darkflix.API.RetrofitClient
import com.example.darkflix.Adapters.GroupListAdapter
import com.example.darkflix.Model.GenreModel
import com.example.darkflix.Model.HomeCatModel
import com.example.darkflix.Model.MovieSearchModel
import com.example.darkflix.Repository.MovieSearchRepo
import com.example.darkflix.Utility.AppConstants
import com.example.darkflix.ui.dashboard.DashboardFragment
import com.example.darkflix.ui.notifications.NotificationsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject


class GroupListActivity : AppCompatActivity() {
    // BottomNavView
    var fragmentList: MutableList<Fragment?>? = java.util.ArrayList()
    var bottomNavView: BottomNavigationView? = null
    var frameLayout: FrameLayout? = null

    private var homeReqList: ArrayList<HomeCatModel> = ArrayList()
    private var layoutManager: LinearLayoutManager? = null
    private var listGroup_rv: RecyclerView? = null
    private var HOME_CAT_LISTS: ArrayList<ArrayList<MovieSearchModel>> = ArrayList()

    private var genreReqList: ArrayList<GenreModel>? = ArrayList()
    private var genreList: ArrayList<GenreModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_list)

        listGroup_rv = findViewById(R.id.cat_list_group_rv)
        homeReqList = ArrayList()

        // Lang wise
        homeReqList.add(HomeCatModel("Tamil Movies", AppConstants.FIND_LANG_MOVIE_TYPE, "movie", "now_playing"))

        homeReqList.add(HomeCatModel("Trending Movies", AppConstants.MOVIE_TYPE, "movie", "now_playing"))
        homeReqList.add(HomeCatModel("Trending TV Shows", AppConstants.SHOWS_TYPE, "tv", "on_the_air"))
        homeReqList.add(HomeCatModel("Top Rated Movies", AppConstants.MOVIE_TYPE, "movie", "top_rated"))
        homeReqList.add(HomeCatModel("Top Rated TV Shows", AppConstants.MOVIE_TYPE, "tv", "top_rated"))

        getGenreList()

        bottomNavView = findViewById<BottomNavigationView>(R.id.nav_view)
        frameLayout = findViewById<FrameLayout>(R.id.nav_fragment)

        fragmentList?.add(null)
        fragmentList?.add(DashboardFragment())
        fragmentList?.add(NotificationsFragment())

        bottomNavView?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
//                    //setFragment(fragmentList.get(0));
//                    startActivity(Intent(applicationContext, GroupListActivity::class.java))
//                    overridePendingTransition(0, 0)
                    true
                }
                R.id.navigation_dashboard -> {
                    setFragment(fragmentList?.get(1))
                    true
                }
                R.id.navigation_notifications -> {
                    setFragment(fragmentList?.get(2))
                    true
                }
                else -> false
            }
        })
    }

    fun setFragment(fragment: Fragment?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_fragment, fragment!!)
        fragmentTransaction.commit()
    }

    private fun getAllHomeList() {
        Log.i("genreList.size", genreList.size.toString())

        val requests: MutableList<Observable<ResponseBody>> = ArrayList()
        val apiInterface = RetrofitClient.getRftInstance().create(
            MoviesDBAPIInterface::class.java)

        for (i in 0 until homeReqList.size) {
            val req = homeReqList[i]
            if(req.listType == AppConstants.FIND_LANG_MOVIE_TYPE) {
                requests.add(apiInterface.getMoviesByReg("ta", AppConstants.MOVIESDB_APIKEY))
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
            val genre = GenreModel();
            genre.id = obj.getInt("id")
            genre.name = obj.getString("name")
            genreList.add(genre);
        }
    }

    private fun getMoviesData(results: List<ResponseBody>) {
        for (idx in results.indices) {
            val str = results[idx].string()
            val jObj = JSONObject(str)
            val ls: JSONArray = jObj.getJSONArray("results")
            val movList:ArrayList<MovieSearchModel> = ArrayList()

            for (i in 0 until ls.length()) {
                val obj = ls.getJSONObject(i)
                val search = MovieSearchModel()
                val isTypeMovie = obj.has("title")

                search.id = obj.getInt("id")
                search.poster_path = obj.getString("poster_path")
                search.backdrop_path = obj.getString("backdrop_path")
                search.vote_average = obj.getString("vote_average")
                search.overview = obj.getString("overview")
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
        initRecyclerView()
    }

    private fun initRecyclerView() {
//        for(i in HOME_CAT_LISTS.indices) {
            MovieSearchRepo.setList(HOME_CAT_LISTS)
            Log.i("ls size: on start", HOME_CAT_LISTS.size.toString())
            layoutManager = LinearLayoutManager(this)
            layoutManager!!.orientation = RecyclerView.VERTICAL
            listGroup_rv?.layoutManager = layoutManager
            val listAdapter = GroupListAdapter(this@GroupListActivity, homeReqList)
            listAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            listGroup_rv?.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
//        }
    }
}