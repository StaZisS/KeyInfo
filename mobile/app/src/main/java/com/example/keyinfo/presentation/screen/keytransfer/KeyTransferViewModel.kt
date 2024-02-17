package com.example.keyinfo.presentation.screen.keytransfer

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.keyinfo.domain.state.KeysState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KeyTransferViewModel(context: Context) : ViewModel() {

    private val _state = MutableStateFlow(KeysState())
    val state: StateFlow<KeysState> get() = _state

    fun processIntent(intent: KeyTransferIntent) {
        when (intent) {
            KeyTransferIntent.AcceptTransfer -> {

            }

            KeyTransferIntent.ConfirmReserve -> {

            }

            KeyTransferIntent.RejectTransfer -> {

            }

            KeyTransferIntent.UpdateConfirmDialogState -> {
                _state.value =
                    state.value.copy(isConfirmDialogOpen = !state.value.isConfirmDialogOpen)
            }

            KeyTransferIntent.UpdateTransferDialogState -> {
                _state.value =
                    state.value.copy(isTransferDialogOpen = !state.value.isTransferDialogOpen)
            }

            is KeyTransferIntent.ClickOnCard -> {

            }

            is KeyTransferIntent.ClickOnButton -> {
                _state.value = state.value.copy(currentButton = intent.id)
            }
        }
    }
}