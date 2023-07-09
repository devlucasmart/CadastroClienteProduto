#!/bin/bash

apt update -y
apt upgrade -y

echo "[ -- Configuring DateTime with systemd]"
apt install systemd
timedatectl set-timezone America/Sao_Paulo

# Se o ubuntu precisar ser atualizado
# sudo do-release-upgrade -d
# sudo reboot

echo "[ -- Installing postgresql-12]"
apt install postgresql-12 postgresql-client-12 postgresql-contrib -y

echo "[ -- Installing openjdk-11]"
apt-get install openjdk-11-jdk -y
echo "Verificar variável de ambiente java"

echo "[ -- Installing nodejs v16.13.1]"
sudo curl -sL https://deb.nodesource.com/setup_16.x | sudo -E bash -
apt install nodejs -y

echo "[ -- Updating NPM 8.3.1 to 8.5.0]"
npm install -g npm@8.5.0
npm install -g npm@8.5.0

echo "[ -- Add NPM serve package v13.0.2]"
npm install -g serve@13.0.2

echo "[ -- Installing NGINX v1.18.0]"
apt install nginx=1.18.* -y

echo "[ -- Update Password Postgres]"
sudo -u postgres psql template1 -c "ALTER USER postgres with encrypted password '!QAZ2wsx';"
sudo -u postgres psql template1 -c "CREATE DATABASE noivapp;"

echo "[ -- Criando diretórios de trabalho /opt/noivapp]"
mkdir /opt/noivapp
mkdir /opt/noivapp/backend
mkdir /opt/noivapp/frontend
mkdir /opt/noivapp/backups
mkdir /opt/noivapp/scripts

echo "[ -- Setando permissões 777 para os diretórios de trabalho /opt/noivapp]"
chmod -R 777 /opt/noivapp/backups/ /opt/noivapp/backend/ /opt/noivapp/frontend/ /opt/noivapp/scripts/

echo "[ -- Criando Verificador de Status de Daemon para os Services]"
{
    echo '#!/bin/bash'
    echo 'SERVICE=$1;'
    echo 'SERVICE_STATUS=$(echo $(sudo systemctl is-active ${SERVICE}));'
    echo 'if [ "inactive" = "${SERVICE_STATUS}" ]; then'
    echo '  echo "[ -- Service ${SERVICE} is inactive, nothing to do];";'
    echo 'else {'
    echo '  sudo systemctl stop ${SERVICE};'
    echo '  echo "[ -- The Service ${SERVICE} were stopped!]";'
    echo '};'
    echo 'fi'
} >> /opt/noivapp/scripts/verificador-stop-daemon-service.sh

echo "[ -- Criando Script de Backup Diário da Base]"
{
    echo 'DATETIME=$(date +"%m-%d-%Y_%H-%M-%S");'
    echo '\n'
    echo 'HOST=127.0.0.1;'
    echo 'PORT=5432;'
    echo 'DBUSER="postgres";'
    echo 'Exportando senha para visualização do comando pg_dump'
    echo 'export PGPASSWORD="postgres";'
    echo 'DATABASE_NAME="noivapp";'
    echo 'PATH_TO_SAVE="/opt/noivapp/backups/";'
    echo 'FILE_NAME="backup_$1_${DATABASE_NAME}_${DATETIME}.sql";'

    echo '# -- insert - serve para substituir comandos \COPY por INSERT;'
    echo 'pg_dump --inserts -U ${DBUSER} -h ${HOST} -p ${PORT} -d ${DATABASE_NAME} > ${PATH_TO_SAVE}${FILE_NAME};'
    echo 'chmod -R 777 ${PATH_TO_SAVE}${FILE_NAME};'

    echo '# -t refere-se a flag que ordena os arquivos pela data de modificação;'
    echo 'FILES_IN_FOLDER=`ls -t ${PATH_TO_SAVE}*.sql`;'
    echo 'COUNTER=0;'

    echo 'for file in $FILES_IN_FOLDER; do'
    echo '  echo $file;'
    echo '  if [ $COUNTER -gt 13 ]; then'
    echo '      # delete arquivo excedente'
    echo '      echo "Deletando aquivo antigo - ${file}";'
    echo '      rm -f ${file};'
    echo '  fi'
    echo '  COUNTER=$(($COUNTER+1))'
    echo 'done'
} >> /opt/noivapp/scripts/backup.sh


echo "[ -- Criando Daemon para o Backend Service]"
{
    echo '[Unit]'
    echo 'Description=REST Service in Spring Boot from noivapp Platform'
    echo 'After=syslog.target'
    echo '\n'
    echo '[Service]'
    echo 'User=ubuntu'
    echo 'WorkingDirectory=/opt/noivapp/backend'
    echo 'StandardOutput=journal'
    echo 'StandardError=journal'
    echo 'SyslogIdentifier=noivapp-backend'
    echo 'ExecStart=/usr/bin/java -jar /opt/noivapp/backend/noivapp-backend.jar'
    echo 'SuccessExitStatus=143'
    echo '\n' 
    echo '[Install]'
    echo 'WantedBy=multi-user.target'
} >> /etc/systemd/system/noivapp-backend.service

echo "[ -- Criando Daemon para o Frontend Service]"
{
    echo '[Unit]'
    echo 'Description=UI Service in ReactJS from noivapp Platform'
    echo 'After=syslog.target'
    echo '\n'
    echo '[Service]'
    echo 'User=ubuntu'
    echo 'WorkingDirectory=/opt/noivapp/frontend'
    echo 'StandardOutput=journal'
    echo 'StandardError=journal'
    echo 'SyslogIdentifier=noivapp-frontend'
    echo 'ExecStart=serve -s /opt/noivapp/frontend/build'
    echo 'SuccessExitStatus=143'
    echo '\n'
    echo '[Install]'
    echo 'WantedBy=multi-user.target'
} >> /etc/systemd/system/noivapp-frontend.service

echo "[ -- Criando arquivo para proxy reverso do Frontend com NGINX]"
{
    echo 'server {'
    echo '  server_name dev.noivapp.com;'
    echo '  location / {'
    echo '      proxy_pass http://127.0.0.1:3000;'
    echo '  }'
    echo '  location /api/v1 {'
    echo '      proxy_pass http://127.0.0.1:8081;'
    echo '  }'
    echo '}'
} >> /etc/nginx/sites-available/noivapp.com.conf

echo "[ -- Inicializando daemons criadas]"
systemctl enable noivapp-backend.service
systemctl enable noivapp-frontend.service
systemctl daemon-reload

echo "[ -- Criando Links Simbólicos para Proxy Reverso]"
unlink /etc/nginx/sites-enabled/default
ln -s /etc/nginx/sites-available/noivapp.com.conf /etc/nginx/sites-enabled/noivapp.com.conf
systemctl restart nginx

# Para ajustar o letscncrypt
# https://certbot.eff.org/instructions?ws=nginx&os=ubuntufocal
