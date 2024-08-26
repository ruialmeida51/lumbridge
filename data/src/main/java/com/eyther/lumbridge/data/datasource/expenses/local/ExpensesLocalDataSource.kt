package com.eyther.lumbridge.data.datasource.expenses.local

import com.eyther.lumbridge.data.datasource.expenses.dao.ExpensesDao
import com.eyther.lumbridge.data.mappers.expenses.toCached
import com.eyther.lumbridge.data.mappers.expenses.toEntity
import com.eyther.lumbridge.data.model.expenses.local.ExpensesCategoryCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesDetailedCached
import com.eyther.lumbridge.data.model.expenses.local.ExpensesMonthCached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ExpensesLocalDataSource @Inject constructor(
    private val expensesDao: ExpensesDao
) {
    val expensesFlow: Flow<List<ExpensesMonthCached>> = expensesDao.getAllExpensesForMonth()
        .mapNotNull { flowItem ->
            flowItem?.map { expenseEntity -> expenseEntity.toCached() }
        }

    /**
     * Fetches the detailed expense with the given ID from the local data source.
     *
     * @param detailedExpenseId The ID of the detailed expense to fetch.
     */
    suspend fun getDetailedExpense(detailedExpenseId: Long): ExpensesDetailedCached {
        val detailedExpense = expensesDao.getExpensesDetailedById(detailedExpenseId)
            ?: throw IllegalArgumentException("No detailed expense found with ID $detailedExpenseId")

        return detailedExpense.toCached()
    }

    /**
     * Fetches the expenses for the given year and month from the local data source.
     *
     * @param year The year to fetch expenses for.
     * @param month The month to fetch expenses for.
     */
    suspend fun getExpenseByYearMonth(year: Int, month: Int): ExpensesMonthCached? {
        return expensesDao.getExpensesForMonthByMonthYear(year, month)?.toCached()
    }

    /**
     * Fetches the expenses for the given category from the local data source.
     *
     * @param monthId The ID of the month to fetch categories for.
     */
    suspend fun getMonthCategoriesExpense(monthId: Long): List<ExpensesCategoryCached> {
        return runCatching { expensesDao.getExpensesForMonthCategories(monthId) }
            .getOrNull()
            .orEmpty()
            .map { it.expensesCategoryEntity.toCached(it.detailedExpenses) }
    }

    /**
     * Saves the given expense to the local data source.
     *
     * This method will run a loop for each month, category, and detail in the given list of expenses.
     *
     * @param expensesMonthCached The expense to save.
     */
    suspend fun saveExpense(expensesMonthCached: ExpensesMonthCached) {
        val monthId = expensesDao.saveExpensesMonth(expensesMonth = expensesMonthCached.toEntity())

        expensesMonthCached.categories.forEach { category ->
            val categoryId = expensesDao.saveExpensesCategory(expensesCategory = category.toEntity(monthId))

            category.details.forEach { detail ->
                expensesDao.saveExpensesDetail(expensesDetailed = detail.toEntity(categoryId))
            }
        }
    }

    /**
     * Updates the given expenses in the local data source.
     *
     * This assumes that the given expenses month already exist in the local data source.
     *
     * @param expensesMonthCached The expenses to update.
     */
    suspend fun saveNewExpenseOnExistingMonth(
        expensesMonthCached: ExpensesMonthCached,
        expensesCategoryCached: ExpensesCategoryCached,
        expensesDetailedCached: ExpensesDetailedCached
    ) {
        val categoryEntity = kotlin.runCatching { expensesDao.getExpensesCategoryById(expensesCategoryCached.id) }.getOrNull()

        if (categoryEntity == null) {
            val newCategory = expensesCategoryCached.toEntity(expensesMonthCached.id)

            expensesDao.saveExpensesCategory(newCategory)

            val detailedEntity = expensesDetailedCached
                .toEntity(newCategory.categoryId)

            expensesDao.saveExpensesDetail(detailedEntity)
        } else {
            val detailedEntity = expensesDetailedCached
                .toEntity(categoryEntity.categoryId)

            expensesDao.saveExpensesDetail(detailedEntity)
        }
    }

    /**
     * Saves the given detailed expense to the local data source.
     *
     * @param expensesDetailedCached The detailed expense to save.
     */
    suspend fun updateExpensesDetail(expensesDetailedCached: ExpensesDetailedCached) {
        val entity = expensesDetailedCached
            .toEntity(expensesDetailedCached.parentCategoryId)
            .copy(detailId = expensesDetailedCached.id)

        expensesDao.updateExpensesDetail(entity)
    }

    /**
     * Deletes the given expense from the local data source.
     *
     * @param expensesMonthCached The expense to delete.
     */
    suspend fun deleteExpense(expensesMonthCached: ExpensesMonthCached) {
        val entity = expensesMonthCached
            .toEntity()
            .copy(monthId = expensesMonthCached.id)

        expensesDao.deleteExpensesMonth(entity)
    }

    /**
     * Deletes the given detailed expense from the local data source.
     *
     * @param detailedExpenseId The ID of the detailed expense to delete.
     */
    suspend fun deleteExpenseDetailed(detailedExpenseId: Long) {
        // Fetch the detailed expense to delete
        val detailedExpense = expensesDao.getExpensesDetailedById(detailedExpenseId)
            ?: throw IllegalArgumentException("No detailed expense found with ID $detailedExpenseId")

        // Fetch the parent category for the detail
        val parentCategory = expensesDao.getExpensesCategoryById(detailedExpense.parentCategoryId)

        // Fetch the parent month for the category
        val parentMonth = expensesDao.getExpensesForMonthById(parentCategory?.parentMonthId ?: 0)

        // Delete the detail
        expensesDao.deleteExpensesDetailed(detailedExpense)

        // After deleting the detailed expense, check if the category has any other details.
        // If not, delete the category.
        expensesDao
            .getExpensesForCategory(detailedExpense.parentCategoryId)
            .detailedExpenses
            .ifEmpty { expensesDao.deleteExpensesCategory(parentCategory!!) }

        // After deleting the category, check if the month has any other categories.
        // If not, delete the month.
        expensesDao
            .getExpensesForMonthById(parentCategory?.parentMonthId ?: 0)
            .categories
            .ifEmpty { expensesDao.deleteExpensesMonth(parentMonth.expensesMonthEntity) }
    }
}
