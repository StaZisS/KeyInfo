package org.example.key_info.rest.controller.transfer;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers/requests")
public class RestTransferRequestController {

    @PostMapping("/{receiver_id}")
    public ResponseEntity<Void> create(@RequestHeader("Authorization") String accessToken,
                                 @PathVariable(name = "receiver_id") UUID receiverId,
                                 @RequestBody CreateTransferRequest createTransferRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping()
    public ResponseEntity<List<GetTransferRequestDto>> getMyRequests(@RequestHeader("Authorization") String accessToken,
                                                                     @RequestParam(required = false, name = "status_transfer_request") String status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteMyRequest(@RequestHeader("Authorization") String accessToken,
                                                @RequestParam(name = "transfer_request_id") UUID transferRequestId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PatchMapping("/{transfer_request_id}/status")
    public ResponseEntity<Void> acceptTransferRequest(@RequestHeader("Authorization") String accessToken,
                                                      @PathVariable(name = "transfer_request_id") UUID transferRequestId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
