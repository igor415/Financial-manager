package com.varivoda.igor.tvz.financijskimanager.ui.picture

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.bitmapToBase64
import com.varivoda.igor.tvz.financijskimanager.util.convertStringToBitmap
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.android.synthetic.main.fragment_picture.view.*
import kotlinx.android.synthetic.main.full_image.view.*

private const val REQUEST_CODE = 42
private const val CAMERA_PERMISSION_CODE = 1
class PictureFragment : Fragment(), PictureAdapter.OnItemClickListener, PictureAdapter.OnImageClickListener {

    private val pictureViewModel by viewModels<PictureViewModel> {
        PictureViewModelFactory((requireContext().applicationContext as App).productRepository)
    }
    private val pictureAdapter = PictureAdapter()
    private var productId: Int? = null
    private var cameraPermission: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_picture, container, false)
        pictureAdapter.setOnItemClickListener(this)
        pictureAdapter.setOnImageClickListener(this)
        view.pictureRecyclerView.adapter = pictureAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askForCameraPermission()
        pictureViewModel.products.observe(viewLifecycleOwner, Observer {
            if(it==null) return@Observer
            pictureAdapter.submitList(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun askForCameraPermission(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity as HomeActivity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }else{
            cameraPermission = 1
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode== CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                cameraPermission = 1
                startCamera()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(product: Product) {
        productId = product.id
        if(cameraPermission == 1) {
            startCamera()
        }else{
            askForCameraPermission()
        }
    }

    private fun startCamera(){
        val takeImageIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takeImageIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(takeImageIntent, REQUEST_CODE)
        } else {
            context.toast(getString(R.string.unable_to_open_camera_resource))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val takenImage = data?.extras?.get("data") as Bitmap
            val encodedImage = takenImage.bitmapToBase64()
            if(encodedImage!=null && productId!=null){
                pictureViewModel.updateProductImage(encodedImage, productId!!)
            }
        }
    }

    private var dialog: AlertDialog? = null

    private fun openPictureFullScreen(image: String?){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.full_image,null,false)
        if (image != null) {
            dialogView.img.setImageBitmap(image.convertStringToBitmap())
        } else {
            dialogView.img.setImageResource(R.drawable.product_picture_placeholder)
        }
        dialogView.close.setOnClickListener {
            dialog?.cancel()
        }
        dialog?.cancel()
        dialog = builder.setView(dialogView).create()
        dialog?.show()
    }

    override fun onImageClick(product: Product) {
        openPictureFullScreen(product.image)
    }

}