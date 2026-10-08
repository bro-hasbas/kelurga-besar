package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.FamilyRepository
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.PendingEditEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppRole(val label: String, val badge: String) {
    ADMIN("Administrator", "Akses Penuh + Approval"),
    USER("Anggota Keluarga", "Lihat Data + Ajukan Edit")
}

class FamilyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FamilyRepository

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = FamilyRepository(database.familyDao())
    }

    // Role state
    private val _currentRole = MutableStateFlow(AppRole.ADMIN)
    val currentRole: StateFlow<AppRole> = _currentRole.asStateFlow()

    private val _currentUserName = MutableStateFlow("Ir. H. Ahmad Fauzi (Admin)")
    val currentUserName: StateFlow<String> = _currentUserName.asStateFlow()

    private val _currentUserId = MutableStateFlow("ADM-001")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    // Search and filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenerationFilter = MutableStateFlow<Int?>(null) // null = all
    val selectedGenerationFilter: StateFlow<Int?> = _selectedGenerationFilter.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Dialog & Detail states
    private val _detailMember = MutableStateFlow<FamilyMemberEntity?>(null)
    val detailMember: StateFlow<FamilyMemberEntity?> = _detailMember.asStateFlow()

    private val _editTargetMember = MutableStateFlow<FamilyMemberEntity?>(null)
    val editTargetMember: StateFlow<FamilyMemberEntity?> = _editTargetMember.asStateFlow()

    private val _reviewingPendingEdit = MutableStateFlow<PendingEditEntity?>(null)
    val reviewingPendingEdit: StateFlow<PendingEditEntity?> = _reviewingPendingEdit.asStateFlow()

    private val _isAddMemberDialogOpen = MutableStateFlow(false)
    val isAddMemberDialogOpen: StateFlow<Boolean> = _isAddMemberDialogOpen.asStateFlow()

    // Data streams from repository
    val allMembers: StateFlow<List<FamilyMemberEntity>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activePendingEdits: StateFlow<List<PendingEditEntity>> = repository.activePendingEdits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPendingHistory: StateFlow<List<PendingEditEntity>> = repository.allPendingEdits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingCount: StateFlow<Int> = repository.activePendingCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Filtered members
    val filteredMembers: StateFlow<List<FamilyMemberEntity>> = combine(
        allMembers,
        _searchQuery,
        _selectedGenerationFilter
    ) { members, query, gen ->
        members.filter { member ->
            val matchQuery = if (query.isBlank()) true else {
                member.fullName.contains(query, ignoreCase = true) ||
                        member.nickname.contains(query, ignoreCase = true) ||
                        member.address.contains(query, ignoreCase = true) ||
                        member.occupation.contains(query, ignoreCase = true) ||
                        member.familyRole.contains(query, ignoreCase = true)
            }
            val matchGen = gen == null || member.generation == gen
            matchQuery && matchGen
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleRole() {
        if (_currentRole.value == AppRole.ADMIN) {
            _currentRole.value = AppRole.USER
            _currentUserName.value = "Rizky Ramadhan (Anggota)"
            _currentUserId.value = "USR-002"
            showMessage("Beralih ke mode: Anggota Keluarga (User)")
        } else {
            _currentRole.value = AppRole.ADMIN
            _currentUserName.value = "Ir. H. Ahmad Fauzi (Admin)"
            _currentUserId.value = "ADM-001"
            showMessage("Beralih ke mode: Administrator (Akses Penuh & Approval)")
        }
    }

    fun setRole(role: AppRole) {
        _currentRole.value = role
        if (role == AppRole.ADMIN) {
            _currentUserName.value = "Ir. H. Ahmad Fauzi (Admin)"
            _currentUserId.value = "ADM-001"
        } else {
            _currentUserName.value = "Rizky Ramadhan (Anggota)"
            _currentUserId.value = "USR-002"
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGenerationFilter(gen: Int?) {
        _selectedGenerationFilter.value = gen
    }

    fun openMemberDetail(member: FamilyMemberEntity) {
        _detailMember.value = member
    }

    fun closeMemberDetail() {
        _detailMember.value = null
    }

    fun openEditDialog(member: FamilyMemberEntity) {
        _editTargetMember.value = member
    }

    fun closeEditDialog() {
        _editTargetMember.value = null
    }

    fun openAddMemberDialog() {
        _isAddMemberDialogOpen.value = true
    }

    fun closeAddMemberDialog() {
        _isAddMemberDialogOpen.value = false
    }

    fun openReviewDialog(pending: PendingEditEntity) {
        _reviewingPendingEdit.value = pending
    }

    fun closeReviewDialog() {
        _reviewingPendingEdit.value = null
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearMessage() {
        _snackbarMessage.value = null
    }

    // --- Action: User Submits Edit Proposal (Saved to Staging Table) ---
    fun submitEditProposal(
        member: FamilyMemberEntity,
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
    ) {
        viewModelScope.launch {
            val pendingEdit = PendingEditEntity(
                memberId = member.id,
                originalMemberName = member.fullName,
                submittedByUserId = _currentUserId.value,
                submittedByUserName = _currentUserName.value,
                submissionType = "EDIT_DATA",
                proposedFullName = proposedFullName.trim(),
                proposedNickname = proposedNickname.trim(),
                proposedGender = proposedGender,
                proposedBirthPlace = proposedBirthPlace.trim(),
                proposedBirthDate = proposedBirthDate.trim(),
                proposedAddress = proposedAddress.trim(),
                proposedPhone = proposedPhone.trim(),
                proposedOccupation = proposedOccupation.trim(),
                proposedMaritalStatus = proposedMaritalStatus,
                proposedFamilyRole = proposedFamilyRole,
                proposedGeneration = proposedGeneration,
                proposedParentId = proposedParentId,
                proposedSpouseName = proposedSpouseName?.trim()?.ifEmpty { null },
                proposedBloodType = proposedBloodType,
                proposedIsAlive = proposedIsAlive,
                proposedNotes = proposedNotes.trim(),
                changeReason = changeReason.trim(),
                status = "PENDING"
            )
            repository.submitEditProposal(pendingEdit)
            closeEditDialog()
            showMessage("Pengajuan perubahan berhasil dikirim! Status: Pending Approval.")
        }
    }

    // --- Action: User Submits New Member Proposal ---
    fun submitNewMemberProposal(
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
    ) {
        viewModelScope.launch {
            val pending = PendingEditEntity(
                memberId = 0, // 0 denotes new member
                originalMemberName = "(Anggota Baru)",
                submittedByUserId = _currentUserId.value,
                submittedByUserName = _currentUserName.value,
                submissionType = "TAMBAH_ANGGOTA",
                proposedFullName = fullName.trim(),
                proposedNickname = nickname.trim(),
                proposedGender = gender,
                proposedBirthPlace = birthPlace.trim(),
                proposedBirthDate = birthDate.trim(),
                proposedAddress = address.trim(),
                proposedPhone = phone.trim(),
                proposedOccupation = occupation.trim(),
                proposedMaritalStatus = maritalStatus,
                proposedFamilyRole = familyRole,
                proposedGeneration = generation,
                proposedParentId = parentId,
                proposedSpouseName = spouseName?.trim()?.ifEmpty { null },
                proposedBloodType = bloodType,
                proposedIsAlive = true,
                proposedNotes = notes.trim(),
                changeReason = reason.trim().ifEmpty { "Pengajuan anggota keluarga baru ke silsilah" },
                status = "PENDING"
            )
            repository.submitEditProposal(pending)
            closeAddMemberDialog()
            showMessage("Usulan penambahan anggota baru berhasil diajukan untuk ditinjau Admin.")
        }
    }

    // --- Action: Admin Approves Proposal (Overwrites Main Table) ---
    fun approveProposal(pendingEditId: Long, adminNote: String?) {
        viewModelScope.launch {
            val success = repository.approveProposal(
                pendingEditId = pendingEditId,
                adminName = _currentUserName.value,
                adminNote = adminNote?.trim()?.ifEmpty { "Disetujui dan diverifikasi oleh Administrator." }
            )
            closeReviewDialog()
            if (success) {
                showMessage("Pengajuan disetujui! Data tabel utama berhasil diperbarui.")
            } else {
                showMessage("Gagal menyetujui pengajuan.")
            }
        }
    }

    // --- Action: Admin Rejects Proposal ---
    fun rejectProposal(pendingEditId: Long, reason: String) {
        viewModelScope.launch {
            val success = repository.rejectProposal(
                pendingEditId = pendingEditId,
                adminName = _currentUserName.value,
                reason = reason.trim().ifEmpty { "Ditolak: Informasi tidak dapat diverifikasi." }
            )
            closeReviewDialog()
            if (success) {
                showMessage("Pengajuan ditolak. Data tabel utama tetap aman tidak berubah.")
            } else {
                showMessage("Gagal menolak pengajuan.")
            }
        }
    }

    // --- Action: Admin Direct Add Member ---
    fun adminAddMemberDirect(member: FamilyMemberEntity) {
        viewModelScope.launch {
            repository.insertMember(member)
            closeAddMemberDialog()
            showMessage("Anggota keluarga baru berhasil ditambahkan langsung ke database!")
        }
    }

    // --- Action: Admin Direct Update Member ---
    fun adminUpdateMemberDirect(member: FamilyMemberEntity) {
        viewModelScope.launch {
            repository.updateMember(member)
            closeEditDialog()
            showMessage("Data ${member.fullName} berhasil diperbarui langsung oleh Admin.")
        }
    }

    // --- Action: Admin Direct Delete Member ---
    fun adminDeleteMember(member: FamilyMemberEntity) {
        viewModelScope.launch {
            repository.deleteMember(member)
            closeMemberDetail()
            showMessage("Data ${member.fullName} telah dihapus dari sistem.")
        }
    }

    // --- Action: Reset Sample Data ---
    fun resetToDefaultData() {
        viewModelScope.launch {
            repository.resetDatabaseToInitial()
            showMessage("Data telah di-reset ke data bawaan keluarga Ramsiah-Fatmah.")
        }
    }
}
