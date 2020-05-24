package phonebook;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PdfGeneration {
    
    public PdfGeneration() {
    }
    
    public PdfGeneration(String filename, String text){
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
            image1.scaleToFit(200,56);
            //pozíció
            image1.setAbsolutePosition(200f, 750f);
            //hozzáadjuk a képet a doksihoz
            document.add(image1);
            //szöveg hozzáadása
            document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n" + text, FontFactory.getFont("betutipus", BaseFont.IDENTITY_H, BaseFont.EMBEDDED)));
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
