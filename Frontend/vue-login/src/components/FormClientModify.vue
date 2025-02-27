<template>
  <div v-if="visible" class="modal-overlay">
    <div class="body-div">
      <div class="register-client-container">
        <h2>Modificar Cliente</h2>
        <!--Formulario para editar un cliente-->
        <form @submit.prevent="validCif">
          <div class="form-group">
            <!--Nombre del negocio-->
            <label>Nombre Del Cliente</label>
            <input
              type="text"
              v-model="nameClient"
              required
              placeholder="Escribe el nombre del cliente"
            />
          </div>
          <!--Identificador del cliente-->
          <div class="form-group">
            <label>Identificador Del Cliente</label>
            <input
              type="text"
              v-model="cifClient"
              required
              placeholder="Escribe el CIF del cliente o el DNI/NIF"
            />
          </div>
          <!--Direccion del cliente-->
          <div class="form-group">
            <label>Direccion Del Cliente</label>
            <input
              type="text"
              v-model="addressClient"
              required
              placeholder="Escribe la direccion del cliente"
            />
          </div>
          <!--Email del cliente-->
          <div class="form-group">
            <label>Email Del Cliente</label>
            <input
              type="email"
              v-model="emailClient"
              required
              placeholder="Escribe el correo de contacto del cliente"
            />
          </div>
          <!--Botones para  registro o regreso a la seleccion del negocios-->
          <div class="button-group">
            <button id="button-save" type="submit">Guardar Cambios</button>
            <button id="button-cancel" type="button" @click="closeForm1">
              Cancelar
            </button>
          </div>
        </form>
        <!--Mensaje de error del formulario-->
        <p v-if="error" class="error">{{ error }}</p>
      </div>
    </div>
  </div>
</template>
<script>
import { UseEditClient } from "./scripts/formClientEditScript";

export default {
  ...UseEditClient(),
};
</script>
<style scoped>
@import url("../assets/css/form-client.css");
</style>
