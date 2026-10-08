package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PendingEditEntity
import com.example.ui.theme.SoftSurface
import com.example.ui.theme.StatusRejected

@Composable
fun RejectReasonDialog(
    pendingEdit: PendingEditEntity,
    onDismiss: () -> Unit,
    onConfirmReject: (reason: String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp)),
            color = SoftSurface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Tolak Pengajuan Perubahan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = StatusRejected
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Pengajuan oleh: ${pendingEdit.submittedByUserName} untuk '${pendingEdit.originalMemberName}'",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = reason,
                    onValueChange = {
                        reason = it
                        isError = false
                    },
                    label = { Text("Alasan Penolakan (Wajib) *") },
                    placeholder = { Text("Contoh: Format nomor telepon keliru, harap konfirmasi alamat ke orang tua dahulu.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_reject_reason"),
                    minLines = 3,
                    isError = isError
                )

                if (isError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Harap cantumkan alasan penolakan agar user memahami koreksi yang dibutuhkan.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }

                    Button(
                        onClick = {
                            if (reason.isBlank()) {
                                isError = true
                                return@Button
                            }
                            onConfirmReject(reason.trim())
                        },
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("btn_confirm_reject"),
                        colors = ButtonDefaults.buttonColors(containerColor = StatusRejected)
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tolak Pengajuan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
