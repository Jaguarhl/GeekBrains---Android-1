package ru.dmitry.kartsev.fotopodushki.models;

import android.content.Context;

import java.io.Serializable;

import ru.dmitry.kartsev.fotopodushki.R;

/**
 * Created by Jag on 03.12.2016.
 */

public class Order implements Serializable {
    private Context mContext;
    private String filePathA, filePathB, clientName, clientPhone, clientEmail, clientComments,
            pillowSize, pillowMaterial, pillowBrushes, orderPrice;
    private String[] filePath;

    public Order(Context context) {
        mContext = context;
        filePathA = filePathB = clientName = clientPhone = clientEmail = clientComments = pillowSize =
                pillowMaterial = pillowBrushes = orderPrice = "";
    }

    public String getMail() {
        String mailBody = mContext.getResources().getString(R.string.email_contact_info) +
                mContext.getResources().getString(R.string.email_lead_name) + " " + clientName + "\n" +
                mContext.getResources().getString(R.string.email_lead_phone) + " " + clientPhone + "\n" +
                mContext.getResources().getString(R.string.email_lead_email) + " " + clientEmail + "\n" +
                mContext.getResources().getString(R.string.email_lead_comments) + " " + clientComments + "\n" +
                mContext.getResources().getString(R.string.email_delimiter) + "\n" +
                mContext.getResources().getString(R.string.email_order) + "\n" +
                mContext.getResources().getString(R.string.email_order_size) + " " + pillowSize + "\n" +
                mContext.getResources().getString(R.string.email_order_material) + " " + pillowMaterial +
                "\n" + mContext.getResources().getString(R.string.email_order_brushes) + " " + pillowBrushes + "\n\n\n" +
                mContext.getResources().getString(R.string.email_order_cost) + " " + orderPrice + "\n" +
                mContext.getResources().getString(R.string.email_order_images);
        return mailBody;
    }

    public void setFilePathA(String filePathA) {
        this.filePathA = filePathA;
    }

    public void setFilePathB(String filePathB) {
        this.filePathB = filePathB;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public void setClientComments(String clientComments) {
        this.clientComments = clientComments;
    }

    public void setPillowSize(String pillowSize) {
        this.pillowSize = pillowSize;
    }

    public void setPillowMaterial(String pillowMaterial) {
        this.pillowMaterial = pillowMaterial;
    }

    public void setPillowBrushes(String pillowBrushes) {
        this.pillowBrushes = pillowBrushes;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getFilePathA() {
        return filePathA;
    }

    public String getFilePathB() {
        return filePathB;
    }

    public boolean isFilePathA() {
        if (filePathA.length() != 0) return true;
        return false;
    }

    public boolean isFilePathB() {
        if (filePathB.length() != 0) return true;
        return false;
    }

    public String[] getFilePath() {
        if(isFilePathA()) {
            filePath[0] = filePathA;
        }
        if(isFilePathB()) {
            filePath[1] = filePathB;
        }
        return filePath;
    }
}
