CREATE DATABASE rh;
USE rh;

CREATE TABLE Departamento(
    id_departamento INT NOT NULL AUTO_INCREMENT, 
    nome_departamento VARCHAR(50) NOT NULL, 
    descricao TEXT NULL,
    PRIMARY KEY (id_departamento)
);