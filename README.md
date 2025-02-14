# 🏥 Proyecto: Gestión de Hospital

## 📌 Descripción del Proyecto

Este proyecto consiste en el desarrollo de una aplicación que permita validar y gestionar la estructura de un hospital. Se utilizará **Hibernate** para la gestión de la base de datos.

## 📊 Diagrama Conceptual de la Base de Datos

[Diagrama conceptural](./Recursos/Proyecto2.png)_

## 📜 Script de la Base de Datos

El script correspondiente de la base de datos se encuentra en el archivo [`bd-hospital.sql`](./Recursos/Scripts_SQL/bd-hospital.sql).

## 🎯 Tareas a Realizar

La aplicación desarrollada con **Hibernate** deberá contar con las siguientes funcionalidades:

### 🩺 Gestión de Doctores
- Crear un doctor.
- Borrar un doctor por su **ID**.
- Modificar los datos de un doctor.

### 👨‍⚕️ Gestión de Pacientes
- Crear un paciente.
- Borrar un paciente por su **nombre**.
- Modificar los datos de un paciente.

### 🔗 Asignación de Doctores a Pacientes
- Asignar un **doctor** a un **paciente**.
- La asignación se hará a partir del **nombre** del doctor y el paciente.
- Se pedirá por teclado introducir ambos nombres.

### 📆 Gestión de Tratamientos
- Indicar la **fecha de fin del tratamiento** de un paciente.
  - Se ingresará el **nombre del paciente**, la **fecha de inicio**, el **tipo** y la **fecha de fin** del tratamiento.
- Cambiar el **hospital** donde se realiza un tratamiento.
  - Se ingresará el **ID del tratamiento**, el **nombre del hospital actual** y el **nuevo hospital**.

### 📋 Consultas de Datos
- **Mostrar los datos de un paciente**:
  - **ID**, **nombre**, **fecha de nacimiento**, **dirección**, **tratamientos** y **citas**.
  - La consulta se realizará ingresando el **nombre del paciente**.
- **Mostrar los tratamientos y el hospital donde se realizan**.
  - La consulta se realizará ingresando el **nombre del hospital**.
- **Mostrar el número total de tratamientos por hospital**.
  - La consulta se realizará ingresando el **nombre del hospital**.

## 📌 Notas Importantes
✅ Todas las relaciones deben ser **bidireccionales**.<br>
✅ Todos los datos se pedirán **por teclado**.<br>
✅ Se implementará un **menú** para probar cada una de las funcionalidades anteriores.

```

## 🛠️ Tecnologías Utilizadas

- Java
- Hibernate
- MySQL
- Maven
