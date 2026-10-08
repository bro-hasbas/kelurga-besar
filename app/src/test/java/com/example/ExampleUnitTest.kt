package com.example

import com.example.data.model.DiffCalculator
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.PendingEditEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testDiffCalculatorIdentifiesChanges() {
        val original = FamilyMemberEntity(
            id = 7,
            fullName = "Rizky Ramadhan",
            gender = "L",
            birthPlace = "Jakarta",
            birthDate = "10 Oktober 1998",
            address = "Apartemen Bassura City, Jakarta Timur",
            phone = "0812-9876-5432",
            occupation = "Mobile Engineer",
            familyRole = "Cucu",
            generation = 3
        )

        val pending = PendingEditEntity(
            id = 1,
            memberId = 7,
            originalMemberName = "Rizky Ramadhan",
            submittedByUserId = "USR-002",
            submittedByUserName = "Rizky Ramadhan",
            proposedFullName = "Rizky Ramadhan, M.T.",
            proposedGender = "L",
            proposedBirthPlace = "Jakarta",
            proposedBirthDate = "10 Oktober 1998",
            proposedAddress = "Pakuwon City, Surabaya",
            proposedPhone = "0812-3456-7890",
            proposedOccupation = "Lead Engineer",
            proposedFamilyRole = "Cucu",
            proposedGeneration = 3,
            changeReason = "Pindah domisili ke Surabaya",
            status = "PENDING"
        )

        val diffs = DiffCalculator.calculateDiff(original, pending)
        val changed = diffs.filter { it.hasChanged }

        // Must detect changes in: Nama Lengkap, Alamat, No HP, Pekerjaan
        assertTrue(changed.any { it.fieldName == "Nama Lengkap" && it.newValue == "Rizky Ramadhan, M.T." })
        assertTrue(changed.any { it.fieldName == "Alamat Domisili" && it.newValue == "Pakuwon City, Surabaya" })
        assertTrue(changed.any { it.fieldName == "Nomor HP / WhatsApp" && it.newValue == "0812-3456-7890" })
        assertTrue(changed.any { it.fieldName == "Pekerjaan / Profesi" && it.newValue == "Lead Engineer" })
    }
}
