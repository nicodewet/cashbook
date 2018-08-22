# cashbook

```shell
curl -d '{"type":"INVOICE_PAYMENT","parentTransactionUUID":null,"scheduledDate":"2018-08-20","completedDate":null,"amountInCents":23000,"gstInCents":0,"evidenceLink":null}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/business/transaction
```