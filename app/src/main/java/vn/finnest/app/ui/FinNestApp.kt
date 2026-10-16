package vn.finnest.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale
import vn.finnest.app.data.local.HoldingEntity
import vn.finnest.app.data.local.LedgerEntryEntity

private val Clay = Color(0xFF8C302B)
private val Leaf = Color(0xFF617A55)
private val Cream = Color(0xFFF8F1E7)
private val Muted = Color(0xFF77716A)
private val Vnd = NumberFormat.getNumberInstance(Locale("vi", "VN"))
private fun money(value: Long) = "${Vnd.format(value)} ₫"

private enum class AddKind(val label: String) {
    STOCK("Cổ phiếu"), ASSET("Tài sản"), INCOME("Thu nhập"), EXPENSE("Chi tiêu")
}

@Composable
fun FinNestApp(viewModel: FinanceViewModel = hiltViewModel()) {
    val holdings by viewModel.holdings.collectAsState()
    val ledger by viewModel.ledger.collectAsState()
    val summary by viewModel.summary.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddMenu by remember { mutableStateOf(false) }
    var addKind by remember { mutableStateOf<AddKind?>(null) }

    Scaffold(
        containerColor = Cream,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selectedTab == 0, { selectedTab = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Tổng quan") })
                NavigationBarItem(selectedTab == 1, { selectedTab = 1 }, icon = { Icon(Icons.Default.PieChart, null) }, label = { Text("Danh mục") })
                NavigationBarItem(selectedTab == 2, { selectedTab = 2 }, icon = { Icon(Icons.Default.Analytics, null) }, label = { Text("Thị trường") })
                NavigationBarItem(selectedTab == 3, { selectedTab = 3 }, icon = { Icon(Icons.Default.AccountBalanceWallet, null) }, label = { Text("Tài chính") })
            }
        },
        floatingActionButton = {
            Box {
                FloatingActionButton(onClick = { showAddMenu = true }, containerColor = Clay, contentColor = Color.White) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm tài sản hoặc giao dịch")
                }
                DropdownMenu(expanded = showAddMenu, onDismissRequest = { showAddMenu = false }) {
                    AddKind.entries.forEach { kind ->
                        DropdownMenuItem(text = { Text(kind.label) }, onClick = {
                            showAddMenu = false
                            addKind = kind
                        })
                    }
                }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp, vertical = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(54.dp).background(Clay, RoundedCornerShape(17.dp)), contentAlignment = Alignment.Center) {
                    Text("⌂↗", color = Color(0xFFF8F1E7), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("FinNest", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Clay)
                    Text("Tài chính an tâm, đầu tư vững bước", fontSize = 12.sp, color = Muted)
                }
            }
            Spacer(Modifier.height(18.dp))
            when (selectedTab) {
                0 -> Dashboard(summary.assetsVnd, summary.holdingCount, summary.profitLossVnd,
                    summary.incomeVnd, summary.expenseVnd, holdings, ledger,
                    onDeleteHolding = viewModel::deleteHolding, onDeleteLedger = viewModel::deleteLedger)
                1 -> HoldingsScreen(holdings, viewModel::deleteHolding)
                2 -> MarketScreen()
                else -> LedgerScreen(ledger, viewModel::deleteLedger, summary.incomeVnd, summary.expenseVnd)
            }
        }
    }

    addKind?.let { kind ->
        AddEntryDialog(kind = kind, onDismiss = { addKind = null }, onSaveHolding = { name, symbol, type, qty, cost, price ->
            viewModel.saveHolding(name, symbol, type, qty, cost, price)
            addKind = null
        }, onSaveLedger = { title, amount, category, income ->
            viewModel.saveLedger(title, amount, category, income)
            addKind = null
        })
    }
}

