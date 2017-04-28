package org.ligi.walleth.activities

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import com.github.salomonbrys.kodein.LazyKodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.instance
import kotlinx.android.synthetic.main.activity_select_fiat.*
import kotlinx.android.synthetic.main.dialog_add_reference.view.*
import org.ligi.walleth.R
import org.ligi.walleth.data.config.Settings
import org.ligi.walleth.data.exchangerate.ExchangeRateProvider
import org.ligi.walleth.ui.FiatListAdapter

class SelectReferenceActivity : AppCompatActivity() {

    val exchangeRateProvider: ExchangeRateProvider by LazyKodein(appKodein).instance()
    val settings: Settings by LazyKodein(appKodein).instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_select_fiat)

        supportActionBar?.subtitle = "Select reference"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fiat_list_recycler.layoutManager = LinearLayoutManager(this)
        fiat_list_recycler.adapter = FiatListAdapter(exchangeRateProvider, this, settings)

        fab.setOnClickListener {
            val inflater = LayoutInflater.from(this@SelectReferenceActivity)
            val layout = inflater.inflate(R.layout.dialog_add_reference, null, false)

            AlertDialog.Builder(this@SelectReferenceActivity)
                    .setTitle("Add Reference")
                    .setView(layout)

                    .setPositiveButton("OK", { _, _ ->
                        exchangeRateProvider.addFiat( layout.reference_text.text.toString().toUpperCase())
                        Handler().postDelayed({
                            fiat_list_recycler.adapter = FiatListAdapter(exchangeRateProvider, this, settings)
                        }, 1000)
                    })
                    .show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
