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
import androidx.compose.material.icons.filled.PersonAdd
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
fun AddMemberDialog(
    currentRole: AppRole,
    existingMembers: List<FamilyMemberEntity>,
    onDismiss: () -> Unit,
    onAdminAddDirect: (FamilyMemberEntity) -> Unit,
    onUserProposeNew: (
        fullName: String,
        nickname: String,
        gender: String,
        birthPlace: String,
        birthDate: String,
        address: String,
        phone: String,
        occupation: String,
        maritalStatus: String,
        familyRole: String,
        generation: Int,
        parentId: Long?,
        spouseName: String?,
        bloodType: String,
        notes: String,
        reason: String
    ) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("L") }
    var birthPlace by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var occupation by remember { mutableStateOf("") }
    var maritalStatus by remember { mutableStateOf("Belum Menikah") }
    var familyRole by remember { mutableStateOf("Cucu") }
    var generation by remember { mutableIntStateOf(3) }
    var spouseName by remember { mutableStateOf("") }
    var bloodType by remember { mutableStateOf("-") }
    var notes by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentRole == AppRole.ADMIN) "Tambah Anggota (Admin)" else "Ajukan Anggota Baru",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "Keluarga Besar Bapak Ramsiah & Ibu Fatmah",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (currentRole == AppRole.USER) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = GoldenAmber40,
                                modifier = Modifier.size(16.dp).padding(top = 1.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Data anggota baru ini akan dikirim ke antrean persetujuan Admin terlebih dahulu.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = Color(0xFF78350F))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Nama Lengkap & Gelar *") },
                    modifier = Modifier.fillMaxWidth().testTag("add_input_fullname"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("Panggilan") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bloodType,
                        onValueChange = { bloodType = it },
                        label = { Text("Gol. Darah") },
                        modifier = Modifier.weight(0.7f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Gender Selector
                Text(text = "Jenis Kelamin:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = gender == "L", onClick = { gender = "L" })
                        Text("Laki-laki")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = gender == "P", onClick = { gender = "P" })
                        Text("Perempuan")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                    label = { Text("Alamat Domisili *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Nomor HP / WA") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = occupation,
                        onValueChange = { occupation = it },
                        label = { Text("Profesi / Status") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = familyRole,
                        onValueChange = { familyRole = it },
                        label = { Text("Peran Keluarga (e.g. Cucu)") },
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

                // Generation Selector
                Text(text = "Tingkat Generasi:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(1 to "Gen 1 (Sesepuh)", 2 to "Gen 2 (Anak)", 3 to "Gen 3 (Cucu)", 4 to "Gen 4 (Cicit)").forEach { (genNum, _) ->
                        Surface(
                            onClick = { generation = genNum },
                            shape = RoundedCornerShape(8.dp),
                            color = if (generation == genNum) EmeraldGreen40 else Color(0xFFF1F5F9),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Gen $genNum",
                                    color = if (generation == genNum) Color.White else Color(0xFF475569),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Catatan / Keterangan") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (currentRole == AppRole.USER) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text("Alasan Pengusulan Anggota Baru *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
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
                            if (fullName.isBlank() || address.isBlank()) {
                                errorMessage = "Nama lengkap dan alamat domisili wajib diisi."
                                return@Button
                            }
                            if (currentRole == AppRole.ADMIN) {
                                val newMember = FamilyMemberEntity(
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
                                    parentId = 1L, // Default link to family root
                                    spouseName = spouseName.trim().ifEmpty { null },
                                    bloodType = bloodType.trim(),
                                    notes = notes.trim()
                                )
                                onAdminAddDirect(newMember)
                            } else {
                                onUserProposeNew(
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
                                    1L,
                                    spouseName,
                                    bloodType,
                                    notes,
                                    reason
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("btn_submit_add_member"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (currentRole == AppRole.ADMIN) EmeraldGreen40 else GoldenAmber40
                        )
                    ) {
                        Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (currentRole == AppRole.ADMIN) "Simpan Anggota" else "Ajukan Usulan",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
