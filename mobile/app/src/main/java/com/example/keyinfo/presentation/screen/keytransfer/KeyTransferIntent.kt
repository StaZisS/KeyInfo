package com.example.keyinfo.presentation.screen.keytransfer

import com.example.keyinfo.domain.model.keys.Transfer

sealed class KeyTransferIntent {
    data class ClickOnButton(val id: Int): KeyTransferIntent()
    data class ClickOnCard(val transfer: Transfer): KeyTransferIntent()
    data object UpdateTransferDialogState: KeyTransferIntent()
    data object UpdateConfirmDialogState: KeyTransferIntent()

    data object ConfirmReserve: KeyTransferIntent()
    data object AcceptTransfer: KeyTransferIntent()
    data object RejectTransfer: KeyTransferIntent()
}