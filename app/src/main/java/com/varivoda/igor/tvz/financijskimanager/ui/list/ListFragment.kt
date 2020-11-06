package com.varivoda.igor.tvz.financijskimanager.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginViewModel
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@ExperimentalCoroutinesApi
class ListFragment : Fragment() {

    private lateinit var listViewModelFactory: ListViewModelFactory
    private lateinit var listViewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    @FlowPreview
    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).setActionBarText("Popis kupaca")
        listViewModelFactory = ListViewModelFactory(requireContext())
        listViewModel = ViewModelProvider(this,listViewModelFactory).get(ListViewModel::class.java)
       // listRecyclerView.adapter = adapter
        //getCountiesStream()
        /*lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { listRecyclerView.scrollToPosition(0) }
        }*/
        //initAdapter()


    }

  /*  private fun initAdapter() {
        listRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ListLoadStateAdapter { adapter.retry() },
            footer = ListLoadStateAdapter { adapter.retry() }
        )
    }*/

    private var searchJob: Job? = null
    //private var adapter: ListCountyAdapter = ListCountyAdapter(clickListener = ListItemClickListener {  })


   /* private fun getCountiesStream() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            listViewModel.getCountiesStream().collectLatest {
                adapter.submitData(it)
            }
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

}