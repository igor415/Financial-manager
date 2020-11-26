package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.*
import androidx.paging.*
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

    fun getProducts(): Flow<PagingData<ProductModel>> {
        return productRepository.getProductStream().map { pagingData -> pagingData.map { ProductModel.ProductItem(it) } }
            .map {
                it.insertSeparators<ProductModel.ProductItem, ProductModel> { before, after ->
                    if (after == null) {
                        // we're at the end of the list
                        return@insertSeparators null
                    }

                    if (before == null) {
                        // we're at the beginning of the list
                        return@insertSeparators ProductModel.SeparatorItem("Komponente")
                    }
                    if(before.category < after.category){
                        ProductModel.SeparatorItem("Monitori")
                    }else{
                        null
                    }

                    /// check between 2 items
                    /*if (before.priceTitle > after.priceTitle) {
                        if (after.priceTitle >= 1) {
                            ProductModel.SeparatorItem("${after.priceTitle}0.000+ stars")
                        } else {
                            ProductModel.SeparatorItem("< 10.000+ stars")
                        }
                    } else {
                        // no separator
                        null
                    }*/
                    //null
                }
            }.cachedIn(viewModelScope)

    }

    sealed class ProductModel {
        data class ProductItem(val product: Product) : ProductModel()
        data class SeparatorItem(val description: String) : ProductModel()


    }

    val ProductModel.ProductItem.category: Int
        get() = this.product.categoryId

    private val ProductModel.ProductItem.priceTitle: Double
        get() = this.product.price / 10_000

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
                insertProduct(Product(item?.id!!, nameInput, priceInput.toDouble(),1))
            }else{
                insertProduct(Product(productName = nameInput,price = priceInput.toDouble(),categoryId = 1))
            }
            clearProductInfo()
        }else{
            if(priceInput.isEmpty()) errorFieldPrice = productPopup?.context?.getString(R.string.field_empty)
            if(nameInput.isEmpty()) errorFieldName = productPopup?.context?.getString(R.string.field_empty)
        }
    }
}