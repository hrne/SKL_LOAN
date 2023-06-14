#!/bin/bash
# 2023-06-14 ST1-Wei 列出證書別名及有效期限

# 設定 .pfx 檔案路徑
PFX_PATH=startwca.pfx

# 提示輸入密碼
echo "Please enter the keystore password:"
read -s PFX_PASSWORD

# 列出證書別名
openssl pkcs12 -info -in $PFX_PATH -nocerts -passin pass:$PFX_PASSWORD -passout pass:$PFX_PASSWORD | grep "friendlyName:"

# 提取證書並列出有效期限
echo "=== Certificate Validity Period ==="
openssl pkcs12 -in $PFX_PATH -nokeys -passin pass:$PFX_PASSWORD | openssl x509 -noout -dates
