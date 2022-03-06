package com.pragma.mymusicapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pragma.mymusicapp.R
import com.pragma.mymusicapp.adapters.FavoriteSongAdapter
import com.pragma.mymusicapp.data.entities.FavoriteSong
import com.pragma.mymusicapp.data.remote.FavoriteSongsDatabase
import com.pragma.mymusicapp.data.remote.FavoritesViewModel
import com.pragma.mymusicapp.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoritesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {
    private var adapter: FavoriteSongAdapter? = null
    private lateinit var favoritesViewModel: FavoritesViewModel
    private var favoriteSongList: MutableList<FavoriteSong> = mutableListOf()
    private var position=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel = ViewModelProvider(this).get(FavoritesViewModel::class.java)
        val dataBaseInstance = FavoriteSongsDatabase.getDatabasenIstance(requireContext())
        favoritesViewModel.setInstanceOfDb(dataBaseInstance)
        favoritesViewModel.getFavoritesList()
        setupUI()
        observerViewModel()

    }

    private fun observerViewModel() {
        favoritesViewModel.favoritesList.observe(requireActivity(), Observer {
            favoriteSongList.clear()
            if (!it.isNullOrEmpty()) {
                favoriteSongList.addAll(it)
            }else{
                layoutEmpty.visibility=View.VISIBLE
            }
            adapter?.apply {
                notifyDataSetChanged()
            }
        })

    }


    private fun setupUI() {
        adapter = FavoriteSongAdapter(favoriteSongList, onFavoritesClick = ::onFavoritesClick,onItemClick = ::onItemClick)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun onItemClick(position: Int, favoriteSong: FavoriteSong) {
        val bundle = Bundle()
        bundle.putParcelable("data", favoriteSong)
        //MainActivity.navController.navigate(R.id.detail_dest, bundle)
    }

    private fun onFavoritesClick(pos: Int, favoriteSong: FavoriteSong) {
        position=pos
        favoritesViewModel.delete(favoriteSong)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}