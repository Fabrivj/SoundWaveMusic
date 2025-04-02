package com.cenfotec.soundwavemusic.controllers;

import com.cenfotec.soundwavemusic.models.Factura;
import com.cenfotec.soundwavemusic.models.Pedido;
import com.cenfotec.soundwavemusic.models.PedidoDetalle;
import com.cenfotec.soundwavemusic.repos.FacturaRepository;
import com.cenfotec.soundwavemusic.repos.PedidoDetalleRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/factura")
public class FacturaController {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    // Vista HTML con detalle completo de la factura
    @GetMapping("/ver/{id}")
    public String verFactura(@PathVariable int id, Model model) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Pedido pedido = factura.getPedido();
        List<PedidoDetalle> detalles = pedidoDetalleRepository.findByPedidoId(pedido.getId());

        model.addAttribute("factura", factura);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        return "factura_detalle";
    }

    // Descarga de la factura como PDF
    @GetMapping("/pdf/{id}")
    public void descargarPDF(@PathVariable int id, HttpServletResponse response) throws IOException {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        Pedido pedido = factura.getPedido();
        List<PedidoDetalle> detalles = pedidoDetalleRepository.findByPedidoId(pedido.getId());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura_" + factura.getId() + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font bold = new Font(Font.HELVETICA, 14, Font.BOLD);

        document.add(new Paragraph("Factura #" + factura.getId(), bold));
        document.add(new Paragraph("Fecha: " + factura.getFechaEmision()));
        document.add(new Paragraph("Cliente: " + pedido.getUsuario().getNombreUsuario()));
        document.add(new Paragraph("Dirección de envío: " + pedido.getDireccionEnvio()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell("Producto");
        table.addCell("Cantidad");
        table.addCell("Precio Unitario");
        table.addCell("Subtotal");

        for (PedidoDetalle d : detalles) {
            table.addCell(d.getProducto().getNombre_producto());
            table.addCell(String.valueOf(d.getCantidad()));
            table.addCell("₡" + d.getPrecioUnitario());
            table.addCell("₡" + d.getSubtotal());
        }

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Impuesto: ₡" + factura.getImpuesto()));
        document.add(new Paragraph("Total: ₡" + factura.getMontoTotal(), bold));

        document.close();
    }
}
