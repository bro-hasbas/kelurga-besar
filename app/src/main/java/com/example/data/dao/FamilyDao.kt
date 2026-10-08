package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.PendingEditEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyDao {

    // --- Family Members (Tabel Utama) ---
    @Query("SELECT * FROM family_members ORDER BY generation ASC, id ASC")
    fun getAllMembers(): Flow<List<FamilyMemberEntity>>

    @Query("SELECT * FROM family_members WHERE id = :id LIMIT 1")
    suspend fun getMemberById(id: Long): FamilyMemberEntity?

    @Query("SELECT * FROM family_members WHERE generation = :gen ORDER BY id ASC")
    fun getMembersByGeneration(gen: Int): Flow<List<FamilyMemberEntity>>

    @Query("SELECT * FROM family_members WHERE fullName LIKE '%' || :query || '%' OR nickname LIKE '%' || :query || '%' OR address LIKE '%' || :query || '%'")
    fun searchMembers(query: String): Flow<List<FamilyMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: FamilyMemberEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<FamilyMemberEntity>)

    @Update
    suspend fun updateMember(member: FamilyMemberEntity)

    @Delete
    suspend fun deleteMember(member: FamilyMemberEntity)

    @Query("DELETE FROM family_members WHERE id = :id")
    suspend fun deleteMemberById(id: Long)

    @Query("SELECT COUNT(*) FROM family_members")
    suspend fun getMemberCount(): Int

    // --- Pending Edits (Tabel Staging Persetujuan) ---
    @Query("SELECT * FROM pending_edits ORDER BY createdAt DESC")
    fun getAllPendingEdits(): Flow<List<PendingEditEntity>>

    @Query("SELECT * FROM pending_edits WHERE status = 'PENDING' ORDER BY createdAt ASC")
    fun getActivePendingEdits(): Flow<List<PendingEditEntity>>

    @Query("SELECT COUNT(*) FROM pending_edits WHERE status = 'PENDING'")
    fun getActivePendingCount(): Flow<Int>

    @Query("SELECT * FROM pending_edits WHERE submittedByUserId = :userId ORDER BY createdAt DESC")
    fun getSubmissionsByUser(userId: String): Flow<List<PendingEditEntity>>

    @Query("SELECT * FROM pending_edits WHERE id = :id LIMIT 1")
    suspend fun getPendingEditById(id: Long): PendingEditEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingEdit(pendingEdit: PendingEditEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingEdits(pendingEdits: List<PendingEditEntity>)

    @Update
    suspend fun updatePendingEdit(pendingEdit: PendingEditEntity)

    // --- Transactional Approval Workflow ---
    @Transaction
    suspend fun approvePendingEdit(
        pendingEditId: Long,
        adminName: String,
        adminNote: String?
    ): Boolean {
        val pending = getPendingEditById(pendingEditId) ?: return false

        if (pending.memberId > 0) {
            // Update existing member
            val existing = getMemberById(pending.memberId)
            val updated = (existing ?: FamilyMemberEntity(
                id = pending.memberId,
                fullName = pending.proposedFullName,
                gender = pending.proposedGender,
                birthPlace = pending.proposedBirthPlace,
                birthDate = pending.proposedBirthDate,
                address = pending.proposedAddress,
                familyRole = pending.proposedFamilyRole,
                generation = pending.proposedGeneration
            )).copy(
                fullName = pending.proposedFullName,
                nickname = pending.proposedNickname,
                gender = pending.proposedGender,
                birthPlace = pending.proposedBirthPlace,
                birthDate = pending.proposedBirthDate,
                address = pending.proposedAddress,
                phone = pending.proposedPhone,
                occupation = pending.proposedOccupation,
                maritalStatus = pending.proposedMaritalStatus,
                familyRole = pending.proposedFamilyRole,
                generation = pending.proposedGeneration,
                parentId = pending.proposedParentId,
                spouseName = pending.proposedSpouseName,
                bloodType = pending.proposedBloodType,
                isAlive = pending.proposedIsAlive,
                notes = pending.proposedNotes,
                updatedAt = System.currentTimeMillis()
            )
            updateMember(updated)
        } else {
            // Insert brand new member
            val newMember = FamilyMemberEntity(
                fullName = pending.proposedFullName,
                nickname = pending.proposedNickname,
                gender = pending.proposedGender,
                birthPlace = pending.proposedBirthPlace,
                birthDate = pending.proposedBirthDate,
                address = pending.proposedAddress,
                phone = pending.proposedPhone,
                occupation = pending.proposedOccupation,
                maritalStatus = pending.proposedMaritalStatus,
                familyRole = pending.proposedFamilyRole,
                generation = pending.proposedGeneration,
                parentId = pending.proposedParentId,
                spouseName = pending.proposedSpouseName,
                bloodType = pending.proposedBloodType,
                isAlive = pending.proposedIsAlive,
                notes = pending.proposedNotes,
                updatedAt = System.currentTimeMillis()
            )
            insertMember(newMember)
        }

        // Mark pending edit as APPROVED
        val approvedPending = pending.copy(
            status = "APPROVED",
            adminNote = adminNote ?: "Pengajuan disetujui oleh admin.",
            reviewedByAdminName = adminName,
            reviewedAt = System.currentTimeMillis()
        )
        updatePendingEdit(approvedPending)
        return true
    }

    @Transaction
    suspend fun rejectPendingEdit(
        pendingEditId: Long,
        adminName: String,
        rejectReason: String
    ): Boolean {
        val pending = getPendingEditById(pendingEditId) ?: return false
        val rejectedPending = pending.copy(
            status = "REJECTED",
            adminNote = rejectReason,
            reviewedByAdminName = adminName,
            reviewedAt = System.currentTimeMillis()
        )
        updatePendingEdit(rejectedPending)
        return true
    }

    // --- Users ---
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
}
