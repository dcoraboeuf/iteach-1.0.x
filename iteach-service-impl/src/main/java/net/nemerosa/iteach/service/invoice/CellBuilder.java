package net.nemerosa.iteach.service.invoice;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

public class CellBuilder {

    private String text = "";
    private Font font = new Font();
    private int align = Element.ALIGN_LEFT;
    private float borderWith = 0;
    private float padding = 1f;
    private int colspan = 1;
    private int rowspan = 1;

    public static CellBuilder create() {
        return new CellBuilder();
    }

    public CellBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CellBuilder withAlign(int align) {
        this.align = align;
        return this;
    }

    public CellBuilder withPadding(float padding) {
        this.padding = padding;
        return this;
    }

    public CellBuilder withBorderWidth(float borderWidth) {
        this.borderWith = borderWidth;
        return this;
    }

    public CellBuilder withFont(Font font) {
        this.font = font;
        return this;
    }

    public CellBuilder withColspan(int colspan) {
        this.colspan = colspan;
        return this;
    }

    public CellBuilder withRowspan(int rowspan) {
        this.rowspan = rowspan;
        return this;
    }

    public PdfPCell done() {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(align);
        cell.setBorderWidth(borderWith);
        cell.setPadding(padding);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        return cell;
    }
}
