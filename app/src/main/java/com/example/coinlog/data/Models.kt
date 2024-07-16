package com.example.coinlog.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Expenses(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val category: Category,
    val credit: Boolean,
    val amount: Double,
    val dateAdded: Long
)

@Entity
data class Pots(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val reason: String,
    val amount: Double,
//    val transaction: List<Expenses>,
    val dateAdded: Long
)

@Entity
data class Summary(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val balance: Double = 0.00,
    val expenditure: Double = 0.00,
    val income: Double = 0.00
)

enum class Category(private val displayName: String) {
    Miscellaneous("Miscellaneous"),
    FoodAndDrinks("Food & Drinks"),
    Groceries("Groceries"),
    Shopping("Shopping"),
    Entertainment("Entertainment"),
    Fuel("Fuel"),
    Commute("Commute"),
    Travel("Travel"),
    PersonalCare("Personal Care"),
    BillsAndUtilities("Bills & Utilities"),
    Rent("Rent"),
    Household("Household"),
    Insurance("Insurance"),
    Education("Education"),
    Medical("Medical"),
    Fitness("Fitness"),
    FamilyAndPets("Family & Pets"),
    Investments("Investments"),
    CreditBills("Credit Bills"),
    Loans("Loans"),
    Emi("EMI"),
    FeesAndCharges("Fees & Charges"),
    Atm("ATM"),
    Charity("Charity"),
    MoneyTransfer("Money Transfer");


    override fun toString(): String {
        return displayName
    }
}