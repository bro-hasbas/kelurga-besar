package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val nickname: String = "",
    val gender: String, // "L" (Laki-laki) or "P" (Perempuan)
    val birthPlace: String,
    val birthDate: String, // e.g. "12 Agustus 1975"
    val address: String,
    val phone: String = "",
    val occupation: String = "",
    val maritalStatus: String = "Menikah", // "Menikah", "Belum Menikah", "Wafat", "Cerai"
    val familyRole: String, // "Kepala Keluarga Besar", "Ibu Sesepuh", "Anak ke-1", "Anak ke-2", "Cucu", "Cicit", "Menantu"
    val generation: Int, // 1 (Sesepuh), 2 (Anak), 3 (Cucu), 4 (Cicit)
    val parentId: Long? = null,
    val spouseName: String? = null,
    val bloodType: String = "-", // "A", "B", "AB", "O", "-"
    val isAlive: Boolean = true,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
