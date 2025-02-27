import { createRouter, createWebHistory } from "vue-router";
import Login from "../components/Login.vue";
import Register from "../components/Register.vue";
import SelectBusiness from "../components/SelectBusiness.vue";
import RegisterBusiness from "../components/RegisterBusiness.vue";
import Menu from "../components/Menu.vue";
import Dashboard from "../components/Dashboard.vue";
import Invoice from "../components/Invoice.vue";
import Client from "../components/Client.vue";

//Rutas de todos los componentes
const routes = [
  { path: "/", component: Login },
  { path: "/register", component: Register },
  { path: "/selectBusiness", component: SelectBusiness },
  { path: "/registerBusiness", component: RegisterBusiness },
  {
    //De menu, tendra componentes hijos, dashboard, facturas y clientes
    path: "/menu",
    component: Menu,
    children: [
      { path: "dashboard", component: Dashboard },
      { path: "invoice", component: Invoice },
      { path: "client", component: Client },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
