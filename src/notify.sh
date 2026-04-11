#!/bin/bash

echo "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
echo " Pipeline CI/CD - Notificacao de Deploy"
echo "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
echo " Repositorio: $REPO"
echo " Status do Deploy: $DEPLOY_STATUS"
echo " Data: $(date '+%d/%m/%Y %H:%M:%S')"
echo " Detalhes: $RUN_URL"
echo "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="

python3 - <<'PY'
import os, smtplib
from email.mime.text import MIMEText

repo = os.environ.get("REPO", "Pack0042/C14-AtividadeAvaliativa-NP1")
run_url = os.environ.get("RUN_URL", "")
deploy = os.environ.get("DEPLOY_STATUS", "unknown")
email_to = os.environ.get("EMAIL_TO", "")
email_from = os.environ.get("EMAIL_FROM", "")
email_pass = os.environ.get("EMAIL_PASSWORD", "")

if not all([email_to, email_from, email_pass]):
    print("Envio de emails nao ocorreu, secrets nao estao configurados")
    exit(0)

status = "SUCESSO" if deploy == "success" else "FALHA"

body = f"""Pipeline CI/CD - {status}

Repositorio: {repo}
Deploy: {deploy}

Detalhes: {run_url}
"""

msg = MIMEText(body)
msg["Subject"] = f"[{repo}] Pipeline - {status}"
msg["From"] = email_from
msg["To"] = email_to

with smtplib.SMTP_SSL("smtp.gmail.com", 465) as server:
    server.login(email_from, email_pass)
    server.send_message(msg, to_addrs=email_to.split(","))

print(f"Email enviado para {email_to}")
PY