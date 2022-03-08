package com.pragma.mymusicapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pragma.mymusicapp.R
import com.pragma.mymusicapp.adapters.FavoriteSongAdapter
import com.pragma.mymusicapp.data.entities.FavoriteSong
import com.pragma.mymusicapp.data.remote.FavoriteSongsDatabase
import com.pragma.mymusicapp.data.remote.FavoritesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*
//import kotlinx.android.synthetic.main.layout_error.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    //private lateinit var viewModel: TouristicSiteViewModel
    private var adapter: FavoriteSongAdapter? = null
    private lateinit var favoritesViewModel: FavoritesViewModel
    private var touristicSiteList: MutableList<FavoriteSong> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val dataBaseInstance = FavoriteSongsDatabase.getDatabasenIstance(requireContext())
        favoritesViewModel.setInstanceOfDb(dataBaseInstance)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
        observerViewModel()
    }

    private fun observerViewModel() {
        favoritesViewModel.checkRecord.observe(viewLifecycleOwner, Observer {
            if(!it.second){
                favoritesViewModel.insert(it.first)
                Toast.makeText(requireContext(),"Item added in favorites list",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Item already exists in favorites list",Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupUI() {
        adapter = FavoriteSongAdapter(touristicSiteList, onFavoritesClick = ::onFavoritesClick,onItemClick = ::onItemClick)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun onItemClick(position: Int, favoriteSong: FavoriteSong) {
        val bundle = Bundle()
        bundle.putParcelable("data", favoriteSong)
        //MainActivity.navController.navigate(R.id.detail_dest, bundle)
    }

    private fun onFavoritesClick(id: Int, favoriteSong: FavoriteSong) {
        favoritesViewModel.checkRecordExist(favoriteSong)
    }

    private fun setupViewModel() {
        /*viewModel = ViewModelProvider(this, Injection.provideViewModelFactory()).get(
            TouristicSiteViewModel::class.java
        )
        viewModel.touristicsites.observe(requireActivity(), renderTouristicSites)

        viewModel.isViewLoading.observe(requireActivity(), isViewLoadingObserver)
        viewModel.onMessageError.observe(requireActivity(), onMessageErrorObserver)
        viewModel.isEmptyList.observe(requireActivity(), emptyListObserver)*/
    }

    //observers
    private val renderTouristicSites = Observer<MutableList<FavoriteSong>> {
        //Log.v(MainActivity.TAG, "data updated $it")
        if(layoutError!=null) {
            layoutError.visibility = View.GONE
            layoutError.visibility = View.GONE
        }
        adapter?.apply {
            update(it)
        }
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        //Log.v(MainActivity.TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        if(progressBar!=null)
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        //Log.v(MainActivity.TAG, "onMessageError $it")
        if(layoutError!=null) {
            layoutError.visibility = View.VISIBLE
            layoutEmpty.visibility = View.GONE
            //textViewError.text = "Error $it"
        }
    }

    private val emptyListObserver = Observer<Boolean> {
        //Log.v(MainActivity.TAG, "emptyListObserver $it")
        if(layoutError!=null) {
            layoutEmpty.visibility = View.VISIBLE
            layoutError.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        //viewModel.loadTouristicSites()
    }


}