package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Schema
import androidx.compose.material.icons.filled.ViewQuilt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen40
import com.example.ui.theme.GoldenAmber40
import com.example.ui.theme.SoftSurface

@Composable
fun SystemArchitectureScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        "1. Skema Database (SQL)" to Icons.Default.Schema,
        "2. Backend Controller (Laravel/PHP)" to Icons.Default.Code,
        "3. Backend Controller (Node.js)" to Icons.Default.DataObject,
        "4. Wireframe & UI/UX" to Icons.Default.ViewQuilt
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("system_architecture_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SoftSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Rancangan Sistem & Arsitektur",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Spesifikasi skema database relasional, logika backend approval workflow, dan kerangka UI/UX sistem.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Sub tabs
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tabs.size) { index ->
                    val (label, icon) = tabs[index]
                    FilterChip(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        leadingIcon = {
                            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = EmeraldGreen40,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                item {
                    SectionHeader(
                        title = "1. Desain Skema Database Relasional (SQL)",
                        description = "Tabel 'users' (otentikasi & hak akses), 'family_members' (data utama publik), dan 'pending_edits' (tabel staging validasi)."
                    )
                }
                item {
                    CodeCard(
                        title = "A. Skema Tabel users",
                        code = """
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(191) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') DEFAULT 'USER',
    branch_family VARCHAR(100) NULL,
    phone VARCHAR(30) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
                        """.trimIndent()
                    )
                }
                item {
                    CodeCard(
                        title = "B. Skema Tabel family_members (Tabel Utama)",
                        code = """
CREATE TABLE family_members (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(200) NOT NULL,
    nickname VARCHAR(50) NULL,
    gender ENUM('L', 'P') NOT NULL,
    birth_place VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(30) NULL,
    occupation VARCHAR(100) NULL,
    marital_status ENUM('Menikah', 'Belum Menikah', 'Wafat', 'Cerai') DEFAULT 'Menikah',
    family_role VARCHAR(100) NOT NULL,
    generation INT NOT NULL, -- 1=Sesepuh, 2=Anak, 3=Cucu, 4=Cicit
    parent_id BIGINT UNSIGNED NULL,
    spouse_name VARCHAR(150) NULL,
    blood_type ENUM('A', 'B', 'AB', 'O', '-') DEFAULT '-',
    is_alive BOOLEAN DEFAULT TRUE,
    notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (parent_id) REFERENCES family_members(id) ON DELETE SET NULL,
    INDEX idx_generation (generation),
    INDEX idx_full_name (full_name)
);
                        """.trimIndent()
                    )
                }
                item {
                    CodeCard(
                        title = "C. Skema Tabel pending_edits (Tabel Staging Persetujuan)",
                        code = """
CREATE TABLE pending_edits (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT UNSIGNED NULL, -- NULL jika usulan anggota baru
    submitted_by VARCHAR(36) NOT NULL,
    submission_type ENUM('EDIT_DATA', 'TAMBAH_ANGGOTA') DEFAULT 'EDIT_DATA',
    
    -- Kolom Data Usulan (Draft Staging)
    proposed_full_name VARCHAR(200) NOT NULL,
    proposed_nickname VARCHAR(50) NULL,
    proposed_gender ENUM('L', 'P') NOT NULL,
    proposed_birth_place VARCHAR(100) NOT NULL,
    proposed_birth_date DATE NOT NULL,
    proposed_address TEXT NOT NULL,
    proposed_phone VARCHAR(30) NULL,
    proposed_occupation VARCHAR(100) NULL,
    proposed_marital_status VARCHAR(50) DEFAULT 'Menikah',
    proposed_family_role VARCHAR(100) NOT NULL,
    proposed_generation INT NOT NULL,
    proposed_parent_id BIGINT UNSIGNED NULL,
    proposed_spouse_name VARCHAR(150) NULL,
    proposed_blood_type VARCHAR(5) DEFAULT '-',
    proposed_is_alive BOOLEAN DEFAULT TRUE,
    proposed_notes TEXT NULL,
    
    -- Alur Persetujuan
    change_reason TEXT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    admin_note TEXT NULL,
    reviewed_by VARCHAR(36) NULL,
    reviewed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (member_id) REFERENCES family_members(id) ON DELETE CASCADE,
    FOREIGN KEY (submitted_by) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_status (status)
);
                        """.trimIndent()
                    )
                }
            }
            1 -> {
                item {
                    SectionHeader(
                        title = "2. Logika Backend (PHP / Laravel Controller)",
                        description = "FamilyApprovalController.php: Menangani submit perubahan data ke staging & eksekusi transaksi DB::transaction() saat Admin Approve/Reject."
                    )
                }
                item {
                    CodeCard(
                        title = "FamilyApprovalController.php",
                        code = """
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\Models\FamilyMember;
use App\Models\PendingEdit;

class FamilyApprovalController extends Controller
{
    /**
     * 1. USER: Submit draft perubahan (Masuk ke tabel staging)
     */
    public function submitEditProposal(Request ${'$'}request, ${'$'}memberId)
    {
        ${'$'}validated = ${'$'}request->validate([
            'proposed_full_name'   => 'required|string|max:200',
            'proposed_address'     => 'required|string',
            'proposed_phone'       => 'nullable|string|max:30',
            'proposed_occupation'  => 'nullable|string|max:100',
            'change_reason'        => 'required|string|min:5',
        ]);

        ${'$'}pending = PendingEdit::create([
            'member_id'           => ${'$'}memberId,
            'submitted_by'        => auth()->id(),
            'submission_type'     => 'EDIT_DATA',
            'proposed_full_name'  => ${'$'}validated['proposed_full_name'],
            'proposed_address'    => ${'$'}validated['proposed_address'],
            'proposed_phone'      => ${'$'}validated['proposed_phone'] ?? null,
            'proposed_occupation' => ${'$'}validated['proposed_occupation'] ?? null,
            'change_reason'       => ${'$'}validated['change_reason'],
            'status'              => 'PENDING'
        ]);

        return response()->json([
            'status'  => 'success',
            'message' => 'Perubahan berhasil diajukan! Menunggu tinjauan Admin.',
            'data'    => ${'$'}pending
        ], 201);
    }

    /**
     * 2. ADMIN: Approve Pengajuan (Menimpa tabel utama secara atomic)
     */
    public function approveProposal(Request ${'$'}request, ${'$'}pendingId)
    {
        // Pastikan hanya admin yang bisa approve
        if (auth()->user()->role !== 'ADMIN') {
            return response()->json(['message' => 'Unauthorized'], 403);
        }

        DB::transaction(function () use (${'$'}pendingId, ${'$'}request) {
            ${'$'}pending = PendingEdit::where('id', ${'$'}pendingId)
                ->where('status', 'PENDING')
                ->lockForUpdate()
                ->firstOrFail();

            // Timpa / Update tabel utama
            ${'$'}member = FamilyMember::findOrFail(${'$'}pending->member_id);
            ${'$'}member->update([
                'full_name'  => ${'$'}pending->proposed_full_name,
                'address'    => ${'$'}pending->proposed_address,
                'phone'      => ${'$'}pending->proposed_phone,
                'occupation' => ${'$'}pending->proposed_occupation,
                'updated_at' => now()
            ]);

            // Ubah status antrean menjadi APPROVED
            ${'$'}pending->update([
                'status'       => 'APPROVED',
                'admin_note'   => ${'$'}request->admin_note ?? 'Disetujui oleh Admin.',
                'reviewed_by'  => auth()->id(),
                'reviewed_at'  => now()
            ]);
        });

        return response()->json([
            'status'  => 'success',
            'message' => 'Pengajuan disetujui! Data tabel utama telah diperbarui.'
        ]);
    }

    /**
     * 3. ADMIN: Reject Pengajuan (Data utama aman, simpan alasan penolakan)
     */
    public function rejectProposal(Request ${'$'}request, ${'$'}pendingId)
    {
        if (auth()->user()->role !== 'ADMIN') {
            return response()->json(['message' => 'Unauthorized'], 403);
        }

        ${'$'}request->validate(['reject_reason' => 'required|string']);

        ${'$'}pending = PendingEdit::where('id', ${'$'}pendingId)
            ->where('status', 'PENDING')
            ->firstOrFail();

        ${'$'}pending->update([
            'status'      => 'REJECTED',
            'admin_note'  => ${'$'}request->reject_reason,
            'reviewed_by' => auth()->id(),
            'reviewed_at' => now()
        ]);

        return response()->json([
            'status'  => 'success',
            'message' => 'Pengajuan berhasil ditolak. Tabel utama tidak berubah.'
        ]);
    }
}
                        """.trimIndent()
                    )
                }
            }
            2 -> {
                item {
                    SectionHeader(
                        title = "3. Logika Backend (Node.js / Express & Prisma / SQL)",
                        description = "Implementasi REST API & Atomic Transaction menggunakan TypeScript / Node.js."
                    )
                }
                item {
                    CodeCard(
                        title = "approvalController.ts (Node.js / Express)",
                        code = """
import { Request, Response } from 'express';
import { prisma } from '../prismaClient';

export const submitEditProposal = async (req: Request, res: Response) => {
  const { memberId } = req.params;
  const { proposedFullName, proposedAddress, proposedPhone, changeReason } = req.body;
  const userId = req.user.id;

  const pending = await prisma.pendingEdit.create({
    data: {
      memberId: Number(memberId),
      submittedBy: userId,
      submissionType: 'EDIT_DATA',
      proposedFullName,
      proposedAddress,
      proposedPhone,
      changeReason,
      status: 'PENDING'
    }
  });

  return res.status(201).json({
    message: 'Draft perubahan tersimpan di staging table (status: PENDING).',
    pending
  });
};

export const approveProposal = async (req: Request, res: Response) => {
  const { pendingId } = req.params;
  const adminId = req.user.id;
  const { adminNote } = req.body;

  // Jalankan dalam transaksi atomic
  const result = await prisma.${'$'}transaction(async (tx) => {
    const pending = await tx.pendingEdit.findUniqueOrThrow({
      where: { id: Number(pendingId) }
    });

    if (pending.status !== 'PENDING') {
      throw new Error('Pengajuan sudah tidak dalam status PENDING');
    }

    // 1. Timpa data di tabel utama
    const updatedMember = await tx.familyMember.update({
      where: { id: pending.memberId! },
      data: {
        fullName: pending.proposedFullName,
        address: pending.proposedAddress,
        phone: pending.proposedPhone,
        updatedAt: new Date()
      }
    });

    // 2. Update status antrean
    const updatedPending = await tx.pendingEdit.update({
      where: { id: pending.id },
      data: {
        status: 'APPROVED',
        adminNote: adminNote || 'Disetujui oleh admin.',
        reviewedBy: adminId,
        reviewedAt: new Date()
      }
    });

    return { updatedMember, updatedPending };
  });

  return res.json({ message: 'Approved! Data utama diperbarui.', result });
};

export const rejectProposal = async (req: Request, res: Response) => {
  const { pendingId } = req.params;
  const adminId = req.user.id;
  const { rejectReason } = req.body;

  const rejected = await prisma.pendingEdit.update({
    where: { id: Number(pendingId) },
    data: {
      status: 'REJECTED',
      adminNote: rejectReason,
      reviewedBy: adminId,
      reviewedAt: new Date()
    }
  });

  return res.json({ message: 'Pengajuan ditolak.', rejected });
};
                        """.trimIndent()
                    )
                }
            }
            3 -> {
                item {
                    SectionHeader(
                        title = "4. Struktur UI/UX & Wireframe Text",
                        description = "Representasi kerangka halaman untuk Dashboard Admin dan Menu Utama User."
                    )
                }
                item {
                    CodeCard(
                        title = "Wireframe Text: Dashboard Persetujuan Admin",
                        code = """
+-------------------------------------------------------------------------+
| [ADMIN] SISTEM INFORMASI KELUARGA RAMSIAH & FATMAH   | Mode: Admin [V] |
+-------------------------------------------------------------------------+
| [Tab: Direktori] [Tab: Silsilah] [Tab: Persetujuan (2)]* [Tab: Riwayat] |
+-------------------------------------------------------------------------+
| DASHBOARD PERSETUJUAN (APPROVAL DASHBOARD)                              |
| Ada 2 perubahan data keluarga yang menunggu tinjauan                    |
+-------------------------------------------------------------------------+
| CARD PENGUSULAN #1                                                      |
| Anggota Target : Rizky Ramadhan Fauzi (Cucu - Gen 3)                    |
| Diajukan Oleh  : Rizky Ramadhan (08 Okt 2026, 09:15 WIB)                |
| Alasan         : "Pindah alamat baru ke Surabaya & update no WhatsApp"  |
|                                                                         |
| PERBANDINGAN PERUBAHAN (DIFF):                                          |
| [Alamat]   : (Lama) Apartemen Bassura City, Jaktim [STRIKE]             |
|              (Baru) Pakuwon City Cluster San Diego, Surabaya  [GREEN]   |
| [No HP]    : (Lama) 0812-9876-5432 [STRIKE]                             |
|              (Baru) 0812-3456-7890                            [GREEN]   |
|                                                                         |
| [  [TOLAK (REJECT)]  ]            [  [SETUJUI (APPROVE)]  ]             |
| (Dialog: Input Alasan)            (Update Atomic ke Tabel Utama)        |
+-------------------------------------------------------------------------+
                        """.trimIndent()
                    )
                }
                item {
                    CodeCard(
                        title = "Wireframe Text: Menu Utama User (Anggota Keluarga)",
                        code = """
+-------------------------------------------------------------------------+
| [USER] DIREKTORI KELUARGA BESAR RAMSIAH & FATMAH   | Mode: User  [V] |
+-------------------------------------------------------------------------+
| BANNER KELUARGA: "Guyub Rukun Saklawase" | 13 Anggota | 4 Generasi     |
+-------------------------------------------------------------------------+
| [ Cari Nama / Alamat / Profesi...               ] [ + Usulkan Anggota ] |
| Filter Generasi: [Semua] [Gen 1] [Gen 2] [Gen 3] [Gen 4]                |
+-------------------------------------------------------------------------+
| LIST KARTU KELUARGA (PUBLIK):                                           |
| +---------------------------------------------------------------------+ |
| | [Foto] H. Ramsiah (Gen 1)                           [Sesepuh Utama] | |
| |        Lahir : Solo, 12 Agustus 1948                                | |
| |        Alamat: Jl. Melati Indah No. 12, Sleman, Yogyakarta          | |
| |        [ Detail Lengkap ]           [ Ajukan Edit Data (User) ]     | |
| +---------------------------------------------------------------------+ |
| | [Foto] Ir. H. Ahmad Fauzi Ramsiah (Gen 2)           [Anak ke-1]     | |
| |        Alamat: Jl. Cendrawasih No. 45, Jakarta Selatan              | |
| |        [ Detail Lengkap ]           [ Ajukan Edit Data (User) ]     | |
| +---------------------------------------------------------------------+ |
|                                                                         |
| FORM MODAL "AJUKAN EDIT":                                               |
| - Input Form: Nama, TTL, Alamat, No HP, Profesi                         |
| - Field Wajib: Alasan Perubahan Data                                    |
| - Info Box: "Data tidak langsung mengubah publik, tunggu verifikasi"    |
| - Tombol: [Batal]  [Kirim Pengajuan (Pending)]                          |
+-------------------------------------------------------------------------+
                        """.trimIndent()
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, description: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen40,
                fontSize = 16.sp
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
fun CodeCard(title: String, code: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GoldenAmber40,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = code,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color(0xFFE2E8F0),
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}
