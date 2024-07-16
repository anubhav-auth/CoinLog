package com.example.coinlog.data

import com.example.coinlog.R

object HelperObj {
    fun getIcon(category: Category): Int {
        return when (category) {
            Category.FoodAndDrinks -> R.drawable.finance_food_drinks
            Category.Groceries -> R.drawable.finance_grocery
            Category.Shopping -> R.drawable.finance_shopping
            Category.Entertainment -> R.drawable.finance_entertainment
            Category.Fuel -> R.drawable.finance_fuel
            Category.Commute -> R.drawable.finance_commute
            Category.Travel -> R.drawable.finance_travel
            Category.PersonalCare -> R.drawable.finance_personal_care
            Category.BillsAndUtilities -> R.drawable.finance_bill_utilities
            Category.Rent -> R.drawable.finance_rent
            Category.Household -> R.drawable.finance_household
            Category.Insurance -> R.drawable.finance_insurance
            Category.Education -> R.drawable.finance_education
            Category.Medical -> R.drawable.finance_medical
            Category.Fitness -> R.drawable.finance_fitness
            Category.FamilyAndPets -> R.drawable.finance_family
            Category.Investments -> R.drawable.finance_investment
            Category.CreditBills -> R.drawable.finance_credit_bills
            Category.Loans -> R.drawable.finance_loans
            Category.Emi -> R.drawable.finance_emi
            Category.FeesAndCharges -> R.drawable.finance_fees
            Category.Atm -> R.drawable.finance_atm
            Category.Charity -> R.drawable.finance_charity
            Category.MoneyTransfer -> R.drawable.finance_mony_transfer
            Category.Miscellaneous -> R.drawable.finance_miscellaneous
        }
    }
}