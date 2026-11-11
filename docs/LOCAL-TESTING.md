# Local testing — messaging-service (HU14, SMS + security PIN)

The SMS + PIN flow uses **real AWS SNS** (no LocalStack). The SMS is delivered by Amazon.

## Prerequisites

- An **AWS account** with SNS SMS enabled. New accounts start in the **SMS sandbox**, so you can
  only deliver to **verified destination phone numbers** (verify yours in the AWS End User
  Messaging / SNS console, or via `aws sns create-sms-sandbox-phone-number` + `verify-sms-sandbox-phone-number`).
  SMS is **pay-per-message**.
- AWS credentials available to the standard provider chain — export them as environment variables
  (never hardcode them):
  ```bash
  export AWS_ACCESS_KEY_ID=...
  export AWS_SECRET_ACCESS_KEY=...
  export AWS_REGION=us-east-1
  ```
- A PostgreSQL `messaging` database at `localhost:5432` with the `plazoleta` role
  (override with `DB_NAME` / `DB_USERNAME` / `DB_PASSWORD`).

## 1. Run messaging-service

```bash
./gradlew bootRun
```

The `SnsClient` is built for the real AWS SNS endpoint of `AWS_REGION`; credentials come from the
standard AWS provider chain (env vars / shared config / IAM role).

## 2. Send a notification

Trigger `POST /api/v1/notifications/order-ready` (or food-court's `PATCH /orders/{id}/ready`) with a
**verified** destination phone number:

```bash
curl -X POST http://localhost:8083/api/v1/notifications/order-ready \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"<uuid>","phone":"+57XXXXXXXXXX","clientName":"Julian"}'
```

On success the service returns `201` with `status: SENT`, and a real SMS carrying the 6-digit
security PIN is delivered to the phone. By design the PIN is **not** in the API response — it travels
only by SMS. Use it against `POST /api/v1/notifications/{orderId}/validate` (HU15).

## Notes

- To validate credentials without sending an SMS: `aws sts get-caller-identity`.
- Sandbox status / verified numbers: `aws sns get-sms-sandbox-account-status`,
  `aws sns list-sms-sandbox-phone-numbers`.
- Some destination countries (e.g. Colombia) require a registered **Sender ID** / origination
  identity in addition to a verified sandbox number.
