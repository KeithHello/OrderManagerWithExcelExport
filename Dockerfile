FROM mysql:8.0

ENV MYSQL_ROOT_PASSWORD=my-secret-pw
ENV MYSQL_DATABASE=mydb
ENV MYSQL_USER=myuser
ENV MYSQL_PASSWORD=mypassword

# COPY my.cnf /etc/mysql/my.cnf

CMD ["mysqld"]
