<template>
  <div class="invoice-container">
    <div class="invoice-data-container">
      <!--Cuadros de datos de las facturas-->
      <!--Cuadro de numero de facturas-->
      <div class="data-container">
        <h2 style="background-color: #d3d3d3">Numero de Facturas</h2>
        <p>{{ numInvoices }}</p>
      </div>
      <!--Cuadro de total facturado-->
      <div class="data-container">
        <h2 style="background-color: #add8e6">Total <br />Facturado</h2>
        <p>{{ invoiceTotal || 0 }}€</p>
      </div>
      <!--Cuadro de total facturado y pagado-->
      <div class="data-container">
        <h2 style="background-color: #90ee90">
          Total Facturado <br />
          Pagado
        </h2>
        <p>{{ invoiceTotalPaid || 0 }}€</p>
      </div>
      <!--Cuadro de total facturado y no pagado-->
      <div class="data-container">
        <h2 style="background-color: #f08080">
          Total Facturado <br />
          No Pagado
        </h2>
        <p>{{ invoiceTotalNoPaid || 0 }}€</p>
      </div>
      <!--Formulario para agregar una nueva factura-->
      <FormInvoice
        v-if="showForm"
        :visible="showForm"
        @close="formOpenOrClose"
        @saved="formOpenOrCloseSaved"
      />
      <!--Formulario para modificar una factura-->
      <FormInvoiceModify
        v-if="showFormMod"
        :visible="showFormMod"
        :invoiceData="selectedInvoice"
        @close="formModOpenOrClose"
      />
    </div>
    <!--Botones para agregar una nueva factura, exportar en excel, y actualizar los datos-->
    <div class="buttons-container">
      <div class="button-add" @click="formOpenOrClose">Agregar</div>
      <div class="button-export" @click="exportExcel">Exportar</div>
      <div class="button-update" @click="updateTable">↻</div>
    </div>
    <div class="table-container">
      <!-- Tabla de las facturas con su configuracion -->
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
  </div>
</template>

<script>
import { UseInvoice } from "./scripts/InvoiceScript";

export default {
  ...UseInvoice(),
};
</script>

<style scoped>
@import url("../assets/css/global.css");
@import url("../assets/css/invoice.css");
</style>
