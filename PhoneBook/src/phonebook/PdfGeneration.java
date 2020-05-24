package phonebook;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public void PdfGeneration {
    private void pdfGeneration(String filename, String text){
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
            document.add(image1);
        }catch(Exception e){            
        }
    }
}
