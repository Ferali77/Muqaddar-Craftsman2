package com.example.ui

import android.widget.Toast
import com.example.data.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.*
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: CraftsmanViewModel = viewModel()

    NavHost(navController = navController, startDestination = "intro") {
        
        // --- 1. Onboarding / Splash Screen ---
        composable("intro") {
            IntroScreen(navController)
        }

        // --- 2. Dashboard Screen ---
        composable("dashboard") {
            DashboardScreen(navController, viewModel)
        }

        // --- 3. Clients List Screen ---
        composable("clients") {
            ClientsScreen(navController, viewModel)
        }

        // --- 4. Settings Screen ---
        composable("settings") {
            SettingsScreen(navController, viewModel)
        }

        // --- 5. Project Detail Screen ---
        composable(
            route = "project_detail/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
            ProjectDetailScreen(navController, viewModel, projectId)
        }

        // --- 6. Add Room Screen ---
        composable(
            route = "add_room/{projectId}",
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
            AddRoomScreen(navController, viewModel, projectId)
        }

        // --- 7. Room Detail / Openings Screen ---
        composable(
            route = "room_detail/{roomId}",
            arguments = listOf(navArgument("roomId") { type = NavType.LongType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getLong("roomId") ?: 0L
            RoomDetailScreen(navController, viewModel, roomId)
        }
    }
}

// ==========================================
// 1. Intro Screen
// ==========================================
@Composable
fun IntroScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, SlateBlue, HighDensityBlue)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated/Stylized Logo Icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Brush.radialGradient(colors = listOf(Color.White, HighDensityBlue.copy(alpha = 0.1f))))
                    .border(1.5.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(28.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Construction,
                    contentDescription = "Logo",
                    tint = HighDensityBlue,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "المقدّر الذكي للمقاولات",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "نظام التقدير والحساب الهندسي الفوري",
                color = SoftGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Value Props
            val props = listOf(
                Icons.Default.SquareFoot to "حساب دقيق لمساحات الجدران والأسقف والأرضيات",
                Icons.Default.RemoveCircleOutline to "الخصم التلقائي الذكي لفتحات الأبواب والنوافذ",
                Icons.Default.Analytics to "تقدير دقيق لكمية المواد (المعجون، الدهان، الصنفرة)",
                Icons.Default.PictureAsPdf to "توليد عروض أسعار PDF احترافية ومشاركتها فوراً"
            )

            props.forEach { (icon, text) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = SoftGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = text,
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Button(
            onClick = { navController.navigate("dashboard") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = HighDensityBlue),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .testTag("start_button"),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "ابدأ العمل الآن 🔨",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = HighDensityBlue
            )
        }
    }
}

