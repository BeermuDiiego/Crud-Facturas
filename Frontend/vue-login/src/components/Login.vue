<template>
  <div class="body-div">
    <div class="login-container" id="loginContainer">
      <h2>
        Iniciar sesión<br />
        CRUD FACTURAS
      </h2>
      <!-- Formulario del usuario para logearse -->
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="email">Correo Electrónico</label>
          <input
            type="email"
            id="email"
            v-model="email"
            required
            placeholder="ejemplo555@hotmail.com"
          />
        </div>
        <div class="form-group">
          <label for="password">Contraseña</label>
          <input
            type="password"
            id="password"
            v-model="password"
            required
            placeholder="Contraseña"
          />
        </div>
        <!-- Botones de iniciar sesion y registrarse -->
        <button type="submit">Iniciar Sesión</button>
        <p>¿No tienes cuenta? <a @click="goRegister">Registrarse</a></p>
      </form>
      <p v-if="error" class="error">{{ error }}</p>

      <!-- Pantalla de carga -->
      <div v-if="cargando" class="overlay">
        <div class="spinner"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { useLogin } from "./scripts/loginScript.js";

export default {
  ...useLogin(),
};
</script>

<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/login.css");
</style>
