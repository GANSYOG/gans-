package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.Item
import com.example.ui.MainViewModel

@Composable
fun BookingDialog(
    item: Item,
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var hours by remember { mutableIntStateOf(1) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirm Booking")
        },
        text = {
            Column {
                Text("Select duration for ${item.name}")
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Button(onClick = { if (hours > 1) hours-- }) { Text("-") }
                    Text("$hours Hour(s)", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = { hours++ }) { Text("+") }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Total: $${item.pricePerHour * hours}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.createBooking(
                        item = item, 
                        hours = hours, 
                        onSuccess = onSuccess,
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Pay Now")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
