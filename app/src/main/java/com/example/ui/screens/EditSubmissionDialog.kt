package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FamilyMemberEntity
import com.example.ui.AppRole
import com.example.ui.theme.EmeraldGreen40
import com.example.ui.theme.GoldenAmber40
import com.example.ui.theme.SoftSurface

@Composable
fun EditSubmissionDialog(
    member: FamilyMemberEntity,
    currentRole: AppRole,
    onDismiss: () -> Unit,
    onSubmitProposal: (
        proposedFullName: String,
        proposedNickname: String,
        proposedGender: String,
        proposedBirthPlace: String,
        proposedBirthDate: String,
        proposedAddress: String,
        proposedPhone: String,
        proposedOccupation: String,
        proposedMaritalStatus: String,
        proposedFamilyRole: String,
        proposedGeneration: Int,
        proposedParentId: Long?,
        proposedSpouseName: String?,
        proposedBloodType: String,
        proposedIsAlive: Boolean,
        proposedNotes: String,
        changeReason: String
    ) -> Unit,
    onAdminDirectSave: ((FamilyMemberEntity) -> Unit)? = null
) {
    var fullName by remember { mutableStateOf(member.fullName) }
    var nickname by remember { mutableStateOf(member.nickname) }
    var gender by remember { mutableStateOf(member.gender) }
    var birthPlace by remember { mutableStateOf(member.birthPlace) }
    var birthDate by remember { mutableStateOf(member.birthDate) }
    var address by remember { mutableStateOf(member.address) }
    var phone by remember { mutableStateOf(member.phone) }
    var occupation by remember { mutableStateOf(member.occupation) }
    var maritalStatus by remember { mutableStateOf(member.maritalStatus) }
    var familyRole by remember { mutableStateOf(member.familyRole) }
    var generation by remember { mutableIntStateOf(member.generation) }
    var spouseName by remember { mutableStateOf(member.spouseName ?: "") }
    var bloodType by remember { mutableStateOf(member.bloodType) }
    var notes by remember { mutableStateOf(member.notes) }
    var changeReason by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp)),
            color = SoftSurface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentRole == AppRole.ADMIN) "Edit Data (Administrator)" else "Ajukan Perubahan Data",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "Target: ${member.fullName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_edit_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Information banner about Approval Workflow
                if (currentRole == AppRole.USER) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = GoldenAmber40,
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mekanisme Approval: Data yang Anda simpan akan masuk ke antrean 'Pending Approval' dan memerlukan persetujuan Admin sebelum tampil di menu publik.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Form Fields
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nama Lengkap & Gelar *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_fullname"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("Nama Panggilan") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_nickname"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = bloodType,
                        onValueChange = { bloodType = it },
                        label = { Text("Gol. Darah") },
                        modifier = Modifier
                            .weight(0.7f)
                            .testTag("input_blood"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Gender Selector
                Text(
                    text = "Jenis Kelamin:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "L",
                            onClick = { gender = "L" },
                            modifier = Modifier.testTag("radio_male")
                        )
                        Text("Laki-laki", style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "P",
                            onClick = { gender = "P" },
                            modifier = Modifier.testTag("radio_female")
                        )
                        Text("Perempuan", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = birthPlace,
                        onValueChange = { birthPlace = it },
                        label = { Text("Tempat Lahir") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        label = { Text("Tanggal Lahir") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Alamat Domisili Lengkap *") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_address"),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Nomor HP / WA") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_phone"),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = occupation,
                        onValueChange = { occupation = it },
                        label = { Text("Pekerjaan / Profesi") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = familyRole,
                        onValueChange = { familyRole = it },
                        label = { Text("Posisi / Peran Keluarga") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = spouseName,
                        onValueChange = { spouseName = it },
                        label = { Text("Nama Pasangan") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan / Keterangan Lainnya") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Reason for change (Mandatory for User, optional for Admin)
                OutlinedTextField(
                    value = changeReason,
                    onValueChange = {
                        changeReason = it
                        errorMessage = null
                    },
                    label = { Text(if (currentRole == AppRole.USER) "Alasan Perubahan Data (Wajib) *" else "Catatan Riwayat Perubahan") },
                    placeholder = { Text("Contoh: Pindah alamat ke Bandung, ganti nomor telepon, penambahan gelar sarjana.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_change_reason"),
                    minLines = 2,
                    isError = errorMessage != null
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
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

                    // If Admin, provide Direct Save option
                    if (currentRole == AppRole.ADMIN && onAdminDirectSave != null) {
                        Button(
                            onClick = {
                                if (fullName.isBlank() || address.isBlank()) {
                                    errorMessage = "Nama lengkap dan alamat wajib diisi."
                                    return@Button
                                }
                                val updated = member.copy(
                                    fullName = fullName.trim(),
                                    nickname = nickname.trim(),
                                    gender = gender,
                                    birthPlace = birthPlace.trim(),
                                    birthDate = birthDate.trim(),
                                    address = address.trim(),
                                    phone = phone.trim(),
                                    occupation = occupation.trim(),
                                    maritalStatus = maritalStatus,
                                    familyRole = familyRole.trim(),
                                    generation = generation,
                                    spouseName = spouseName.trim().ifEmpty { null },
                                    bloodType = bloodType.trim(),
                                    notes = notes.trim(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                onAdminDirectSave(updated)
                            },
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("btn_admin_direct_save"),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen40)
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simpan Langsung", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // User submits to Pending Approval
                        Button(
                            onClick = {
                                if (fullName.isBlank() || address.isBlank()) {
                                    errorMessage = "Nama lengkap dan alamat wajib diisi."
                                    return@Button
                                }
                                if (changeReason.isBlank()) {
                                    errorMessage = "Harap sertakan alasan perubahan data agar Admin dapat memverifikasi."
                                    return@Button
                                }
                                onSubmitProposal(
                                    fullName,
                                    nickname,
                                    gender,
                                    birthPlace,
                                    birthDate,
                                    address,
                                    phone,
                                    occupation,
                                    maritalStatus,
                                    familyRole,
                                    generation,
                                    member.parentId,
                                    spouseName,
                                    bloodType,
                                    member.isAlive,
                                    notes,
                                    changeReason
                                )
                            },
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("btn_submit_proposal"),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldenAmber40)
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kirim Pengajuan", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
