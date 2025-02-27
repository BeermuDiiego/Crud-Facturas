<template>
  <div class="client-container">
    <div class="client-data-container">
      <!--Cuadros de informacion sobre clientes y sus facturas-->
      <div class="box-data-container">
        <div class="data-container">
          <h2 style="background-color: #d3d3d3">Numero De Clientes</h2>
          <p>{{ numClients || 0 }}</p>
        </div>
        <div class="data-container">
          <h2 style="background-color: #add8e6">Numero De Facturas</h2>
          <p>{{ numInvoices || 0 }}</p>
        </div>
        <div class="data-container">
          <h2 style="background-color: #90ee90">
            Total <br />
            Facturado
          </h2>
          <p>{{ invoiceTotal || 0 }}€</p>
        </div>
      </div>
      <!--Cuadro de tops clientes-->
      <div class="tops-container">
        <div class="tops-data-container">
          <h3>Nº Facturas por Cliente</h3>
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
                <h4>{{ clients.numberInvoices || 0 }}</h4>
              </div>
            </div>
          </div>
          <div v-else id="alert-top"><h2>NO HAY CLIENTES</h2></div>
        </div>
        <div class="tops-data-container">
          <h3>Total Facturas Por Cliente</h3>
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
      </div>
    </div>

    <div class="table-container">
      <!--Botones para agregar un nuevo cliente, exportar en excel, y actualizar los datos-->
      <div class="buttons-container">
        <div class="button-add" @click="formOpenOrClose">Agregar</div>
        <div class="button-export" @click="exportExcel">Exportar</div>
        <div class="button-update" @click="updateTable">↻</div>
      </div>
      <!--Tabla de clientes-->
      <ag-grid-vue
        :rowData="rowData"
        :columnDefs="columnDefs"
        :localeText="localeText"
        :rowSelection="rowSelection"
        @grid-ready="onGridReady"
        :gridOptions="gridOptions"
        style="height: 50vh; width: 100%"
      >
      </ag-grid-vue>
    </div>
    <!--Formulario para crear un nuevo cliente-->
    <FormClient
      v-if="showForm"
      :visible="showForm"
      @close="formOpenOrClose"
      @saved="formOpenOrCloseSaved"
    />
    <!--Formulario para crear un nuevo cliente-->
    <FormClientModify
      v-if="showFormMod"
      :visible="showFormMod"
      :clientData="selectedClient"
      @close="formModOpenOrClose"
      @saved="formModOpenOrClose"
    />
  </div>
</template>

<script>
import { UseClientScript } from "./scripts/clientScript";

export default {
  ...UseClientScript(),
};
</script>

<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/client.css");
</style>
