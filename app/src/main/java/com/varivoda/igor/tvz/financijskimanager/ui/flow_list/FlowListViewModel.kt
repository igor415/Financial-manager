package com.varivoda.igor.tvz.financijskimanager.ui.flow_list

import android.app.AlertDialog
import androidx.lifecycle.*
import androidx.paging.*
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.data.local.entity.*
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseCustomerRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseEmployeeRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseProductRepository
import com.varivoda.igor.tvz.financijskimanager.data.local.repository.base.BaseStoreRepository
import com.varivoda.igor.tvz.financijskimanager.model.EmployeeDTO
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowListViewModel(private val storeRepository: BaseStoreRepository,
                        private val productRepository: BaseProductRepository,
                        private val employeeRepository: BaseEmployeeRepository,
                        private val customerRepository: BaseCustomerRepository,
                        private val preferences: Preferences
) : ViewModel(){

    //private val productRepository = ProductRepository(AppDatabase.getInstance(context))
    //private val employeeRepository = EmployeeRepository(AppDatabase.getInstance(context))
    //private val customerRepository = CustomerRepository(AppDatabase.getInstance(context))
    //private val storeRepository = StoreRepository(AppDatabase.getInstance(context))

    val allProducts: Flow<List<Product>> = productRepository.getAllProducts()
        .onEach { it.map { product -> product.productName = "${product.productName} : ${product.price} kn" } } .flowOn(Dispatchers.Main).conflate()

    val employees: Flow<List<EmployeeDTO>> = employeeRepository.getEmployeesAndStores()
        //.onEach { it.map { employee -> employee.employeeName = "${employee.employeeName} ${employee.employeeLastName} (${employee.storeName})" } }
        .flowOn(Dispatchers.Default).conflate()

    val customers: Flow<List<Customer>> = customerRepository.getAllCustomers()
        .flowOn(Dispatchers.Default).conflate()

    val stores: Flow<List<Store>> = storeRepository.getStores()
        .flowOn(Dispatchers.Default).conflate()

    var locations = MutableLiveData<List<Location>>()
    var allStores = MutableLiveData<List<Store>>()

    init {
        getAllLocations()
        getAllStores()
    }
    

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

    var errorMessage = MutableLiveData<String>()
    fun insertEmployee(employee: Employee){
        viewModelScope.launch(Dispatchers.IO) {
            val response = employeeRepository.changeEmployeeInfo(preferences.getUserToken()!!
                ,employee)
            when(response){
                is NetworkResult.Success -> employeeRepository.insertEmployee(employee)
                is NetworkResult.NoNetworkConnection -> {errorMessage.postValue("Pojavio se problem. Molimo vas pokušajte kasnije.")}
                is NetworkResult.Error -> errorMessage.postValue("Nema internetske povezivosti")
                else -> {errorMessage.postValue("Pojavio se problem. Molimo vas pokušajte kasnije.")}
            }

        }
    }

    fun insertProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.addEditProduct(preferences.getUserToken()!!, product)
            when(response){
                is NetworkResult.Success -> productRepository.insertProduct(product)
                is NetworkResult.NoNetworkConnection -> {errorMessage.postValue("Pojavio se problem. Molimo vas pokušajte kasnije.")}
                is NetworkResult.Error -> errorMessage.postValue("Nema internetske povezivosti")
                else -> {errorMessage.postValue("Pojavio se problem. Molimo vas pokušajte kasnije.")}
            }

        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteProduct(id)
        }
    }

    /***products pager**/

    fun getProducts(): Flow<PagingData<ProductModel>> {
        return productRepository.getProductStream()!!.map { pagingData -> pagingData.map { ProductModel.ProductItem(it) } }
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
                        when(after.category){
                            2 -> ProductModel.SeparatorItem("Periferija")
                            3 -> ProductModel.SeparatorItem("Mobiteli")
                            4 -> ProductModel.SeparatorItem("Software")
                            5 -> ProductModel.SeparatorItem("Tableti")
                            6 -> ProductModel.SeparatorItem("Kućni telefoni")
                            else -> ProductModel.SeparatorItem("")
                        }
                    }else{
                        null
                    }
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

    fun getCustomersPaging(): Flow<PagingData<Customer>>{
        return customerRepository.getCustomersPaging()!!.cachedIn(viewModelScope)
    }

    /***product popup***/
    var nameInput: String = ""
    var priceInput: String = ""
    var title: String = ""
    var edit: Boolean = false
    var item: Product? = null
    var selectedCategory: Int? = null
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
                insertProduct(Product(item?.id!!, nameInput, priceInput.toDouble(),item?.categoryId!!))
            }else{
                if(selectedCategory != null){
                    insertProduct(Product(productName = nameInput,price = priceInput.toDouble(),categoryId = selectedCategory!!))
                }else{

                }

            }
            clearProductInfo()
        }else{
            if(priceInput.isEmpty()) errorFieldPrice = productPopup?.context?.getString(R.string.field_empty)
            if(nameInput.isEmpty()) errorFieldName = productPopup?.context?.getString(R.string.field_empty)
        }
    }


    /***employee popup***/
    var firstNameInput: String = ""
    var lastNameInput: String = ""
    var address: String = ""
    var employeeTitle: String = ""
    var editEmployee: Boolean = false
    var employee: EmployeeDTO? = null
    var employeePopup: AlertDialog? = null
    var employeeErrorFieldPrice: String? = null
    var employeeErrorFieldName: String? = null
    var selectedStore: Store? = null
    var selectedLocation: Location? = null
    var selectedStoreName: String = ""
    var selectedLocationName: String = ""

    fun clearEmployeeInfo() {
        firstNameInput = ""
        lastNameInput = ""
        address = ""
        employeeTitle = ""
        employeePopup?.dismiss()
        selectedStore = null
        selectedLocation = null
        selectedStoreName = ""
        selectedLocationName = ""
    }

    fun onEmployeeConfirmClicked(){
        if(firstNameInput.isNotEmpty() && lastNameInput.isNotEmpty() && selectedLocation != null && selectedStore != null){
            if(editEmployee){
                insertEmployee(Employee(employee!!.id,firstNameInput,lastNameInput,address,selectedStore!!.id,selectedLocation!!.locationId))
            }else{
                insertEmployee(Employee(employeeName = firstNameInput, employeeLastName = lastNameInput, address = address, storeId = selectedStore!!.id, locationId = selectedLocation!!.locationId))
            }
            clearEmployeeInfo()
        }else{
            if(priceInput.isEmpty()) employeeErrorFieldPrice = productPopup?.context?.getString(R.string.field_empty)
            if(nameInput.isEmpty()) employeeErrorFieldName = productPopup?.context?.getString(R.string.field_empty)
        }
    }


    fun getAllLocations(){
        viewModelScope.launch(Dispatchers.IO) {
            locations.postValue(storeRepository.getAllLocations())
        }
    }

    fun getAllStores(){
        viewModelScope.launch(Dispatchers.IO) {
            allStores.postValue(storeRepository.getAllStores())
        }
    }



}