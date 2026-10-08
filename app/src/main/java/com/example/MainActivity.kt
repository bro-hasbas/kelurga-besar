package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schema
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.FamilyMemberEntity
import com.example.ui.AppRole
import com.example.ui.FamilyViewModel
import com.example.ui.components.FamilyTreeGraph
import com.example.ui.screens.AddMemberDialog
import com.example.ui.screens.ApprovalDashboardScreen
import com.example.ui.screens.DirectoryScreen
import com.example.ui.screens.EditSubmissionDialog
import com.example.ui.screens.MemberDetailDialog
import com.example.ui.screens.SubmissionHistoryScreen
import com.example.ui.screens.SystemArchitectureScreen
import com.example.ui.theme.EmeraldGreen40
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.GoldenAmber40
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FamilyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: FamilyViewModel) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentUserName by viewModel.currentUserName.collectAsStateWithLifecycle()
    val allMembers by viewModel.allMembers.collectAsStateWithLifecycle()
    val filteredMembers by viewModel.filteredMembers.collectAsStateWithLifecycle()
    val activePendingEdits by viewModel.activePendingEdits.collectAsStateWithLifecycle()
    val allPendingHistory by viewModel.allPendingHistory.collectAsStateWithLifecycle()
    val pendingCount by viewModel.pendingCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedGenFilter by viewModel.selectedGenerationFilter.collectAsStateWithLifecycle()
    val snackbarMsg by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    // Dialogs
    val detailMember by viewModel.detailMember.collectAsStateWithLifecycle()
    val editTargetMember by viewModel.editTargetMember.collectAsStateWithLifecycle()
    val isAddMemberOpen by viewModel.isAddMemberDialogOpen.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var selectedNavTab by remember { mutableIntStateOf(0) }
    var showMenuDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Keluarga Ramsiah-Fatmah",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = currentUserName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFD1FAE5),
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                actions = {
                    // Role Toggle Button
                    Surface(
                        onClick = { viewModel.toggleRole() },
                        shape = RoundedCornerShape(20.dp),
                        color = if (currentRole == AppRole.ADMIN) Color(0xFFD4EEDC) else Color(0xFFFEF3C7),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .testTag("role_switcher_chip")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (currentRole == AppRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                contentDescription = null,
                                tint = if (currentRole == AppRole.ADMIN) EmeraldGreen40 else GoldenAmber40,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentRole == AppRole.ADMIN) "Admin" else "User",
                                color = if (currentRole == AppRole.ADMIN) EmeraldGreen40 else Color(0xFF92400E),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Ganti Role",
                                tint = Color.Gray,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Overflow Menu
                    IconButton(onClick = { showMenuDropdown = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }

                    DropdownMenu(
                        expanded = showMenuDropdown,
                        onDismissRequest = { showMenuDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Mode: Administrator") },
                            leadingIcon = {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = EmeraldGreen40)
                            },
                            onClick = {
                                viewModel.setRole(AppRole.ADMIN)
                                showMenuDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mode: Anggota (User)") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = GoldenAmber40)
                            },
                            onClick = {
                                viewModel.setRole(AppRole.USER)
                                showMenuDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Reset ke Data Bawaan") },
                            leadingIcon = {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                            },
                            onClick = {
                                viewModel.resetToDefaultData()
                                showMenuDropdown = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldGreenDark
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                // 1. Direktori
                NavigationBarItem(
                    selected = selectedNavTab == 0,
                    onClick = { selectedNavTab = 0 },
                    icon = { Icon(Icons.Default.People, contentDescription = "Direktori") },
                    label = { Text("Direktori", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldGreen40,
                        indicatorColor = Color(0xFFD4EEDC)
                    ),
                    modifier = Modifier.testTag("tab_directory")
                )

                // 2. Silsilah Pohon
                NavigationBarItem(
                    selected = selectedNavTab == 1,
                    onClick = { selectedNavTab = 1 },
                    icon = { Icon(Icons.Default.AccountTree, contentDescription = "Silsilah") },
                    label = { Text("Silsilah", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldGreen40,
                        indicatorColor = Color(0xFFD4EEDC)
                    ),
                    modifier = Modifier.testTag("tab_tree")
                )

                // 3. Persetujuan (Approval) with pending badge
                NavigationBarItem(
                    selected = selectedNavTab == 2,
                    onClick = { selectedNavTab = 2 },
                    icon = {
                        if (pendingCount > 0) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = Color(0xFFDC2626),
                                        contentColor = Color.White
                                    ) {
                                        Text("$pendingCount")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.FactCheck, contentDescription = "Persetujuan")
                            }
                        } else {
                            Icon(Icons.Default.FactCheck, contentDescription = "Persetujuan")
                        }
                    },
                    label = { Text("Approval", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldGreen40,
                        indicatorColor = Color(0xFFD4EEDC)
                    ),
                    modifier = Modifier.testTag("tab_approval")
                )

                // 4. Riwayat Pengajuan
                NavigationBarItem(
                    selected = selectedNavTab == 3,
                    onClick = { selectedNavTab = 3 },
                    icon = { Icon(Icons.Default.History, contentDescription = "Riwayat") },
                    label = { Text("Riwayat", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldGreen40,
                        indicatorColor = Color(0xFFD4EEDC)
                    ),
                    modifier = Modifier.testTag("tab_history")
                )

                // 5. Skema & Arsitektur
                NavigationBarItem(
                    selected = selectedNavTab == 4,
                    onClick = { selectedNavTab = 4 },
                    icon = { Icon(Icons.Default.Schema, contentDescription = "Skema") },
                    label = { Text("Skema SQL", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EmeraldGreen40,
                        indicatorColor = Color(0xFFD4EEDC)
                    ),
                    modifier = Modifier.testTag("tab_schema")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedNavTab) {
                0 -> DirectoryScreen(
                    members = filteredMembers,
                    totalCount = allMembers.size,
                    searchQuery = searchQuery,
                    onSearchChange = { viewModel.updateSearchQuery(it) },
                    selectedGeneration = selectedGenFilter,
                    onGenerationSelect = { viewModel.setGenerationFilter(it) },
                    currentRole = currentRole,
                    onMemberClick = { viewModel.openMemberDetail(it) },
                    onEditClick = { viewModel.openEditDialog(it) },
                    onAddMemberClick = { viewModel.openAddMemberDialog() }
                )

                1 -> FamilyTreeGraph(
                    members = allMembers,
                    onMemberClick = { viewModel.openMemberDetail(it) }
                )

                2 -> ApprovalDashboardScreen(
                    currentRole = currentRole,
                    pendingEdits = activePendingEdits,
                    allMembers = allMembers,
                    onApprove = { id, note -> viewModel.approveProposal(id, note) },
                    onReject = { id, reason -> viewModel.rejectProposal(id, reason) },
                    onSwitchRoleToAdmin = { viewModel.setRole(AppRole.ADMIN) }
                )

                3 -> SubmissionHistoryScreen(
                    submissions = allPendingHistory
                )

                4 -> SystemArchitectureScreen()
            }
        }
    }

    // Modal: Member Detail
    detailMember?.let { member ->
        MemberDetailDialog(
            member = member,
            currentRole = currentRole,
            onDismiss = { viewModel.closeMemberDetail() },
            onEditClick = {
                viewModel.closeMemberDetail()
                viewModel.openEditDialog(member)
            },
            onDeleteClick = if (currentRole == AppRole.ADMIN) {
                { viewModel.adminDeleteMember(member) }
            } else null
        )
    }

    // Modal: Edit Submission
    editTargetMember?.let { member ->
        EditSubmissionDialog(
            member = member,
            currentRole = currentRole,
            onDismiss = { viewModel.closeEditDialog() },
            onSubmitProposal = { name, nick, gen, bp, bd, addr, ph, occ, mar, role, g, pid, sp, bt, al, n, reason ->
                viewModel.submitEditProposal(
                    member = member,
                    proposedFullName = name,
                    proposedNickname = nick,
                    proposedGender = gen,
                    proposedBirthPlace = bp,
                    proposedBirthDate = bd,
                    proposedAddress = addr,
                    proposedPhone = ph,
                    proposedOccupation = occ,
                    proposedMaritalStatus = mar,
                    proposedFamilyRole = role,
                    proposedGeneration = g,
                    proposedParentId = pid,
                    proposedSpouseName = sp,
                    proposedBloodType = bt,
                    proposedIsAlive = al,
                    proposedNotes = n,
                    changeReason = reason
                )
            },
            onAdminDirectSave = if (currentRole == AppRole.ADMIN) {
                { updated ->
                    viewModel.adminUpdateMemberDirect(updated)
                }
            } else null
        )
    }

    // Modal: Add Member
    if (isAddMemberOpen) {
        AddMemberDialog(
            currentRole = currentRole,
            existingMembers = allMembers,
            onDismiss = { viewModel.closeAddMemberDialog() },
            onAdminAddDirect = { viewModel.adminAddMemberDirect(it) },
            onUserProposeNew = { name, nick, gen, bp, bd, addr, ph, occ, mar, role, g, pid, sp, bt, n, reason ->
                viewModel.submitNewMemberProposal(
                    fullName = name,
                    nickname = nick,
                    gender = gen,
                    birthPlace = bp,
                    birthDate = bd,
                    address = addr,
                    phone = ph,
                    occupation = occ,
                    maritalStatus = mar,
                    familyRole = role,
                    generation = g,
                    parentId = pid,
                    spouseName = sp,
                    bloodType = bt,
                    notes = n,
                    reason = reason
                )
            }
        )
    }
}

