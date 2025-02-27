<template>
  <div class="body-div">
    <div class="menu-container">
      <!--Menu Izquierdo-->
      <div class="sidebar">
        <h2>CRUD <br />FACTURAS</h2>
        <div class="sidebar-menu">
          <hr />
          <a @click="goDashboard">Dashboard</a>
          <hr />
          <a @click="goClient">Clientes</a>
          <hr />
          <a @click="goInvoice">Facturas</a>
          <hr />
        </div>
        <!--Menu Izquierdo Inferior-->
        <div class="sidebar-foot">
          <div class="sidebar-foot-business">
            <div>
              <a>{{ nameBusiness }}</a>
              <p>{{ cifBusiness }}</p>
            </div>
            <button @click="useSubmenu()">{{ iconSubmenu }}</button>
          </div>
          <!--Subemnu despegable-->
          <div class="sidebar-foot-submenu" id="submenu1">
            <a @click="backSelectBusinesses()">Cambiar de empresa</a>
          </div>
          <div class="sidebar-foot-submenu" id="submenu2">
            <a @click="closeSession()">Cerrar Sesion</a>
          </div>
        </div>
      </div>
      <div>
        <router-view />
      </div>
    </div>
  </div>
</template>
<script>
import { useMenu } from "./scripts/menuScript";
export default {
  ...useMenu(),
};
</script>
<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/menu.css");
</style>
