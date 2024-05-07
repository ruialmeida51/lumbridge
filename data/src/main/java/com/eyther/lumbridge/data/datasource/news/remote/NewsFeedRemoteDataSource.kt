package com.eyther.lumbridge.data.datasource.news.remote

import com.eyther.lumbridge.data.datasource.news.service.EconomyOECDRssClient
import com.eyther.lumbridge.data.datasource.news.service.EuronewsRssClient
import com.eyther.lumbridge.data.datasource.news.service.FinanceOECDRssClient
import com.eyther.lumbridge.data.datasource.news.service.PortugalNewsRssClient
import javax.inject.Inject

class NewsFeedRemoteDataSource @Inject constructor(
    private val economyOECDRssClient: EconomyOECDRssClient,
    private val financeOECDRssClient: FinanceOECDRssClient,
    private val euronewsRssClient: EuronewsRssClient,
    private val portugalNewsRssClient: PortugalNewsRssClient
) {
    suspend fun getEconomyOECDRssFeed(): String? {
        return if (economyOECDRssClient.getRssFeed().isSuccessful) {
            economyOECDRssClient.getRssFeed().body()
        } else {
            null
        }
    }

    suspend fun getFinanceOECDRssFeed(): String? {
        return if (financeOECDRssClient.getRssFeed().isSuccessful) {
            financeOECDRssClient.getRssFeed().body()
        } else {
            null
        }
    }

    suspend fun getEuronewsRssFeed(): String? {
        return if (euronewsRssClient.getRssFeed().isSuccessful) {
            euronewsRssClient.getRssFeed().body()
        } else {
            null
        }
    }

    suspend fun getPortugalNewsRssFeed(): String? {
        return if (portugalNewsRssClient.getRssFeed().isSuccessful) {
            portugalNewsRssClient.getRssFeed().body()
        } else {
            null
        }
    }
}
