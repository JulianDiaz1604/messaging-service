# Local testing — messaging-service (HU14, SMS + security PIN)

The SMS + PIN flow uses **AWS SNS**. To exercise the happy path locally without an AWS
account, without real credentials and without any cost, run SNS against **LocalStack**
(SNS is emulated — no real SMS leaves your machine).

## Prerequisites

- Docker (for LocalStack)
- A PostgreSQL `messaging` database reachable at `localhost:5432` with the `plazoleta` role
  (same convention as the other services; override with `DB_NAME`/`DB_USERNAME`/`DB_PASSWORD`).

## 1. Start LocalStack (emulated SNS)

```bash
docker compose up -d        # from the messaging-service directory
```

LocalStack listens on `http://localhost:4566`.

## 2. Run messaging-service pointing at LocalStack

`aws.sns.endpoint` defaults to `http://localhost:4566` (LocalStack). Credentials are resolved
from the standard AWS environment variables (never hardcoded); LocalStack accepts any value:

```bash
AWS_ACCESS_KEY_ID=test AWS_SECRET_ACCESS_KEY=test AWS_REGION=us-east-1 ./gradlew bootRun
```

To talk to **real AWS SNS** instead, clear the endpoint and export your real credentials
(pay-per-message; new accounts start in the SMS sandbox requiring verified destination numbers):

```bash
AWS_SNS_ENDPOINT= AWS_REGION=us-east-1 \
AWS_ACCESS_KEY_ID=... AWS_SECRET_ACCESS_KEY=... ./gradlew bootRun
```

## 3. Send a notification and read the PIN

Trigger `POST /api/v1/notifications/order-ready` (or food-court's `PATCH /orders/{id}/ready`).
LocalStack does not deliver a real SMS, but it records what would have been sent — read it to
recover the PIN and then exercise `POST /api/v1/notifications/{orderId}/validate`:

```bash
curl http://localhost:4566/_aws/sns/sms-messages
```

The response lists the published messages; the 6-digit PIN is inside the message body.

## 4. Tear down

```bash
docker compose down
```
