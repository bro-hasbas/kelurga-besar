package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyMemberEntity
import com.example.ui.theme.EmeraldGreen40
import com.example.ui.theme.GoldenAmber40
import com.example.ui.theme.SoftSurface

@Composable
fun FamilyTreeGraph(
    members: List<FamilyMemberEntity>,
    onMemberClick: (FamilyMemberEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val gen1 = members.filter { it.generation == 1 }
    val gen2 = members.filter { it.generation == 2 }
    val gen3 = members.filter { it.generation == 3 }
    val gen4 = members.filter { it.generation == 4 }

    val horizontalScroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(horizontalScroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- LEVEL 1: Sesepuh & Pendiri Keluarga ---
        Text(
            text = "GENERASI I : PILAR UTAMA KELUARGA BESAR",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = GoldenAmber40,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .border(2.dp, GoldenAmber40, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                gen1.forEachIndexed { index, founder ->
                    TreeNodeCard(
                        member = founder,
                        badgeColor = GoldenAmber40,
                        onClick = { onMemberClick(founder) }
                    )
                    if (index == 0 && gen1.size > 1) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Pernikahan",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Menikah",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Connecting Line Gen 1 to Gen 2
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(28.dp)
                .background(GoldenAmber40)
        )

        // --- LEVEL 2: Anak-anak ---
        Text(
            text = "GENERASI II : ANAK-ANAK (4 CABANG KELUARGA)",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen40,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            gen2.forEach { child ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(220.dp)
                ) {
                    TreeNodeCard(
                        member = child,
                        badgeColor = EmeraldGreen40,
                        subtext = child.spouseName?.let { "Pasangan: $it" } ?: "",
                        onClick = { onMemberClick(child) }
                    )

                    // Line to descendants of this child
                    val childrenOfThisChild = gen3.filter { it.parentId == child.id }
                    if (childrenOfThisChild.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(20.dp)
                                .background(Color(0xFF94A3B8))
                        )

                        Text(
                            text = "Cucu (${childrenOfThisChild.size})",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF0284C7)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        // Descendants (Gen 3)
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            childrenOfThisChild.forEach { grandchild ->
                                TreeNodeCard(
                                    member = grandchild,
                                    badgeColor = Color(0xFF0284C7),
                                    compact = true,
                                    onClick = { onMemberClick(grandchild) }
                                )

                                // Check if this grandchild has Gen 4 children (Cicit)
                                val greatGrandchildren = gen4.filter { it.parentId == grandchild.id }
                                if (greatGrandchildren.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(14.dp)
                                            .background(Color(0xFFC084FC))
                                    )
                                    Text(
                                        text = "Cicit (${greatGrandchildren.size})",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFF7E22CE)
                                    )
                                    greatGrandchildren.forEach { cicit ->
                                        TreeNodeCard(
                                            member = cicit,
                                            badgeColor = Color(0xFF7E22CE),
                                            compact = true,
                                            onClick = { onMemberClick(cicit) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TreeNodeCard(
    member: FamilyMemberEntity,
    badgeColor: Color,
    subtext: String = "",
    compact: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("tree_node_${member.id}"),
        colors = CardDefaults.cardColors(containerColor = SoftSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (compact) 8.dp else 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(if (compact) 28.dp else 36.dp)
                        .clip(CircleShape)
                        .background(if (member.gender == "L") Color(0xFFE0F2FE) else Color(0xFFFCE7F3)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (member.gender == "L") Color(0xFF0284C7) else Color(0xFFDB2777),
                        modifier = Modifier.size(if (compact) 16.dp else 22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member.fullName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = if (compact) 12.sp else 13.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = member.familyRole,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = if (compact) 10.sp else 11.sp,
                            color = badgeColor,
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (subtext.isNotEmpty() && !compact) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = Color.Gray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
