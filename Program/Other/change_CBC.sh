#!/bin/bash

sudo cp /etc/ssh/sshd_config /etc/ssh/sshd_config.bak  

sudo sed -i '$a\Ciphers aes128-ctr,aes192-ctr,aes256-ctr,aes128-gcm@openssh.com,aes256-gcm@openssh.com' /etc/ssh/sshd_config

sudo service ssh restart
