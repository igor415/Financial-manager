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
import com.varivoda.igor.tvz.financijskimanager.ui.settings.SettingsActivity
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
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_title))
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.describe_problem)
            )
            try {
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)))
            } catch (ex: ActivityNotFoundException) {
                this.toast(getString(R.string.problem_with_email))
            }
            return true
        } else if (item.itemId == R.id.podijeli) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            val message =
                "\nhttps://play.google.com/store/apps/details?id=com.varivoda.igor.financijskimanager\n\n"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, getString(R.string.choose_to_share)))
        }else if(item.itemId == R.id.settings){
            startActivity(Intent(this,SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}