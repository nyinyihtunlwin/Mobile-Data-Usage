package com.nyinyihtunlwin.mdusage.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyinyihtunlwin.mdusage.R
import com.nyinyihtunlwin.mdusage.adapters.AnnualRecordsRecyclerAdapter
import com.nyinyihtunlwin.mdusage.data.vos.YearDUsageVO
import com.nyinyihtunlwin.mdusage.mvp.presenters.MDUPresenter
import com.nyinyihtunlwin.mdusage.mvp.views.MDUView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : BaseActivity(), MDUView {


    private lateinit var mPresenter: MDUPresenter
    private lateinit var mAdapter: AnnualRecordsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mPresenter = ViewModelProviders.of(this@MainActivity).get(MDUPresenter::class.java)
        mPresenter.initPresenter(this)


        rvRecords.setEmptyView(vpEmpty)
        mAdapter = AnnualRecordsRecyclerAdapter(applicationContext)
        rvRecords.adapter = mAdapter
        rvRecords.layoutManager = LinearLayoutManager(this)

        mPresenter.onGetDataUsage("a807b7ab-6cad-4aa6-87d0-e283a7353a0f")
        swipeRefresh.setOnRefreshListener {
            mPresenter.onGetDataUsage("a807b7ab-6cad-4aa6-87d0-e283a7353a0f")
        }

        mPresenter.mResponseLD.observe(this, Observer {
            hideLoading()
            displayAnnualRecordLists(it as MutableList<YearDUsageVO>)
        })
        mPresenter.mErrorLD.observe(this, Observer {
            hideLoading()
            showPrompt(it)
        })

    }

    override fun showPrompt(message: String) {
        showPromptDialog(message)
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun displayAnnualRecordLists(recList: MutableList<YearDUsageVO>) {
        mAdapter.setNewData(recList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
