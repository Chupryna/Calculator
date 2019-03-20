package com.example.calculator.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculator.R
import com.example.calculator.adapter.RVAdapterHistory
import com.example.calculator.room.CalculatorDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_history_operations.*


class HistoryOperationsActivity : AppCompatActivity() {

    private var database: CalculatorDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_operations)

        supportActionBar?.setTitle(R.string.history)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database = CalculatorDatabase.getInstance(this)
        /*database!!.expressionDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:DisposableSingleObserver<List<Expression>>(){
                    override fun onSuccess(t: List<Expression>?) {
                        val adapter = RVAdapterHistory(t!!)
                        recyclerViewHistory.adapter = adapter
                    }

                    override fun onError(e: Throwable?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })*/

        database!!.expressionDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val adapter = RVAdapterHistory(it)
                recyclerViewHistory.adapter = adapter
            }.isDisposed


        recyclerViewHistory.layoutManager = LinearLayoutManager(this)

        btnCleanHistory.setOnClickListener{
            database!!.expressionDao().deleteAll()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        CalculatorDatabase.destroyInstance()
        super.onDestroy()
    }
}