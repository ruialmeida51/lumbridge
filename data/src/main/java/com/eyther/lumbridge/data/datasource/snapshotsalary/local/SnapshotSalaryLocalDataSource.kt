package com.eyther.lumbridge.data.datasource.snapshotsalary.local

import com.eyther.lumbridge.data.datasource.snapshotsalary.dao.SnapshotSalaryDao
import com.eyther.lumbridge.data.mappers.snapshotsalary.toCached
import com.eyther.lumbridge.data.mappers.snapshotsalary.toEntity
import com.eyther.lumbridge.data.model.snapshotsalary.local.SnapshotNetSalaryCached
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SnapshotSalaryLocalDataSource @Inject constructor(
    private val snapshotSalaryDao: SnapshotSalaryDao
) {
    val snapshotNetSalaryFlow = snapshotSalaryDao
        .getSnapshotNetSalariesFlow()
        .map { flowItem ->
            flowItem.map { snapshotNetSalaryEntity -> snapshotNetSalaryEntity.toCached() }
        }

    suspend fun saveSnapshotNetSalary(snapshotNetSalaryCached: SnapshotNetSalaryCached) {
        if (snapshotNetSalaryCached.snapshotId != -1L) {
            snapshotSalaryDao.updateSnapshotNetSalary(
                snapshotNetSalaryCached.toEntity()
                .copy(snapshotId = snapshotNetSalaryCached.snapshotId)
            )
        } else {
            snapshotSalaryDao.insertSnapshotNetSalary(snapshotNetSalaryCached.toEntity())
        }
    }

    suspend fun getSnapshotNetSalaryByYearMonth(year: Int, month: Int): SnapshotNetSalaryCached? {
        return snapshotSalaryDao.getSnapshotNetSalaryByYearMonth(year, month)?.toCached()
    }

    suspend fun getAllSnapshotNetSalaries(): List<SnapshotNetSalaryCached> {
        return snapshotSalaryDao.getAllSnapshotNetSalaries().map { it.toCached() }
    }
}
