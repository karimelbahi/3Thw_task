package com.example.task.presentation.utils

import java.util.concurrent.TimeUnit

object Constants {

    // DB
    const val LOCAL_DATABASE_NAME = "LOCAL_DATABASE_NAME"
    const val PRODUCT_TABLE = "PRODUCT_TABLE"

    // product
    const val PRODUCT_ID = "PRODUCT_ID"
    const val PRODUCT_NAME = "PRODUCT_NAME"
    const val PRODUCT_CODE = "PRODUCT_CODE"
    const val PRODUCT_TYPE = "PRODUCT_TYPE"

    // threads
    const val PRODUCT_EXPIRED_DATE_NOTIFICATION_THREAD = "PRODUCT_EXPIRED_DATE_NOTIFICATION_THREAD"
    const val PRODUCT_EXPIRED_DATE_STATUS_THREAD = "PRODUCT_EXPIRED_DATE_STATUS_THREAD"

    // notification
    const val NOTIFICATION_ID = "appName_notification_id"
    const val NOTIFICATION_NAME = "appName"
    const val NOTIFICATION_CHANNEL = "appName_channel_01"

    // work manager
    const val THIRD_WAY_WORK_MANAGER = "THIRD_WAY_WORK_MANAGER"
    const val HOURLY_EXPIRY_DATE_REPORTER_REPEAT_INTERVAL = 15L
    val HOURLY_EXPIRY_DATE_REPORTER_REPEAT_TIME_UNITE = TimeUnit.MINUTES

    // view
    const val PRODUCTS_LIST_MINIMUM_COUNT = 4
    const val ALLOWED_DIFF_DAYS = 1

    //sharedPref
    const val MOCK_TIME_NUM_KEY = "MOCK_TIME_NUM_KEY"
    const val NUM_OF_MOCKED_HORS = 6





}