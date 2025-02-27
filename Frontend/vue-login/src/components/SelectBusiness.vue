<template>
  <div id="body-div">
    <div class="select-container">
      <div class="business-container">
        <!--Botones de Editar y eliminar negocio-->
        <div class="buttons-top-container">
          <!--Editar-->
          <p
            id="button-edit"
            :style="{
              visibility: isHiddenEdit ? 'hidden' : 'visible',
            }"
            @click="activeEdit"
          >
            {{ buttonTextEdit }}
          </p>
          <!--Eliminar-->
          <p
            id="button-delete"
            :style="{
              visibility: isHiddenDelete ? 'hidden' : 'visible',
            }"
            @click="activeDelete"
          >
            {{ buttonTextDelete }}
          </p>
        </div>
        <!--Formulario para editar una empresa-->
        <BusinessEdit
          v-if="showForm"
          :visible="showForm"
          :invoiceData="selectedBusiness"
          @close="formOpenOrClose"
          @saved="formOpenOrCloseSaved"
        />
        <!--Titulo-->
        <h2
          :style="{
            color: colorTitle,
          }"
        >
          {{ title }}
        </h2>

        <!-- Mostrar negocios que tiene el usuario o el mensaje de que no hay negocios -->
        <div
          v-if="businesses.length > 0"
          id="business-cards"
          class="business-cards"
        >
          <!-- Recorre el array mostrando los negocios guardados -->
          <div
            v-for="business in businesses"
            :key="business.idBusiness"
            class="business-card"
            @click="goMenuOrDelete(business)"
          >
            <p class="business-name">{{ business.nameBusiness }}</p>
            <p>{{ business.cifBusiness }}</p>
          </div>
        </div>
        <!--Mensaje de que no hay  negocios registrados-->
        <div v-else id="no-business" class="no-business">
          <p>No hay negocios disponibles</p>
        </div>

        <!-- Botones para cerrar sesion y registrar una nuevo negocio-->
        <div id="buttons-bottom-container">
          <button type="button" @click="backLogin">Cerrar Sesión</button>
          <p>
            ¿No tienes un negocio? <a @click="goRegisterBusiness">Crealo</a>
          </p>
        </div>
        <!--Cargador de pagina-->
        <div v-if="loader" class="overlay">
          <div class="spinner"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useSelectBusiness } from "./scripts/selectBusiness";

export default {
  ...useSelectBusiness(),
};
</script>

<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/select-business.css");
</style>
