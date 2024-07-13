package com.example.coinlog.data

import com.example.coinlog.R

object HelperObj {
    fun getIcon(category: Category): Int {
        return when (category) {
            Category.FoodAndDrinks -> R.drawable.ic_launcher_background
            Category.Groceries -> R.drawable.ic_launcher_background
            Category.Shopping -> R.drawable.ic_launcher_background
            Category.Entertainment -> R.drawable.ic_launcher_background
            Category.Fuel -> R.drawable.ic_launcher_background
            Category.Commute -> R.drawable.ic_launcher_background
            Category.Travel -> R.drawable.ic_launcher_background
            Category.PersonalCare -> R.drawable.ic_launcher_background
            Category.BillsAndUtilities -> R.drawable.ic_launcher_background
            Category.Rent -> R.drawable.ic_launcher_background
            Category.Household -> R.drawable.ic_launcher_background
            Category.Insurance -> R.drawable.ic_launcher_background
            Category.Education -> R.drawable.ic_launcher_background
            Category.Medical -> R.drawable.ic_launcher_background
            Category.Fitness -> R.drawable.ic_launcher_background
            Category.FamilyAndPets -> R.drawable.ic_launcher_background
            Category.Investments -> R.drawable.ic_launcher_background
            Category.CreditBills -> R.drawable.ic_launcher_background
            Category.Loans -> R.drawable.ic_launcher_background
            Category.Emi -> R.drawable.ic_launcher_background
            Category.FeesAndCharges -> R.drawable.ic_launcher_background
            Category.Atm -> R.drawable.ic_launcher_background
            Category.Charity -> R.drawable.ic_launcher_background
            Category.MoneyTransfer -> R.drawable.ic_launcher_background
            Category.Miscellaneous -> R.drawable.ic_launcher_background
        }
    }
}