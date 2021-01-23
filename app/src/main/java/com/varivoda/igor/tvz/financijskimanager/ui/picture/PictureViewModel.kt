package com.varivoda.igor.tvz.financijskimanager.ui.picture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PictureViewModel(private val productRepository: BaseProductRepository) : ViewModel(){

    var products = MutableLiveData<List<Product>>()
    var error = MutableLiveData<Boolean>()

    init {
        getAllProducts()
    }

    private fun getAllProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            products.postValue(productRepository.getProducts())
        }
    }

    fun updateProductImage(image: String, id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val bol = productRepository.updateProductImage(image, id)
            if(bol){
                getAllProducts()
            }else{
                error.postValue(true)
            }

        }
    }
}