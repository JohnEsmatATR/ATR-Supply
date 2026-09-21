import com.google.gson.annotations.SerializedName

data class StatusGroup(
    @SerializedName("COLOR")
    val color: String,

    @SerializedName("DATE")
    val date: String,

    @SerializedName("DESCRIPTION")
    val description: String,

    @SerializedName("IS_COMPLETED")
    val isCompleted: Boolean,

    @SerializedName("NAME")
    val name: String
)