@Composable
private fun Dashboard(
    assets: Long, count: Int, profitLoss: Long, income: Long, expense: Long,
    holdings: List<HoldingEntity>, ledger: List<LedgerEntryEntity>,
    onDeleteHolding: (HoldingEntity) -> Unit, onDeleteLedger: (LedgerEntryEntity) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp), contentPadding = PaddingValues(bottom = 76.dp)) {
        item { Text("Chào bạn 👋", style = MaterialTheme.typography.titleMedium) }
        item { Text("Bức tranh tài chính của bạn", color = Muted) }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Clay), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp)) {
                    Text("TỔNG GIÁ TRỊ TÀI SẢN", color = Color(0xFFF4DCD2), fontSize = 12.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(money(assets), color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("Giá trị dựa trên dữ liệu bạn đã nhập", color = Color(0xFFF4DCD2), fontSize = 12.sp)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Metric("Danh mục", "$count mục", Leaf, Modifier.weight(1f))
                Metric("Lãi / lỗ", money(profitLoss), if (profitLoss >= 0) Leaf else Clay, Modifier.weight(1f))
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Metric("Thu nhập", money(income), Leaf, Modifier.weight(1f))
                Metric("Chi tiêu", money(expense), Clay, Modifier.weight(1f))
            }
        }
        item { Text("Tài sản gần đây", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium) }
        if (holdings.isEmpty()) item { EmptyHint("Chưa có tài sản. Nhấn + để thêm cổ phiếu hoặc tài sản.") }
        items(holdings.take(5), key = { "h-${it.id}" }) { holding -> HoldingRow(holding, onDeleteHolding) }
        item { Text("Giao dịch gần đây", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.titleMedium) }
        if (ledger.isEmpty()) item { EmptyHint("Chưa có giao dịch thu chi.") }
        items(ledger.take(5), key = { "l-${it.id}" }) { entry -> LedgerRow(entry, onDeleteLedger) }
    }
}

@Composable
private fun HoldingsScreen(holdings: List<HoldingEntity>, onDelete: (HoldingEntity) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("Danh mục tài sản", style = MaterialTheme.typography.headlineSmall, color = Clay, fontWeight = FontWeight.Bold)
        Text("Giá thị trường do bạn nhập; chưa kết nối DNSE.", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(12.dp))
        if (holdings.isEmpty()) EmptyHint("Nhấn + → Cổ phiếu hoặc Tài sản để bắt đầu.")
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 76.dp)) {
            items(holdings, key = { it.id }) { HoldingRow(it, onDelete) }
        }
    }
}

@Composable
private fun LedgerScreen(
    entries: List<LedgerEntryEntity>, onDelete: (LedgerEntryEntity) -> Unit,
    income: Long, expense: Long
) {
    Column(Modifier.fillMaxSize()) {
        Text("Tài chính cá nhân", style = MaterialTheme.typography.headlineSmall, color = Clay, fontWeight = FontWeight.Bold)
        Text("Thu ${money(income)} · Chi ${money(expense)}", color = Muted)
        Spacer(Modifier.height(12.dp))
        if (entries.isEmpty()) EmptyHint("Nhấn + để thêm thu nhập hoặc chi tiêu.")
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 76.dp)) {
            items(entries, key = { it.id }) { LedgerRow(it, onDelete) }
        }
    }
}

@Composable
private fun HoldingRow(item: HoldingEntity, onDelete: (HoldingEntity) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(if (item.symbol.isBlank()) item.name else "${item.symbol} · ${item.name}", fontWeight = FontWeight.SemiBold)
                Text("${item.assetType} · SL ${item.quantity}", color = Muted, fontSize = 12.sp)
                Text("Giá trị ${money(item.marketValueVnd())} · Lãi/lỗ ${money(item.profitLossVnd())}",
                    color = if (item.profitLossVnd() >= 0) Leaf else Clay, fontSize = 12.sp)
            }
            IconButton(onClick = { onDelete(item) }) { Icon(Icons.Default.DeleteOutline, "Xóa tài sản", tint = Clay) }
        }
    }
}

