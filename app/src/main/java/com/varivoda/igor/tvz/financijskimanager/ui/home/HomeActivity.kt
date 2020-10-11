package com.varivoda.igor.tvz.financijskimanager.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.util.toast

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun setActionBarText(text: String){
        supportActionBar?.elevation = 2f
        supportActionBar?.title = text
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.bug) {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("mailto:igor.varivodaa@gmail.com.com")
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Naslov")
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Molimo Vas opišite problem na koji ste naišli.."
            )
            try {
                startActivity(Intent.createChooser(emailIntent, "Pošalji e-mail"))
            } catch (ex: ActivityNotFoundException) {
                this.toast("Pojavio se problem sa otvaranjem e-maila")
            }
            return true
        } else if (item.itemId == R.id.podijeli) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Financijski manager 2020")
            val message =
                "\nhttps://play.google.com/store/apps/details?id=com.varivoda.igor.financijskimanager\n\n"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "Izaberite gdje želite podijeliti:"))
        }
        return super.onOptionsItemSelected(item)
    }
}