// ==========================================
// 2. Dashboard Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: CraftsmanViewModel) {
    val projects by viewModel.allProjects.collectAsState()
    val customers by viewModel.allCustomers.collectAsState()
    val allRooms by viewModel.allRooms.collectAsState()
    
    var showAddProjectDialog by remember { mutableStateOf(false) }

    // Global live stats across all projects/rooms
    var totalEstimatedCost = 0.0
    var totalMeasuredArea = 0.0
    var totalWallsArea = 0.0
    var totalCeilingsArea = 0.0
    var totalPaintLiters = 0.0
    var totalPuttyKg = 0.0

    allRooms.forEach { room ->
        val perimeter = 2 * (room.length + room.width)
        val grossWall = perimeter * room.height
        val ceiling = if (room.hasCeiling) (room.length * room.width) else 0.0
        val flooring = if (room.hasFlooring) (room.length * room.width) else 0.0

        val wCost = grossWall * room.wallPricePerM2
        val cCost = ceiling * room.ceilingPricePerM2
        val fCost = flooring * room.flooringPricePerM2

        totalEstimatedCost += (wCost + cCost + fCost)
        totalMeasuredArea += (grossWall + ceiling + flooring)
        totalWallsArea += grossWall
        totalCeilingsArea += ceiling

        totalPuttyKg += (grossWall * room.puttyCoats * 1.2 * room.materialCostFactor)
        totalPaintLiters += (((grossWall * room.paintCoats / 10.0) + (ceiling * room.paintCoats / 10.0)) * room.materialCostFactor)
    }
    
    Scaffold(
        bottomBar = {
            HighDensityBottomNavigation(navController = navController, activeTab = "dashboard")
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddProjectDialog = true },
                containerColor = HighDensityBlue,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("مشروع جديد", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("add_project_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header: Project Context
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "مشروع نشط",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = HighDensityBlue,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "المقدّر الذكي للمقاولات",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    // Stylized V/M logo icon
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(HighDensityBlue.copy(alpha = 0.08f))
                            .border(1.dp, HighDensityBlue.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "V",
                            color = HighDensityBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                // Horizontal badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BadgeItem(text = "سكني / فيلا / شقة", bgColor = HighDensityBlue.copy(alpha = 0.1f), textColor = HighDensityBlue)
                    BadgeItem(text = "قيد الحساب", bgColor = Color(0xFFDCFCE7), textColor = Color(0xFF15803D))
                    BadgeItem(text = "العميل: أحمد العلوي", bgColor = SoftGold.copy(alpha = 0.15f), textColor = GoldAmber)
                }
            }

            // Main Content: Bento Statistics & List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary Metrics Bento
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: blue-600 container (HighDensityBlue)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(HighDensityBlue, RoundedCornerShape(24.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("التكلفة التقديرية", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${String.format(Locale.ENGLISH, "%,.0f", totalEstimatedCost)} DA",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("+12% هوامش", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Card 2: White background with subtle border
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text("المساحة الكلية", color = SlateGrey, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${String.format(Locale.ENGLISH, "%.0f", totalMeasuredArea)} م²",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("إجمالي غرف: ${allRooms.size}", color = HighDensityBlue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Secondary Breakdown Bento (3 columns)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BentoMiniCard(emoji = "🧱", label = "جدران", value = "${String.format(Locale.ENGLISH, "%.0f", totalWallsArea)}م²", modifier = Modifier.weight(1f))
                    BentoMiniCard(emoji = "☁️", label = "أسقف", value = "${String.format(Locale.ENGLISH, "%.0f", totalCeilingsArea)}م²", modifier = Modifier.weight(1f))
                    BentoMiniCard(emoji = "📦", label = "سلعة", value = "${String.format(Locale.ENGLISH, "%.0f", totalPuttyKg / 25.0 + totalPaintLiters / 15.0)} كيس/سطل", modifier = Modifier.weight(1f))
                }

                // List of Projects Container: Density Focused
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "المشاريع الحالية (${projects.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            TextButton(
                                onClick = { showAddProjectDialog = true },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("+ إضافة مشروع", color = HighDensityBlue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (projects.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.FolderOpen,
                                        contentDescription = null,
                                        tint = Color.Gray,
                                        modifier = Modifier.size(54.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "لا توجد مشاريع مضافة بعد.\nاضغط على 'إضافة مشروع' للبدء!",
                                        textAlign = TextAlign.Center,
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(projects) { project ->
                                    val customer = customers.find { it.id == project.customerId }
                                    ProjectItemCard(
                                        project = project,
                                        customerName = customer?.name ?: "عميل عام",
                                        onClick = {
                                            viewModel.selectProject(project.id)
                                            navController.navigate("project_detail/${project.id}")
                                        },
                                        onDelete = {
                                            viewModel.deleteProject(project.id)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddProjectDialog) {
        AddProjectDialog(
            customers = customers,
            onDismiss = { showAddProjectDialog = false },
            onConfirm = { name, type, custId, notes, discount, tax ->
                viewModel.addProject(name, type, custId, notes, discount, tax) { newId ->
                    viewModel.selectProject(newId)
                    navController.navigate("project_detail/$newId")
                }
                showAddProjectDialog = false
            }
        )
    }
}

@Composable
fun BadgeItem(text: String, bgColor: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun BentoMiniCard(emoji: String, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SlateGrey, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(1.dp))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun HighDensityBottomNavigation(navController: NavController, activeTab: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column {
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = "🏠",
                    label = "الرئيسية",
                    active = activeTab == "dashboard",
                    onClick = { if (activeTab != "dashboard") navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } } }
                )
                BottomNavItem(
                    icon = "📊",
                    label = "الإحصائيات",
                    active = activeTab == "dashboard", // Toggle stats or same dashboard since it has stats
                    onClick = { if (activeTab != "dashboard") navController.navigate("dashboard") }
                )
                BottomNavItem(
                    icon = "👥",
                    label = "العملاء",
                    active = activeTab == "clients",
                    onClick = { if (activeTab != "clients") navController.navigate("clients") }
                )
                BottomNavItem(
                    icon = "⚙️",
                    label = "المواد",
                    active = activeTab == "settings",
                    onClick = { if (activeTab != "settings") navController.navigate("settings") }
                )
            }
        }
    }
}

@Composable
fun RowScope.BottomNavItem(icon: String, label: String, active: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            color = if (active) HighDensityBlue else SlateGrey
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
            color = if (active) HighDensityBlue else SlateGrey
        )
    }
}


@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 11.sp, color = Color.Gray)
                Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun ProjectItemCard(project: Project, customerName: String, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("project_card_${project.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val spec = ProjectTypeDetails.getSpecForType(project.type)
            val typeIcon = spec.icon
            val badgeBg = spec.pastelBgColor
            val badgeIconColor = spec.iconColor

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = typeIcon, contentDescription = null, tint = badgeIconColor)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = project.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "العميل: $customerName", fontSize = 12.sp, color = Color.Gray)
                
                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (project.status) {
                                    "مكتمل" -> EmeraldSuccess.copy(alpha = 0.15f)
                                    "قيد العمل" -> GoldAmber.copy(alpha = 0.15f)
                                    else -> Color.Gray.copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = project.status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (project.status) {
                                "مكتمل" -> EmeraldSuccess
                                "قيد العمل" -> GoldAmber
                                else -> Color.Gray
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = project.type,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = CrimsonDeduction)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectDialog(
    customers: List<Customer>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, type: String, customerId: Long?, notes: String, discount: Double, tax: Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("0") }
    var tax by remember { mutableStateOf("15") }
    
    val projectTypes = ProjectTypeDetails.specs.map { it.typeNameAr }
    var selectedType by remember { mutableStateOf("شقة") }
    var typeDropdownExpanded by remember { mutableStateOf(false) }
    
    var selectedCustomerIndex by remember { mutableStateOf(0) } // 0 means "عام - بلا زبون"
    var customerDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إنشاء مشروع هندسي جديد", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم المشروع (مثلاً: فيلا الرياض)") },
                    modifier = Modifier.fillMaxWidth().testTag("project_name_input"),
                    singleLine = true
                )

                // Type selector
                val selectedSpec = ProjectTypeDetails.getSpecForType(selectedType)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = "${selectedSpec.typeNameAr} (${selectedSpec.typeNameFr})",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("نوع المنشأة") },
                        trailingIcon = { IconButton(onClick = { typeDropdownExpanded = true }) { Icon(Icons.Default.ArrowDropDown, null) } },
                        leadingIcon = { Icon(selectedSpec.icon, contentDescription = null, tint = selectedSpec.iconColor, modifier = Modifier.size(20.dp)) },
                        modifier = Modifier.fillMaxWidth().clickable { typeDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = typeDropdownExpanded,
                        onDismissRequest = { typeDropdownExpanded = false }
                    ) {
                        ProjectTypeDetails.specs.forEach { spec ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(spec.icon, contentDescription = null, tint = spec.iconColor, modifier = Modifier.size(20.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${spec.typeNameAr} (${spec.typeNameFr})", fontSize = 13.sp)
                                    }
                                },
                                onClick = {
                                    selectedType = spec.typeNameAr
                                    typeDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Customer selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    val labelText = if (selectedCustomerIndex == 0) "عميل مباشر (بلا زبون)" else customers[selectedCustomerIndex - 1].name
                    OutlinedTextField(
                        value = labelText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("الزبون / العميل المرتبط") },
                        trailingIcon = { IconButton(onClick = { customerDropdownExpanded = true }) { Icon(Icons.Default.ArrowDropDown, null) } },
                        modifier = Modifier.fillMaxWidth().clickable { customerDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = customerDropdownExpanded,
                        onDismissRequest = { customerDropdownExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("عميل مباشر (بلا زبون)") },
                            onClick = {
                                selectedCustomerIndex = 0
                                customerDropdownExpanded = false
                            }
                        )
                        customers.forEachIndexed { index, cust ->
                            DropdownMenuItem(
                                text = { Text(cust.name) },
                                onClick = {
                                    selectedCustomerIndex = index + 1
                                    customerDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = discount,
                        onValueChange = { discount = it },
                        label = { Text("الخصم %") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = tax,
                        onValueChange = { tax = it },
                        label = { Text("الضريبة %") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات إضافية") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        val custId = if (selectedCustomerIndex > 0) customers[selectedCustomerIndex - 1].id else null
                        onConfirm(
                            name,
                            selectedType,
                            custId,
                            notes,
                            discount.toDoubleOrNull() ?: 0.0,
                            tax.toDoubleOrNull() ?: 15.0
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber)
            ) {
                Text("تأكيد وإنشاء", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

// ==========================================
// 3. Clients Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientsScreen(navController: NavController, viewModel: CraftsmanViewModel) {
    val customers by viewModel.allCustomers.collectAsState()
    
    var showAddCustomerDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            HighDensityBottomNavigation(navController = navController, activeTab = "clients")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCustomerDialog = true },
                containerColor = HighDensityBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "زبون جديد")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                text = "قائمة الزبائن والعملاء النشطين",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            if (customers.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.PeopleOutline, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("قائمة عملائك فارغة.\nأضف عميلاً جديداً بالضغط على الزر بالأسفل.", color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(customers) { customer ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(HighDensityBlue.copy(alpha = 0.08f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = HighDensityBlue)
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(customer.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                                    if (customer.phone.isNotEmpty()) {
                                        Text("جوال: ${customer.phone}", fontSize = 13.sp, color = Color.Gray)
                                    }
                                    if (customer.address.isNotEmpty()) {
                                        Text("العنوان: ${customer.address}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                                IconButton(onClick = { viewModel.deleteCustomer(customer.id) }) {
                                    Icon(Icons.Default.Delete, "حذف", tint = CrimsonDeduction)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddCustomerDialog) {
        AddCustomerDialog(
            onDismiss = { showAddCustomerDialog = false },
            onConfirm = { name, phone, email, address, notes ->
                viewModel.addCustomer(name, phone, email, address, notes)
                showAddCustomerDialog = false
            }
        )
    }
}

@Composable
fun AddCustomerDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, email: String, address: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة زبون جديد", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("اسم العميل كامل") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الجوال") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("البريد الإلكتروني") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("العنوان السكني / التجاري") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات خاصة بالعميل") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty()) {
                        onConfirm(name, phone, email, address, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber)
            ) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

// ==========================================
// 4. Settings Screen
// ==========================================
@Composable
fun PastelIconBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(26.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: CraftsmanViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Labor Prices
    var wallPrice by remember { mutableStateOf("15.0") }
    var ceilingPrice by remember { mutableStateOf("18.0") }
    var flooringPrice by remember { mutableStateOf("25.0") }
    var plasterPrice by remember { mutableStateOf("12.0") }
    var gypsumPrice by remember { mutableStateOf("30.0") }

    // Material Coverage Standards
    var puttyCoverage by remember { mutableStateOf("1.2") }
    var paintCoverage by remember { mutableStateOf("10.0") }
    var sandpaperCoverage by remember { mutableStateOf("8.0") }
    var wasteRatio by remember { mutableStateOf("10.0") }

    // Contractor Info
    var contractorName by remember { mutableStateOf("المقدّر الذكي للمقاولات") }
    var contractorPhone by remember { mutableStateOf("") }
    var contractorAddress by remember { mutableStateOf("") }
    var contractorTaxId by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        wallPrice = viewModel.repository.getSetting("default_wall_price", "15.0")
        ceilingPrice = viewModel.repository.getSetting("default_ceiling_price", "18.0")
        flooringPrice = viewModel.repository.getSetting("default_flooring_price", "25.0")
        plasterPrice = viewModel.repository.getSetting("default_plaster_price", "12.0")
        gypsumPrice = viewModel.repository.getSetting("default_gypsum_price", "30.0")

        puttyCoverage = viewModel.repository.getSetting("default_putty_coverage", "1.2")
        paintCoverage = viewModel.repository.getSetting("default_paint_coverage", "10.0")
        sandpaperCoverage = viewModel.repository.getSetting("default_sandpaper_coverage", "8.0")
        wasteRatio = viewModel.repository.getSetting("default_waste_ratio", "10.0")

        contractorName = viewModel.repository.getSetting("contractor_name", "المقدّر الذكي للمقاولات")
        contractorPhone = viewModel.repository.getSetting("contractor_phone", "")
        contractorAddress = viewModel.repository.getSetting("contractor_address", "")
        contractorTaxId = viewModel.repository.getSetting("contractor_tax_id", "")
    }

    Scaffold(
        bottomBar = {
            HighDensityBottomNavigation(navController = navController, activeTab = "settings")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Screen Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "الإعدادات والأسعار الافتراضية",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "قم بضبط تفاصيل أسعار المصنعيات، نسب تغطية المواد، وهوية مقاولتك المهنية لحساب التكاليف بدقة وتوليد عروض أسعار متكاملة.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
            }

            // SECTION 1: Labor & Work default prices (Amber/Gold Pastel)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PastelIconBadge(
                            icon = Icons.Default.Brush,
                            backgroundColor = Color(0xFFFEF3C7),
                            iconColor = Color(0xFFD97706)
                        )
                        Column {
                            Text(
                                text = "مصنعيات اليد العاملة الافتراضية",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "الأسعار المرجعية الافتراضية للمتر المربع والطولي",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    OutlinedTextField(
                        value = wallPrice,
                        onValueChange = { wallPrice = it },
                        label = { Text("سعر دهان الجدران الافتراضي") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("د.ج / م²", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = ceilingPrice,
                        onValueChange = { ceilingPrice = it },
                        label = { Text("سعر دهان الأسقف الافتراضي") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("د.ج / م²", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = flooringPrice,
                        onValueChange = { flooringPrice = it },
                        label = { Text("سعر تركيب البلاط والأرضيات") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("د.ج / م²", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = plasterPrice,
                        onValueChange = { plasterPrice = it },
                        label = { Text("سعر تمليط وتهيئة الجدران (المرطوب)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("د.ج / م²", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = gypsumPrice,
                        onValueChange = { gypsumPrice = it },
                        label = { Text("سعر تركيب ديكورات الجبس بورد") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("د.ج / متر طولي", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // SECTION 2: Material Consumption & Estimates (Green Pastel)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PastelIconBadge(
                            icon = Icons.Default.Settings,
                            backgroundColor = Color(0xFFDCFCE7),
                            iconColor = Color(0xFF15803D)
                        )
                        Column {
                            Text(
                                text = "معايير استهلاك وتحليل المواد",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "النسب الحسابية لتقدير كميات المعجون والدهانات والمستلزمات",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    OutlinedTextField(
                        value = puttyCoverage,
                        onValueChange = { puttyCoverage = it },
                        label = { Text("معدل استهلاك معجون التأسيس (لكل طبقة/وجه)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("كجم / م²", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = paintCoverage,
                        onValueChange = { paintCoverage = it },
                        label = { Text("معدل تغطية دهان التشطيب النهائي (لكل طبقة)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("م² / لتر", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = sandpaperCoverage,
                        onValueChange = { sandpaperCoverage = it },
                        label = { Text("معدل استهلاك ورق الصنفرة والتنعيم") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("م² / ورقة", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = wasteRatio,
                        onValueChange = { wasteRatio = it },
                        label = { Text("نسبة هدر المواد واحتياط الطوارئ العام") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        trailingIcon = { Text("% نسبة", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(end = 12.dp)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // SECTION 3: Contractor Professional Profile (Blue Pastel)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PastelIconBadge(
                            icon = Icons.Default.Person,
                            backgroundColor = Color(0xFFE0F2FE),
                            iconColor = Color(0xFF0369A1)
                        )
                        Column {
                            Text(
                                text = "بيانات المقاول والشركاء",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "تدرج هذه الهوية المهنية تلقائياً في تقارير PDF للعميل",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    OutlinedTextField(
                        value = contractorName,
                        onValueChange = { contractorName = it },
                        label = { Text("اسم المقاول أو المؤسسة التجارية") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = contractorPhone,
                        onValueChange = { contractorPhone = it },
                        label = { Text("رقم الهاتف المهني والاتصال") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = contractorAddress,
                        onValueChange = { contractorAddress = it },
                        label = { Text("عنوان المقر أو مكتب العمل") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = contractorTaxId,
                        onValueChange = { contractorTaxId = it },
                        label = { Text("رقم السجل التجاري أو المعرف الجبائي (اختياري)") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Save Settings Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.repository.saveSetting("default_wall_price", wallPrice)
                        viewModel.repository.saveSetting("default_ceiling_price", ceilingPrice)
                        viewModel.repository.saveSetting("default_flooring_price", flooringPrice)
                        viewModel.repository.saveSetting("default_plaster_price", plasterPrice)
                        viewModel.repository.saveSetting("default_gypsum_price", gypsumPrice)

                        viewModel.repository.saveSetting("default_putty_coverage", puttyCoverage)
                        viewModel.repository.saveSetting("default_paint_coverage", paintCoverage)
                        viewModel.repository.saveSetting("default_sandpaper_coverage", sandpaperCoverage)
                        viewModel.repository.saveSetting("default_waste_ratio", wasteRatio)

                        viewModel.repository.saveSetting("contractor_name", contractorName)
                        viewModel.repository.saveSetting("contractor_phone", contractorPhone)
                        viewModel.repository.saveSetting("contractor_address", contractorAddress)
                        viewModel.repository.saveSetting("contractor_tax_id", contractorTaxId)

                        Toast.makeText(context, "تم حفظ الإعدادات بنجاح!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = HighDensityBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("حفظ الأسعار والإعدادات ومزامنتها", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// ==========================================
// 5. Project Detail Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    viewModel: CraftsmanViewModel,
    projectId: Long
) {
    val context = LocalContext.current
    val project by viewModel.currentProject.collectAsState()
    val rooms by viewModel.roomsForSelectedProject.collectAsState()
    val openingsMap by viewModel.openingsMapForProject.collectAsState()
    val tasks by viewModel.tasksForSelectedProject.collectAsState()
    
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("قيد الانتظار") }
    var statusDropdownExpanded by remember { mutableStateOf(false) }

    var defaultPuttyCoverage by remember { mutableStateOf(1.2) }
    var defaultPaintCoverage by remember { mutableStateOf(10.0) }
    var defaultSandpaperCoverage by remember { mutableStateOf(8.0) }
    var defaultWasteRatio by remember { mutableStateOf(10.0) }

    val projectParamsState = remember { mutableStateMapOf<String, Float>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(projectId) {
        viewModel.selectProject(projectId)
    }

    LaunchedEffect(Unit) {
        defaultPuttyCoverage = viewModel.repository.getSetting("default_putty_coverage", "1.2").toDoubleOrNull() ?: 1.2
        defaultPaintCoverage = viewModel.repository.getSetting("default_paint_coverage", "10.0").toDoubleOrNull() ?: 10.0
        defaultSandpaperCoverage = viewModel.repository.getSetting("default_sandpaper_coverage", "8.0").toDoubleOrNull() ?: 8.0
        defaultWasteRatio = viewModel.repository.getSetting("default_waste_ratio", "10.0").toDoubleOrNull() ?: 10.0
    }

    LaunchedEffect(project?.id) {
        val proj = project
        if (proj != null) {
            val typeSpec = ProjectTypeDetails.getSpecForType(proj.type)
            typeSpec.specificParameters.forEach { param ->
                val key = "proj_${proj.id}_${param.key}"
                val dbVal = viewModel.repository.getSetting(key, param.defaultValue.toString())
                projectParamsState[param.key] = dbVal.toFloatOrNull() ?: param.defaultValue
            }
        }
    }

    LaunchedEffect(project) {
        project?.let {
            selectedStatus = it.status
        }
    }

    if (project == null) {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    val currentProj = project!!

    // Global Project Calculations
    var totalGrossWallArea = 0.0
    var totalNetWallArea = 0.0
    var totalCeilingArea = 0.0
    var totalFlooringArea = 0.0
    var totalOpeningsArea = 0.0

    var wallCost = 0.0
    var ceilingCost = 0.0
    var flooringCost = 0.0

    rooms.forEach { room ->
        val perimeter = 2 * (room.length + room.width)
        val grossWall = perimeter * room.height
        val ceiling = if (room.hasCeiling) (room.length * room.width) else 0.0
        val flooring = if (room.hasFlooring) (room.length * room.width) else 0.0
        
        val ops = openingsMap[room.id] ?: emptyList()
        val opsArea = ops.sumOf { it.width * it.height * it.quantity }
        val netWall = maxOf(0.0, grossWall - opsArea)

        totalGrossWallArea += grossWall
        totalNetWallArea += netWall
        totalCeilingArea += ceiling
        totalFlooringArea += flooring
        totalOpeningsArea += opsArea

        wallCost += netWall * room.wallPricePerM2
        ceilingCost += ceiling * room.ceilingPricePerM2
        flooringCost += flooring * room.flooringPricePerM2
    }

    val typeSpec = ProjectTypeDetails.getSpecForType(currentProj.type)
    val adaptedResult = CalculationRules.calculateAdaptedRules(
        type = currentProj.type,
        params = projectParamsState,
        rooms = rooms,
        openingsMap = openingsMap,
        baseSubtotal = wallCost + ceilingCost + flooringCost
    )

    val difficultyMultiplier = adaptedResult.difficultyMultiplier
    val materialMultiplier = adaptedResult.materialMultiplier

    val adaptedWallCost = wallCost * difficultyMultiplier
    val adaptedCeilingCost = ceilingCost * difficultyMultiplier
    val adaptedFlooringCost = flooringCost * difficultyMultiplier

    val extraTasksCost = adaptedResult.extraTasks.sumOf { it.total }
    val tasksCost = tasks.sumOf { it.unitPrice * it.quantity }
    
    val subtotal = adaptedWallCost + adaptedCeilingCost + adaptedFlooringCost + tasksCost + extraTasksCost
    val discountAmt = subtotal * (currentProj.discount / 100.0)
    val afterDiscount = subtotal - discountAmt
    val vatAmt = afterDiscount * (currentProj.taxRate / 100.0)
    val finalTotal = afterDiscount + vatAmt

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentProj.name, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                    }
                },
                actions = {
                    // Export PDF action
                    IconButton(onClick = { viewModel.exportAndSharePdf(context, currentProj) }) {
                        Icon(Icons.Default.Share, "مشاركة PDF", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SlateBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Project Status Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("الحالة التشغيلية للمشروع:", fontSize = 12.sp, color = Color.Gray)
                        Text(currentProj.status, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Box {
                        Button(
                            onClick = { statusDropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = HighDensityBlue),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("تغيير الحالة")
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                        DropdownMenu(
                            expanded = statusDropdownExpanded,
                            onDismissRequest = { statusDropdownExpanded = false }
                        ) {
                            listOf("قيد الانتظار", "قيد العمل", "مكتمل").forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status) },
                                    onClick = {
                                        viewModel.updateProjectStatus(currentProj.id, status)
                                        statusDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // --- Architectural Building Dashboard Card ---
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Header Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(typeSpec.pastelBgColor)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.8f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = typeSpec.icon,
                                    contentDescription = null,
                                    tint = typeSpec.iconColor,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = typeSpec.typeNameAr,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = typeSpec.iconColor
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(${typeSpec.typeNameFr})",
                                        fontSize = 12.sp,
                                        color = typeSpec.iconColor.copy(alpha = 0.7f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "بوابة المعايير والخصائص الهندسية للمنشأة",
                                    fontSize = 11.sp,
                                    color = typeSpec.iconColor.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Description
                        Text(
                            text = typeSpec.description,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )

                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                        // Characteristics section
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "الخصائص والاشتراطات الفنية للمبنى:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            typeSpec.characteristics.forEach { characteristic ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = characteristic,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        if (typeSpec.specificParameters.isNotEmpty()) {
                            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                            // Parameters section
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "المعايير الهندسية القابلة للتخصيص:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                typeSpec.specificParameters.forEach { param ->
                                    val currentVal = projectParamsState[param.key] ?: param.defaultValue
                                    
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f))
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = param.labelAr,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 12.sp,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = param.labelFr,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            when (param.type) {
                                                ParamType.TOGGLE -> {
                                                    Switch(
                                                        checked = currentVal == 1.0f,
                                                        onCheckedChange = { isChecked ->
                                                            val newVal = if (isChecked) 1.0f else 0.0f
                                                            projectParamsState[param.key] = newVal
                                                            coroutineScope.launch {
                                                                viewModel.repository.saveSetting(
                                                                    "proj_${currentProj.id}_${param.key}",
                                                                    newVal.toString()
                                                                )
                                                            }
                                                        },
                                                        colors = SwitchDefaults.colors(
                                                            checkedThumbColor = typeSpec.iconColor,
                                                            checkedTrackColor = typeSpec.pastelBgColor
                                                        )
                                                    )
                                                }
                                                ParamType.NUMBER -> {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        IconButton(
                                                            onClick = {
                                                                val newVal = maxOf(param.minVal, currentVal - 1f)
                                                                projectParamsState[param.key] = newVal
                                                                coroutineScope.launch {
                                                                    viewModel.repository.saveSetting(
                                                                        "proj_${currentProj.id}_${param.key}",
                                                                        newVal.toString()
                                                                    )
                                                                }
                                                            },
                                                            modifier = Modifier
                                                                .size(32.dp)
                                                                .background(
                                                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                                                    RoundedCornerShape(6.dp)
                                                                )
                                                        ) {
                                                            Text("-", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                        }

                                                        Text(
                                                            text = "${currentVal.toInt()} ${param.valueSuffixAr}",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 12.sp,
                                                            color = MaterialTheme.colorScheme.onSurface
                                                        )

                                                        IconButton(
                                                            onClick = {
                                                                val newVal = minOf(param.maxVal, currentVal + 1f)
                                                                projectParamsState[param.key] = newVal
                                                                coroutineScope.launch {
                                                                    viewModel.repository.saveSetting(
                                                                        "proj_${currentProj.id}_${param.key}",
                                                                        newVal.toString()
                                                                    )
                                                                }
                                                            },
                                                            modifier = Modifier
                                                                .size(32.dp)
                                                                .background(
                                                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                                                    RoundedCornerShape(6.dp)
                                                                )
                                                        ) {
                                                            Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                        }
                                                    }
                                                }
                                                ParamType.SLIDER -> {
                                                    val formattedValue = if (param.valueSuffixAr.isNotEmpty()) {
                                                        "${currentVal} ${param.valueSuffixAr}"
                                                    } else {
                                                        String.format(Locale.ENGLISH, "%.1f", currentVal)
                                                    }
                                                    Text(
                                                        text = formattedValue,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 12.sp,
                                                        color = typeSpec.iconColor
                                                    )
                                                }
                                            }
                                        }

                                        if (param.type == ParamType.SLIDER) {
                                            Slider(
                                                value = currentVal,
                                                onValueChange = { newVal ->
                                                    val rounded = Math.round(newVal * 10f) / 10f
                                                    projectParamsState[param.key] = rounded
                                                },
                                                onValueChangeFinished = {
                                                    val valToSave = projectParamsState[param.key] ?: param.defaultValue
                                                    coroutineScope.launch {
                                                        viewModel.repository.saveSetting(
                                                            "proj_${currentProj.id}_${param.key}",
                                                            valToSave.toString()
                                                        )
                                                    }
                                                },
                                                valueRange = param.minVal..param.maxVal,
                                                steps = if (param.maxVal - param.minVal <= 5) (param.maxVal - param.minVal).toInt() - 1 else 0,
                                                colors = SliderDefaults.colors(
                                                    thumbColor = typeSpec.iconColor,
                                                    activeTrackColor = typeSpec.iconColor,
                                                    inactiveTrackColor = typeSpec.pastelBgColor
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Calculation Sheet Card
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("صحيفة الحساب الإجمالية وعرض السعر:", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    CalculationRow(label = "مساحة الجدران الإجمالية:", value = "${String.format(Locale.ENGLISH, "%.1f", totalGrossWallArea)} م²")
                    CalculationRow(label = "الفتحات المخصومة:", value = "- ${String.format(Locale.ENGLISH, "%.1f", totalOpeningsArea)} م²", valueColor = CrimsonDeduction)
                    CalculationRow(label = "صافي الجدران للدهان:", value = "${String.format(Locale.ENGLISH, "%.1f", totalNetWallArea)} م²")
                    
                    if (totalCeilingArea > 0) {
                        CalculationRow(label = "مساحة الأسقف الإجمالية:", value = "${String.format(Locale.ENGLISH, "%.1f", totalCeilingArea)} م²")
                    }
                    if (totalFlooringArea > 0) {
                        CalculationRow(label = "مساحة الأرضية الإجمالية:", value = "${String.format(Locale.ENGLISH, "%.1f", totalFlooringArea)} م²")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Base Cost breakdown
                    val baseLaborSubtotal = wallCost + ceilingCost + flooringCost
                    CalculationRow(label = "تكلفة المصنعية الأساسية للغرف:", value = "${String.format(Locale.ENGLISH, "%.2f", baseLaborSubtotal)} د.ج")
                    
                    if (difficultyMultiplier != 1.0) {
                        val percentStr = String.format(Locale.ENGLISH, "%.0f%%", (difficultyMultiplier - 1.0) * 100)
                        val multiplierLabel = if (difficultyMultiplier > 1.0) "معامل الصعوبة الخاص للمبنى (+${percentStr}):" else "خصم صعوبة العمل للمبنى (${percentStr}):"
                        CalculationRow(
                            label = multiplierLabel,
                            value = "×${String.format(Locale.ENGLISH, "%.2f", difficultyMultiplier)}",
                            valueColor = SoftGold
                        )
                    }

                    if (tasksCost > 0) {
                        CalculationRow(label = "تكلفة المهام الإضافية العادية:", value = "${String.format(Locale.ENGLISH, "%.2f", tasksCost)} د.ج")
                    }

                    // Specialty tasks
                    if (adaptedResult.extraTasks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("المهام التخصصية الذكية المضافة:", color = SoftGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        adaptedResult.extraTasks.forEach { extraTask ->
                            CalculationRow(
                                label = "• ${extraTask.title} (${String.format(Locale.ENGLISH, "%.1f", extraTask.quantity)} ${extraTask.unit}):",
                                value = "${String.format(Locale.ENGLISH, "%.2f", extraTask.total)} د.ج"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    CalculationRow(label = "المجموع الفرعي المكيّف:", value = "${String.format(Locale.ENGLISH, "%.2f", subtotal)} د.ج")
                    if (currentProj.discount > 0) {
                        CalculationRow(label = "الخصم (${currentProj.discount}%):", value = "- ${String.format(Locale.ENGLISH, "%.2f", discountAmt)} د.ج", valueColor = SoftGold)
                    }
                    CalculationRow(label = "ضريبة القيمة المضافة (${currentProj.taxRate}%):", value = "+ ${String.format(Locale.ENGLISH, "%.2f", vatAmt)} د.ج")
                    
                    // Explanation bullet points
                    if (adaptedResult.explanationLines.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "تفسير الحسابات والتحليل المعماري:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = SoftGold
                                )
                                adaptedResult.explanationLines.forEach { line ->
                                    Text(
                                        text = line,
                                        fontSize = 10.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldSuccess)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("السعر الإجمالي النهائي المستحق", color = Color.White, fontSize = 11.sp)
                            Text("${String.format(Locale.ENGLISH, "%.2f", finalTotal)} د.ج", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Material Estimate Box
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("المواد المطلوبة التقديرية (تحليل تلقائي):", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    var totalPuttyCoatsWeight = 0.0
                    var totalPaintLiters = 0.0
                    val wasteMultiplier = 1.0 + (defaultWasteRatio / 100.0)

                    rooms.forEach { room ->
                        val perimeter = 2 * (room.length + room.width)
                        val grossWall = perimeter * room.height
                        val ops = openingsMap[room.id] ?: emptyList()
                        val opsArea = ops.sumOf { it.width * it.height * it.quantity }
                        val netWall = maxOf(0.0, grossWall - opsArea)
                        
                        totalPuttyCoatsWeight += netWall * room.puttyCoats * defaultPuttyCoverage * room.materialCostFactor * wasteMultiplier * materialMultiplier
                        val wallPaintLiters = (netWall * room.paintCoats) / defaultPaintCoverage
                        val ceilingPaintLiters = if (room.hasCeiling) {
                            ((room.length * room.width) * room.paintCoats) / defaultPaintCoverage
                        } else 0.0
                        totalPaintLiters += (wallPaintLiters + ceilingPaintLiters) * room.materialCostFactor * wasteMultiplier * materialMultiplier
                    }
                    val puttyBags = Math.ceil(totalPuttyCoatsWeight / 25.0).toInt()
                    val paintDrums18L = Math.ceil(totalPaintLiters / 18.0).toInt()
                    val sandpaperSheets = Math.ceil((((totalNetWallArea + totalCeilingArea) / defaultSandpaperCoverage) * wasteMultiplier * materialMultiplier)).toInt()

                    Text("• المعجون: ${String.format(Locale.ENGLISH, "%.1f", totalPuttyCoatsWeight)} كجم (حوالي $puttyBags كيس - 25 كجم)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    Text("• الدهان: ${String.format(Locale.ENGLISH, "%.1f", totalPaintLiters)} لتر (حوالي $paintDrums18L برميل - 18 لتر)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    Text("• ورق الصنفرة: $sandpaperSheets ورقة تقديرياً", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                }
            }

            // --- Section: Rooms List ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("المساحات والغرف (${rooms.size}):", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                Button(
                    onClick = { navController.navigate("add_room/${currentProj.id}") },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_room_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إضافة غرفة", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            if (rooms.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.MeetingRoom, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("لا توجد غرف مضافة في هذا المشروع بعد.", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            } else {
                rooms.forEach { room ->
                    val ops = openingsMap[room.id] ?: emptyList()
                    val opsArea = ops.sumOf { it.width * it.height * it.quantity }
                    val perimeter = 2 * (room.length + room.width)
                    val grossWall = perimeter * room.height
                    val netWall = maxOf(0.0, grossWall - opsArea)
                    val ceiling = if (room.hasCeiling) (room.length * room.width) else 0.0
                    val flooring = if (room.hasFlooring) (room.length * room.width) else 0.0
                    val roomCost = (netWall * room.wallPricePerM2) + (ceiling * room.ceilingPricePerM2) + (flooring * room.flooringPricePerM2)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("room_detail/${room.id}") },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(room.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                                Row {
                                    IconButton(onClick = { viewModel.deleteRoom(room.id) }) {
                                        Icon(Icons.Default.Delete, "حذف الغرفة", tint = CrimsonDeduction)
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("أبعاد الغرفة: ${room.length}م طول × ${room.width}م عرض × ${room.height}م ارتفاع", fontSize = 12.sp, color = Color.Gray)
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text("الجدران (صافي): ${String.format(Locale.ENGLISH, "%.1f", netWall)} م²", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                if (room.hasCeiling) {
                                    Text("السقف: ${String.format(Locale.ENGLISH, "%.1f", ceiling)} م²", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                }
                                if (room.hasFlooring) {
                                    Text("الأرضية: ${String.format(Locale.ENGLISH, "%.1f", flooring)} م²", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GoldAmber.copy(alpha = 0.12f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("الفتحات المخصومة: ${ops.size} فتحة (${String.format(Locale.ENGLISH, "%.1f", opsArea)}م²)", fontSize = 11.sp, color = GoldAmber, fontWeight = FontWeight.Medium)
                                }
                                Text("${String.format(Locale.ENGLISH, "%.2f", roomCost)} د.ج", fontWeight = FontWeight.Bold, color = EmeraldSuccess, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // --- Section: Custom Tasks ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("أعمال ومصنعيات مخصصة:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                Button(
                    onClick = { showAddTaskDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = HighDensityBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("عمل مخصص", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            if (tasks.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("لا توجد أعمال مخصصة مضافة حالياً (مثل جبس بورد، أعمال فنية...)", color = Color.Gray, fontSize = 11.sp)
                    }
                }
            } else {
                tasks.forEach { task ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(task.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text("الكمية: ${task.quantity} ${task.unit} | سعر الوحدة: ${task.unitPrice} د.ج", fontSize = 12.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${String.format(Locale.ENGLISH, "%.2f", task.quantity * task.unitPrice)} د.ج", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                                IconButton(onClick = { viewModel.deleteWorkTask(task.id) }) {
                                    Icon(Icons.Default.Delete, null, tint = CrimsonDeduction)
                                }
                            }
                        }
                    }
                }
            }

            // Big generate button at bottom
            Button(
                onClick = { viewModel.exportAndSharePdf(context, currentProj) },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("generate_pdf_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("توليد ومشاركة عرض السعر PDF", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }

    if (showAddTaskDialog) {
        AddWorkTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { title, unit, price, qty ->
                viewModel.addWorkTask(projectId, title, unit, price, qty)
                showAddTaskDialog = false
            }
        )
    }
}

@Composable
fun CalculationRow(label: String, value: String, valueColor: Color = Color.White) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun AddWorkTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, unit: String, price: Double, quantity: Double) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    
    val units = listOf("متر مربع", "متر طولي", "قطعة", "سعر ثابت")
    var selectedUnit by remember { mutableStateOf(units.first()) }
    var unitDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة عمل مخصص أو مصنعية إضافية", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("بيان العمل (مثلاً: جبس بورد ممر)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedUnit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("وحدة قياس العمل") },
                        trailingIcon = { IconButton(onClick = { unitDropdownExpanded = true }) { Icon(Icons.Default.ArrowDropDown, null) } },
                        modifier = Modifier.fillMaxWidth().clickable { unitDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = unitDropdownExpanded,
                        onDismissRequest = { unitDropdownExpanded = false }
                    ) {
                        units.forEach { u ->
                            DropdownMenuItem(
                                text = { Text(u) },
                                onClick = {
                                    selectedUnit = u
                                    unitDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("سعر الوحدة (د.ج)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("الكمية") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val pDouble = price.toDoubleOrNull() ?: 0.0
                    val qDouble = quantity.toDoubleOrNull() ?: 1.0
                    if (title.isNotEmpty() && pDouble > 0) {
                        onConfirm(title, selectedUnit, pDouble, qDouble)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber)
            ) {
                Text("إضافة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

// ==========================================
// 6. Add Room Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomScreen(
    navController: NavController,
    viewModel: CraftsmanViewModel,
    projectId: Long
) {
    var name by remember { mutableStateOf("") }
    var lengthStr by remember { mutableStateOf("4.0") }
    var widthStr by remember { mutableStateOf("4.0") }
    var heightStr by remember { mutableStateOf("3.0") }
    
    var hasCeiling by remember { mutableStateOf(true) }
    var hasFlooring by remember { mutableStateOf(true) }
    
    var paintCoats by remember { mutableStateOf(2) }
    var puttyCoats by remember { mutableStateOf(2) }
    var sandingRequired by remember { mutableStateOf(true) }

    // Override default prices
    var wallPrice by remember { mutableStateOf("15.0") }
    var ceilingPrice by remember { mutableStateOf("18.0") }
    var flooringPrice by remember { mutableStateOf("25.0") }
    var materialFactor by remember { mutableStateOf("1.0") }

    var defaultPuttyCoverage by remember { mutableStateOf("1.2") }
    var defaultPaintCoverage by remember { mutableStateOf("10.0") }
    var defaultSandpaperCoverage by remember { mutableStateOf("8.0") }
    var defaultWasteRatio by remember { mutableStateOf("10.0") }

    LaunchedEffect(Unit) {
        wallPrice = viewModel.repository.getSetting("default_wall_price", "15.0")
        ceilingPrice = viewModel.repository.getSetting("default_ceiling_price", "18.0")
        flooringPrice = viewModel.repository.getSetting("default_flooring_price", "25.0")

        defaultPuttyCoverage = viewModel.repository.getSetting("default_putty_coverage", "1.2")
        defaultPaintCoverage = viewModel.repository.getSetting("default_paint_coverage", "10.0")
        defaultSandpaperCoverage = viewModel.repository.getSetting("default_sandpaper_coverage", "8.0")
        defaultWasteRatio = viewModel.repository.getSetting("default_waste_ratio", "10.0")
    }

    // Interactive Realtime Calculations as they type
    val length = lengthStr.toDoubleOrNull() ?: 0.0
    val width = widthStr.toDoubleOrNull() ?: 0.0
    val height = heightStr.toDoubleOrNull() ?: 0.0
    
    val perimeter = 2 * (length + width)
    val grossWallArea = perimeter * height
    val ceilingArea = if (hasCeiling) (length * width) else 0.0
    val flooringArea = if (hasFlooring) (length * width) else 0.0
    
    val pCoverage = defaultPuttyCoverage.toDoubleOrNull() ?: 1.2
    val ptCoverage = defaultPaintCoverage.toDoubleOrNull() ?: 10.0
    val wRatio = 1.0 + ((defaultWasteRatio.toDoubleOrNull() ?: 10.0) / 100.0)

    val puttyCoatsWeight = grossWallArea * puttyCoats * pCoverage * (materialFactor.toDoubleOrNull() ?: 1.0) * wRatio
    val paintLiters = ((grossWallArea * paintCoats / ptCoverage) + (ceilingArea * paintCoats / ptCoverage)) * (materialFactor.toDoubleOrNull() ?: 1.0) * wRatio
    
    val wCost = grossWallArea * (wallPrice.toDoubleOrNull() ?: 15.0)
    val cCost = ceilingArea * (ceilingPrice.toDoubleOrNull() ?: 18.0)
    val fCost = flooringArea * (flooringPrice.toDoubleOrNull() ?: 25.0)
    val estimatedRoomTotal = wCost + cCost + fCost

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("إضافة غرفة وقياسات جديدة", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SlateBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("تفاصيل وأبعاد المساحة الغرفة:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("اسم الغرفة (مثلاً: المجلس الرجالي، المطبخ)") },
                        modifier = Modifier.fillMaxWidth().testTag("room_name_input"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = lengthStr,
                            onValueChange = { lengthStr = it },
                            label = { Text("الطول (م)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("length_input")
                        )
                        OutlinedTextField(
                            value = widthStr,
                            onValueChange = { widthStr = it },
                            label = { Text("العرض (م)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("width_input")
                        )
                        OutlinedTextField(
                            value = heightStr,
                            onValueChange = { heightStr = it },
                            label = { Text("الارتفاع (م)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("height_input")
                        )
                    }
                }
            }

            // Calculation options
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("خيارات وجوانب العمل:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = hasCeiling, onCheckedChange = { hasCeiling = it })
                        Text("حساب السقف للمصنعية والدهان")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = hasFlooring, onCheckedChange = { hasFlooring = it })
                        Text("حساب الأرضية للتبليط / السيراميك")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = sandingRequired, onCheckedChange = { sandingRequired = it })
                        Text("صنفرة يدوية / ميكانيكية للجدران")
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("عدد طبقات المعالجة (الوجه):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("عدد وجوه معجون التأسيس:")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (puttyCoats > 0) puttyCoats-- }) { Icon(Icons.Default.Remove, null) }
                            Text("$puttyCoats", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { if (puttyCoats < 5) puttyCoats++ }) { Icon(Icons.Default.Add, null) }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("عدد وجوه الدهان النهائي:")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (paintCoats > 1) paintCoats-- }) { Icon(Icons.Default.Remove, null) }
                            Text("$paintCoats", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { if (paintCoats < 5) paintCoats++ }) { Icon(Icons.Default.Add, null) }
                        }
                    }
                }
            }

            // Pricing Override Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("أسعار الغرفة الخاصة (لتجاوز الافتراضيات):", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                    
                    OutlinedTextField(
                        value = wallPrice,
                        onValueChange = { wallPrice = it },
                        label = { Text("سعر جدران الغرفة / م²") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = ceilingPrice,
                            onValueChange = { ceilingPrice = it },
                            label = { Text("سعر سقف الغرفة / م²") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = flooringPrice,
                            onValueChange = { flooringPrice = it },
                            label = { Text("سعر أرضية الغرفة / م²") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Real-time calculation helper (5 seconds comprehension)
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateGrey),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("المعاينة الفورية للحسابات (قبل الحفظ):", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("مساحة الجدران الإجمالية:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("${String.format(Locale.ENGLISH, "%.1f", grossWallArea)} م²", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    if (hasCeiling) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("مساحة السقف:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                            Text("${String.format(Locale.ENGLISH, "%.1f", ceilingArea)} م²", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("كمية معجون التأسيس المطلوبة:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("${String.format(Locale.ENGLISH, "%.1f", puttyCoatsWeight)} كجم", color = SoftGold, fontWeight = FontWeight.Bold)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("كمية الدهان المقدرة للغرفة:", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                        Text("${String.format(Locale.ENGLISH, "%.1f", paintLiters)} لتر", color = SoftGold, fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("التكلفة التقديرية للغرفة:", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("${String.format(Locale.ENGLISH, "%.2f", estimatedRoomTotal)} د.ج", color = SoftGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Button(
                onClick = {
                    if (name.isNotEmpty() && length > 0 && width > 0 && height > 0) {
                        viewModel.addRoom(
                            projectId = projectId,
                            name = name,
                            length = length,
                            width = width,
                            height = height,
                            hasCeiling = hasCeiling,
                            hasFlooring = hasFlooring,
                            paintCoats = paintCoats,
                            puttyCoats = puttyCoats,
                            sandingRequired = sandingRequired,
                            wallPrice = wallPrice.toDoubleOrNull() ?: 15.0,
                            ceilingPrice = ceilingPrice.toDoubleOrNull() ?: 18.0,
                            flooringPrice = flooringPrice.toDoubleOrNull() ?: 25.0,
                            materialFactor = materialFactor.toDoubleOrNull() ?: 1.0
                        ) { roomId ->
                            // Automatically go to Room details to let them add openings easily!
                            navController.navigate("room_detail/$roomId") {
                                popUpTo("add_room/$projectId") { inclusive = true }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_room_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("حفظ وتحديد الفتحات المخصومة 🚪", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

// ==========================================
// 7. Room Detail & Openings Screen
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailScreen(
    navController: NavController,
    viewModel: CraftsmanViewModel,
    roomId: Long
) {
    var room by remember { mutableStateOf<RoomEntity?>(null) }
    val openings by viewModel.repository.getOpeningsForRoomFlow(roomId).collectAsState(initial = emptyList())
    var showCustomOpeningDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(roomId) {
        room = viewModel.repository.getRoomById(roomId)
    }

    if (room == null) {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        return
    }

    val r = room!!

    // Calculations
    val perimeter = 2 * (r.length + r.width)
    val grossWallArea = perimeter * r.height
    val totalOpeningsArea = openings.sumOf { it.width * it.height * it.quantity }
    val netWallArea = maxOf(0.0, grossWallArea - totalOpeningsArea)
    val ceilingArea = if (r.hasCeiling) (r.length * r.width) else 0.0
    val flooringArea = if (r.hasFlooring) (r.length * r.width) else 0.0
    
    val puttyCoatsWeight = netWallArea * r.puttyCoats * 1.2 * r.materialCostFactor
    val paintLiters = ((netWallArea * r.paintCoats / 10.0) + (ceilingArea * r.paintCoats / 10.0)) * r.materialCostFactor
    
    val roomCost = (netWallArea * r.wallPricePerM2) + (ceilingArea * r.ceilingPricePerM2) + (flooringArea * r.flooringPricePerM2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(r.name, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SlateBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Room Specs & Math Card
            Card(
                colors = CardDefaults.cardColors(containerColor = SlateGrey),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("تفاصيل وحسابات هندسية لـ: ${r.name}", color = SoftGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    CalculationRow(label = "مساحة الجدران الكلية:", value = "${String.format(Locale.ENGLISH, "%.1f", grossWallArea)} م²")
                    CalculationRow(label = "مجموع مساحات الفتحات المخصومة:", value = "- ${String.format(Locale.ENGLISH, "%.1f", totalOpeningsArea)} م²", valueColor = CrimsonDeduction)
                    CalculationRow(label = "صافي مساحة دهان الجدران:", value = "${String.format(Locale.ENGLISH, "%.1f", netWallArea)} م²")
                    
                    if (r.hasCeiling) {
                        CalculationRow(label = "مساحة دهان السقف:", value = "${String.format(Locale.ENGLISH, "%.1f", ceilingArea)} م²")
                    }
                    if (r.hasFlooring) {
                        CalculationRow(label = "مساحة بلاط الأرضية:", value = "${String.format(Locale.ENGLISH, "%.1f", flooringArea)} م²")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(8.dp))

                    CalculationRow(label = "المعجون المقدر (أكياس تأسيس):", value = "${String.format(Locale.ENGLISH, "%.1f", puttyCoatsWeight)} كجم (~ ${Math.ceil(puttyCoatsWeight/25.0).toInt()} كيس)")
                    CalculationRow(label = "الدهان المقدر:", value = "${String.format(Locale.ENGLISH, "%.1f", paintLiters)} لتر")
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldSuccess)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("تكلفة الغرفة التقديرية: ${String.format(Locale.ENGLISH, "%.2f", roomCost)} د.ج", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            // --- Section: Add Openings ---
            Text("إضافة فتحة مخصومة (أبواب ونوافذ):", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            
            // Fast selection grid for standard openings (UX target: 5-seconds)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("انقر على أي فتحة لإضافتها وخصمها فوراً:", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                    
                    val stdOpenings = listOf(
                        Triple("باب داخلي 🚪", 1.0, 2.2),
                        Triple("باب شرفة", 1.2, 2.2),
                        Triple("نافذة قياسية 🪟", 1.2, 1.2),
                        Triple("نافذة كبيرة", 1.8, 1.5),
                        Triple("نافذة صغيرة (مطبخ/حمام)", 0.6, 0.6),
                        Triple("فتحة مكيف جداري ❄️", 0.8, 0.6),
                        Triple("باب كراج خارجي", 2.8, 2.4),
                        Triple("واجهة زجاجية", 2.5, 2.5)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        stdOpenings.chunked(2).forEach { rowList ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowList.forEach { (title, w, h) ->
                                    Button(
                                        onClick = {
                                            viewModel.addOpening(roomId, title, w, h, 1)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = HighDensityBlue, contentColor = Color.White),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).testTag("add_opening_btn")
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text("(${w}م × ${h}م)", fontSize = 9.sp, color = SoftGold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- Custom Opening dialog triggers ---
            Button(
                onClick = { showCustomOpeningDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = HighDensityBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("إضافة فتحة بأبعاد مخصصة", fontWeight = FontWeight.Bold)
            }

            // --- Current openings list ---
            Text("الفتحات المخصومة الحالية:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            
            if (openings.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("لا توجد فتحات مخصومة مضافة حالياً. الجدران مصمتة كلياً.", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            } else {
                openings.forEach { op ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(op.type, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                Text("الأبعاد: ${op.width}م عرض × ${op.height}م ارتفاع | مساحة الخصم: ${String.format(Locale.ENGLISH, "%.2f", op.width * op.height)} م²", fontSize = 12.sp, color = Color.Gray)
                            }
                            IconButton(onClick = { viewModel.deleteOpening(op.id) }) {
                                Icon(Icons.Default.Delete, "حذف", tint = CrimsonDeduction)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("تأكيد وحفظ الغرفة والعودة للمشروع", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }

    if (showCustomOpeningDialog) {
        AddCustomOpeningDialog(
            onDismiss = { showCustomOpeningDialog = false },
            onConfirm = { title, w, h, qty ->
                viewModel.addOpening(roomId, title, w, h, qty)
                showCustomOpeningDialog = false
            }
        )
    }
}

@Composable
fun AddCustomOpeningDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, width: Double, height: Double, qty: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var widthStr by remember { mutableStateOf("") }
    var heightStr by remember { mutableStateOf("") }
    var qtyStr by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة فتحة خصم مخصصة", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("نوع الفتحة المخصصة (مثلاً: فتحة بلاكار)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = widthStr,
                        onValueChange = { widthStr = it },
                        label = { Text("العرض (م)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = heightStr,
                        onValueChange = { heightStr = it },
                        label = { Text("الارتفاع (م)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = qtyStr,
                    onValueChange = { qtyStr = it },
                    label = { Text("الكمية") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val w = widthStr.toDoubleOrNull() ?: 0.0
                    val h = heightStr.toDoubleOrNull() ?: 0.0
                    val q = qtyStr.toIntOrNull() ?: 1
                    if (title.isNotEmpty() && w > 0 && h > 0) {
                        onConfirm(title, w, h, q)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAmber)
            ) {
                Text("إضافة الفتحة")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
