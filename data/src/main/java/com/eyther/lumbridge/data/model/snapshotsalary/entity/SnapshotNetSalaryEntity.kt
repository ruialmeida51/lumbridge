package com.eyther.lumbridge.data.model.snapshotsalary.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

const val SNAPSHOT_NET_SALARY_TABLE_NAME = "snapshot_net_salary"

@Entity(
    tableName = SNAPSHOT_NET_SALARY_TABLE_NAME,
    indices = [
        Index(value = ["month", "year"], unique = true)
    ]
)
data class SnapshotNetSalaryEntity(
    @PrimaryKey(autoGenerate = true) val snapshotId: Long = 0,
    val savingsPercentage: Float? = null,
    val necessitiesPercentage: Float? = null,
    val luxuriesPercentage: Float? = null,
    val foodCardAmount: Float? = null,
    val amount: Float,
    val year: Int,
    val month: Int
)
