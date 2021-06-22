package com.cppsystem.cppbus.cppcardlib.nfclib.targets;

public class ClessCard extends Target {
    private String cardNumber = null;

    public ClessCard(byte[] bArr, byte[] bArr2) {
        super(bArr, bArr2);
    }

    public ClessCard(byte[] bArr, String str) {
        super(bArr, str);
    }

    public void setCardNumber(String str) {
        this.cardNumber = str;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [uid=");
        sb.append(this.suid);
        sb.append(",cn=");
        sb.append(this.cardNumber);
        sb.append("]");
        return sb.toString();
    }
}
