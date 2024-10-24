package personal_finance_app_backend.models;

public class Receipt {
    private int receiptId;
    private int transactionId;
    private String filePath;
    private String uploadedAt;

    public Receipt(int receiptId, int transactionId, String filePath, String uploadedAt) {
        this.receiptId = receiptId;
        this.transactionId = transactionId;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
    }

    public int getReceiptId() { return receiptId; }
    public void setReceiptId(int receiptId) { this.receiptId = receiptId; }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(String uploadedAt) { this.uploadedAt = uploadedAt; }
}
