package org.example.key_info.rest.controller.transfer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.key_info.core.transfer.TransferService;
import org.example.key_info.public_interface.transfer.AcceptTransferDto;
import org.example.key_info.public_interface.transfer.CreateTransferDto;
import org.example.key_info.public_interface.transfer.DeclineTransferDto;
import org.example.key_info.public_interface.transfer.DeleteTransferDto;
import org.example.key_info.public_interface.transfer.GetForeignTransfersDto;
import org.example.key_info.public_interface.transfer.GetMyTransfersDto;
import org.example.key_info.public_interface.transfer.TransferDto;
import org.example.key_info.rest.util.JwtTools;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers/requests")
@Tag(name = "Для работы с запросами на передачу ключа между пользователями")
public class RestTransferRequestController {
    private final TransferService transferService;
    private final JwtTools jwtTools;

    @Operation(summary = "Создать запрос на передачу ключа другому пользователю")
    @PostMapping("/{receiver_id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UUID> create(@PathVariable(name = "receiver_id") UUID receiverId,
                                       @RequestBody CreateTransferRequest createTransferRequest) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var createTransferDto = new CreateTransferDto(
                infoAboutClient.clientId(),
                receiverId,
                createTransferRequest.keyId()
        );
        var transferId = transferService.createTransfer(createTransferDto);

        return ResponseEntity.ok(transferId);
    }

    @Operation(summary = "Получить запросы, отправленные другим пользователям")
    @GetMapping("/my")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TransferResponseDto>> getMyRequests(@RequestParam(required = false, name = "status_transfer_request", defaultValue = "IN_PROCESS") String status) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var getMyTransfers = new GetMyTransfersDto(infoAboutClient.clientId(), status);
        var transfers = transferService.getMyTransfers(getMyTransfers);

        var body = transfers.parallelStream()
                .map(this::mapDtoToResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Получить запросы от пользователей, которые хотят передать мне ключ")
    @GetMapping("/foreign")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<TransferResponseDto>> getForeignRequests(@RequestParam(required = false, name = "status_transfer_request", defaultValue = "IN_PROCESS") String status) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var getForeignTransfers = new GetForeignTransfersDto(infoAboutClient.clientId(), status);
        var transfers = transferService.getForeignTransfer(getForeignTransfers);

        var body = transfers.parallelStream()
                .map(this::mapDtoToResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Удалить мой запрос на передачу ключа")
    @DeleteMapping()
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteMyRequest(@RequestParam(name = "transfer_request_id") UUID transferRequestId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var deleteTransferDto = new DeleteTransferDto(infoAboutClient.clientId(), transferRequestId);
        transferService.deleteMyTransfer(deleteTransferDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Принять запрос на передачу мне ключа")
    @PatchMapping("/{transfer_request_id}/accept")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> acceptTransferRequest(@PathVariable(name = "transfer_request_id") UUID transferRequestId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var acceptTransferDto = new AcceptTransferDto(infoAboutClient.clientId(), transferRequestId);
        transferService.acceptTransfer(acceptTransferDto);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отклонить запрос на передачу мне ключа")
    @PatchMapping("/{transfer_request_id}/decline")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> declineTransferRequest(@PathVariable(name = "transfer_request_id") UUID transferRequestId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var infoAboutClient = jwtTools.getClientInfoFromAccessToken(auth);

        var declineTransferDto = new DeclineTransferDto(infoAboutClient.clientId(), transferRequestId);
        transferService.declineTransfer(declineTransferDto);

        return ResponseEntity.ok().build();
    }

    private TransferResponseDto mapDtoToResponse(TransferDto dto) {
        return new TransferResponseDto(
                dto.keyId(),
                dto.ownerId(),
                dto.receiverId(),
                dto.status().name(),
                dto.ownerName(),
                dto.ownerEmail(),
                dto.receiverName(),
                dto.receiverEmail(),
                dto.transferId()
        );
    }
}
