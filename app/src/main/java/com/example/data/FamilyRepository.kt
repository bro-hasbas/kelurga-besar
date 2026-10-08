package com.example.data

import com.example.data.dao.FamilyDao
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.PendingEditEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

class FamilyRepository(private val familyDao: FamilyDao) {

    val allMembers: Flow<List<FamilyMemberEntity>> = familyDao.getAllMembers()
    val activePendingEdits: Flow<List<PendingEditEntity>> = familyDao.getActivePendingEdits()
    val allPendingEdits: Flow<List<PendingEditEntity>> = familyDao.getAllPendingEdits()
    val activePendingCount: Flow<Int> = familyDao.getActivePendingCount()
    val allUsers: Flow<List<UserEntity>> = familyDao.getAllUsers()

    suspend fun getMemberById(id: Long): FamilyMemberEntity? = familyDao.getMemberById(id)

    fun searchMembers(query: String): Flow<List<FamilyMemberEntity>> = familyDao.searchMembers(query)

    fun getMembersByGeneration(gen: Int): Flow<List<FamilyMemberEntity>> = familyDao.getMembersByGeneration(gen)

    fun getSubmissionsByUser(userId: String): Flow<List<PendingEditEntity>> = familyDao.getSubmissionsByUser(userId)

    suspend fun insertMember(member: FamilyMemberEntity): Long = familyDao.insertMember(member)

    suspend fun updateMember(member: FamilyMemberEntity) = familyDao.updateMember(member)

    suspend fun deleteMember(member: FamilyMemberEntity) = familyDao.deleteMember(member)

    suspend fun deleteMemberById(id: Long) = familyDao.deleteMemberById(id)

    suspend fun submitEditProposal(pendingEdit: PendingEditEntity): Long =
        familyDao.insertPendingEdit(pendingEdit)

    suspend fun approveProposal(pendingEditId: Long, adminName: String, adminNote: String?): Boolean =
        familyDao.approvePendingEdit(pendingEditId, adminName, adminNote)

    suspend fun rejectProposal(pendingEditId: Long, adminName: String, reason: String): Boolean =
        familyDao.rejectPendingEdit(pendingEditId, adminName, reason)

    suspend fun resetDatabaseToInitial() {
        AppDatabase.seedInitialData(familyDao)
    }
}
