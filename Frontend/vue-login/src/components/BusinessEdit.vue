<template>
  <div v-if="visible" class="modal-overlay">
    <div class="body-div">
      <div class="register-business-container">
        <h2>Modificar Negocio</h2>
        <!--Formulario para editar un negocio-->
        <form @submit.prevent="validCif">
          <div class="form-group">
            <!--Nombre del negocio-->
            <label>Nombre Del Negocio</label>
            <input
              type="text"
              v-model="nameBusiness"
              required
              placeholder="Escribe el nombre del negocio"
            />
          </div>
          <!--Identificador del negocio-->
          <div class="form-group">
            <label>Identificador Del Negocio</label>
            <input
              type="text"
              v-model="cifBusiness"
              required
              placeholder="Escribe el CIF del negocio o el DNI/NIF"
            />
          </div>
          <!--Email del negocio-->
          <div class="form-group">
            <label>Email Del Negocio</label>
            <input
              type="email"
              v-model="emailBusiness"
              required
              placeholder="Escribe el correo de contacto"
            />
          </div>
          <!--Telefono del negocio-->
          <div class="form-group">
            <label>Telefono De Contacto</label>
            <input
              type="tel"
              v-model="phoneBusiness"
              placeholder="Telefono de contacto del negocio"
            />
          </div>
          <!--Direccion del negocio-->
          <div class="form-group">
            <label>Direccion Del Negocio</label>
            <input
              type="text"
              v-model="addressBusiness"
              required
              placeholder="Escribe la direccion del negocio"
            />
          </div>
          <!--Numero de cuenta del negocio-->
          <div class="form-group">
            <label>Cuenta Bancaria</label>
            <input
              type="text"
              v-model="accountNumberBusiness"
              required
              placeholder="Escribe la cuenta bancaria del negocio"
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
import { useEditBusiness } from "./scripts/businessEditScript";

export default {
  ...useEditBusiness(),
};
</script>
<style scoped>
@import url("../assets/css/register-business.css");
</style>
