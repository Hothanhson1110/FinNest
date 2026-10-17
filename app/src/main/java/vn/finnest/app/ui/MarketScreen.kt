package vn.finnest.app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale
import vn.finnest.app.data.market.MarketQuoteDto
import vn.finnest.app.domain.OhlcCandle
import vn.finnest.app.domain.TechnicalIndicators

private val MarketClay = Color(0xFF8C302B)
private val MarketLeaf = Color(0xFF617A55)
private val MarketCopper = Color(0xFFDDA65D)
private val MarketMuted = Color(0xFF77716A)
private val MarketGrid = Color(0xFFE7DED2)
private val MarketNumber = NumberFormat.getNumberInstance(Locale("vi", "VN"))

@Composable
fun MarketScreen(viewModel: MarketViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var symbols by remember { mutableStateOf("FPT, HPG, VCB") }
    val demoCandles = remember { sampleCandles() }
    val closes = remember(demoCandles) { demoCandles.map { it.close } }
    val sma = remember(closes) { TechnicalIndicators.sma(closes, 5) }
    val ema = remember(closes) { TechnicalIndicators.ema(closes, 5) }
    val rsi = remember(closes) { TechnicalIndicators.rsi(closes, 14).lastOrNull() }
    val macd = remember(closes) { TechnicalIndicators.macd(closes).lastOrNull() }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Thị trường", style = MaterialTheme.typography.headlineSmall, color = MarketClay, fontWeight = FontWeight.Bold)
        Text("Biểu đồ minh họa · Chưa phải dữ liệu DNSE thật", color = MarketMuted, fontSize = 12.sp)

        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("FPT", color = MarketClay, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text("OHLC · 30 phiên mẫu", color = MarketMuted, fontSize = 12.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LegendDot(MarketLeaf, "Tăng")
                    LegendDot(MarketClay, "Giảm")
                    LegendDot(MarketCopper, "SMA 5")
                    LegendDot(Color(0xFF4777A8), "EMA 5")
                }
                OhlcChart(candles = demoCandles, sma = sma, ema = ema, modifier = Modifier.fillMaxWidth().height(220.dp))
                Text("Giá đóng cửa cuối: ${MarketNumber.format(closes.last())} (đơn vị minh họa)",
                    color = MarketMuted, fontSize = 12.sp)
            }
        }

        Text("Phân tích kỹ thuật", style = MaterialTheme.typography.titleLarge, color = MarketClay, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IndicatorCard("RSI (14)", rsi?.let { String.format(Locale.US, "%.2f", it) } ?: "Chưa đủ dữ liệu",
                when { rsi == null -> MarketMuted; rsi >= 70 -> MarketClay; rsi <= 30 -> MarketLeaf; else -> MarketMuted },
                Modifier.weight(1f))
            IndicatorCard("SMA (5)", sma.lastOrNull()?.let { MarketNumber.format(it) } ?: "—", MarketCopper, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IndicatorCard("EMA (5)", ema.lastOrNull()?.let { MarketNumber.format(it) } ?: "—", Color(0xFF4777A8), Modifier.weight(1f))
            IndicatorCard("MACD (12,26,9)", macd?.macd?.let { String.format(Locale.US, "%.3f", it) } ?: "Chưa đủ dữ liệu",
                if ((macd?.histogram ?: 0.0) >= 0.0) MarketLeaf else MarketClay, Modifier.weight(1f))
        }
        macd?.let {
            InfoCard("Tín hiệu MACD: ${if ((it.histogram ?: 0.0) >= 0.0) "histogram dương" else "histogram âm"}. Đây là chỉ báo tham khảo, không phải khuyến nghị mua/bán.")
        }

        Divider(color = MarketGrid)
        Text("Kết nối dữ liệu thị trường", style = MaterialTheme.typography.titleLarge, color = MarketClay, fontWeight = FontWeight.Bold)
        Text("Báo giá chỉ hiển thị khi backend HTTPS được cấu hình.", color = MarketMuted, fontSize = 12.sp)
        OutlinedTextField(
            value = symbols, onValueChange = { symbols = it }, modifier = Modifier.fillMaxWidth(),
            label = { Text("Mã chứng khoán") }, placeholder = { Text("FPT, HPG, VCB") }, singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
        )
        Button(onClick = { viewModel.refresh(symbols) }, modifier = Modifier.fillMaxWidth(),
            enabled = state !is MarketUiState.Loading) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tải dữ liệu thị trường")
        }
        when (val current = state) {
            MarketUiState.Idle -> InfoCard(if (viewModel.isConfigured)
                "Backend đã được cấu hình. Nhấn tải dữ liệu để kiểm tra kết nối."
                else "Chưa kết nối DNSE. Cần triển khai backend trung gian HTTPS và cấu hình MARKET_API_BASE_URL. Không nhập API Secret vào ứng dụng.")
            MarketUiState.Loading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is MarketUiState.Error -> InfoCard(current.message)
            is MarketUiState.Success -> {
                if (current.quotes.isEmpty()) InfoCard("Backend chưa trả về báo giá cho các mã đã chọn.")
                current.quotes.forEach { quote -> QuoteCard(quote) }
            }
        }
        Spacer(Modifier.height(84.dp))
    }
}

