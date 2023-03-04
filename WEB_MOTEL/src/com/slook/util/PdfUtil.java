/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slook.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.motel.model.Bill;
import com.motel.model.BillService;
import com.slook.model.Member;
import com.slook.model.MemberPayment;
import com.slook.model.Membership;
import com.slook.model.ServiceTicket;
import com.slook.object.PaymentPackObj;
import com.slook.object.PrintPaymentForm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author VietNV
 */
public class PdfUtil
{

    public static InputStream excel2pdf(InputStream input_document, String outPath) throws Exception
    {
        //First we read the Excel file in binary format into FileInputStream
//                FileInputStream input_document = new FileInputStream(new File("C:\\excel_to_pdf.xls"));
        // Read workbook into HSSFWorkbook
        HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
        // Read worksheet into HSSFSheet
        HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
        // To iterate over the rows
        Iterator<Row> rowIterator = my_worksheet.iterator();
        //We will create output PDF document objects at this point
        Document iText_xls_2_pdf = new Document();
        PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(outPath));
        iText_xls_2_pdf.open();
        //we have two columns in the Excel sheet, so we create a PDF table with two columns
        //Note: There are ways to make this dynamic in nature, if you want to.
        PdfPTable my_table = new PdfPTable(3);
        //We will use the object below to dynamically add new data to the table
        PdfPCell table_cell;
        //Loop through rows.
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next(); //Fetch CELL
                switch (cell.getCellType())
                { //Identify CELL type
                    //you need to add more code here based on
                    //your requirement / transformations
                    case Cell.CELL_TYPE_STRING:
                        //Push the data from Excel to PDF Cell
                        table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
                        //feel free to move the code below to suit to your needs
                        my_table.addCell(table_cell);
                        break;
                }
                //next line
            }

        }
        //Finally add the table to PDF document
        iText_xls_2_pdf.add(my_table);
        iText_xls_2_pdf.close();
        //we created our pdf file..
        input_document.close(); //close xls

        InputStream stream = new FileInputStream(outPath);
        return stream;
    }

    private static final String FILE_NAME = "D:\\Downloads\\itext.pdf";

    public static void main(String[] args) throws FileNotFoundException, Exception
    {
        /*
        String pathIn = "D:\\Downloads\\Template_serviceTicket_D.xls";
        String pathout = "D:\\Downloads\\Template_serviceTicket_Convert.pdf";
        FileInputStream file = new FileInputStream(new File(pathIn));
        // Workbook wbTemplate = new HSSFWorkbook(file);

//        test2(pathIn, pathout);
        excel2pdf(file, pathout);

        // writeUsingIText();
        Member curMember = new Member();
        curMember.setMemberName("vit");
        Membership membership = new Membership();
        membership.setGroupPack(new CatGroupPack());
        membership.getGroupPack().setGroupPackName("abc");
        MemberPayment mp = new MemberPayment();
        mp.setPaymentValue(10000l);
        mp.setPaymentCode("1010101010");
        mp.setCreateTime(new Date());

        ServiceTicket tk = new ServiceTicket(1l, "tick 1", 1l);
        ServiceTicket tk2 = new ServiceTicket(2l, "tick 2", 2l);
        ServiceTicket tk3 = new ServiceTicket(3l, "tick 3", 3l);
        ServiceTicket tk4 = new ServiceTicket(4l, "tick4", 4l);
        List<ServiceTicket> lstTicket = Arrays.asList(tk, tk2, tk3, tk4,
                tk2, tk3, tk4,
                tk2, tk3, tk4,
                tk2, tk3, tk4,
                tk2, tk3, tk4,
                tk2, tk3, tk4);
        createPdfTicket(curMember, membership, "D:\\Downloads\\ticket.pdf", mp, lstTicket,
                "E:\\SLOOK_PROJECT\\GYM\\SOURCE\\WEB\\WebContent\\resources\\olympos-layout\\fonts\\times.ttf");
         */
        PrintPaymentForm printForm = new PrintPaymentForm();
        printForm.setPaymentCode("123132");
        printForm.setCustomerType(Constant.PAYMENT_TYPE.MEMBER);
        String path = generateBillPdf(printForm);
        System.out.println("ok: " + path);
    }

    private static void writeUsingIText()
    {

        Document document = new Document();

        try
        {

            PdfWriter.getInstance(document, new FileOutputStream(new File(FILE_NAME)));

            //open
            document.open();

            Paragraph p = new Paragraph();
            p.add("This is my paragraph 1");
            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            Paragraph p2 = new Paragraph();
            p2.add("This is my paragraph 2"); //no alignment

            document.add(p2);

            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);

            document.add(new Paragraph("This is my paragraph 3", f));

            //close
            document.close();

            System.out.println("Done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static void test2(String sourcepath, String destinationPath) throws FileNotFoundException, DocumentException
    {
        FileInputStream filecontent = new FileInputStream(new File(sourcepath));
        FileOutputStream out = new FileOutputStream(new File(destinationPath));
        HSSFWorkbook my_xls_workbook = null;
        HSSFSheet my_worksheet = null;
        XSSFWorkbook my_xlsx_workbook = null;
        XSSFSheet my_worksheet_xlsx = null;
        Document document = new Document(PageSize.ARCH_E, 0, 0, 0, 0);
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        PdfDestination magnify = null;
        float magnifyOpt = (float) 70.0;
        magnify = new PdfDestination(PdfDestination.XYZ, -1, -1, magnifyOpt / 100);
        int pageNumberToOpenTo = 1;
        PdfAction zoom = PdfAction.gotoLocalPage(pageNumberToOpenTo, magnify, writer);
        writer.setOpenAction(zoom);
        document.addDocListener(writer);

        Iterator<Row> rowIterator = null;
        int maxColumn = 0;
        if (sourcepath.endsWith("xls"))
        {
            try
            {
                my_xls_workbook = new HSSFWorkbook(filecontent);
                my_worksheet = my_xls_workbook.getSheetAt(0);
                rowIterator = my_worksheet.iterator();
                maxColumn = my_worksheet.getRow(0).getLastCellNum();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        if (sourcepath.endsWith(".xlsx"))
        {
            try
            {
                my_xlsx_workbook = new XSSFWorkbook(filecontent);
                my_worksheet_xlsx = my_xlsx_workbook.getSheetAt(0);
                rowIterator = my_worksheet_xlsx.iterator();
                maxColumn = my_worksheet_xlsx.getRow(0).getLastCellNum();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();

            }
        }
        PdfPTable my_table = new PdfPTable(maxColumn);
        my_table.setHorizontalAlignment(Element.ALIGN_CENTER);
        my_table.setWidthPercentage(100);
        my_table.setSpacingBefore(0f);
        my_table.setSpacingAfter(0f);
        PdfPCell table_cell;
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext())
            {
                Cell cell = cellIterator.next(); //Fetch CELL
                switch (cell.getCellType())
                { //Identify CELL type
                    case Cell.CELL_TYPE_STRING:
                        //Push the data from Excel to PDF Cell
                        table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
                        if (row.getRowNum() == 0)
                        {
                            table_cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            table_cell.setBorderColor(BaseColor.BLACK);
                        }
                        my_table.addCell(table_cell);
                        break;
                }
            }
        }
        document.add(my_table);
        document.close();
        System.out.println("Excel file converted to PDF successfully");
    }

    public static void createPdfTicket(Member curMember, Membership membership,
                                       String resultPath, MemberPayment mp, List<ServiceTicket> lstTicket,
                                       String pathFont)
    {
        try
        {
            System.out.println("start tao file anh:" + resultPath);
            //tao file ma
            String centerName = "Trung Tâm Hoa Sen Spa";
            String perserviceName = "Phiếu Dịch Vụ: ";
            String serviceName = perserviceName + membership.getGroupPack().getGroupPackName();
            String customerName = "Khách Hàng : " + curMember.getMemberName() + "";
            String customerUsed = "Khách hàng Sử Dụng DV:…………………………";
            String paymentValue = "Số Tiền: " + DataUtil.getStringNumber(mp.getPaymentValue()) + " VNĐ";
            String paymentCode = "Mã Thanh Toán: " + mp.getPaymentCode();
            String paymentDate = "Ngày Thanh Toán: " + DateTimeUtils.format(mp.getCreateTime(), "dd/MM/yyyy");
            String paymentDateUsed = "Ngày Sử dụng:……………………………………";
            //tao file pdf
            Document document = new Document();
//        String outPath = Util.getRealPath(resultPath) + "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
//        String outPath = resultPath + "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(resultPath));

            document.open();
            document.setMargins(1, 1, 10, 1);
            float[] columnWidths = {4.9f, 0.2f, 4.9f};
            PdfPTable table = new PdfPTable(columnWidths);
//                addTableHeader(table);
            BaseFont bf = BaseFont.createFont(pathFont, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            Font normalFont = new Font(bf, 10);

            PdfPCell pdfSp = new PdfPCell();
            pdfSp.setPadding(0);
            pdfSp.setBorder(0);
            pdfSp.rectangle(0, 0);
            pdfSp.addElement(new Phrase(" "));
            for (int i = 0; i < lstTicket.size(); i++)
            {

//                addRows(table);
                String seria1 = "Seri phiếu: ";
                String seria2 = lstTicket.get(i).getServiceTicketCode();
                String seria3 = "…… Số Lần: ………………";
                String seria = seria1 + seria2 + seria3;
//            String seria = "Seri phiếu: " + lstTicket.get(i).getServiceTicketCode() + "…… Số Lần: ………………";
//            PdfPTable tb = new PdfPTable(1);
//            tb.setWidthPercentage(100);
                Paragraph paragraph = new Paragraph();
                paragraph.setAlignment(Element.ALIGN_CENTER);
                paragraph.setFont(boldFont);
                Phrase firstLine = new Phrase(centerName, boldFont);
                paragraph.add(firstLine);

                Paragraph p2 = new Paragraph();
                p2.setAlignment(Element.ALIGN_CENTER);
                p2.add(new Phrase(perserviceName, normalFont));
                p2.add(new Phrase(membership.getGroupPack().getGroupPackName(), new Font(bf, 10, Font.BOLD)));

//            Phrase secondLine = new Phrase(serviceName, normalFont);
                Phrase p3 = new Phrase(customerName, normalFont);
                Phrase p4 = new Phrase(customerUsed, normalFont);

                Paragraph p5 = new Paragraph();
                p5.add(new Phrase(seria1, normalFont));
                p5.add(new Phrase(seria2, new Font(bf, 10, Font.BOLD)));
                p5.add(new Phrase(seria3, normalFont));

//            Phrase p5 = new Phrase(seria, normalFont);
                Phrase p6 = new Phrase(paymentValue, normalFont);
                Phrase p7 = new Phrase(paymentCode, normalFont);
                Phrase p8 = new Phrase(paymentDate, normalFont);
                Phrase p9 = new Phrase(paymentDateUsed, normalFont);

                PdfPCell pdfWordCell = new PdfPCell();
                pdfWordCell.setPadding(5);
                pdfWordCell.setBorderWidth(1);
//            pdfWordCell.setBorder(0);
//            pdfWordCell.addElement(firstLine);
                pdfWordCell.addElement(paragraph);
//            pdfWordCell.addElement(secondLine);
                pdfWordCell.addElement(p2);
                pdfWordCell.addElement(p3);
                pdfWordCell.addElement(p4);
                pdfWordCell.addElement(p5);
                pdfWordCell.addElement(p6);
                pdfWordCell.addElement(p7);
                pdfWordCell.addElement(p8);
                pdfWordCell.addElement(p9);

//            tb.addCell(pdfWordCell);
//            table.addCell(tb);
                table.addCell(pdfWordCell);
                if (i % 2 == 0)
                {
//                PdfPTable tbS = new PdfPTable(1);

                    PdfPCell pdfSpCol = new PdfPCell();
                    pdfSpCol.setBorder(0);
//                tbS.addCell(pdfSp);
//                table.addCell(tbS);
                    table.addCell(pdfSpCol);
                }
                if (i % 2 == 1)
                {

//                tbS.addCell(pdfSp);
//                table.addCell(tbS);
                    table.addCell(pdfSp);
                    table.addCell(pdfSp);
                    table.addCell(pdfSp);
                }
                //table.addCell(image);
                // addCustomRows(table);
                if (i % 10 == 0 && i > 0)
                {
                    document.newPage();
                }
            }
            table.addCell(pdfSp);// do bi mat row insert sau nen phai goi add them cel gia lap

            document.add(table);
            document.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String generateBillPdf(PrintPaymentForm printForm)
    {
        String resPath = "";

        try
        {
            String centerName = "Hoa Sen SPA";
            String adr1 = "Lô 1+2, số 19 Phan Chu Trinh, P. Điện Biên";
            String adr2 = "TP Thanh Hóa";
            String tile = "HÓA ĐƠN THANH TOÁN";
            String tilePayCode = "Mã thanh toán: ";
            String tileTime = "Thời gian thanh toán: ";
            String tileCustomer = "Khách hàng: ";
            String tileKtv = "Kỹ thuật viên: ";

            //tao file pdf
            String outputName = "";
            if (Constant.PAYMENT_TYPE.CLIENT.equals(printForm.getCustomerType()))
            {
                outputName = "clientPayment";
            }
            else if (Constant.PAYMENT_TYPE.MEMBER.equals(printForm.getCustomerType()))
            {
                outputName = "memberPayment";
            }
            else if (Constant.PAYMENT_TYPE.GROUP_MEMBER.equals(printForm.getCustomerType()))
            {
                outputName = "groupMemberPayment";
            }
            String endCode = printForm.getPaymentCode();
            String resultPath = Constant.OUT_FOLDER;
            CommonUtil.makeDirectory(Util.getRealPath(resultPath));

            String desFileNamePdf = outputName + "_" + endCode + ".pdf";
            String des = resultPath + desFileNamePdf;
            resPath = Util.getRealPath(des);

            String impTempPath = Util.getRealPath("templates" + File.separator + "logoHoasen.jpg");
            Image img = Image.getInstance(impTempPath);
            img.setAlignment(Image.ALIGN_CENTER);
            BaseFont bf = BaseFont.createFont(Util.getRealPath(Constant.FONT_TIMES), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFontCenter = new Font(bf, 14, Font.BOLD);
            Font boldFont = new Font(bf, 12, Font.BOLD);
            Font normalFont = new Font(bf, 12);
            Font headerboldFont = new Font(bf, 10, Font.BOLD);
            Font contentboldFont = new Font(bf, 9.5f, Font.BOLD);
            Font contentFont = new Font(bf, 10f);

            float width = 252f;
            float height = 362f;

            float scaler = ((width - 4f) / img.getWidth()) * 100 * 0.5f;
//            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
//                    - document.rightMargin()) / img.getWidth()) * 100*0.5f;
            img.scalePercent(scaler);

            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(img);
            Phrase firstLine = new Phrase(centerName, boldFontCenter);
            Phrase sLine2 = new Phrase(adr1, normalFont);
            Phrase sLine3 = new Phrase(adr2, normalFont);
            Phrase sLine4 = new Phrase(tile, boldFont);
            Paragraph p1 = new Paragraph();
            p1.setAlignment(Element.ALIGN_CENTER);
            Paragraph p2 = new Paragraph();
            p2.setAlignment(Element.ALIGN_CENTER);
            Paragraph p3 = new Paragraph();
            p3.setAlignment(Element.ALIGN_CENTER);
            Paragraph p4 = new Paragraph();
            p4.setAlignment(Element.ALIGN_CENTER);
            Paragraph pLine = new Paragraph();
            pLine.add(new Phrase(" "));

            p1.add(firstLine);
            p2.add(sLine2);
            p3.add(sLine3);
            p4.add(sLine4);

            Paragraph paragraphInfo = new Paragraph();
            paragraphInfo.setAlignment(Element.ALIGN_LEFT);
            Phrase pPayCode = new Phrase(tilePayCode + printForm.getPaymentCode(), normalFont);
            Phrase pPayTime = new Phrase(tileTime + printForm.getPaymentTime(), normalFont);
            Phrase pCustomerPay = new Phrase(tileCustomer + printForm.getCustomerName(), normalFont);
            Phrase pKtv = new Phrase(tileKtv, normalFont);

            paragraphInfo.add(new Paragraph(pPayCode));
            paragraphInfo.add(new Paragraph(pPayTime));
            paragraphInfo.add(new Paragraph(pCustomerPay));
            paragraphInfo.add(new Paragraph(pKtv));

            PdfPCell pdfSp = new PdfPCell();
            pdfSp.setPadding(0);
            pdfSp.setBorder(0);
            pdfSp.rectangle(0, 0);
            pdfSp.addElement(new Phrase(" "));
            float[] columnWidths = {3.7f, 1.3f, 3f, 3f, 3.1f};
            PdfPTable table = new PdfPTable(columnWidths);
//            table.setWidthPercentage(99);
            table.setTotalWidth(width * 0.99f);
            table.setLockedWidth(true);
            List<String> lstTileCol = Arrays.asList("Dịch vụ", "SL", "Đơn giá", "Khuyến mãi", "Thành tiền thanh toán");
            List<PdfPCell> lstTileCell = new ArrayList<>();
            for (String tileStr : lstTileCol)
            {
                PdfPCell tileCell1 = new PdfPCell();
                tileCell1.setPadding(5);
                tileCell1.setBorderWidth(1);
                Paragraph c1 = new Paragraph();
                c1.setAlignment(Element.ALIGN_CENTER);
                c1.add(new Phrase(tileStr, headerboldFont));
                tileCell1.addElement(c1);
                lstTileCell.add(tileCell1);
                table.addCell(tileCell1);

            }
            if (printForm.getLstPaymentPackObjs() != null && printForm.getLstPaymentPackObjs().size() > 0)
            {
                for (PaymentPackObj bo : printForm.getLstPaymentPackObjs())
                {
                    PdfPCell cel1 = new PdfPCell();
                    cel1.setPadding(2);
                    cel1.setBorderWidth(1);
                    PdfPCell cel2 = new PdfPCell();
                    cel2.setPadding(2);
                    cel2.setBorderWidth(1);
                    PdfPCell cel3 = new PdfPCell();
                    cel3.setPadding(2);
                    cel3.setBorderWidth(1);
                    PdfPCell cel4 = new PdfPCell();
                    cel4.setPadding(2);
                    cel4.setBorderWidth(1);
                    PdfPCell cel5 = new PdfPCell();
                    cel5.setPadding(2);
                    cel5.setBorderWidth(1);

                    String groupPackName = bo.getGroupPackName() != null ? bo.getGroupPackName() : "";
                    Paragraph c1 = new Paragraph();
                    c1.setAlignment(Element.ALIGN_LEFT);
                    c1.add(new Phrase(groupPackName, contentboldFont));
                    cel1.addElement(c1);

                    Paragraph c2 = new Paragraph();
                    c2.setAlignment(Element.ALIGN_RIGHT);
                    c2.add(new Phrase(String.valueOf(bo.getQuantity()), contentFont));
                    cel2.addElement(c2);

                    Paragraph c3 = new Paragraph();
                    c3.setAlignment(Element.ALIGN_RIGHT);
                    c3.add(new Phrase(DataUtil.getStringNumber(bo.getPrice()), contentFont));
                    cel3.addElement(c3);

                    Paragraph c4 = new Paragraph();
                    c4.setAlignment(Element.ALIGN_RIGHT);
                    c4.add(new Phrase((bo.getProm() != null ? bo.getProm() : ""), contentFont));
                    cel4.addElement(c4);

                    Paragraph c5 = new Paragraph();
                    c5.setAlignment(Element.ALIGN_RIGHT);
                    c5.add(new Phrase((DataUtil.getStringNumber(bo.getAmount())), contentFont));
                    cel5.addElement(c5);

                    table.addCell(cel1);
                    table.addCell(cel2);
                    table.addCell(cel3);
                    table.addCell(cel4);
                    table.addCell(cel5);
                }
            }
            else
            {
                PdfPCell cel1 = new PdfPCell();
                cel1.setPadding(2);
                cel1.setBorderWidth(1);
                cel1.addElement(new Phrase(" "));
                table.addCell(cel1);
                table.addCell(cel1);
                table.addCell(cel1);
                table.addCell(cel1);
                table.addCell(cel1);
            }
// tong hop
            PdfPCell cel1 = new PdfPCell();
            cel1.setPadding(2);
            cel1.setBorderWidth(1);
            PdfPCell cel2 = new PdfPCell();
            cel2.setPadding(2);
            cel2.setBorderWidth(1);
            PdfPCell cel3 = new PdfPCell();
            cel3.setPadding(2);
            cel3.setBorderWidth(1);
            PdfPCell cel4 = new PdfPCell();
            cel4.setPadding(2);
            cel4.setBorderWidth(1);
            PdfPCell cel5 = new PdfPCell();
            cel5.setPadding(2);
            cel5.setBorderWidth(1);

            Paragraph c1 = new Paragraph();
            c1.setAlignment(Element.ALIGN_LEFT);
            c1.add(new Phrase("Tổng tiền", boldFont));
            cel1.addElement(c1);

            // total price service
            Long totalPriceService = printForm.getTotalPriceService() != null ? printForm.getTotalPriceService() : 0l;
            Paragraph c3 = new Paragraph();
            c3.setAlignment(Element.ALIGN_RIGHT);
            c3.add(new Phrase((""), contentFont));
//            c3.add(new Phrase((!totalPriceService.equals(0l) ? DataUtil.getStringNumber(totalPriceService) : ""), contentFont));
            cel3.addElement(c3);

            Long discount = printForm.getDiscount() != null ? printForm.getDiscount() : 0l;
            Paragraph c4 = new Paragraph();
            c4.setAlignment(Element.ALIGN_RIGHT);
//            c4.add(new Phrase((!discount.equals(0l) ? DataUtil.getStringNumber(discount) : ""), contentFont));
            c4.add(new Phrase((""), contentFont));
            cel4.addElement(c4);
            Paragraph c5 = new Paragraph();
            c5.setAlignment(Element.ALIGN_RIGHT);
            c5.add(new Phrase(DataUtil.getStringNumber(printForm.getTotal()), contentFont));
            cel5.addElement(c5);

            table.addCell(cel1);
            table.addCell(cel2);
            table.addCell(cel3);
            table.addCell(cel4);
            table.addCell(cel5);
            //them cell gia de khong bi mat du lieu

            table.addCell(pdfSp);

            // chu ky
            float[] columnWidths2 = {2f, 2f};
            PdfPTable table2 = new PdfPTable(columnWidths2);
            PdfPCell sigCel = new PdfPCell();
            sigCel.setPadding(0);
            sigCel.setBorder(0);
            sigCel.rectangle(0, 0);
            sigCel.addElement(new Phrase(" "));

            Phrase sig1 = new Phrase("Thu ngân", boldFont);
            Phrase sig2 = new Phrase("(Ký, ghi rõ họ tên)", normalFont);
            Paragraph pSig1 = new Paragraph();
            pSig1.setAlignment(Element.ALIGN_CENTER);
            pSig1.add(sig1);
            Paragraph pSig2 = new Paragraph();
            pSig2.setAlignment(Element.ALIGN_CENTER);
            pSig2.add(sig2);

            sigCel.addElement(pSig1);
            sigCel.addElement(pSig2);
            table2.addCell(pdfSp);
            table2.addCell(sigCel);

            //fix mat cel
            table2.addCell(pdfSp);

            //cam on
            Phrase thanks = new Phrase("Cảm ơn Quý khách, hẹn gặp lại!", normalFont);
            Paragraph pThank = new Paragraph();
            pThank.setAlignment(Element.ALIGN_CENTER);
            pThank.add(thanks);

            Paragraph pPhone = new Paragraph();
            pPhone.setAlignment(Element.ALIGN_CENTER);
            pPhone.add(new Phrase("Hotline: 02373943888", new Font(bf, 11)));

            // create documet
            float tblHeight = table.getTotalHeight();
            Rectangle envelope = new Rectangle(width, height + tblHeight);

            Document document = new Document(envelope);
            document.setMargins(2f, 2f, 0.1f, 2f);
//        String outPath = Util.getRealPath(resultPath) + "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
//        String outPath = resultPath + "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(resPath));

            document.open();

            document.add(paragraph);
            document.add(p1);
            document.add(pPhone);
//            document.add(p2);
//            document.add(p3);
            document.add(pLine);
            document.add(p4);
//            document.add(pLine);
            document.add(paragraphInfo);
            document.add(pLine);
            document.add(table);
//            document.add(pLine);
            document.add(table2);
            document.add(pLine);
            document.add(pLine);
            document.add(pLine);
//            document.add(pLine);
            document.add(pThank);

            document.close();
            return resPath;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String createBillPdf(Bill bill)
    {
        String resPath = "";

        try
        {
            String centerName = "PHIẾU THANH TOÁN";
            String tile = "HÓA ĐƠN THANH TOÁN";
            String tilePayCode = "Mã hóa đơn: ";
            String tileTime = "Ngày tạo: ";
            String tileContract = "Hợp đồng: ";
            String tileDescription = "Ghi chú: ";
//            String tileCustomer = "Khách hàng: ";
//            String tileKtv = "Kỹ thuật viên: ";

            //tao file pdf
            String resultPath = Constant.OUT_FOLDER;
            CommonUtil.makeDirectory(Util.getRealPath(resultPath));

            String desFileNamePdf = bill.getBillCode() + "_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
            String des = resultPath + desFileNamePdf;
            resPath = Util.getRealPath(des);

//            String impTempPath = Util.getRealPath("templates" + File.separator + "logoHoasen.jpg");
//            Image img = Image.getInstance(impTempPath);
//            img.setAlignment(Image.ALIGN_CENTER);
            BaseFont bf = BaseFont.createFont(Util.getRealPath(Constant.FONT_TIMES), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Font boldFontCenter = new Font(bf, 14, Font.BOLD);
            Font boldFont = new Font(bf, 13, Font.BOLD);
            Font normalFont = new Font(bf, 12);
            Font headerboldFont = new Font(bf, 11, Font.BOLD);
            Font contentboldFont = new Font(bf, 11f, Font.BOLD);
            Font contentFont = new Font(bf, 12f);

//            float width = 252f;
            float height = 362f;
            String roomName = (bill.getRoom() != null && bill.getRoom().getRoomName() != null) ? bill.getRoom().getRoomName() : "";
            Phrase firstLine = new Phrase(centerName + " " + roomName.toUpperCase(), boldFontCenter);
            Phrase sLine4 = new Phrase(tile, boldFont);
            Paragraph p1 = new Paragraph();
            p1.setAlignment(Element.ALIGN_CENTER);

            Paragraph p4 = new Paragraph();
            p4.setAlignment(Element.ALIGN_CENTER);
            Paragraph pLine = new Paragraph();
            pLine.add(new Phrase(" "));

            p1.add(firstLine);
            p4.add(sLine4);

            Paragraph paragraphInfo = new Paragraph();
            paragraphInfo.setAlignment(Element.ALIGN_LEFT);
            Phrase pPayCode = new Phrase(tilePayCode + bill.getBillCode(), normalFont);
            Phrase pPayTime = new Phrase(tileTime + DateTimeUtils.formatDateCommon(bill.getPaymentDate()), normalFont);
            Phrase pContractCode = new Phrase(tileContract + bill.getContractCode(), normalFont);
            Phrase pDesc = new Phrase(tileDescription + (bill.getDescription() != null ? bill.getDescription() : ""), normalFont);
//            Phrase pRoom = new Phrase(bill.getRoom()!=null ? bill.getRoom().getRoomName():"", normalFont);

            paragraphInfo.add(new Paragraph(pPayCode));
            paragraphInfo.add(new Paragraph(pPayTime));
            paragraphInfo.add(new Paragraph(pContractCode));
            paragraphInfo.add(new Paragraph(pDesc));

            PdfPCell pdfSp = new PdfPCell();
            pdfSp.setPadding(0);
            pdfSp.setBorder(0);
            pdfSp.rectangle(0, 0);
            pdfSp.addElement(new Phrase(" "));
            float[] columnWidths = {5f, 3f, 2.5f, 2.5f, 2.5f, 3f, 4f};
            PdfPTable table = new PdfPTable(columnWidths);
//            table.setWidthPercentage(99);
            table.setTotalWidth(PageSize.A5.getHeight() * 0.93f);
            table.setLockedWidth(true);
            List<String> lstTileCol = Arrays.asList(
                    MessageUtil.getResourceBundleMessage("contract.service"),
                    MessageUtil.getResourceBundleMessage("billService.unit"),
                    MessageUtil.getResourceBundleMessage("billService.indexOld"),
                    MessageUtil.getResourceBundleMessage("billService.indexNew"),
                    MessageUtil.getResourceBundleMessage("billService.amount"),
                    MessageUtil.getResourceBundleMessage("billService.price"),
                    MessageUtil.getResourceBundleMessage("billService.totalPrice"));
            List<PdfPCell> lstTileCell = new ArrayList<>();
            for (String tileStr : lstTileCol)
            {
                PdfPCell tileCell1 = new PdfPCell();
                tileCell1.setPadding(5);
                tileCell1.setBorderWidth(1);
                Paragraph c1 = new Paragraph();
                c1.setAlignment(Element.ALIGN_CENTER);
                c1.add(new Phrase(tileStr, headerboldFont));
                tileCell1.addElement(c1);
                lstTileCell.add(tileCell1);
                table.addCell(tileCell1);

            }
            if (bill.getBillServiceList() != null && bill.getBillServiceList().size() > 0)
            {
                for (BillService bo : bill.getBillServiceList())
                {
                    PdfPCell cel1 = new PdfPCell();
                    cel1.setPadding(2);
                    cel1.setBorderWidth(1);
                    PdfPCell cel2 = new PdfPCell();
                    cel2.setPadding(2);
                    cel2.setBorderWidth(1);
                    PdfPCell cel3 = new PdfPCell();
                    cel3.setPadding(2);
                    cel3.setBorderWidth(1);
                    PdfPCell cel4 = new PdfPCell();
                    cel4.setPadding(2);
                    cel4.setBorderWidth(1);
                    PdfPCell cel5 = new PdfPCell();
                    cel5.setPadding(2);
                    cel5.setBorderWidth(1);
                    PdfPCell cel6 = new PdfPCell();
                    cel6.setPadding(2);
                    cel6.setBorderWidth(1);
                    PdfPCell cel7 = new PdfPCell();
                    cel7.setPadding(2);
                    cel7.setBorderWidth(1);

                    String groupPackName = (bo.getService() != null && bo.getService().getServiceName() != null) ? bo.getService().getServiceName() : "";
                    Paragraph c1 = new Paragraph();
                    c1.setAlignment(Element.ALIGN_LEFT);
                    c1.add(new Phrase(groupPackName, contentboldFont));
                    cel1.addElement(c1);

                    String unitName = (bo.getService() != null && bo.getService().getUnitBO() != null
                            && bo.getService().getUnitBO().getName() != null)
                            ? bo.getService().getUnitBO().getName() : "";
                    Paragraph c2 = new Paragraph();
                    c2.setAlignment(Element.ALIGN_RIGHT);
                    c2.add(new Phrase(String.valueOf(unitName), contentFont));
                    cel2.addElement(c2);

                    Paragraph c3 = new Paragraph();
                    c3.setAlignment(Element.ALIGN_RIGHT);
                    c3.add(new Phrase(DataUtil.getStringNumber(bo.getIndexOld()), contentFont));
                    cel3.addElement(c3);

                    Paragraph c4 = new Paragraph();
                    c4.setAlignment(Element.ALIGN_RIGHT);
                    c4.add(new Phrase(DataUtil.getStringNumber(bo.getIndexNew()), contentFont));
                    cel4.addElement(c4);

                    Paragraph c5 = new Paragraph();
                    c5.setAlignment(Element.ALIGN_RIGHT);
                    c5.add(new Phrase((DataUtil.getStringNumber(bo.getAmount())), contentFont));
                    cel5.addElement(c5);

                    Paragraph c6 = new Paragraph();
                    c6.setAlignment(Element.ALIGN_RIGHT);
                    c6.add(new Phrase((DataUtil.getStringNumber(bo.getPrice())), contentFont));
                    cel6.addElement(c6);

                    Paragraph c7 = new Paragraph();
                    c7.setAlignment(Element.ALIGN_RIGHT);
                    c7.add(new Phrase((DataUtil.getStringNumber(bo.getTotalPrice())), contentFont));
                    cel7.addElement(c7);

                    table.addCell(cel1);
                    table.addCell(cel2);
                    table.addCell(cel3);
                    table.addCell(cel4);
                    table.addCell(cel5);
                    table.addCell(cel6);
                    table.addCell(cel7);
                }
            }
            else
            {
                PdfPCell cel1 = new PdfPCell();
                cel1.setPadding(2);
                cel1.setBorderWidth(1);
                cel1.addElement(new Phrase(" "));
                table.addCell(cel1);
                table.addCell(cel1);
                table.addCell(cel1);
                table.addCell(cel1);
                table.addCell(cel1);
            }
// tong hop
            PdfPCell cel1 = new PdfPCell();
            cel1.setPadding(2);
            cel1.setBorderWidth(1);
            PdfPCell cel2 = new PdfPCell();
            cel2.setPadding(2);
            cel2.setBorderWidth(1);
            PdfPCell cel3 = new PdfPCell();
            cel3.setPadding(2);
            cel3.setBorderWidth(1);
            PdfPCell cel4 = new PdfPCell();
            cel4.setPadding(2);
            cel4.setBorderWidth(1);
            PdfPCell cel5 = new PdfPCell();
            cel5.setPadding(2);
            cel5.setBorderWidth(1);
            PdfPCell cel6 = new PdfPCell();
            cel6.setPadding(2);
            cel6.setBorderWidth(1);

            PdfPCell cel7 = new PdfPCell();
            cel7.setPadding(2);
            cel7.setBorderWidth(1);

            Paragraph c1 = new Paragraph();
            c1.setAlignment(Element.ALIGN_LEFT);
            c1.add(new Phrase(MessageUtil.getResourceBundleMessage("bill.totalPrice"), boldFont));
            cel1.addElement(c1);

            // total price service
//            Long totalPriceService = bill.getTotalPrice() != null ? bill.getTotalPrice() : 0l;
            Paragraph c3 = new Paragraph();
            c3.setAlignment(Element.ALIGN_RIGHT);
            c3.add(new Phrase((""), contentFont));
//            c3.add(new Phrase((!totalPriceService.equals(0l) ? DataUtil.getStringNumber(totalPriceService) : ""), contentFont));
            cel3.addElement(c3);

            Paragraph c4 = new Paragraph();
            c4.setAlignment(Element.ALIGN_RIGHT);
//            c4.add(new Phrase((!discount.equals(0l) ? DataUtil.getStringNumber(discount) : ""), contentFont));
            c4.add(new Phrase((""), contentFont));
            cel4.addElement(c4);
            Paragraph c5 = new Paragraph();
            c5.setAlignment(Element.ALIGN_RIGHT);
            c5.add(new Phrase((""), contentFont));
            cel5.addElement(c5);
            Paragraph c6 = new Paragraph();
            c6.setAlignment(Element.ALIGN_RIGHT);
            c6.add(new Phrase((""), contentFont));
            cel6.addElement(c6);
            Paragraph c7 = new Paragraph();
            c7.setAlignment(Element.ALIGN_RIGHT);
            c7.add(new Phrase(DataUtil.getStringNumber(bill.getTotalPrice()) + " VNĐ", boldFont));
            cel7.addElement(c7);

            table.addCell(cel1);
//            table.addCell(cel2);
//            table.addCell(cel3);
//            table.addCell(cel4);
//            table.addCell(cel5);
//            table.addCell(cel6);
            cel7.setColspan(6);
            table.addCell(cel7);

            //them cell gia de khong bi mat du lieu
            table.addCell(pdfSp);

            // chu ky
            float[] columnWidths2 = {2f, 2f};
            PdfPTable table2 = new PdfPTable(columnWidths2);
            PdfPCell sigCel = new PdfPCell();
            sigCel.setPadding(0);
            sigCel.setBorder(0);
            sigCel.rectangle(0, 0);
            sigCel.addElement(new Phrase(" "));

            Phrase sig1 = new Phrase("Người thu", boldFont);
            Phrase sig2 = new Phrase("(Ký, ghi rõ họ tên)", normalFont);
            Paragraph pSig1 = new Paragraph();
            pSig1.setAlignment(Element.ALIGN_CENTER);
            pSig1.add(sig1);
            Paragraph pSig2 = new Paragraph();
            pSig2.setAlignment(Element.ALIGN_CENTER);
            pSig2.add(sig2);

            sigCel.addElement(pSig1);
            sigCel.addElement(pSig2);
            table2.addCell(pdfSp);
            table2.addCell(sigCel);

            //fix mat cel
            table2.addCell(pdfSp);

            //cam on
/*            Phrase thanks = new Phrase("Cảm ơn Quý khách, hẹn gặp lại!", normalFont);
            Paragraph pThank = new Paragraph();
            pThank.setAlignment(Element.ALIGN_CENTER);
            pThank.add(thanks);

            Paragraph pPhone = new Paragraph();
            pPhone.setAlignment(Element.ALIGN_CENTER);
            pPhone.add(new Phrase("Hotline: 02373943888", new Font(bf, 11)));
*/
            // create documet
            float tblHeight = table.getTotalHeight();
//            Rectangle envelope = new Rectangle(width, height + tblHeight);

//            Document document = new Document(envelope);
            Document document = new Document(PageSize.A5.rotate());
            document.setMargins(20f, 20f, 10f, 2f);
//        String outPath = Util.getRealPath(resultPath) + "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
//        String outPath = resultPath + "Template_serviceTicket_" + DateTimeUtils.format(new Date(), "yyyyMMddHHmmss") + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(resPath));

            document.open();

//            document.add(paragraph);
            document.add(p1);
//            document.add(pPhone);
//            document.add(p2);
//            document.add(p3);
            document.add(pLine);
//            document.add(p4);
//            document.add(pLine);
            document.add(paragraphInfo);
            document.add(pLine);
            document.add(table);
//            document.add(pLine);
            document.add(table2);
            document.add(pLine);
//            document.add(pLine);
//            document.add(pLine);
//            document.add(pLine);
//            document.add(pThank);

            document.close();
            return resPath;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
