package com.daicom.daicombackend.common.audit;

import com.daicom.daicombackend.common.audit.dto.LogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogEntryRepository logEntryRepository;

    public LogController(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @GetMapping
    public ResponseEntity<Page<LogResponse>> getLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int page_size) {

        // el front pagina desde 1, Spring desde 0
        int zeroBasedPage = Math.max(page - 1, 0);

        // Retorna la lista paginada ordenada por los más recientes primero
        Page<LogEntry> logsPage = logEntryRepository.findAll(
                PageRequest.of(zeroBasedPage, page_size, Sort.by("createdAt").descending())
        );

        return ResponseEntity.ok(logsPage.map(LogResponse::new));
    }
}