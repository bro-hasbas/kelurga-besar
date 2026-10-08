package com.example.data.model

data class DiffItem(
    val fieldName: String,
    val oldValue: String,
    val newValue: String,
    val hasChanged: Boolean
)

object DiffCalculator {
    fun calculateDiff(original: FamilyMemberEntity?, pending: PendingEditEntity): List<DiffItem> {
        val diffs = mutableListOf<DiffItem>()
        if (original == null) {
            // New member proposal
            diffs.add(DiffItem("Jenis Pengajuan", "-", "Penambahan Anggota Baru", true))
            diffs.add(DiffItem("Nama Lengkap", "-", pending.proposedFullName, true))
            diffs.add(DiffItem("Nama Panggilan", "-", pending.proposedNickname.ifEmpty { "-" }, true))
            diffs.add(DiffItem("Jenis Kelamin", "-", if (pending.proposedGender == "L") "Laki-laki" else "Perempuan", true))
            diffs.add(DiffItem("TTL", "-", "${pending.proposedBirthPlace}, ${pending.proposedBirthDate}", true))
            diffs.add(DiffItem("Alamat", "-", pending.proposedAddress, true))
            diffs.add(DiffItem("Nomor HP", "-", pending.proposedPhone.ifEmpty { "-" }, true))
            diffs.add(DiffItem("Pekerjaan", "-", pending.proposedOccupation.ifEmpty { "-" }, true))
            diffs.add(DiffItem("Status Pernikahan", "-", pending.proposedMaritalStatus, true))
            diffs.add(DiffItem("Peran Keluarga", "-", pending.proposedFamilyRole, true))
            diffs.add(DiffItem("Generasi", "-", "Generasi ke-${pending.proposedGeneration}", true))
            diffs.add(DiffItem("Golongan Darah", "-", pending.proposedBloodType, true))
            diffs.add(DiffItem("Catatan", "-", pending.proposedNotes.ifEmpty { "-" }, true))
            return diffs
        }

        diffs.add(
            DiffItem(
                fieldName = "Nama Lengkap",
                oldValue = original.fullName,
                newValue = pending.proposedFullName,
                hasChanged = original.fullName != pending.proposedFullName
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Nama Panggilan",
                oldValue = original.nickname.ifEmpty { "-" },
                newValue = pending.proposedNickname.ifEmpty { "-" },
                hasChanged = original.nickname != pending.proposedNickname
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Jenis Kelamin",
                oldValue = if (original.gender == "L") "Laki-laki" else "Perempuan",
                newValue = if (pending.proposedGender == "L") "Laki-laki" else "Perempuan",
                hasChanged = original.gender != pending.proposedGender
            )
        )

        val oldTtl = "${original.birthPlace}, ${original.birthDate}"
        val newTtl = "${pending.proposedBirthPlace}, ${pending.proposedBirthDate}"
        diffs.add(
            DiffItem(
                fieldName = "Tempat, Tanggal Lahir",
                oldValue = oldTtl,
                newValue = newTtl,
                hasChanged = oldTtl != newTtl
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Alamat Domisili",
                oldValue = original.address,
                newValue = pending.proposedAddress,
                hasChanged = original.address != pending.proposedAddress
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Nomor HP / WhatsApp",
                oldValue = original.phone.ifEmpty { "-" },
                newValue = pending.proposedPhone.ifEmpty { "-" },
                hasChanged = original.phone != pending.proposedPhone
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Pekerjaan / Profesi",
                oldValue = original.occupation.ifEmpty { "-" },
                newValue = pending.proposedOccupation.ifEmpty { "-" },
                hasChanged = original.occupation != pending.proposedOccupation
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Status Pernikahan",
                oldValue = original.maritalStatus,
                newValue = pending.proposedMaritalStatus,
                hasChanged = original.maritalStatus != pending.proposedMaritalStatus
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Posisi / Peran Keluarga",
                oldValue = original.familyRole,
                newValue = pending.proposedFamilyRole,
                hasChanged = original.familyRole != pending.proposedFamilyRole
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Generasi",
                oldValue = "Gen ${original.generation}",
                newValue = "Gen ${pending.proposedGeneration}",
                hasChanged = original.generation != pending.proposedGeneration
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Pasangan (Suami/Istri)",
                oldValue = original.spouseName ?: "-",
                newValue = pending.proposedSpouseName ?: "-",
                hasChanged = (original.spouseName ?: "") != (pending.proposedSpouseName ?: "")
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Golongan Darah",
                oldValue = original.bloodType,
                newValue = pending.proposedBloodType,
                hasChanged = original.bloodType != pending.proposedBloodType
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Status Hidup",
                oldValue = if (original.isAlive) "Masih Hidup" else "Almarhum/Almarhumah",
                newValue = if (pending.proposedIsAlive) "Masih Hidup" else "Almarhum/Almarhumah",
                hasChanged = original.isAlive != pending.proposedIsAlive
            )
        )

        diffs.add(
            DiffItem(
                fieldName = "Catatan Tambahan",
                oldValue = original.notes.ifEmpty { "-" },
                newValue = pending.proposedNotes.ifEmpty { "-" },
                hasChanged = original.notes != pending.proposedNotes
            )
        )

        return diffs
    }
}
