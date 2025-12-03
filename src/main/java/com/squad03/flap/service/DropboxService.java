package com.squad03.flap.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.squad03.flap.DTO.DropboxFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DropboxService {

    @Autowired
    private DbxClientV2 dropboxClient;

    // ✅ 1. LISTAR ARQUIVOS DE UMA PASTA
    public List<DropboxFileDTO> listFolder(String path) throws DbxException {
        System.out.println("📂 Listando arquivos da pasta: " + path);

        List<DropboxFileDTO> files = new ArrayList<>();
        ListFolderResult result = dropboxClient.files().listFolder(path);

        for (Metadata metadata : result.getEntries()) {
            DropboxFileDTO fileDTO = new DropboxFileDTO();
            fileDTO.setId(metadata.getPathLower());
            fileDTO.setName(metadata.getName());
            fileDTO.setPath(metadata.getPathDisplay());

            if (metadata instanceof FolderMetadata) {
                fileDTO.setType("folder");
            } else if (metadata instanceof FileMetadata) {
                FileMetadata fileMetadata = (FileMetadata) metadata;
                fileDTO.setType("file");
                fileDTO.setSize(fileMetadata.getSize());

                Date modifiedDate = fileMetadata.getClientModified();
                if (modifiedDate != null) {
                    fileDTO.setModified(
                            LocalDateTime.ofInstant(modifiedDate.toInstant(), ZoneId.systemDefault())
                    );
                }
            }

            files.add(fileDTO);
        }

        System.out.println("✅ " + files.size() + " arquivos encontrados");
        return files;
    }

    // ✅ 2. OBTER LINK TEMPORÁRIO PARA PREVIEW/DOWNLOAD
    public String getTemporaryLink(String path) throws DbxException {
        System.out.println("🔗 Gerando link temporário para: " + path);

        GetTemporaryLinkResult result = dropboxClient.files().getTemporaryLink(path);
        String link = result.getLink();

        System.out.println("✅ Link gerado: " + link);
        return link;
    }

    // ✅ 3. FAZER UPLOAD DE ARQUIVO
    public DropboxFileDTO uploadFile(String path, String fileName, byte[] fileContent)
            throws DbxException, IOException {
        System.out.println("📤 Fazendo upload: " + fileName + " para " + path);

        String fullPath = path + "/" + fileName;

        try (InputStream inputStream = new ByteArrayInputStream(fileContent)) {
            FileMetadata metadata = dropboxClient.files()
                    .uploadBuilder(fullPath)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);

            DropboxFileDTO fileDTO = new DropboxFileDTO();
            fileDTO.setId(metadata.getPathLower());
            fileDTO.setName(metadata.getName());
            fileDTO.setPath(metadata.getPathDisplay());
            fileDTO.setType("file");
            fileDTO.setSize(metadata.getSize());

            Date modifiedDate = metadata.getClientModified();
            if (modifiedDate != null) {
                fileDTO.setModified(
                        LocalDateTime.ofInstant(modifiedDate.toInstant(), ZoneId.systemDefault())
                );
            }

            System.out.println("✅ Upload concluído: " + fullPath);
            return fileDTO;
        }
    }

    // ✅ 4. DOWNLOAD DE ARQUIVO
    public byte[] downloadFile(String path) throws DbxException, IOException {
        System.out.println("📥 Baixando arquivo: " + path);

        try (InputStream inputStream = dropboxClient.files().download(path).getInputStream()) {
            byte[] fileContent = inputStream.readAllBytes();
            System.out.println("✅ Download concluído: " + fileContent.length + " bytes");
            return fileContent;
        }
    }

    // ✅ 5. RENOMEAR/MOVER ARQUIVO
    public DropboxFileDTO renameFile(String fromPath, String toPath) throws DbxException {
        System.out.println("📝 Renomeando: " + fromPath + " → " + toPath);

        Metadata metadata = dropboxClient.files()
                .moveV2(fromPath, toPath)
                .getMetadata();

        DropboxFileDTO fileDTO = new DropboxFileDTO();
        fileDTO.setId(metadata.getPathLower());
        fileDTO.setName(metadata.getName());
        fileDTO.setPath(metadata.getPathDisplay());

        if (metadata instanceof FileMetadata) {
            FileMetadata fileMetadata = (FileMetadata) metadata;
            fileDTO.setType("file");
            fileDTO.setSize(fileMetadata.getSize());
        } else {
            fileDTO.setType("folder");
        }

        System.out.println("✅ Arquivo renomeado com sucesso");
        return fileDTO;
    }

    // ✅ 6. DELETAR ARQUIVO/PASTA
    public void deleteFile(String path) throws DbxException {
        System.out.println("🗑️ Deletando: " + path);

        dropboxClient.files().deleteV2(path);

        System.out.println("✅ Arquivo deletado com sucesso");
    }

    // ✅ 7. CRIAR PASTA
    public DropboxFileDTO createFolder(String path) throws DbxException {
        System.out.println("📁 Criando pasta: " + path);

        FolderMetadata metadata = dropboxClient.files().createFolderV2(path).getMetadata();

        DropboxFileDTO fileDTO = new DropboxFileDTO();
        fileDTO.setId(metadata.getPathLower());
        fileDTO.setName(metadata.getName());
        fileDTO.setPath(metadata.getPathDisplay());
        fileDTO.setType("folder");

        System.out.println("✅ Pasta criada com sucesso");
        return fileDTO;
    }
}
