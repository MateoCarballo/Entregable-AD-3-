-- Creación de la base de datos
DROP DATABASE if exists HospitalDB;
CREATE DATABASE HospitalDB;
USE HospitalDB;

-- Creación de la tabla Paciente
CREATE TABLE Paciente (
    id INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    direccion VARCHAR(255)
);

-- Creación de la tabla Doctor
CREATE TABLE Doctor (
    id INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100),
    telefono VARCHAR(20)
);

-- Creación de la tabla Hospital
CREATE TABLE Hospital (
    id INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(255)
);

-- Creación de la tabla Tratamiento
CREATE TABLE Tratamiento (
    id INT PRIMARY KEY,
    tipo VARCHAR(100) NOT NULL,
    costo DECIMAL(10, 2),
    id_hospital INT,
    FOREIGN KEY (id_hospital) REFERENCES Hospital(id)
);

-- Creación de la tabla Cita
CREATE TABLE Cita (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha DATE NOT NULL,
    estado VARCHAR(50),
    id_paciente INT,
    id_doctor INT UNIQUE,
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id),
    FOREIGN KEY (id_doctor) REFERENCES Doctor(id)
);

-- Creación de la tabla Relacion para la relación "Recibe" entre Paciente y Tratamiento
CREATE TABLE Recibe (
    id_paciente INT,
    tratamiento_id INT,
    fecha_inicio DATE,
    fecha_fin DATE,
    PRIMARY KEY (id_paciente, tratamiento_id, fecha_inicio),
    FOREIGN KEY (id_paciente) REFERENCES Paciente(id),
    FOREIGN KEY (tratamiento_id) REFERENCES Tratamiento(id)
);

-- Inserción de datos en la tabla Paciente
INSERT INTO Paciente (id, nombre, fecha_nacimiento, direccion) VALUES
(1, 'Juan Perez', '1985-05-15', 'Calle 123, Ciudad A'),
(2, 'Ana Lopez', '1992-10-30', 'Avenida 45, Ciudad B'),
(3, 'Carlos Martínez', '1978-07-12', 'Callejón 7, Ciudad A'),
(4, 'María Gómez', '2000-03-22', 'Calle 18, Ciudad C');

-- Inserción de datos en la tabla Doctor
INSERT INTO Doctor (id, nombre, especialidad, telefono) VALUES
(1, 'Dr. Roberto Sanchez', 'Cardiología', '123-456-789'),
(2, 'Dra. Lucia Fernandez', 'Neurología', '987-654-321'),
(3, 'Dr. Miguel Alvarez', 'Dermatología', '456-789-123'),
(4, 'Dr. Fernando Ruiz', 'Pediatría', '321-654-987'),
(5, 'Dra. Carolina Mendoza', 'Ginecología', '654-321-987'),
(6, 'Dr. Javier Castro', 'Ortopedia', '789-123-456');

-- Inserción de datos en la tabla Hospital
INSERT INTO Hospital (id, nombre, ubicacion) VALUES
(1, 'Hospital Central', 'Ciudad A'),
(2, 'Hospital del Norte', 'Ciudad B'),
(3, 'Hospital Universitario', 'Ciudad C');

-- Inserción de datos en la tabla Tratamiento
INSERT INTO Tratamiento (id, tipo, costo, id_hospital) VALUES
(1, 'Tratamiento Cardiovascular', 1200.50, 1),
(2, 'Terapia Neurológica', 2500.00, 2),
(3, 'Tratamiento Dermatológico', 800.00, 1),
(4, 'Rehabilitación Física', 1500.00, 3);

-- Inserción de datos en la tabla Cita
INSERT INTO Cita (fecha, estado, id_paciente, id_doctor) VALUES
('2024-11-10', 'Pendiente', 1, 1),
('2024-11-15', 'Completada', 2, 2),
('2024-11-20', 'Cancelada', 3, 3),
('2024-11-25', 'Pendiente', 4, 4);

-- Inserción de datos en la tabla Recibe
INSERT INTO Recibe (id_paciente, tratamiento_id, fecha_inicio, fecha_fin) VALUES
(1, 1, '2024-01-01', '2024-02-01'),
(2, 2, '2024-03-01', '2024-04-01'),
(3, 3, '2024-05-01', '2024-06-01'),
(4, 4, '2024-07-01', '2024-08-01'),
(1, 3, '2024-09-01', '2024-10-01');
