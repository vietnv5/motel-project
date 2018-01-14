package com.slook.controller;

import com.slook.model.CatPromotion;
import com.slook.object.PaymentPackObj;
import com.slook.object.PrintPaymentForm;
import com.slook.util.CommonUtil;
import com.slook.util.Constant;
import com.slook.util.DataUtil;
import com.slook.util.DateTimeUtils;
import com.slook.util.MessageUtil;
import com.slook.util.StringUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import static org.apache.log4j.Logger.getLogger;

/**
 * Created by Thanh Pham on 07/07/2017.
 */
@ManagedBean(name = "imageStreamerBill")
@ApplicationScoped
public class ImageStreamerBill {

    private static final Logger logger = getLogger(ImageStreamerBill.class);

    @PostConstruct
    public void init() {
    }

    public StreamedContent getImage() throws IOException {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, String> params = context.getExternalContext().getRequestParameterMap();

            if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
                return new DefaultStreamedContent();
            } else {
                String imgPath = params.get("imgPath");
                Path path = Paths.get(imgPath);
                byte[] buf = Files.readAllBytes(path);
                return new DefaultStreamedContent(new ByteArrayInputStream(buf));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultStreamedContent();
    }

    public static void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if (m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++) {
                if (m.stringWidth(currentLine + words[i]) < lineWidth) {
                    currentLine += " " + words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if (currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }

    public static String descProm(CatPromotion catPromotion) {
        String desc = "";
        if (catPromotion != null && catPromotion.getValue() != null) {

            if (Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType())) {
                desc = DataUtil.getStringNumber(catPromotion.getValue());
            } else 
            if (Constant.PROMOTION_TYPE.THEM_THOI_GIAN.equals(catPromotion.getType())) {
                desc = DataUtil.getStringNumber(catPromotion.getValue());
            } else 
            {
                desc = catPromotion.getValue().toString();
            }
            desc = desc
                    + (Constant.PROMOTION_TYPE.GIAM_TIEN_PERCENT.equals(catPromotion.getType())
                    ? "%" : Constant.PROMOTION_TYPE.GIAM_TIEN_MAT.equals(catPromotion.getType())
                    ? "" : (Constant.PROMOTION_TYPE.THEM_LUOT.equals(catPromotion.getType())
                    ? " lần" : (Constant.PROMOTION_TYPE.THEM_THOI_GIAN.equals(catPromotion.getType()))
                    ? " ngày" : ""));
        }

        return desc;
    }

    public static String generateBillImg(PrintPaymentForm printForm) {
        try {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String path = File.separator + "templates" + File.separator + "payment_v2.jpg";

            String outputName = "";
            if (Constant.PAYMENT_TYPE.CLIENT.equals(printForm.getCustomerType())) {
                outputName = "clientPayment";
            } else if (Constant.PAYMENT_TYPE.MEMBER.equals(printForm.getCustomerType())) {
                outputName = "memberPayment";
            } else if (Constant.PAYMENT_TYPE.GROUP_MEMBER.equals(printForm.getCustomerType())) {
                outputName = "groupMemberPayment";
            }
            String endCode = DateTimeUtils.format(new Date(), "yyMMddHHmmss");
            if (StringUtil.isNotNull(printForm.getPaymentCode())) {
                endCode = printForm.getPaymentCode();
            }
            String desPath = File.separator + "resources" + File.separator + "exported" + File.separator + outputName + endCode + ".jpg";
            String des = ctx.getRealPath("/") + "resources" + File.separator + "exported" + File.separator + outputName + endCode + ".jpg";

            if (!CommonUtil.makeDirectory(ctx.getRealPath("/") + "resources" + File.separator + "exported")) {
                MessageUtil.setErrorMessage("Tạo folder thất bại");
                return null;
            }

            BufferedImage im = ImageIO.read(new File(ctx.getRealPath("/") + path));

            Graphics2D gFullName = im.createGraphics();
            gFullName.setColor(Color.BLACK);
            gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
            // Branch name
/*            int branchNameX = im.getWidth() * 197 / 380;
            int branchNameY = im.getHeight() * 112 / 725;
            gFullName.drawString(new String(curMember.getBranch().getBranchName()), branchNameX, branchNameY);
             */

            // Floor code
            int floorCodeX = im.getWidth() * 339 / 798;
            int floorCodeY = im.getHeight() * 610 / 3290;//3004
            gFullName.drawString(new String(printForm.getBarCode()), floorCodeX, floorCodeY);
            // Date
            int dateX = im.getWidth() * 420 / 798; //320
            int dateY = im.getHeight() * 850 / 3290;//770 834
            gFullName.drawString(new String(printForm.getPaymentTime()), dateX, dateY);

            // Bill code
            int billCodeX = im.getWidth() * 300 / 798;
            int billCodeY = im.getHeight() * 738 / 3290;
            gFullName.drawString(new String(printForm.getPaymentCode()), billCodeX, billCodeY);
            // customer
            int customerX = im.getWidth() * 260 / 798;
            int customerY = im.getHeight() * 978 / 3290;
            gFullName.drawString(printForm.getCustomerName(), customerX, customerY);
            // customer type
            int customerTypeX = im.getWidth() * 220 / 798;
            int customerTypeY = im.getHeight() * 1089 / 3290;
            gFullName.drawString(printForm.getCustomerTypeName(), customerTypeX, customerTypeY);
            //num customer
            int numCustomerX = im.getWidth() * 200 / 798;
            int numCustomerY = im.getHeight() * 1150 / 3290;
            gFullName.drawString(String.valueOf(printForm.getNumCustomer()), numCustomerX, numCustomerY);
            // thu ngan
            int empX = im.getWidth() * 370 / 798;
            int empY = im.getHeight() * 1385 / 3290;
            gFullName.drawString(printForm.getEmployeeName(), empX, empY);

//            Font fontOld=gFullName.getFont();
//            gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
            // List package
            int itemStart = 1585;
            int distince = 45;//17
            int idxStart = 0;

            Font oldFont = gFullName.getFont();
            gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 27));
            if (printForm.getLstPaymentPackObjs() != null) {
                for (PaymentPackObj bo : printForm.getLstPaymentPackObjs()) {

                    // num
                    int numX = im.getWidth() * 200 / 798;
                    int numY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                    gFullName.drawString(String.valueOf(bo.getQuantity()), numX, numY);

                    int priceX = im.getWidth() * 255 / 798;
                    int priceY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                    gFullName.drawString(String.valueOf(DataUtil.getStringNumber(bo.getPrice())), priceX, priceY);

                    int promotionX = im.getWidth() * 420 / 798;
                    int promotionY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                    gFullName.drawString(new String(bo.getProm() != null ? bo.getProm() : ""), promotionX, promotionY);

                    int amountX = im.getWidth() * 570 / 798;
                    int amountY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                    gFullName.drawString(new String((DataUtil.getStringNumber(bo.getAmount()))), amountX, amountY);
                    /*
                    //hsd
                    int endDateX = im.getWidth() * 685 / 798;
                    int endDateY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                    int newidxStart = idxStart;

//                    Font fontOld=gFullName.getFont();
//                    gFullName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
                    String hsd = bo.getExp() != null ? bo.getExp() : "";
                    if (hsd.length() < 9) {
                        gFullName.drawString(new String(hsd), endDateX, endDateY);
                    } else {
                        gFullName.drawString(new String(hsd.substring(0, 9)), endDateX, endDateY);
                        int h2 = im.getHeight() * (itemStart + (idxStart + 1) * distince - 2) / 3290;
                        gFullName.drawString(new String(hsd.substring(6, hsd.length())), endDateX, h2);
                        newidxStart += 1;
                    }
//                    gFullName.setFont(fontOld);
                     */
                    // item
                    String groupPackName = bo.getGroupPackName() != null ? bo.getGroupPackName() : "";
                    if (groupPackName.length() > 12) {

                        int itemX = im.getWidth() * 5 / 798;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                        gFullName.drawString(new String(groupPackName.substring(0, 12)), itemX, itemY);

                        idxStart += 1;
                        if (groupPackName.length() > 24) {
                            int itemX2 = im.getWidth() * 12 / 798;
                            int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                            gFullName.drawString(new String(groupPackName.substring(13, 24)), itemX2, itemY2);

                            idxStart += 1;
                            int itemY3 = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                            gFullName.drawString(new String(groupPackName.substring(25, groupPackName.length())), itemX2, itemY3);
                        } else {
                            int itemX2 = im.getWidth() * 12 / 798;
                            int itemY2 = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                            gFullName.drawString(new String(groupPackName.substring(13, groupPackName.length())), itemX2, itemY2);
                        }
                    } else {
                        int itemX = im.getWidth() * 13 / 798;
                        int itemY = im.getHeight() * (itemStart + idxStart * distince) / 3290;
                        gFullName.drawString(new String(groupPackName), itemX, itemY);
                    }
//                    if (newidxStart > idxStart) {
//                        idxStart = newidxStart;
//                    }

                    idxStart++;
                }
            }
            gFullName.setFont(oldFont);
            // total price service
            Long totalPriceService = printForm.getTotalPriceService() != null ? printForm.getTotalPriceService() : 0l;
            int totalPriceServiceX = im.getWidth() * 400 / 798;//105
            int totalPriceServiceY = im.getHeight() * 1905 / 3290;
            gFullName.drawString(new String(!totalPriceService.equals(0l) ? DataUtil.getStringNumber(totalPriceService) : ""), totalPriceServiceX, totalPriceServiceY);
            // giam gia
            Long discount = printForm.getDiscount() != null ? printForm.getDiscount() : 0l;
            int discountX = im.getWidth() * 500 / 798;//105
            int discountY = im.getHeight() * 2020 / 3290;
            gFullName.drawString(new String(!discount.equals(0l) ? DataUtil.getStringNumber(discount) : ""), discountX, discountY);
            // vat
            int vatX = im.getWidth() * 500 / 798;
            int vatY = im.getHeight() * 2075 / 3290;
            Long vatPrice = printForm.getVatPrice() != null ? printForm.getVatPrice() : 0l;
            gFullName.drawString(new String(!vatPrice.equals(0l) ? DataUtil.getStringNumber(vatPrice) : ""), vatX, vatY);

            // Total 
            int totalX = im.getWidth() * 500 / 798;//105
            int totalY = im.getHeight() * 2125 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getTotal())), totalX, totalY);
            // deposit
            int depositX = im.getWidth() * 500 / 798;
            int depositY = im.getHeight() * 2179 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getDeposit())), depositX, depositY);
            // Total khach phai tra
            int totalMustPayX = im.getWidth() * 500 / 798;//126
            int totalMustPayY = im.getHeight() * 2229 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getMustPay())), totalMustPayX, totalMustPayY);

            // deposit
