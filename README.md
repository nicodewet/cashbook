# cashbook

```shell
curl -d '{"type":"INVOICE_PAYMENT","parentTransactionUUID":null,"scheduledDate":"2018-08-20","completedDate":null,"amountInCents":23000,"gstInCents":0,"evidenceLink":null}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/business/transaction
```

## build

We use a Docker multistage build.

Build and then run:
```jshell
$ docker build -t nicodewet/cashbook:latest .
$ docker run --name cashbook -it --rm -p 8080:8080 nicodewet/cashbook:latest
```

To stop at the compile stage:
```jshell
$ docker build --target compile -t nicodewet/cashbook:latest .
```