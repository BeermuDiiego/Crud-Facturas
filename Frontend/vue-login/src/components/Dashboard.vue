<template>
  <div class="dashboard-container">
    <div class="select-container-1">
      <div class="num-container">
        <div class="shortcut-container">
          <div class="shortcut-item" @click="formOpenOrClose">
            <!--Accesos rapido para crear una factura-->
            <h3>Crear Factura</h3>
          </div>
          <!--Formulario para agregar una nueva factura-->
          <FormInvoice
            v-if="showForm"
            :visible="showForm"
            @close="formOpenOrClose"
            @saved="formOpenOrCloseSaved"
          />
          <!--Formulario para crear un nuevo cliente-->
          <FormClient
            v-if="showFormClient"
            :visible="showFormClient"
            @close="formOpenOrCloseClient"
            @saved="formOpenOrCloseClientSaved"
          />
          <!--Acceso rapido para crear un nuevo cliente-->
          <div class="shortcut-item" @click="formOpenOrCloseClient"><h3>Crear Cliente</h3></div>
        </div>
        <!--Cuadros donde se mostraran el numero de clientes y facturas-->
        <div class="item">
          <h2>Numero de Clientes</h2>
          <p>{{ numClients }}</p>
        </div>
        <div class="item">
          <h2>Numero de Facturas</h2>
          <p>{{ numInvoices }}</p>
        </div>
      </div>
      <!--Cuadro de tops-->
      <div class="tops-container">
        <div class="tops-item">
          <!--Lista de top clientes-->
          <h3>TOP CLIENTES</h3>
          <div v-if="topClients.length > 0">
            <div
              v-for="clients in topClients"
              :key="clients.cifClient"
              class="data"
            >
              <div id="name">
                <h4>
                  {{ clients.nameClient }}
                  <p>{{ clients.cifClient }}</p>
                </h4>
              </div>

              <div id="total">
                <h4>{{ clients.totalInvoices || 0 }}€</h4>
              </div>
            </div>
          </div>
          <div v-else id="alert-top"><h2>NO HAY CLIENTES</h2></div>
        </div>
        <!--Lista de tops de facturas-->
        <div class="tops-item">
          <h3>TOP FACTURAS</h3>
          <div v-if="topInvoices.length > 0" class="data-container">
            <div
              v-for="invoices in topInvoices"
              :key="invoices.numInvoice"
              class="data"
            >
              <div id="name">
                <h4>
                  {{ invoices.numInvoice }}
                  <p>{{ invoices.dateInvoice }}</p>
                </h4>
              </div>
              <div id="total">
                <h4>{{ invoices.totalInvoice || 0 }}€</h4>
              </div>
            </div>
          </div>
          <div v-else id="alert-top"><h2>NO HAY FACTURAS</h2></div>
        </div>
      </div>
    </div>
    <!--Botones que cambian la vista por dia, mes o anyo de los graficos-->
    <div class="select-container">
      <div class="head-invoices">
        <!--Boton Dia-->
        <a
          @click.prevent="checkButtons('buttonDay')"
          :class="{ active: buttonActive === 'buttonDay' }"
          >DIA</a
        >
        <!--Boton Mes-->
        <a
          @click.prevent="checkButtons('buttonMonth')"
          :class="{ active: buttonActive === 'buttonMonth' }"
          >MES</a
        >
        <!--Anyo-->
        <a
          @click.prevent="checkButtons('buttonYear')"
          :class="{ active: buttonActive === 'buttonYear' }"
          >AÑO</a
        >
      </div>
      <!--Cuadros de totales con mini grafico-->
      <div class="flex">
        <div class="flex-item">
          <h2>Total <br />Facturado</h2>
          <p>{{ invoiceTotal || 0 }}€</p>
          <canvas id="invoiceTotalChart"></canvas>
        </div>
        <div class="flex-item">
          <h2>
            Total Facturado <br />
            Pagado
          </h2>
          <p>{{ invoiceTotalPaid || 0 }}€</p>
          <canvas id="invoiceTotalPaidChart"></canvas>
        </div>
        <div class="flex-item">
          <h2>
            Total Facturado <br />
            No Pagado
          </h2>
          <p>{{ invoiceTotalNoPaid || 0 }}€</p>
          <canvas id="invoiceTotalNoPaidChart"></canvas>
        </div>
      </div>
      <!--Graficos de facturas-->
      <div class="charts">
        <div class="chart-container-line">
          <canvas id="invoiceChartLine"></canvas>
        </div>
        <div class="chart-container-bar">
          <canvas id="invoiceChartBar"></canvas>
        </div>
      </div>
    </div>
    <!-- Cargador de pagina-->
    <div v-if="loader" class="overlay">
      <div class="spinner"></div>
    </div>
  </div>
</template>

<script>
import { useDashboard } from "./scripts/dashboardScript.js";

export default {
  ...useDashboard(),
};
</script>
<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/dashboard.css");
</style>
