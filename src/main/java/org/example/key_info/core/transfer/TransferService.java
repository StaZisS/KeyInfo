package org.example.key_info.core.transfer;

import lombok.RequiredArgsConstructor;
import org.example.key_info.core.client.repository.ClientRepository;
import org.example.key_info.core.key.repository.KeyRepository;
import org.example.key_info.public_interface.exception.ExceptionInApplication;
import org.example.key_info.public_interface.exception.ExceptionType;
import org.example.key_info.public_interface.transfer.AcceptTransferDto;
import org.example.key_info.public_interface.transfer.CreateTransferDto;
import org.example.key_info.public_interface.transfer.DeclineTransferDto;
import org.example.key_info.public_interface.transfer.DeleteTransferDto;
import org.example.key_info.public_interface.transfer.GetForeignTransfersDto;
import org.example.key_info.public_interface.transfer.GetMyTransfersDto;
import org.example.key_info.public_interface.transfer.TransferDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final ClientRepository clientRepository;
    private final KeyRepository keyRepository;

    public UUID createTransfer(CreateTransferDto dto) {
        keyRepository.getKey(dto.keyId())
                .orElseThrow(() -> new ExceptionInApplication("Данного ключа не существует", ExceptionType.NOT_FOUND));

        var isDuplicate = !transferRepository.isNotDuplicate(dto.ownerId(), dto.receiverId(), dto.keyId());
        if (isDuplicate) {
            throw new ExceptionInApplication("Данная заявка уже существует", ExceptionType.INVALID);
        }

        var transferEntity = new TransferEntity(
                null,
                dto.ownerId(),
                dto.receiverId(),
                TransferStatus.IN_PROCESS,
                OffsetDateTime.now(),
                dto.keyId()
        );

        return transferRepository.createTransfer(transferEntity);
    }

    public List<TransferDto> getMyTransfers(GetMyTransfersDto dto) {
        var status = TransferStatus.getTransferStatusByName(dto.status());
        var transfers = transferRepository.getMyTransfers(dto.clientId(), status);

        return transfers.stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    public List<TransferDto> getForeignTransfer(GetForeignTransfersDto dto) {
        var status = TransferStatus.getTransferStatusByName(dto.status());
        var transfers = transferRepository.getForeignTransfers(dto.clientId(), status);

        return transfers.stream()
                .map(this::mapEntityToDto)
                .toList();
    }

    public void deleteMyTransfer(DeleteTransferDto dto) {
        var transfer = transferRepository.getTransferById(dto.transferId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка на передачу не найдена", ExceptionType.NOT_FOUND));

        if (!transfer.ownerId().equals(dto.clientId())) {
            throw new ExceptionInApplication("Вы не можете удалить чужую заяку", ExceptionType.INVALID);
        }

        if (transfer.status() != TransferStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Нельзя удалить заявку которая обработана", ExceptionType.INVALID);
        }

        transferRepository.deleteTransfer(dto.transferId());
    }

    public void acceptTransfer(AcceptTransferDto dto) {
        var transfer = transferRepository.getTransferById(dto.transferId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка на передачу не найдена", ExceptionType.NOT_FOUND));

        if (!transfer.receiverId().equals(dto.clientId())) {
            throw new ExceptionInApplication("Вы не можете принять чужую заяку", ExceptionType.INVALID);
        }

        if (transfer.status() != TransferStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Нельзя изменить статус уже у обработанной заяки", ExceptionType.INVALID);
        }

        var updatedTransfer = new TransferEntity(
                transfer.transferId(),
                transfer.ownerId(),
                transfer.receiverId(),
                TransferStatus.ACCEPTED,
                transfer.createdTime(),
                transfer.keyId()
        );
        transferRepository.updateTransfer(updatedTransfer);
        keyRepository.updateKeyHolder(transfer.keyId(), transfer.receiverId());
        transferRepository.declineNotActualTransfers(transfer.keyId());
    }

    public void declineTransfer(DeclineTransferDto dto) {
        var transfer = transferRepository.getTransferById(dto.transferId())
                .orElseThrow(() -> new ExceptionInApplication("Заявка на передачу не найдена", ExceptionType.NOT_FOUND));

        if (!transfer.receiverId().equals(dto.clientId())) {
            throw new ExceptionInApplication("Вы не можете отклонить чужую заяку", ExceptionType.INVALID);
        }

        if (transfer.status() != TransferStatus.IN_PROCESS) {
            throw new ExceptionInApplication("Нельзя изменить статус уже у обработанной заяки", ExceptionType.INVALID);
        }

        var updatedTransfer = new TransferEntity(
                transfer.transferId(),
                transfer.ownerId(),
                transfer.receiverId(),
                TransferStatus.DECLINED,
                transfer.createdTime(),
                transfer.keyId()
        );
        transferRepository.updateTransfer(updatedTransfer);
    }

    private TransferDto mapEntityToDto(TransferEntity entity) {
        var owner = clientRepository.getClientByClientId(entity.ownerId())
                .orElseThrow(() -> new ExceptionInApplication("Клиент не найден", ExceptionType.NOT_FOUND));

        var receiver = clientRepository.getClientByClientId(entity.receiverId())
                .orElseThrow(() -> new ExceptionInApplication("Клиент не найден", ExceptionType.NOT_FOUND));

        var key = keyRepository.getKey(entity.keyId())
                .orElseThrow(() -> new ExceptionInApplication("Ключа не найдено", ExceptionType.NOT_FOUND));

        return new TransferDto(
                entity.transferId(),
                entity.ownerId(),
                entity.receiverId(),
                entity.status(),
                entity.createdTime(),
                entity.keyId(),
                owner.name(),
                owner.email(),
                receiver.name(),
                receiver.email(),
                key.buildId(),
                key.roomId()
        );
    }
}
