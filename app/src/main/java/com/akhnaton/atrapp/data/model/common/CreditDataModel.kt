package com.akhnaton.atrapp.data.model.common

import com.google.gson.annotations.SerializedName

data class CreditDataModel(
    @SerializedName("CREDIT_LIMIT") val creditLimit: Double? = 0.0,
    @SerializedName("WITHDRAWALS_LAST_YEAR") val withdrawalsLastYear: Double? = 0.0,
    @SerializedName("WITHDRAWALS_CURR_YEAR") val withdrawalsCurrYear: Double? = 0.0,
    @SerializedName("USED_CREDIT") val usedCredit: Double? = 0.0,
    @SerializedName("AVAILABLE_CREDIT") val availableCredit: Double? = 0.0
)
