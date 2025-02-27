<template>
  <div class="body-div">
    <div class="register-container" id="registerContainer">
      <h2>
        Registrarse en <br />
        CRUD FACTURAS
      </h2>
      <!--Formulario de registro de un nuevo usuario-->
      <form @submit.prevent="registerUser">
        <!--Nombre del usuario-->
        <div class="form-group">
          <label>Nombre</label>
          <input
            type="text"
            v-model="name"
            required
            placeholder="Escribe tu nombre"
          />
        </div>
        <!--Apellido del usuario-->
        <div class="form-group">
          <label>Apellido</label>
          <input
            type="text"
            v-model="surname"
            required
            placeholder="Escribe tu apellido"
          />
        </div>
        <!--Email del usuario (Para iniciar sesion, y no se puede repetir en la bbdd)-->
        <div class="form-group">
          <label>Email</label>
          <input
            type="email"
            v-model="email"
            required
            placeholder="Escribe tu email"
          />
        </div>
        <!--Constrasenya del usuario-->
        <div class="form-group">
          <label>Contraseña</label>
          <input
            type="password"
            v-model="password"
            required
            placeholder="Escribe tu contraseña"
          />
        </div>
        <div class="button-group">
          <button type="submit">Registrarse</button>
          <button id="button-back" type="button" @click="backLogin">Volver</button>
        </div>
      </form>
      <!--Mensaje de error para el formulario-->
      <p v-if="error" class="error">{{ error }}</p>
      <!--Cargador de pantalla-->
      <div v-if="loader" class="overlay">
        <div class="spinner"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRegister } from "./scripts/registerScript";
export default {
  ...useRegister(),
};
</script>

<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/register.css");
</style>
