🛠️ Configuración Inicial del Entorno de Desarrollo
Pasos para poner en marcha la API Rest de Spring utilizando Docker como servidor de base de datos MariaDB local.

# 1. Instalación y Arranque de MariaDB con Docker
Asegúrate de tener Docker Desktop ejecutándose. Este comando crea el servidor MariaDB en el puerto 3306 con las credenciales necesarias:

- docker run --name mi-mariadb-local -e MARIADB_ROOT_PASSWORD=mi_clave_ues -e MARIADB_USER=dam235 -e MARIADB_PASSWORD=D@m235U35. -p 3306:3306 -d mariadb:latest

# 2. Conexión y Solución de Permisos
El usuario dam235 necesita privilegios para acceder a DBTmp. Nos conectamos como root para otorgarlos:

Comando de Conexión (usando clave mi_clave_ues):
- docker exec -it mi-mariadb-local mariadb -u root -p

## Comandos SQL para Otorgar Permisos:
GRANT ALL PRIVILEGES ON DBTmp.* TO 'dam235'@'%';

FLUSH PRIVILEGES;

EXIT;

## SCRIPT SQL
Este script corrige el error de AUTO_INCREMENT y contiene los datos iniciales. Debe ejecutarse una sola vez en la base de datos DBTmp (conectado con el usuario dam235) usando un gestor de base de datos como DBeaver:

-- 1. CREACIÓN DE LA BASE DE DATOS Y CONTEXTO

CREATE DATABASE IF NOT EXISTS DBTmp; 
USE DBTmp; 

-- 2. CREACIÓN DE LA TABLA USER con AUTO_INCREMENT

DROP TABLE IF EXISTS user;
CREATE TABLE user ( 
    id int(11) NOT NULL AUTO_INCREMENT,
    name varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci NOT NULL, 
    last_name varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci NOT NULL, 
    email varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_spanish_ci NOT NULL, 
    password varchar(256) NOT NULL, 
    active bit(1) NOT NULL, 
    PRIMARY KEY (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; 

-- 3. CREACIÓN DE LA TABLA PRODUCT

DROP TABLE IF EXISTS product;
CREATE TABLE product( 
    code int(11) NOT NULL AUTO_INCREMENT, 
    name varchar(255) DEFAULT NULL, 
    status bit(1) DEFAULT NULL, 
    PRIMARY KEY (code) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; 

-- 4. INSERCIÓN DE DATOS INICIALES

INSERT INTO user VALUES 
(1,'Marcos','Martinez','mmartinez@temp.com','{bcrypt}$2a$10$uvWNfki.WxQhUD6e51di0uvC9G0DiGly7/PvyLR0Ay0mpFV24YkLG', 1);

INSERT INTO product VALUES (1,'Mochila backpack Adidas',1),(2,'USB 2.0 Security',1);
