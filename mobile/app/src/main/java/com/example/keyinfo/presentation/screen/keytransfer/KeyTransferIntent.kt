package com.example.keyinfo.presentation.screen.keytransfer

sealed class KeyTransferIntent {
    data class ClickOnButton(val id: Int): KeyTransferIntent()
    data class ClickOnCard(val id: String): KeyTransferIntent()
    data object UpdateTransferDialogState: KeyTransferIntent()
    data object UpdateConfirmDialogState: KeyTransferIntent()
    data object ConfirmReserve: KeyTransferIntent()
    data object AcceptTransfer: KeyTransferIntent()
    data object RejectTransfer: KeyTransferIntent()
}