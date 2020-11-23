package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.AppDatabase
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Customer
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Product
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.Store
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.CustomerRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.EmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.ProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.StoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowListViewModel(context: Context) : ViewModel(){

    private val productRepository = ProductRepository(AppDatabase.getInstance(context))
    private val employeeRepository = EmployeeRepository(AppDatabase.getInstance(context))
    private val customerRepository = CustomerRepository(AppDatabase.getInstance(context))
    private val storeRepository = StoreRepository(AppDatabase.getInstance(context))

    val allProducts: Flow<List<Product>> = productRepository.getAllProducts()
        .onEach { it.map { product -> product.productName = "${product.productName} : ${product.price} kn" } } .flowOn(Dispatchers.Main).conflate()

    val employees: Flow<List<EmployeeDTO>> = employeeRepository.getEmployeesAndStores()
        .onEach { it.map { employee -> employee.employeeName = "${employee.employeeName} ${employee.employeeLastName} (${employee.storeName})" } }
        .flowOn(Dispatchers.Default).conflate()

    val customers: Flow<List<Customer>> = customerRepository.getAllCustomers()
        .flowOn(Dispatchers.Default).conflate()

    val stores: Flow<List<Store>> = storeRepository.getStores()
        .flowOn(Dispatchers.Default).conflate()

    

    fun deleteCustomer(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            customerRepository.deleteCustomer(id)
        }

    }

    fun deleteEmployee(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.deleteEmployee(id)
        }

    }

    fun insertProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.insertProduct(product)
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteProduct(id)
        }
    }

    /***products pager**/

    //private var currentProductResult: Flow<PagingData<Product>>? = null

    fun getProducts(): Flow<PagingData<Product>> {
        /*var result: Flow<PagingData<Product>>? = null
        viewModelScope.launch(Dispatchers.IO) {
            result = productRepository.getProductStream()
                .cachedIn(viewModelScope)
        }
        return result*/
        /*Pager(config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {ProductPagingSource(productRepository)}).flow.cachedIn(viewModelScope)*/
        return productRepository.getProductStream()
       /* return productRepository.getProductStream()
            .cachedIn(viewModelScope)*/
    }



    /***product popup***/
    var nameInput: String = ""
    var priceInput: String = ""
    var title: String = ""
    var edit: Boolean = false
    var item: Product? = null
    var productPopup: AlertDialog? = null
    var errorFieldPrice: String? = null
    var errorFieldName: String? = null
    var imgSrc = "https://img.imageupload.net/2020/11/20/shopping-cart-1.png"

    fun clearProductInfo() {
        nameInput = ""
        priceInput = ""
        title = ""
        productPopup?.dismiss()
    }

    fun onConfirmClicked(){
        if(priceInput.isNotEmpty() && nameInput.isNotEmpty()){
            if(edit){
                insertProduct(Product(item?.id!!, nameInput, priceInput.toDouble()))
            }else{
                insertProduct(Product(productName = nameInput,price = priceInput.toDouble()))
            }
            clearProductInfo()
        }else{
            if(priceInput.isEmpty()) errorFieldPrice = productPopup?.context?.getString(R.string.field_empty)
            if(nameInput.isEmpty()) errorFieldName = productPopup?.context?.getString(R.string.field_empty)
        }
    }
}