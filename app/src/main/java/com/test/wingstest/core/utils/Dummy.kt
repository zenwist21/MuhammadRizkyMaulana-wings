package com.test.wingstest.core.utils

import com.test.wingstest.core.data.model.ProductModel
import com.test.wingstest.core.data.model.UserInformation

fun dummyProduct(): MutableList<ProductModel> = mutableListOf(
    ProductModel(
        productCode = "SKUSKILNP",
        productName = "SO Klin Pewangi",
        discount = (10.0 /* % percent */ / 100),
        price = 15000,
        dimension = "13 cm x 10 cm",
        unit = "PCS",
        currency = "IDR"
    ),
    ProductModel(
        productCode = "GVBRU",
        productName = "Giv Biru",
        discount = (0.0 /* % percent */ / 100),
        price = 11000,
        dimension = "13 cm x 10 cm",
        unit = "PCS",
        currency = "IDR"
    ),
    ProductModel(
        productCode = "SKLQ",
        productName = "SO Klin Liquid",
        discount = (0.0 /* % percent */ / 100),
        price = 18000,
        dimension = "13 cm x 10 cm",
        unit = "PCS",
        currency = "IDR"
    ),
    ProductModel(
        productCode = "GVKNG",
        productName = "Giv Kuning",
        discount = (0.0 /* % percent */ /  100),
        price = 10000,
        dimension = "13 cm x 10 cm",
        unit = "PCS",
        currency = "IDR"
    ),
    ProductModel(
        productCode = "GVMRH",
        productName = "Giv Merah",
        discount = (0.0 /* % percent */ /100),
        price = 10000,
        dimension = "13 cm x 10 cm",
        unit = "PCS",
        currency = "IDR"
    )
)

val dummyUser = UserInformation(
    user = "Smit", password = "_sm1t_OK"
)