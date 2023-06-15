#!/bin/bash
# 2023-06-14 ST1-Wei 建立trustJKS

# 設定 KeyStore 名稱
KEYSTORE_NAME=trustJKS.jks

# 設定 KeyStore 密碼
KEYSTORE_PASS=23895858

# 設定 root.cer 檔案路徑
ROOT_CER_PATH=root.cer

# 設定 uac.cer 檔案路徑
UCA_CER_PATH=uca.cer

# 刪除原來的JKS檔案
rm $KEYSTORE_NAME

# 導入根證書
keytool -import -trustcacerts -alias root -file $ROOT_CER_PATH -keystore $KEYSTORE_NAME -storepass $KEYSTORE_PASS -noprompt

# 導入中繼證書
keytool -import -trustcacerts -alias uca -file $UCA_CER_PATH -keystore $KEYSTORE_NAME -storepass $KEYSTORE_PASS -noprompt