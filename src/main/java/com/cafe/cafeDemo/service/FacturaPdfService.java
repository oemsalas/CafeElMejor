package com.cafe.cafeDemo.service;

import com.cafe.cafeDemo.model.Factura;
import com.cafe.cafeDemo.model.Producto;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class FacturaPdfService {

    // Colores del tema café
    private static final DeviceRgb COLOR_DARK    = new DeviceRgb(26,  15,  10);
    private static final DeviceRgb COLOR_BROWN   = new DeviceRgb(74,  44,  26);
    private static final DeviceRgb COLOR_WARM    = new DeviceRgb(196, 132, 63);
    private static final DeviceRgb COLOR_GOLD    = new DeviceRgb(212, 168, 83);
    private static final DeviceRgb COLOR_CREAM   = new DeviceRgb(245, 237, 224);
    private static final DeviceRgb COLOR_LIGHT   = new DeviceRgb(253, 248, 242);
    private static final DeviceRgb COLOR_GREEN   = new DeviceRgb(109, 191, 138);
    private static final DeviceRgb COLOR_RED     = new DeviceRgb(224, 112, 112);
    private static final DeviceRgb COLOR_MUTED   = new DeviceRgb(138, 115, 85);

    public byte[] generarFacturaPdf(Factura factura) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer   = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc       = new Document(pdfDoc, PageSize.A4);
        doc.setMargins(40, 50, 40, 50);

        PdfFont fontBold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontOblique = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

        // ── ENCABEZADO ──
        Table header = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);

        // Columna izquierda: logo + nombre
        Cell logoCell = new Cell().setBorder(Border.NO_BORDER);
        logoCell.add(new Paragraph("☕ CaféDemo")
                .setFont(fontBold).setFontSize(22).setFontColor(COLOR_DARK));
        logoCell.add(new Paragraph("Sistema de Gestión")
                .setFont(fontRegular).setFontSize(9).setFontColor(COLOR_MUTED));
        header.addCell(logoCell);

        // Columna derecha: FACTURA + número
        Cell facturaCell = new Cell().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
        facturaCell.add(new Paragraph("FACTURA")
                .setFont(fontBold).setFontSize(20).setFontColor(COLOR_WARM));
        facturaCell.add(new Paragraph(String.format("N° %04d", factura.getIdFactura()))
                .setFont(fontBold).setFontSize(14).setFontColor(COLOR_DARK));
        header.addCell(facturaCell);
        doc.add(header);

        // Línea separadora dorada
        doc.add(new LineSeparator(new SolidLine(2f))
                .setStrokeColor(COLOR_WARM)
                .setMarginTop(8).setMarginBottom(16));

        // ── DATOS: CLIENTE + FACTURA ──
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);

        // Bloque cliente
        Cell clienteBlock = new Cell().setBorder(Border.NO_BORDER)
                .setBackgroundColor(COLOR_LIGHT)
                .setPadding(12).setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6));
        clienteBlock.add(new Paragraph("CLIENTE")
                .setFont(fontBold).setFontSize(8).setFontColor(COLOR_MUTED)
                .setCharacterSpacing(1.5f));
        if (factura.getCliente() != null) {
            clienteBlock.add(new Paragraph(factura.getCliente().getNombre())
                    .setFont(fontBold).setFontSize(12).setFontColor(COLOR_DARK).setMarginTop(4));
            clienteBlock.add(new Paragraph("DNI: " + factura.getCliente().getDni())
                    .setFont(fontRegular).setFontSize(10).setFontColor(COLOR_MUTED));
            if (factura.getCliente().getTelefono() != 0)
                clienteBlock.add(new Paragraph("Tel: " + factura.getCliente().getTelefono())
                        .setFont(fontRegular).setFontSize(10).setFontColor(COLOR_MUTED));
            if (factura.getCliente().getDireccion() != null)
                clienteBlock.add(new Paragraph(factura.getCliente().getDireccion())
                        .setFont(fontRegular).setFontSize(10).setFontColor(COLOR_MUTED));
        }
        infoTable.addCell(clienteBlock);

        // Bloque datos de la factura
        Cell facturaBlock = new Cell().setBorder(Border.NO_BORDER)
                .setBackgroundColor(COLOR_LIGHT)
                .setPadding(12).setPaddingLeft(20)
                .setBorderRadius(new com.itextpdf.layout.properties.BorderRadius(6));
        facturaBlock.add(new Paragraph("DATOS DE FACTURA")
                .setFont(fontBold).setFontSize(8).setFontColor(COLOR_MUTED)
                .setCharacterSpacing(1.5f));
        facturaBlock.add(infoRow("Fecha emisión:", factura.getFechaEmision() != null
                ? factura.getFechaEmision().toString() : "—", fontBold, fontRegular));
        facturaBlock.add(infoRow("Estado:", factura.getEstado(), fontBold, fontRegular));
        if (factura.getCobranza() != null) {
            facturaBlock.add(infoRow("Método de pago:", factura.getCobranza().getMetodoDePago()
                    .replace("_", " "), fontBold, fontRegular));
        }
        facturaBlock.add(infoRow("Impuestos:", String.format("$%.2f", (double) factura.getImpuestos()),
                fontBold, fontRegular));
        infoTable.addCell(facturaBlock);
        doc.add(infoTable);

        doc.add(new Paragraph("").setMarginTop(20));

        // ── TABLA DE PRODUCTOS ──
        doc.add(new Paragraph("DETALLE DE PRODUCTOS")
                .setFont(fontBold).setFontSize(9).setFontColor(COLOR_MUTED)
                .setCharacterSpacing(1.5f).setMarginBottom(8));

        Table productosTable = new Table(UnitValue.createPercentArray(new float[]{10, 55, 15, 20}))
                .setWidth(UnitValue.createPercentValue(100));

        // Encabezados de tabla
        String[] headers = {"#", "Descripción", "Lote", "Precio"};
        for (String h : headers) {
            productosTable.addHeaderCell(new Cell()
                    .setBackgroundColor(COLOR_DARK)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(8)
                    .add(new Paragraph(h)
                            .setFont(fontBold).setFontSize(9)
                            .setFontColor(COLOR_GOLD)));
        }

        // Filas de productos
        java.util.List<Producto> productos = factura.getProductos();
        double subtotal = 0;
        if (productos != null && !productos.isEmpty()) {
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                DeviceRgb rowBg = (i % 2 == 0) ? new DeviceRgb(255, 255, 255) : new DeviceRgb(250, 246, 240);
                productosTable.addCell(productoCell(String.valueOf(i + 1), fontRegular, rowBg, TextAlignment.CENTER));
                productosTable.addCell(productoCell(p.getDescripcion() != null ? p.getDescripcion() : "—", fontRegular, rowBg, TextAlignment.LEFT));
                productosTable.addCell(productoCell(p.getLote() != null ? p.getLote() : "—", fontOblique, rowBg, TextAlignment.CENTER));
                productosTable.addCell(productoCell(String.format("$%.2f", p.getPrecioVenta()), fontBold, rowBg, TextAlignment.RIGHT));
                subtotal += p.getPrecioVenta();
            }
        } else {
            Cell emptyCell = new Cell(1, 4)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .add(new Paragraph("Sin productos asociados")
                            .setFont(fontOblique).setFontSize(10).setFontColor(COLOR_MUTED));
            productosTable.addCell(emptyCell);
        }
        doc.add(productosTable);

        // ── TOTALES ──
        doc.add(new Paragraph("").setMarginTop(12));
        Table totalesTable = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                .setWidth(UnitValue.createPercentValue(100))
                .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                .setBorder(Border.NO_BORDER);

        totalesTable.addCell(totalLabel("Subtotal:", fontRegular));
        totalesTable.addCell(totalValue(String.format("$%.2f", subtotal), fontRegular, false));

        totalesTable.addCell(totalLabel("Impuestos:", fontRegular));
        totalesTable.addCell(totalValue(String.format("$%.2f", (double) factura.getImpuestos()), fontRegular, false));

        // Línea separadora
        totalesTable.addCell(new Cell(1, 2).setBorder(Border.NO_BORDER)
                .add(new LineSeparator(new SolidLine(1f)).setStrokeColor(COLOR_WARM)));

        totalesTable.addCell(totalLabel("TOTAL:", fontBold));
        totalesTable.addCell(totalValue(String.format("$%.2f", (double) factura.getTotal()), fontBold, true));
        doc.add(totalesTable);

        // ── ESTADO VISUAL ──
        doc.add(new Paragraph("").setMarginTop(20));
        DeviceRgb estadoColor = "PAGADA".equals(factura.getEstado()) ? COLOR_GREEN : COLOR_RED;
        DeviceRgb estadoBg    = "PAGADA".equals(factura.getEstado())
                ? new DeviceRgb(220, 245, 230) : new DeviceRgb(250, 225, 225);
        doc.add(new Paragraph("Estado: " + factura.getEstado())
                .setFont(fontBold).setFontSize(12).setFontColor(estadoColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(estadoBg)
                .setPadding(10)
                .setBorder(new SolidBorder(estadoColor, 1)));

        // ── PIE ──
        doc.add(new LineSeparator(new SolidLine(1f))
                .setStrokeColor(COLOR_MUTED)
                .setMarginTop(24).setMarginBottom(8));
        doc.add(new Paragraph("CaféDemo — Sistema de Gestión  |  Generado el "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setFont(fontOblique).setFontSize(8).setFontColor(COLOR_MUTED)
                .setTextAlignment(TextAlignment.CENTER));

        doc.close();
        return baos.toByteArray();
    }

    // ── Helpers ──

    private Paragraph infoRow(String label, String value, PdfFont bold, PdfFont regular) {
        return new Paragraph()
                .add(new com.itextpdf.layout.element.Text(label + " ")
                        .setFont(bold).setFontSize(9).setFontColor(COLOR_MUTED))
                .add(new com.itextpdf.layout.element.Text(value)
                        .setFont(regular).setFontSize(9).setFontColor(COLOR_DARK))
                .setMarginTop(4);
    }

    private Cell productoCell(String text, PdfFont font, DeviceRgb bg, TextAlignment align) {
        return new Cell()
                .setBackgroundColor(bg)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(COLOR_CREAM, 0.5f))
                .setPadding(7)
                .setTextAlignment(align)
                .add(new Paragraph(text).setFont(font).setFontSize(9).setFontColor(COLOR_DARK));
    }

    private Cell totalLabel(String text, PdfFont font) {
        return new Cell()
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(6).setPaddingRight(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph(text).setFont(font).setFontSize(10).setFontColor(COLOR_MUTED));
    }

    private Cell totalValue(String text, PdfFont font, boolean highlight) {
        Cell cell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(6)
                .setTextAlignment(TextAlignment.RIGHT);
        Paragraph p = new Paragraph(text).setFont(font)
                .setFontSize(highlight ? 13 : 10)
                .setFontColor(highlight ? COLOR_WARM : COLOR_DARK);
        cell.add(p);
        return cell;
    }
}
