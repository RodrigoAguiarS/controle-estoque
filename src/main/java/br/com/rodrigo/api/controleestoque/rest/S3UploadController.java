package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3UploadController extends ControllerBase<Map<String, String>> {

    private final S3StorageService s3StorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3StorageService.uploadArquivo(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return responderItemCriado(response);
        } catch (IOException e) {
            return responderRequisicaoMalSucedida();
        }
    }

    @DeleteMapping("/apagar/{fileName}")
    public ResponseEntity<Void> deleteImage(@PathVariable String fileName) {
        try {
            s3StorageService.apagarArquivo(fileName);
            return responderSemConteudo();
        } catch (RuntimeException e) {
            return responderRequisicaoMalSucedidaSemConteudo();
        }
    }
}
