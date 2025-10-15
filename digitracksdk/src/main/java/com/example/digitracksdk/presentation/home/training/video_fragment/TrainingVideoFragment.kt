package com.example.digitracksdk.presentation.home.training.video_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.innov.digitrac.databinding.FragmentTrainingVideoBinding
import com.example.digitracksdk.presentation.home.training.video_fragment.adapter.TrainingVideoAdapter
import com.example.digitracksdk.presentation.home.training.video_fragment.model.TrainingVideoModel

class TrainingVideoFragment : Fragment() {

    lateinit var binding:FragmentTrainingVideoBinding
    lateinit var trainingVideoAdapter: TrainingVideoAdapter
    var trainingVideoList :ArrayList<TrainingVideoModel> = ArrayList()
    companion object{
        fun newInstance(): TrainingVideoFragment {
            return TrainingVideoFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTrainingVideoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTrainingAdapter()
    }

     fun setUpTrainingAdapter() {
        binding.apply {
            if (::trainingVideoAdapter.isInitialized){
                trainingVideoAdapter.notifyDataSetChanged()
            }else{
                trainingVideoAdapter = TrainingVideoAdapter(this@TrainingVideoFragment.requireContext(),trainingVideoList)
                recyclerTrainingVideo.adapter = trainingVideoAdapter
            }
        }
    }
}