package com.psg2024.tpprototypeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.psg2024.tpprototypeapp.G
import com.psg2024.tpprototypeapp.G.Companion.documents
import com.psg2024.tpprototypeapp.activities.MainActivity
import com.psg2024.tpprototypeapp.adapters.PlaceListRecyclerAdapter
import com.psg2024.tpprototypeapp.data.Place
import com.psg2024.tpprototypeapp.databinding.FragmentPlaceListBinding

class PlaceListFragment : Fragment() {



    private val binding : FragmentPlaceListBinding by lazy { FragmentPlaceListBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //리사이클러뷰에 메인액티비티가 가지고 있는 대량의 장소정보를 보여지도록..
        val ma:MainActivity = activity as MainActivity
        ma.searchPlaceResponse ?: return //아직 서버로딩이 완료되지 않았을 수도 있어서..
        documents ?: return

        binding.recyclerView.adapter =PlaceListRecyclerAdapter(requireContext(), documents!!)
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //스크롤이 끝에 도달할 경우
                val lastVisibleItemPosition:Int= (binding.recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount:Int= binding.recyclerView.adapter!!.itemCount -1

                if (lastVisibleItemPosition == itemTotalCount) {

                    if(ma.searchPlaceResponse!!.meta.is_end){
                    Toast.makeText(ma, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show()

                    }
                    else{

                    ma.pgIndex1++
                    Toast.makeText(ma, "새로운 정보를 불러오고 있습니다.", Toast.LENGTH_SHORT).show()

                    ma.searchPlaces()
                        binding.recyclerView.post { binding.recyclerView.scrollToPosition((15 * ma.pgIndex1) + 1) }
                        binding.recyclerView.adapter!!.notifyItemRangeInserted((15*ma.pgIndex1)+1, 15)


                    }


                }

            }

        })


    }


}

//스크롤이 끝에 도달할 경우

