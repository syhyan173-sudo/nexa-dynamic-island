package com.nexa.island.feature.island.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexa.island.domain.model.ActiveIslandSession

@Composable
fun ExpandedIslandContent(
    session: ActiveIslandSession,
    onDismiss: () -> Unit,
    onSendReply: (String) -> Unit = {}
) {
    var replyText by remember { mutableStateOf("") }
    val event = session.currentEvent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon / Avatar Penanda
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event?.title?.take(1) ?: "N",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Judul Notifikasi (Nama Pengirim WA / Discord)
                Text(
                    text = event?.title ?: "Notifikasi",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Isi Pesan Lengkap (Tidak terpotong, max 10 baris)
                Text(
                    text = event?.message ?: "",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Kolom Input Balas Pesan Direct Reply
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = replyText,
                onValueChange = { replyText = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Tombol Kirim
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (replyText.isNotBlank()) Color(0xFF25D366) else Color.Gray.copy(alpha = 0.4f),
                        CircleShape
                    )
            ) {
                IconButton(
                    onClick = {
                        if (replyText.isNotBlank()) {
                            onSendReply(replyText)
                            replyText = ""
                        }
                    },
                    enabled = replyText.isNotBlank()
                ) {
                    Text("➤", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}
