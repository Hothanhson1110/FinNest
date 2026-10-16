package vn.finnest.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale
import vn.finnest.app.data.market.MarketQuoteDto

private val MarketClay = Color(0xFF8C302B)
private val MarketLeaf = Color(0xFF617A55)
private val MarketMuted = Color(0xFF77716A)
private val MarketNumber = NumberFormat.getNumberInstance(Locale("vi", "VN"))

@Composable
fun MarketScreen(viewModel: MarketViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var symbols by remember { mutableStateOf("FPT, HPG, VCB") }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Thị trường", style = MaterialTheme.typography.headlineSmall, color = MarketClay, fontWeight = FontWeight.Bold)
        Text("Dữ liệu chỉ hiển thị khi backend của bạn đã được cấu hình.", color = MarketMuted, fontSize = 12.sp)
        OutlinedTextField(
            value = symbols, onValueChange = { symbols = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Mã chứng khoán") }, placeholder = { Text("FPT, HPG, VCB") }, singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
        )
        Button(onClick = { viewModel.refresh(symbols) }, modifier = Modifier.fillMaxWidth(), enabled = state !is MarketUiState.Loading) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.height(1.dp).padding(horizontal = 3.dp))
            Text("Tải dữ liệu thị trường")
        }
        when (val current = state) {
            MarketUiState.Idle -> InfoCard(if (viewModel.isConfigured) "Backend đã được cấu hình. Nhấn tải dữ liệu để kiểm tra kết nối." else "Chưa kết nối DNSE. Cần triển khai backend trung gian HTTPS và cấu hình MARKET_API_BASE_URL. Không nhập API Secret vào ứng dụng.")
            MarketUiState.Loading -> CircularProgressIndicator()
            is MarketUiState.Error -> InfoCard(current.message)
            is MarketUiState.Success -> {
                if (current.quotes.isEmpty()) InfoCard("Backend chưa trả về báo giá cho các mã đã chọn.")
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(current.quotes, key = { it.symbol }) { quote -> QuoteCard(quote) }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(message: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Text(message, Modifier.padding(16.dp), color = MarketMuted)
    }
}

@Composable
private fun QuoteCard(quote: MarketQuoteDto) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(quote.symbol, fontWeight = FontWeight.Bold, color = MarketClay)
                Text("${MarketNumber.format(quote.price)} ${quote.currency}", fontWeight = FontWeight.Bold)
            }
            val positive = quote.change >= 0
            Text("${if (positive) "+" else ""}${MarketNumber.format(quote.change)} (${String.format(Locale.US, "%.2f", quote.changePercent)}%)",
                color = if (positive) MarketLeaf else MarketClay)
            quote.updatedAt?.let { Text("Cập nhật: $it", color = MarketMuted, fontSize = 11.sp) }
        }
    }
}
