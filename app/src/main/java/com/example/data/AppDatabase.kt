package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.FamilyDao
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.PendingEditEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        FamilyMemberEntity::class,
        PendingEditEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun familyDao(): FamilyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "keluarga_ramsiah_fatmah.db"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        seedInitialData(database.familyDao())
                    }
                }
            }
        }

        suspend fun seedInitialData(dao: FamilyDao) {
            // Seed Users
            val defaultUsers = listOf(
                UserEntity(
                    id = "ADM-001",
                    name = "Bapak Ahmad Fauzi (Admin)",
                    email = "admin.fauzi@keluarga-ramsiah.id",
                    role = "ADMIN",
                    branchFamily = "Keluarga Cabang Ahmad Fauzi",
                    phone = "0811-2233-4455"
                ),
                UserEntity(
                    id = "USR-002",
                    name = "Rizky Ramadhan (Anggota)",
                    email = "rizky.ramadhan@gmail.com",
                    role = "USER",
                    branchFamily = "Cucu (Cabang Ahmad Fauzi)",
                    phone = "0812-3456-7890"
                ),
                UserEntity(
                    id = "USR-003",
                    name = "dr. Nur Aini (Anggota)",
                    email = "nur.aini@keluarga-ramsiah.id",
                    role = "USER",
                    branchFamily = "Keluarga Cabang Nur Aini",
                    phone = "0813-8899-7766"
                )
            )
            dao.insertUsers(defaultUsers)

            // Seed Family Members (Generasi 1 s.d. 4)
            val members = listOf(
                // Gen 1: Sesepuh & Pendiri Keluarga
                FamilyMemberEntity(
                    id = 1,
                    fullName = "H. Ramsiah",
                    nickname = "Kakek Ramsiah / Abah",
                    gender = "L",
                    birthPlace = "Solo",
                    birthDate = "12 Agustus 1948",
                    address = "Jl. Melati Indah No. 12, Sleman, D.I. Yogyakarta",
                    phone = "0812-2700-1122",
                    occupation = "Pensiunan Guru & Tokoh Masyarakat",
                    maritalStatus = "Menikah",
                    familyRole = "Kepala Keluarga Besar / Sesepuh",
                    generation = 1,
                    parentId = null,
                    spouseName = "Hj. Fatmah",
                    bloodType = "O",
                    isAlive = true,
                    notes = "Pendiri trah keluarga besar, gemar berkebun dan bercerita sejarah keluarga."
                ),
                FamilyMemberEntity(
                    id = 2,
                    fullName = "Hj. Fatmah",
                    nickname = "Nenek Fatmah / Umi",
                    gender = "P",
                    birthPlace = "Klaten",
                    birthDate = "05 Mei 1952",
                    address = "Jl. Melati Indah No. 12, Sleman, D.I. Yogyakarta",
                    phone = "0812-2700-1133",
                    occupation = "Ibu Rumah Tangga & Pengrajin Batik",
                    maritalStatus = "Menikah",
                    familyRole = "Ibu / Sesepuh Keluarga Besar",
                    generation = 1,
                    parentId = null,
                    spouseName = "H. Ramsiah",
                    bloodType = "B",
                    isAlive = true,
                    notes = "Penyatu keluarga besar, ahli kuliner tradisional gudeg & rawon pusaka."
                ),

                // Gen 2: Anak-anak
                FamilyMemberEntity(
                    id = 3,
                    fullName = "Ir. H. Ahmad Fauzi Ramsiah, M.T.",
                    nickname = "Pak Fauzi / Mas Fauzi",
                    gender = "L",
                    birthPlace = "Yogyakarta",
                    birthDate = "14 Juni 1974",
                    address = "Jl. Cendrawasih No. 45, Cilandak, Jakarta Selatan",
                    phone = "0811-2233-4455",
                    occupation = "Dosen Teknik Mesin & Konsultan",
                    maritalStatus = "Menikah",
                    familyRole = "Anak ke-1 (Sulung)",
                    generation = 2,
                    parentId = 1,
                    spouseName = "Hj. Siti Nurhaliza, S.Pd.",
                    bloodType = "O",
                    isAlive = true,
                    notes = "Ketua panitia silaturahmi & koordinator arisan keluarga besar."
                ),
                FamilyMemberEntity(
                    id = 4,
                    fullName = "Rahmat Hidayat Ramsiah, S.E., Ak.",
                    nickname = "Pak Rahmat",
                    gender = "L",
                    birthPlace = "Yogyakarta",
                    birthDate = "22 November 1977",
                    address = "Jl. Pahlawan No. 8, Dago, Bandung",
                    phone = "0812-4455-6677",
                    occupation = "Manajer Keuangan BUMN",
                    maritalStatus = "Menikah",
                    familyRole = "Anak ke-2",
                    generation = 2,
                    parentId = 1,
                    spouseName = "Dewi Sartika, S.Si.",
                    bloodType = "B",
                    isAlive = true,
                    notes = "Bendahara kas sosial & dana darurat keluarga besar."
                ),
                FamilyMemberEntity(
                    id = 5,
                    fullName = "dr. Hj. Nur Aini Ramsiah, Sp.A.",
                    nickname = "Mbak Aini / Dokter Aini",
                    gender = "P",
                    birthPlace = "Yogyakarta",
                    birthDate = "03 Maret 1982",
                    address = "Jl. Pandanaran No. 19, Semarang",
                    phone = "0813-8899-7700",
                    occupation = "Dokter Spesialis Anak",
                    maritalStatus = "Menikah",
                    familyRole = "Anak ke-3",
                    generation = 2,
                    parentId = 1,
                    spouseName = "Bambang Irawan, S.H., M.Kn.",
                    bloodType = "B",
                    isAlive = true,
                    notes = "Konsultan kesehatan keluarga besar & kegiatan bakti sosial."
                ),
                FamilyMemberEntity(
                    id = 6,
                    fullName = "Muhammad Yusuf Ramsiah, S.Kom.",
                    nickname = "Mas Yusuf (Bungsu)",
                    gender = "L",
                    birthPlace = "Yogyakarta",
                    birthDate = "18 September 1988",
                    address = "Jl. Kaliurang Km 7 No. 10, Sleman, Yogyakarta",
                    phone = "0818-0998-8776",
                    occupation = "Software Architect & Tech Lead",
                    maritalStatus = "Menikah",
                    familyRole = "Anak ke-4 (Bungsu)",
                    generation = 2,
                    parentId = 1,
                    spouseName = "Fitriani Handayani, S.Pd.",
                    bloodType = "A",
                    isAlive = true,
                    notes = "Administrator sistem informasi & digitalisasi silsilah keluarga."
                ),

                // Gen 3: Cucu-cucu
                FamilyMemberEntity(
                    id = 7,
                    fullName = "Rizky Ramadhan Fauzi, S.Kom.",
                    nickname = "Rizky",
                    gender = "L",
                    birthPlace = "Jakarta",
                    birthDate = "10 Oktober 1998",
                    address = "Apartemen Bassura City Tower Cattleya, Jakarta Timur",
                    phone = "0812-9876-5432",
                    occupation = "Mobile Engineer",
                    maritalStatus = "Menikah",
                    familyRole = "Cucu (Anak Pertama Mas Fauzi)",
                    generation = 3,
                    parentId = 3,
                    spouseName = "Anindya Putri, S.Farm.",
                    bloodType = "O",
                    isAlive = true,
                    notes = "Cucu pertama dari generasi ketiga."
                ),
                FamilyMemberEntity(
                    id = 8,
                    fullName = "Nadia Amanda Fauzi",
                    nickname = "Nadia",
                    gender = "P",
                    birthPlace = "Jakarta",
                    birthDate = "15 April 2002",
                    address = "Jl. Cendrawasih No. 45, Jakarta Selatan",
                    phone = "0813-1122-3344",
                    occupation = "Mahasiswi Fakultas Kedokteran UI",
                    maritalStatus = "Belum Menikah",
                    familyRole = "Cucu (Anak Kedua Mas Fauzi)",
                    generation = 3,
                    parentId = 3,
                    spouseName = null,
                    bloodType = "A",
                    isAlive = true,
                    notes = "Aktif di organisasi mahasiswa dan pengabdian masyarakat."
                ),
                FamilyMemberEntity(
                    id = 9,
                    fullName = "Dimas Pratama Hidayat",
                    nickname = "Dimas",
                    gender = "L",
                    birthPlace = "Bandung",
                    birthDate = "08 Juli 2004",
                    address = "Jl. Pahlawan No. 8, Bandung",
                    phone = "0815-5566-7788",
                    occupation = "Mahasiswa Arsitektur ITB",
                    maritalStatus = "Belum Menikah",
                    familyRole = "Cucu (Anak Pertama Pak Rahmat)",
                    generation = 3,
                    parentId = 4,
                    spouseName = null,
                    bloodType = "B",
                    isAlive = true,
                    notes = "Desainer logo reuni keluarga besar."
                ),
                FamilyMemberEntity(
                    id = 10,
                    fullName = "Farhan Maulana Hidayat",
                    nickname = "Farhan",
                    gender = "L",
                    birthPlace = "Bandung",
                    birthDate = "12 Desember 2008",
                    address = "Jl. Pahlawan No. 8, Bandung",
                    phone = "0819-2233-1100",
                    occupation = "Pelajar SMA Negeri 3 Bandung",
                    maritalStatus = "Belum Menikah",
                    familyRole = "Cucu (Anak Kedua Pak Rahmat)",
                    generation = 3,
                    parentId = 4,
                    spouseName = null,
                    bloodType = "O",
                    isAlive = true,
                    notes = "Atlet bulutangkis junior daerah Jawa Barat."
                ),
                FamilyMemberEntity(
                    id = 11,
                    fullName = "Annisa Tri Wahyuni",
                    nickname = "Icha",
                    gender = "P",
                    birthPlace = "Semarang",
                    birthDate = "25 Agustus 2011",
                    address = "Jl. Pandanaran No. 19, Semarang",
                    phone = "0852-7788-9900",
                    occupation = "Pelajar SMP Negeri 1 Semarang",
                    maritalStatus = "Belum Menikah",
                    familyRole = "Cucu (Anak Mbak Aini)",
                    generation = 3,
                    parentId = 5,
                    spouseName = null,
                    bloodType = "B",
                    isAlive = true,
                    notes = "Juara olimpiade sains matematika tingkat kota."
                ),
                FamilyMemberEntity(
                    id = 12,
                    fullName = "Zahra Putri Yusuf",
                    nickname = "Zahra",
                    gender = "P",
                    birthPlace = "Yogyakarta",
                    birthDate = "30 Januari 2017",
                    address = "Jl. Kaliurang Km 7 No. 10, Sleman",
                    phone = "-",
                    occupation = "Pelajar SD Muhammadiyah Condongcatur",
                    maritalStatus = "Belum Menikah",
                    familyRole = "Cucu (Anak Mas Yusuf)",
                    generation = 3,
                    parentId = 6,
                    spouseName = null,
                    bloodType = "A",
                    isAlive = true,
                    notes = "Cucu termuda dari cabang Mas Yusuf."
                ),

                // Gen 4: Cicit
                FamilyMemberEntity(
                    id = 13,
                    fullName = "Kenzo Arkan Ramadhan",
                    nickname = "Kenzo",
                    gender = "L",
                    birthPlace = "Jakarta",
                    birthDate = "01 Juni 2023",
                    address = "Apartemen Bassura City, Jakarta Timur",
                    phone = "-",
                    occupation = "Balita",
                    maritalStatus = "Belum Menikah",
                    familyRole = "Cicit Pertama (Anak Rizky Ramadhan)",
                    generation = 4,
                    parentId = 7,
                    spouseName = null,
                    bloodType = "O",
                    isAlive = true,
                    notes = "Cicit pertama Bapak Ramsiah dan Ibu Fatmah."
                )
            )
            dao.insertMembers(members)

            // Seed 2 Pending Edits for instant demonstration of the approval workflow
            val initialPendingEdits = listOf(
                PendingEditEntity(
                    id = 1,
                    memberId = 7,
                    originalMemberName = "Rizky Ramadhan Fauzi, S.Kom.",
                    submittedByUserId = "USR-002",
                    submittedByUserName = "Rizky Ramadhan",
                    submissionType = "EDIT_DATA",
                    proposedFullName = "Rizky Ramadhan Fauzi, S.Kom., M.T.",
                    proposedNickname = "Rizky",
                    proposedGender = "L",
                    proposedBirthPlace = "Jakarta",
                    proposedBirthDate = "10 Oktober 1998",
                    proposedAddress = "Pakuwon City Cluster San Diego Blok M-22, Mulyorejo, Surabaya",
                    proposedPhone = "0812-3456-7890",
                    proposedOccupation = "Lead Android Engineer (Tech Startup Surabaya)",
                    proposedMaritalStatus = "Menikah",
                    proposedFamilyRole = "Cucu (Anak Pertama Mas Fauzi)",
                    proposedGeneration = 3,
                    proposedParentId = 3,
                    proposedSpouseName = "Anindya Putri, S.Farm., Apt.",
                    proposedBloodType = "O",
                    proposedIsAlive = true,
                    proposedNotes = "Alhamdulillah baru wisuda S2 Teknik Komputer dan pindah dinas ke Surabaya bersama keluarga.",
                    changeReason = "Pembaruan alamat domisili baru di Surabaya, gelar master (M.T.), serta update profesi & nomor WhatsApp aktif.",
                    status = "PENDING",
                    adminNote = null,
                    reviewedByAdminName = null,
                    createdAt = System.currentTimeMillis() - 86400000L * 2 // 2 days ago
                ),
                PendingEditEntity(
                    id = 2,
                    memberId = 5,
                    originalMemberName = "dr. Hj. Nur Aini Ramsiah, Sp.A.",
                    submittedByUserId = "USR-003",
                    submittedByUserName = "dr. Nur Aini",
                    submissionType = "EDIT_DATA",
                    proposedFullName = "dr. Hj. Nur Aini Ramsiah, Sp.A(K).",
                    proposedNickname = "Mbak Aini / dr. Aini",
                    proposedGender = "P",
                    proposedBirthPlace = "Yogyakarta",
                    proposedBirthDate = "03 Maret 1982",
                    proposedAddress = "Jl. Pandanaran No. 19, Semarang (Depan RS Hermina)",
                    proposedPhone = "0813-8899-7766",
                    proposedOccupation = "Dokter Spesialis Anak Konsultan Neonatologi",
                    proposedMaritalStatus = "Menikah",
                    proposedFamilyRole = "Anak ke-3",
                    proposedGeneration = 2,
                    proposedParentId = 1,
                    proposedSpouseName = "Bambang Irawan, S.H., M.Kn.",
                    proposedBloodType = "B",
                    proposedIsAlive = true,
                    proposedNotes = "Konsultan kesehatan keluarga besar. Nomor WhatsApp baru khusus darurat medis keluarga.",
                    changeReason = "Update gelar konsultan Sp.A(K), pergantian nomor HP aktif, serta detail klinik tempat tugas.",
                    status = "PENDING",
                    adminNote = null,
                    reviewedByAdminName = null,
                    createdAt = System.currentTimeMillis() - 3600000L * 5 // 5 hours ago
                )
            )
            dao.insertPendingEdits(initialPendingEdits)
        }
    }
}
