package br.com.rodrigo.api.controleestoque.rest;

import br.com.rodrigo.api.controleestoque.exception.MensagensError;
import br.com.rodrigo.api.controleestoque.exception.ObjetoNaoEncontradoException;
import br.com.rodrigo.api.controleestoque.model.response.ItemVendaResponse;
import br.com.rodrigo.api.controleestoque.model.response.VendaResponse;
import br.com.rodrigo.api.controleestoque.service.IVenda;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class CupomFiscalController {

    private final IVenda vendaService;

    @GetMapping("/{id}/cupom")
    public ResponseEntity<byte[]> gerarCupomFiscal(@PathVariable Long id) throws DocumentException {
        VendaResponse response = vendaService.buscarPorId(id)
                .orElseThrow(() -> new ObjetoNaoEncontradoException(
                        MensagensError.VENDA_NAO_ENCONTRADA.getMessage(id)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");
        String dataFormatada = response.getCriadoEm().format(formatter);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Rectangle pageSize = new Rectangle(165, 400);
        Document document = new Document(pageSize, 10, 10, 10, 10);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Font font = new Font(Font.COURIER, 8);

        document.add(new Paragraph("**** CUPOM FISCAL ****", font));
        document.add(new Paragraph("Data: " + dataFormatada, font));

        document.add(new Paragraph("------------ITENS-------------", font));

        BigDecimal valorBruto = BigDecimal.ZERO;
        for (ItemVendaResponse item : response.getItens()) {
            String linha = formatLinhaItem(item.getProduto().getDescricao(), item.getQuantidade(), item.getValorTotal());
            document.add(new Paragraph(linha, font));
            valorBruto = valorBruto.add(item.getValorTotal());
        }

        document.add(new Paragraph("-----------VALORES------------", font));
        document.add(new Paragraph("Pagamento: " + response.getFormaDePagamento().getNome(), font));
        document.add(new Paragraph("Subtotal: R$ " + String.format("%.2f", valorBruto), font));
        document.add(new Paragraph("Acréscimo: " + response.getFormaDePagamento().getPorcentagemAcrescimo()
                .stripTrailingZeros().toPlainString() + "%", font));
        document.add(new Paragraph("Valor Total: R$ " + String.format("%.2f", response.getValorTotal()), font));
        document.add(new Paragraph("\n\nObrigado pela preferência!", font));
        document.close();

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "cupom_fiscal.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private String formatLinhaItem(String nome, int qtd, BigDecimal preco) {
        String nomeFormatado = nome.length() > 15 ? nome.substring(0, 15) : String.format("%-15s", nome);
        String qtdStr = String.format("%2d", qtd);
        String valorStr = String.format("%6.2f", preco);
        return nomeFormatado + " " + qtdStr + " x " + valorStr;
    }
}
