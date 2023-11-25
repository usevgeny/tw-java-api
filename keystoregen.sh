#!/usr/bin/bash

######################################################################
# @author      : {{NAME}} ({{EMAIL}})
# @file        : {{FILE}}
# @created     : {{TIMESTAMP}}
#
# @description : {{CURSOR}}
######################################################################
keytool -genkeypair -alias task_api_ks_alias -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 3650

keytool -export -alias task_api_ks_alias -keystore keystore.jks -file task_api_certificate.cer
