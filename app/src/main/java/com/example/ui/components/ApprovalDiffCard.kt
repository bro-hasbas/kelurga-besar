package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiffCalculator
import com.example.data.model.DiffItem
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.PendingEditEntity
import com.example.ui.theme.EmeraldGreen40
import com.example.ui.theme.SoftSurface
import com.example.ui.theme.StatusApproved
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusPendingBg
import com.example.ui.theme.StatusRejected
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ApprovalDiffCard(
    pendingEdit: PendingEditEntity,
    originalMember: FamilyMemberEntity?,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val diffList = DiffCalculator.calculateDiff(originalMember, pendingEdit)
    val changedFields = diffList.filter { it.hasChanged }

    val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        .format(Date(pendingEdit.createdAt))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("approval_card_${pendingEdit.id}"),
        colors = CardDefaults.cardColors(containerColor = SoftSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header: Submission type & pending badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (pendingEdit.submissionType == "TAMBAH_ANGGOTA") Color(0xFFEFF6FF) else Color(0xFFF3E8FF)
                ) {
                    Text(
                        text = if (pendingEdit.submissionType == "TAMBAH_ANGGOTA") "USULAN ANGGOTA BARU" else "PERUBAHAN DATA",
                        color = if (pendingEdit.submissionType == "TAMBAH_ANGGOTA") Color(0xFF1D4ED8) else Color(0xFF7E22CE),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StatusPendingBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = StatusPending,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Menunggu Tinjauan",
                            color = StatusPending,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Member target name
            Text(
                text = if (pendingEdit.memberId > 0) pendingEdit.originalMemberName else pendingEdit.proposedFullName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Submitter info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Diajukan oleh: ${pendingEdit.submittedByUserName} • $dateStr",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Submission reason box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Alasan Pembaruan / Catatan User:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF334155)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = pendingEdit.changeReason,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF475569)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comparison Diff View
            Text(
                text = "Perbandingan Data (${changedFields.size} field berubah):",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFDFBF7))
                    .border(1.dp, Color(0xFFEFE8DB), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (changedFields.isEmpty()) {
                    Text(
                        text = "Tidak ada perubahan spesifik yang terdeteksi.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(4.dp)
                    )
                } else {
                    changedFields.forEachIndexed { index, diff ->
                        DiffRow(diff = diff)
                        if (index < changedFields.size - 1) {
                            HorizontalDivider(color = Color(0xFFEFE8DB), thickness = 0.5.dp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Approval Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onReject,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_reject_${pendingEdit.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = StatusRejected
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Tolak (Reject)", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onApprove,
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("btn_approve_${pendingEdit.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StatusApproved,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Setujui (Approve)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DiffRow(diff: DiffItem) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = diff.fieldName,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF64748B)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Old value
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFFEE2E2))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = diff.oldValue,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF991B1B),
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(14.dp)
            )

            // New proposed value
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFDCFCE7))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = diff.newValue,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF166534),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
