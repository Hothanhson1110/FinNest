package vn.finnest.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Clay = Color(0xFF8C302B)
private val Leaf = Color(0xFF617A55)
private val Cream = Color(0xFFF8F1E7)
private val Muted = Color(0xFF77716A)

@Composable
fun FinNestApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = Cream,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Tổng quan") }, label = { Text("Tổng quan") })
                NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.PieChart, contentDescription = "Danh mục") }, label = { Text("Danh mục") })
                NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Analytics, contentDescription = "Phân tích") }, label = { Text("Phân tích") })
                NavigationBarItem(selected = selectedTab == 3, onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Tài chính") }, label = { Text("Tài chính") })
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }, containerColor = Clay, contentColor = Color.White) {
                Icon(Icons.Default.Add, contentDescription = "Thêm giao dịch")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                BrandMark()
                Column {
                    Text("FinNest", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Clay)
                    Text("Tài chính an tâm, đầu tư vững bước", fontSize = 12.sp, color = Muted)
                }
            }
            when (selectedTab) {
                0 -> DashboardContent()
                1 -> PlaceholderContent("Danh mục đầu tư", "Danh sách cổ phiếu và giá vốn sẽ xuất hiện tại đây.")
                2 -> PlaceholderContent("Phân tích kỹ thuật", "Biểu đồ, RSI, MACD và công thức cá nhân sẽ được phát triển ở bước tiếp theo.")
                else -> PlaceholderContent("Tài chính cá nhân", "Thu chi, ngân sách và mục tiêu tài chính.")
            }
        }
    }
}

@Composable
private fun BrandMark() {
    Box(
        modifier = Modifier.size(54.dp).background(Clay, RoundedCornerShape(17.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("⌂", fontSize = 27.sp, color = Cream, lineHeight = 25.sp, fontWeight = FontWeight.Bold)
            Text("↗", fontSize = 15.sp, color = Color(0xFFDDA65D), lineHeight = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DashboardContent() {
    Text("Chào bạn 👋", style = MaterialTheme.typography.titleMedium, color = Color(0xFF282522))
    Text("Bức tranh tài chính của bạn", color = Muted)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Clay)
    ) {
        Column(Modifier.padding(22.dp)) {
            Text("TỔNG TÀI SẢN ƯỚC TÍNH", color = Color(0xFFF4DCD2), fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text("0 ₫", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text("Bắt đầu bằng cách ghi nhận tài sản đầu tiên", color = Color(0xFFF4DCD2), fontSize = 13.sp)
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MetricCard("Danh mục", "0 mã", Leaf, Modifier.weight(1f))
        MetricCard("Lãi / lỗ", "—", Clay, Modifier.weight(1f))
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Thói quen tài chính tốt", fontWeight = FontWeight.SemiBold, color = Color(0xFF282522))
            Text("• Dành quỹ dự phòng trước khi đầu tư.", color = Muted)
            Text("• Đa dạng hóa và xác định mức rủi ro.", color = Muted)
            Text("• Đánh giá kế hoạch dài hạn định kỳ.", color = Muted)
        }
    }
    Text("Dữ liệu minh họa · Chưa kết nối DNSE", color = Muted, fontSize = 11.sp)
}

@Composable
private fun MetricCard(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.size(8.dp).background(accent, CircleShape))
            Text(title, color = Muted, fontSize = 12.sp)
            Text(value, color = Color(0xFF282522), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PlaceholderContent(title: String, description: String) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, color = Clay, fontWeight = FontWeight.Bold)
        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) {
            Text(description, Modifier.padding(20.dp), color = Muted)
        }
        Text("Khung giao diện ban đầu", color = Muted, fontSize = 12.sp)
    }
}