//            int depositX = im.getWidth() * 325 / 380;
//            int depositY = im.getHeight() * 563 / 725;
//            gFullName.drawString(new String(DataUtil.getStringNumber(memberPayment.getTotalDeposit())), depositX, depositY);
            // khach tra
            int custPaysX = im.getWidth() * 500 / 798;//108
            int custPaysY = im.getHeight() * 2280 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getCustomerPay())), custPaysX, custPaysY);

            // cong no mơi
            // debt
            int deptX = im.getWidth() * 500 / 798;//131
            int deptY = im.getHeight() * 2331 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getNewDept())), deptX, deptY);

            // debt old
            int deptOldX = im.getWidth() * 500 / 798;//119
            int deptOldY = im.getHeight() * 2385 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getOldDept())), deptOldX, deptOldY);

            // tong cong no total dept
            int totalDeptX = im.getWidth() * 500 / 798;//108
            int totalDeptY = im.getHeight() * 2438 / 3290;
            gFullName.drawString(new String(DataUtil.getStringNumber(printForm.getTotalDept())), totalDeptX, totalDeptY);

            int itemStart2 = 2650;
//            int distince2 = 50;//17
            int idxStart2 = 0;

            if (printForm.getLstPaymentPackObjs() != null) {
                for (PaymentPackObj bo : printForm.getLstPaymentPackObjs()) {
                    //hsd
                    int endDateX = im.getWidth() * 570 / 798;
                    int endDateY = im.getHeight() * (itemStart2 + idxStart2 * distince) / 3290;
//                    int newidxStart = idxStart;
                    String hsd = bo.getExp() != null ? bo.getExp() : "";
//                    if (hsd.length() < 9) {
                    gFullName.drawString(new String(hsd), endDateX, endDateY);
//                    } else {
//                        gFullName.drawString(new String(hsd.substring(0, 9)), endDateX, endDateY);
//                        int h2 = im.getHeight() * (itemStart + (idxStart + 1) * distince - 2) / 3290;
//                        gFullName.drawString(new String(hsd.substring(6, hsd.length())), endDateX, h2);
////                        newidxStart += 1;
//                    }
//                    gFullName.setFont(fontOld);

                    // item
                    String groupPackName = bo.getGroupPackName() != null ? bo.getGroupPackName() : "";
                    if (groupPackName.length() > 35) {

                        int itemX = im.getWidth() * 13 / 798;
                        int itemY = im.getHeight() * (itemStart2 + idxStart2 * distince) / 3290;
                        gFullName.drawString(new String(groupPackName.substring(0, 35)), itemX, itemY);

                        idxStart2 += 1;
                        if (groupPackName.length() > 70) {
                            int itemX2 = im.getWidth() * 12 / 798;
                            int itemY2 = im.getHeight() * (itemStart2 + idxStart2 * distince) / 3290;
                            gFullName.drawString(new String(groupPackName.substring(36, 70)), itemX2, itemY2);

                            idxStart2 += 1;
                            int itemY3 = im.getHeight() * (itemStart2 + idxStart2 * distince) / 3290;
                            gFullName.drawString(new String(groupPackName.substring(71, groupPackName.length())), itemX2, itemY3);
                        } else {
                            int itemX2 = im.getWidth() * 12 / 798;
                            int itemY2 = im.getHeight() * (itemStart2 + idxStart2 * distince) / 3290;
                            gFullName.drawString(new String(groupPackName.substring(36, groupPackName.length())), itemX2, itemY2);
                        }
                    } else {
                        int itemX = im.getWidth() * 13 / 798;
                        int itemY = im.getHeight() * (itemStart2 + idxStart2 * distince) / 3290;
                        gFullName.drawString(new String(groupPackName), itemX, itemY);
                    }
//                    if (newidxStart > idxStart) {
//                        idxStart = newidxStart;
//                    }

                    idxStart2++;
                }
            }

            gFullName.dispose();
            ImageIO.write(im, "jpg", new File(des));

            return des;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
