package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.PendingEditEntity
import com.example.ui.theme.EmeraldGreen40
import com.example.ui.theme.SoftSurface
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusApprovedBg
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusRejected
import com.example.ui.theme.StatusRejectedBg
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SubmissionHistoryScreen(
    submissions: List<PendingEditEntity>,
    modifier: Modifier = Modifier
) {
    var statusFilter by remember { mutableStateOf<String?>(null) }

    val filteredList = submissions.filter {
        statusFilter == null || it.status == statusFilter
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("history_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SoftSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Color(0xFF0369A1),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Riwayat & Status Pengajuan",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            )
                            Text(
                                text = "Lacak status verifikasi data yang telah diajukan",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Filter chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = statusFilter == null,
                        onClick = { statusFilter = null },
                        label = { Text("Semua (${submissions.size})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldGreen40,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = statusFilter == "PENDING",
                        onClick = { statusFilter = "PENDING" },
                        label = { Text("Pending (${submissions.count { it.status == "PENDING" }})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StatusPending,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = statusFilter == "APPROVED",
                        onClick = { statusFilter = "APPROVED" },
                        label = { Text("Disetujui (${submissions.count { it.status == "APPROVED" }})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StatusApproved,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                item {
                    FilterChip(
                        selected = statusFilter == "REJECTED",
                        onClick = { statusFilter = "REJECTED" },
                        label = { Text("Ditolak (${submissions.count { it.status == "REJECTED" }})") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StatusRejected,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SoftSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tidak ada riwayat pengajuan dengan filter ini.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                HistoryItemCard(item = item)
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: PendingEditEntity) {
    val dateCreated = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        .format(Date(item.createdAt))

    val statusBadgeColor = when (item.status) {
        "APPROVED" -> StatusApproved
        "REJECTED" -> StatusRejected
        else -> StatusPending
    }

    val statusBadgeBg = when (item.status) {
        "APPROVED" -> StatusApprovedBg
        "REJECTED" -> StatusRejectedBg
        else -> StatusPendingBg
    }

    val statusText = when (item.status) {
        "APPROVED" -> "Disetujui (Approved)"
        "REJECTED" -> "Ditolak (Rejected)"
        else -> "Menunggu (Pending)"
    }

    val statusIcon = when (item.status) {
        "APPROVED" -> Icons.Default.CheckCircle
        "REJECTED" -> Icons.Default.Cancel
        else -> Icons.Default.HourglassEmpty
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SoftSurface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (item.memberId > 0) item.originalMemberName else item.proposedFullName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusBadgeBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusBadgeColor,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusText,
                            color = statusBadgeColor,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Oleh: ${item.submittedByUserName} • $dateCreated",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Reason
            Text(
                text = "Alasan: \"${item.changeReason}\"",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF334155),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )

            // Admin response box if reviewed
            if (item.adminNote != null) {
                Spacer(modifier = Modifier.height(10.dp))
                val reviewDate = item.reviewedAt?.let {
                    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(Date(it))
                } ?: ""

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (item.status == "REJECTED") Color(0xFFFEF2F2) else Color(0xFFF0FDF4))
                        .border(
                            1.dp,
                            if (item.status == "REJECTED") Color(0xFFFECACA) else Color(0xFFBBF7D0),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Catatan Admin (${item.reviewedByAdminName ?: "Admin"} • $reviewDate):",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (item.status == "REJECTED") Color(0xFF991B1B) else Color(0xFF166534)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.adminNote,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (item.status == "REJECTED") Color(0xFFB91C1C) else Color(0xFF15803D)
                            )
                        )
                    }
                }
            }
        }
    }
}
