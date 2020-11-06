package com.varivoda.igor.tvz.financijskimanager.ui.home

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginActivity
import com.varivoda.igor.tvz.financijskimanager.ui.settings.SettingsActivity
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import kotlinx.android.synthetic.main.fragment_home.*


class HomeActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        preferences = Preferences(this)
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
                showSelectedToast(this,getString(R.string.problem_with_email))
            }
            return true
        } else if (item.itemId == R.id.share) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            val message =
                "\nhttps://play.google.com/store/apps/details?id=com.varivoda.igor.financijskimanager\n\n"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, getString(R.string.choose_to_share)))
        }else if(item.itemId == R.id.settings){
            startActivity(Intent(this,SettingsActivity::class.java))
        }else if(item.itemId == R.id.enable_all){
            startActivity(Intent(this,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        }else if(item.itemId == R.id.log_out){
            showLogOutDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogOutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle(getString(R.string.log_out_message))
            setPositiveButton(getString(R.string.yes)
            ) { dialog, _ ->
                listOf("username key","password key","remember me").map { preferences.clear(it) }
                returnToLogin()
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        dialog?.dismiss()
        dialog = builder.create()
        dialog?.show()
    }

    private fun returnToLogin() = startActivity(Intent(this,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

    override fun onResume() {
        super.onResume()
        if(this::preferences.isInitialized){
            val brightness: Float = preferences.getSeekBarValue()
            val lp = window.attributes
            lp.screenBrightness = brightness
            window.attributes = lp
        }
    }
}