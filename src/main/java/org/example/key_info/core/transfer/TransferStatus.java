package org.example.key_info.core.transfer;

import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;

public enum TransferStatus {
    IN_PROCESS,
    ACCEPTED,
    DECLINED,
    ;

    public static TransferStatus getTransferStatusByName(String name) {
        try {
            return TransferStatus.valueOf(name);
        } catch (Exception e) {
            throw new ExceptionInApplication("Не сущуствует такого статуса у передачи ключа", ExceptionType.NOT_FOUND);
        }
    }
}
