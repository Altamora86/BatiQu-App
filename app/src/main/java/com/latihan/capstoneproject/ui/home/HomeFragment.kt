package com.latihan.capstoneproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.latihan.capstoneproject.R
import com.latihan.capstoneproject.data.adapter.ListAdapter
import com.latihan.capstoneproject.databinding.FragmentHomeBinding
import com.latihan.capstoneproject.response.ItemsItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var rvStory: RecyclerView
    private val list = ArrayList<ItemsItem>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        rvStory = binding.rvStory
        rvStory.setHasFixedSize(true)


        list.addAll(getListBatik())
        showRecyclerList()
        return root
    }

    private fun getListBatik(): ArrayList<ItemsItem> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataAddress = resources.getStringArray(R.array.data_address)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val listBatik = ArrayList<ItemsItem>()
        for (i in dataName.indices) {
            val batik = ItemsItem(dataName[i], dataAddress[i],
                dataPhoto.getResourceId(i, -1), dataDescription[i]
            )
            listBatik.add(batik)
        }
        return listBatik
    }

    private fun showRecyclerList() {
        rvStory.layoutManager = LinearLayoutManager(requireContext())
        val listBatikAdapter = ListAdapter(list)
        rvStory.adapter = listBatikAdapter
    }
}