@Composable
private fun OhlcChart(
    candles: List<OhlcCandle>,
    sma: List<Double?>,
    ema: List<Double?>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier) {
        if (candles.isEmpty()) return@Canvas
        val minPrice = candles.minOf { it.low }
        val maxPrice = candles.maxOf { it.high }
        val range = (maxPrice - minPrice).takeIf { it > 0.0 } ?: 1.0
        val top = 12.dp.toPx()
        val bottom = size.height - 12.dp.toPx()
        val chartHeight = bottom - top
        fun y(price: Double) = bottom - ((price - minPrice) / range).toFloat() * chartHeight
        repeat(4) { i ->
            val gy = top + chartHeight * i / 3f
            drawLine(MarketGrid, Offset(0f, gy), Offset(size.width, gy), 1.dp.toPx())
        }
        val slot = size.width / candles.size
        val bodyWidth = (slot * 0.52f).coerceAtLeast(2.dp.toPx())
        candles.forEachIndexed { index, candle ->
            val x = slot * (index + 0.5f)
            val rising = candle.close >= candle.open
            val color = if (rising) MarketLeaf else MarketClay
            drawLine(color, Offset(x, y(candle.high)), Offset(x, y(candle.low)), 1.2.dp.toPx())
            val openY = y(candle.open)
            val closeY = y(candle.close)
            val bodyTop = minOf(openY, closeY)
            val bodyHeight = kotlin.math.abs(closeY - openY).coerceAtLeast(1.5.dp.toPx())
            drawRect(color, topLeft = Offset(x - bodyWidth / 2f, bodyTop), size = androidx.compose.ui.geometry.Size(bodyWidth, bodyHeight))
        }
        fun drawSeries(values: List<Double?>, color: Color) {
            val path = Path()
            var started = false
            values.forEachIndexed { index, value ->
                if (value != null) {
                    val point = Offset(slot * (index + 0.5f), y(value))
                    if (!started) { path.moveTo(point.x, point.y); started = true } else path.lineTo(point.x, point.y)
                } else started = false
            }
            drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
        }
        drawSeries(sma, MarketCopper)
        drawSeries(ema, Color(0xFF4777A8))
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier.size(8.dp).background(color, RoundedCornerShape(50)))
        Text(label, fontSize = 10.sp, color = MarketMuted)
    }
}

@Composable
private fun IndicatorCard(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontSize = 11.sp, color = MarketMuted)
            Text(value, fontWeight = FontWeight.Bold, color = accent, fontSize = 16.sp)
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

/**
 * Deterministic illustration data only. Replace with validated backend OHLC before labeling
 * the chart as real market data. No quote is fabricated in the API repository.
 */
private fun sampleCandles(): List<OhlcCandle> {
    val closes = listOf(
        48.2, 48.7, 48.4, 49.1, 49.6, 49.2, 49.8, 50.1, 49.7, 50.4,
        50.8, 50.2, 50.6, 51.1, 50.9, 51.5, 51.2, 51.8, 52.1, 51.6,
        52.4, 52.8, 52.2, 52.7, 53.1, 52.9, 53.4, 53.0, 53.7, 54.0
    )
    return closes.mapIndexed { index, close ->
        val previous = if (index == 0) close - 0.3 else closes[index - 1]
        val open = previous + if (index % 2 == 0) 0.12 else -0.10
        OhlcCandle(
            timestamp = index.toLong(),
            open = open,
            high = maxOf(open, close) + 0.25 + (index % 3) * 0.05,
            low = minOf(open, close) - 0.20 - (index % 2) * 0.05,
            close = close,
            volume = 1000L + index * 125L
        )
    }
}
