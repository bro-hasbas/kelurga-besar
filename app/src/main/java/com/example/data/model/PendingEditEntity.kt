package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_edits")
data class PendingEditEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memberId: Long, // ID anggota keluarga yang diubah (atau 0 jika anggota baru)
    val originalMemberName: String, // Nama saat diajukan untuk kemudahan tampilan list
    val submittedByUserId: String,
    val submittedByUserName: String,
    val submissionType: String = "EDIT_DATA", // "EDIT_DATA" atau "TAMBAH_ANGGOTA"
    
    // Field-field data usulan (staging table)
    val proposedFullName: String,
    val proposedNickname: String = "",
    val proposedGender: String,
    val proposedBirthPlace: String,
    val proposedBirthDate: String,
    val proposedAddress: String,
    val proposedPhone: String = "",
    val proposedOccupation: String = "",
    val proposedMaritalStatus: String = "Menikah",
    val proposedFamilyRole: String,
    val proposedGeneration: Int,
    val proposedParentId: Long? = null,
    val proposedSpouseName: String? = null,
    val proposedBloodType: String = "-",
    val proposedIsAlive: Boolean = true,
    val proposedNotes: String = "",

    val changeReason: String, // Alasan perubahan dari User
    val status: String = "PENDING", // "PENDING", "APPROVED", "REJECTED"
    val adminNote: String? = null, // Catatan penolakan/persetujuan dari Admin
    val reviewedByAdminName: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val reviewedAt: Long? = null
)
