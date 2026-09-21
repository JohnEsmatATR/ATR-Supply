import com.akhnaton.atrapp.data.model.orderHistory.Item
import com.akhnaton.atrapp.data.model.orderHistory.Pagination
import com.akhnaton.atrapp.data.model.orderHistory.Params
import com.google.gson.annotations.SerializedName

data class MyOrderDetailsRes(
    @SerializedName("ORDER_ID")
    val orderId: String,

    @SerializedName("PAYMENT_TERM_DESC")
    val paymentTermDesc: String,

    @SerializedName("TOTAL_ORDER_PRICE_WITHOUT_TAX")
    val totalOrderPriceWithoutTax: Int,

    @SerializedName("TOTAL_ORDER_PRICE_WITH_TAX")
    val totalOrderPriceWithTax: Int,

    @SerializedName("TOTAL_ORDER_TAX")
    val totalOrderTax: Int,

    @SerializedName("STATUS_GROUP")
    val statusGroup: List<StatusGroup>,

    @SerializedName("item")
    val item: List<Item>,

    @SerializedName("message")
    val message: String,

    @SerializedName("pagination")
    val pagination: Pagination,

    @SerializedName("params")
    val params: Params,

    @SerializedName("status")
    val status: Int
)