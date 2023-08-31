#!/bin/bash

cd /u01/domain/user_projects/domains/servyou_domain/bin

#  setDomainEnv.sh
echo "Setting domain environment..."
bash  setDomainEnv.sh

#  setNMJavaHome.sh
echo "Setting Node Manager's Java home..."
bash  setNMJavaHome.sh

#  setStartupEnv.sh
echo "Setting startup environment..."
bash  setStartupEnv.sh

#  startUpDomain.sh
echo "Starting up the domain..."
bash  startUpDomain.sh

#  startUpNode.sh
echo "Starting up the node..."
bash  startUpNode.sh

echo "All scripts executed successfully."
