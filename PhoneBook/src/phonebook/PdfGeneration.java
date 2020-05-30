package phonebook;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import javafx.collections.ObservableList;

public class PdfGeneration {
    
    public PdfGeneration() {
    }
    
    public PdfGeneration(String filename, ObservableList<Person> data){
        //ez maga a pdf file
        Document document = new Document();
        try{
            //üres dokumentum jön létre    //hová mentem
            PdfWriter.getInstance(document, new FileOutputStream(filename + ".pdf")); 
            //megnyitom az üres doksit
            document.open();
            //kép létrehozása
            Image image1 = Image.getInstance(getClass().getResource("/pdfkep.jpeg"));
            //méretezés
            image1.scaleToFit(400,172);
            //pozíció
            image1.setAbsolutePosition(200f, 650f);
            //hozzáadjuk a képet a doksihoz
            document.add(image1);
            //szöveg hozzáadása
            //document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n" + text, FontFactory.getFont("betutipus", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)));
            //a táblázat ne csússzon rá a logóra
            document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n\n\n"));
            //táblázat hozzáadása
            //oszlopszélességek tárolása 
            float[] columnWidth = {2,4,4,6};
            //létrhozzuk a táblát és átadjuk az oszlopszélességeket
            PdfPTable table = new PdfPTable(columnWidth);
            //a teljes táblázat milyen széles legyen
            table.setWidthPercentage(100);
            //fejléc létrehozása
            PdfPCell cell = new PdfPCell(new Phrase("Kontakt Lista"));
            //háttérszín
            cell.setBackgroundColor(GrayColor.GRAYWHITE);
            //align
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            //cella egyesítés
            cell.setColspan(4);
            //hozzáadás a táblához
            table.addCell(cell);
            //a többi cella alapértelmezett színe
            table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            //cím cellák
            table.addCell("Sorszám");
            table.addCell("Vezetéknév");
            table.addCell("Keresztnév");
            table.addCell("Email cím");
            //header sorok száma
            table.setHeaderRows(1);
            //további cellák színei
            table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            //adatfeltöltés
                for (int i = 1; i <= data.size(); i++) {
                Person actualPerson = data.get(i-1);
                table.addCell("" + i);
                table.addCell(actualPerson.getLastName());
                table.addCell(actualPerson.getFirstName());
                table.addCell(actualPerson.getEmail());
            }
            //táblázat doksihoz adása
            document.add(table);
            //aláírás, chunk -> db valami létrehozásásra jó
            Chunk alairas = new Chunk("\n\nGenerálva a Telefonkönyv alkalmazás segítségével.");
            //csinálunk egy paragrafust az aláírásnak
            Paragraph base = new Paragraph(alairas);
            //aláírás doksihoz adása
            document.add(base);
        }catch(Exception e){ 
            e.printStackTrace();
        }
        //mindig le kell zárni a dokumentumot!
        document.close();
    }


}
