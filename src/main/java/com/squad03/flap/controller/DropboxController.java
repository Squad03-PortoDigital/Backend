package com.squad03.flap.controller;

import com.dropbox.core.DbxException;
import com.squad03.flap.DTO.DropboxFileDTO;
import com.squad03.flap.DTO.DropboxRenameRequest;
import com.squad03.flap.service.DropboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dropbox")
@CrossOrigin(origins = "*")
@Tag(name = "Dropbox", description = "API para integração com Dropbox")
public class DropboxController {

    @Autowired
    private DropboxService dropboxService;

    // ✅ 1. LISTAR ARQUIVOS DE UMA PASTA
    @GetMapping("/files")
    @Operation(summary = "Listar arquivos de uma pasta", description = "Retorna lista de arquivos e pastas do Dropbox")
    public ResponseEntity<?> listFiles(
            @RequestParam(defaultValue = "") String path) {
        try {
            List<DropboxFileDTO> files = dropboxService.listFolder(path);
            return ResponseEntity.ok(files);
        } catch (DbxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao listar arquivos: " + e.getMessage()));
        }
    }

    // ✅ 2. OBTER LINK TEMPORÁRIO PARA PREVIEW/DOWNLOAD
    @GetMapping("/preview")
    @Operation(summary = "Obter link de preview", description = "Gera link temporário (4h) para preview/download")
    public ResponseEntity<?> getPreviewLink(@RequestParam String path) {
        try {
            String link = dropboxService.getTemporaryLink(path);
            return ResponseEntity.ok(Map.of("url", link));
        } catch (DbxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao gerar link: " + e.getMessage()));
        }
    }

    // ✅ 3. UPLOAD DE ARQUIVO
    @PostMapping("/upload")
    @Operation(summary = "Fazer upload de arquivo", description = "Envia arquivo para o Dropbox")
    public ResponseEntity<?> uploadFile(
            @RequestParam String path,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Arquivo vazio"));
            }

            DropboxFileDTO uploadedFile = dropboxService.uploadFile(
                    path,
                    file.getOriginalFilename(),
                    file.getBytes()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedFile);
        } catch (DbxException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao fazer upload: " + e.getMessage()));
        }
    }

    // ✅ 4. DOWNLOAD DE ARQUIVO
    @GetMapping("/download")
    @Operation(summary = "Baixar arquivo", description = "Faz download de arquivo do Dropbox")
    public ResponseEntity<?> downloadFile(@RequestParam String path) {
        try {
            byte[] fileContent = dropboxService.downloadFile(path);

            String fileName = path.substring(path.lastIndexOf("/") + 1);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileContent);
        } catch (DbxException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao baixar arquivo: " + e.getMessage()));
        }
    }

    // ✅ 5. RENOMEAR/MOVER ARQUIVO
    @PatchMapping("/rename")
    @Operation(summary = "Renomear arquivo", description = "Renomeia ou move arquivo no Dropbox")
    public ResponseEntity<?> renameFile(@RequestBody DropboxRenameRequest request) {
        try {
            DropboxFileDTO renamedFile = dropboxService.renameFile(
                    request.getFromPath(),
                    request.getToPath()
            );
            return ResponseEntity.ok(renamedFile);
        } catch (DbxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao renomear arquivo: " + e.getMessage()));
        }
    }

    // ✅ 6. DELETAR ARQUIVO/PASTA
    @DeleteMapping("/delete")
    @Operation(summary = "Deletar arquivo", description = "Deleta arquivo ou pasta do Dropbox")
    public ResponseEntity<?> deleteFile(@RequestParam String path) {
        try {
            dropboxService.deleteFile(path);
            return ResponseEntity.ok(Map.of("message", "Arquivo deletado com sucesso"));
        } catch (DbxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao deletar arquivo: " + e.getMessage()));
        }
    }

    // ✅ 7. CRIAR PASTA
    @PostMapping("/folder")
    @Operation(summary = "Criar pasta", description = "Cria nova pasta no Dropbox")
    public ResponseEntity<?> createFolder(@RequestParam String path) {
        try {
            DropboxFileDTO folder = dropboxService.createFolder(path);
            return ResponseEntity.status(HttpStatus.CREATED).body(folder);
        } catch (DbxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao criar pasta: " + e.getMessage()));
        }
    }
}