@Composable
private fun LedgerRow(item: LedgerEntryEntity, onDelete: (LedgerEntryEntity) -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(9.dp).background(if (item.isIncome) Leaf else Clay, CircleShape))
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(item.title, fontWeight = FontWeight.SemiBold)
                Text(item.category, color = Muted, fontSize = 12.sp)
            }
            Text("${if (item.isIncome) "+" else "−"}${money(item.amountVnd)}",
                color = if (item.isIncome) Leaf else Clay, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = { onDelete(item) }) { Icon(Icons.Default.DeleteOutline, "Xóa giao dịch", tint = Clay) }
        }
    }
}

@Composable
private fun Metric(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.size(8.dp).background(accent, CircleShape))
            Text(title, color = Muted, fontSize = 12.sp)
            Text(value, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EmptyHint(text: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Text(text, Modifier.padding(18.dp), color = Muted)
    }
}

@Composable
private fun Placeholder(title: String, text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, color = Clay, fontWeight = FontWeight.Bold)
        EmptyHint(text)
    }
}

@Composable
private fun AddEntryDialog(
    kind: AddKind,
    onDismiss: () -> Unit,
    onSaveHolding: (String, String, String, Double, Long, Long?) -> Unit,
    onSaveLedger: (String, Long, String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var symbol by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var cost by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(if (kind == AddKind.INCOME) "Lương" else "Sinh hoạt") }
    var error by remember { mutableStateOf<String?>(null) }
    val holdingMode = kind == AddKind.STOCK || kind == AddKind.ASSET

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm ${kind.label.lowercase()}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (holdingMode) {
                    OutlinedTextField(name, { name = it }, label = { Text(if (kind == AddKind.STOCK) "Tên công ty / cổ phiếu" else "Tên tài sản") }, singleLine = true)
                    if (kind == AddKind.STOCK) OutlinedTextField(symbol, { symbol = it.uppercase() }, label = { Text("Mã cổ phiếu (VD: FPT)") }, singleLine = true)
                    OutlinedTextField(quantity, { quantity = it }, label = { Text("Số lượng") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), singleLine = true)
                    OutlinedTextField(cost, { cost = it }, label = { Text("Giá vốn / đơn vị (VND)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(price, { price = it }, label = { Text("Giá hiện tại / đơn vị (VND, tùy chọn)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                } else {
                    OutlinedTextField(name, { name = it }, label = { Text("Nội dung") }, singleLine = true)
                    OutlinedTextField(amount, { amount = it }, label = { Text("Số tiền (VND)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                    OutlinedTextField(category, { category = it }, label = { Text("Danh mục") }, singleLine = true)
                }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp) }
                Text("Dữ liệu được lưu trên thiết bị. Giá cổ phiếu chưa phải dữ liệu DNSE realtime.", color = Muted, fontSize = 11.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (holdingMode) {
                    val qty = quantity.replace(",", ".").toDoubleOrNull()
                    val avg = cost.toLongOrNull()
                    val current = price.takeIf { it.isNotBlank() }?.toLongOrNull()
                    when {
                        name.isBlank() -> error = "Vui lòng nhập tên."
                        kind == AddKind.STOCK && symbol.isBlank() -> error = "Vui lòng nhập mã cổ phiếu."
                        qty == null || qty <= 0.0 -> error = "Số lượng phải lớn hơn 0."
                        avg == null || avg < 0L -> error = "Giá vốn không hợp lệ."
                        price.isNotBlank() && (current == null || current < 0L) -> error = "Giá hiện tại không hợp lệ."
                        else -> onSaveHolding(name, symbol, if (kind == AddKind.STOCK) "STOCK" else "OTHER", qty, avg, current)
                    }
                } else {
                    val parsed = amount.toLongOrNull()
                    when {
                        name.isBlank() -> error = "Vui lòng nhập nội dung."
                        parsed == null || parsed <= 0L -> error = "Số tiền phải là số nguyên VND lớn hơn 0."
                        category.isBlank() -> error = "Vui lòng nhập danh mục."
                        else -> onSaveLedger(name, parsed, category, kind == AddKind.INCOME)
                    }
                }
            }) { Text("Lưu") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}
