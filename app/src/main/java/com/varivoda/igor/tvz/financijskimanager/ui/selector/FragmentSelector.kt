package com.varivoda.igor.tvz.financijskimanager.ui.selector

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.varivoda.igor.tvz.financijskimanager.ui.maps.MapsActivity
import com.varivoda.igor.tvz.financijskimanager.ui.menu.MenuListFragmentDirections

class FragmentSelector {

    fun navigate(text: String,navController: NavController,activity: FragmentActivity?){
        when(text){
            "Popis proizvoda" -> {
                //navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToListFragment("Popis proizvoda"))
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis proizvoda"))
            }
            "Popis zaposlenika" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis zaposlenika"))
            }
            "Popis kupaca" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis kupaca"))
            }
            "Stanje skladišta" -> {
                //navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToFlowListFragment("Popis poslovnica"))
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToStockFragment())
            }
            "Lokacije poslovnica" -> {
                activity?.startActivity(Intent(activity, MapsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY))
            }
            "Popis računa" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Popis računa"))
            }
            "Zaposlenik koji je uprihodio najveću svotu novca po mjesecu" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Zaposlenik koji je uprihodio najveću svotu novca po mjesecu"))
            }
            "Proizvod koji se najbolje prodaje po kvartalima godine" ->{
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToQuarterFragment("Proizvod koji se najbolje prodaje po kvartalima godine"))
            }
            "Prodavač koji je najviše dana u godini izdao račun" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Prodavač koji je najviše dana u godini izdao račun"))
            }
            "Top 10 najprodavanijih proizvoda" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToTop10Fragment("Top 10 najprodavanijih proizvoda"))
            }
            "Račun sa najviše stavki po godini" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Račun sa najviše stavki po godini"))
            }
            "Unesite sliku proizvoda" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToPictureFragment())
            }
            "Komparativna analiza" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToAnalysisFragment("Komparativna analiza"))
            }
            "Proizvod koji ima najmanji udio u ukupnoj prodaji po mjesecu" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBillFragment("Proizvod koji ima najmanji udio u ukupnoj prodaji po mjesecu"))
            }
            "Grafički prikaz cjelokupne zarade po kvartalima" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToBarChartFragment())
            }
            "Udio određene poslovnice u ukupnom godišnjem profitu" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToPieChartFragment("Udio određene poslovnice u ukupnom godišnjem profitu"))
            }
            "Poslovnica koja najbolje prodaje određeni proizvod" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToDateAndProductFragment("Poslovnica koja najbolje prodaje određeni proizvod"))
            }
            "Inventura" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToInventoryFragment("Inventura"))

            }
            "Grafički prikaz izdanih računa po zaposleniku" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToHorizontalBarChartFragment())

            }
            "Zaposlenik koji je prodao najveću količinu nekog proizvoda po mjesecu" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToDateAndProductFragment("Zaposlenik koji je prodao najveću količinu nekog proizvoda po mjesecu"))
            }
            "Top 3 tipova proizvoda koji su se najrjeđe prodavali" ->
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToTop3Fragment("Top 3 tipova proizvoda koji su se najrjeđe prodavali"))

            "Top 4 najčešće posjećene trgovine tijekom izabranog razdoblja" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToRadarChartFragment())
            }
            "Posjećenost prema razdoblju dana" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToTimeOfDayFragment())
            }
            "Top 3 zaposlenika sa najboljim koeficijentom količine na računu" ->
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToHorizontalBarChartFragment())

            "Top 3 kupca koji su najviše kupovali određenu kategoriju" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToTop3Fragment("Top 3 kupca koji su najviše kupovali određenu kategoriju"))
            }
            "Grafički prikaz po načinima plaćanja" -> {
                navController.navigate(MenuListFragmentDirections.actionMenuListFragmentToPieChartFragment("Grafički prikaz po načinima plaćanja"))
            }
        }

    }
}